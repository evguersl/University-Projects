package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;



public final class GameState extends PublicGameState
{
    private final Map<PlayerId, PlayerState> completePlayerState;
    private final CardState cardState;
    private final Deck<Ticket> deckTicket;
    private final int  MINIMUM_NUMBER_CARD =2;

    private GameState(int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer, Deck<Ticket> deckTicket)
    {
        super(ticketsCount, cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);

        this.completePlayerState = Map.copyOf(playerState);
        this.cardState = cardState;
        this.deckTicket = deckTicket;
    }

    /**
     * Creates the initial GameState with a CardState, a current player, a last player,
     *  the players states, the deck of tickets and the count of the Tickets
     *
     * @param tickets initial Tickets
     * @param rng random generator to shuffle
     * @return the requested initial GameState
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng)
    {
        List<PlayerId> allList = new ArrayList<>(PlayerId.ALL);
        Collections.shuffle(allList,rng);

        PlayerId player1 = allList.get(0);
        PlayerId player2 = allList.get(1);

        Deck<Card> deckCard = Deck.of(Constants.ALL_CARDS,rng);
        Deck<Ticket> deckTicket = Deck.of(tickets, rng);



        PlayerState currentPlayerState = PlayerState.initial(deckCard.topCards(Constants.INITIAL_CARDS_COUNT));
        deckCard = deckCard.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        PlayerState lastPlayerState = PlayerState.initial(deckCard.topCards(Constants.INITIAL_CARDS_COUNT));
        deckCard = deckCard.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        CardState cardState = CardState.of(deckCard);

        Map<PlayerId, PlayerState> completePlayerState = new EnumMap<>(PlayerId.class);

        completePlayerState.put(player1,currentPlayerState);
        completePlayerState.put(player2,lastPlayerState);

        return new GameState(deckTicket.size(), cardState, player1, completePlayerState, null, deckTicket);
    }

    /**
     *
     * @param newTime the time taken by the player
     * @return an identical GameState with a new time for the playerState
     */
    public GameState addTime(int newTime)
    {
        PlayerState newPlayerState = new PlayerState(completePlayerState.get(currentPlayerId()).tickets(),
                completePlayerState.get(currentPlayerId()).cards(), completePlayerState.get(currentPlayerId()).routes(), newTime);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(currentPlayerId(),newPlayerState);

        return new GameState(deckTicket.size(), cardState, currentPlayerId(), newCompletePlayerState, lastPlayer(), deckTicket);
    }


    @Override
    public PlayerState playerState(PlayerId playerId)
    {
        return completePlayerState.get(playerId);
    }

    @Override
    public PlayerState currentPlayerState()
    {
        return completePlayerState.get(currentPlayerId());
    }

    /**
     * @param count the number of tickets to get
     * @return the first count tickets of the deck of tickets
     * @throws IllegalArgumentException if 0>count or count> deckTicket.size()
     */
    public SortedBag<Ticket> topTickets(int count)
    {
        Preconditions.checkArgument(0<=count && count<= deckTicket.size());

        return deckTicket.topCards(count);
    }

    /**
     * @param count the first n tickets of the deck of tickets
     * @return a new GameState without the first n tickets of the deck of tickets
     */
    public GameState withoutTopTickets(int count)
    {
        Deck<Ticket> newDeckTicket = deckTicket.withoutTopCards(count);

        return new GameState(newDeckTicket.size(), cardState, currentPlayerId(), completePlayerState, lastPlayer(), newDeckTicket);
    }

    /**
     *
     * @return the top card of the deck of cards
     */
    public Card topCard()
    {
        return cardState.topDeckCard();
    }

    /**
     *
     * @return a new GameState without the top card of the deck of cards
     */
    public GameState withoutTopCard()
    {
        return new GameState(deckTicket.size(), cardState.withoutTopDeckCard(), currentPlayerId(), completePlayerState, lastPlayer(), deckTicket);
    }

    /**
     *
     * @param discardedCards cards to add to the discards
     * @return a new GameState with the discardedCards added to the discards
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards)
    {
        return new GameState(deckTicket.size(), cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), completePlayerState, lastPlayer(), deckTicket);
    }

    /**
     * If the decksize of the cardstate is empty then we recreate it with the discards
     *
     * @param rng random generator to shuffle
     * @return the new requested GameSate
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng)
    {
        if(0!=cardState.deckSize())
        {
            return new GameState(deckTicket.size(), cardState, currentPlayerId(), completePlayerState, lastPlayer(), deckTicket);
        }
        else
        {
            return new GameState(deckTicket.size(), cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), completePlayerState, lastPlayer(), deckTicket);
        }
    }

    /**
     *
     * @param playerId the player to add tickets
     * @param chosenTickets the tickets to add
     * @return a new GameState with the added Tickets to the player's state
     * @throws IllegalArgumentException if the playerId already has a Ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets)
    {
        Preconditions.checkArgument(completePlayerState.get(playerId).ticketCount()==0);

        PlayerState newPlayerState = completePlayerState.get(playerId)
                .withAddedTickets(chosenTickets,0);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(playerId,newPlayerState);


        return new GameState(deckTicket.size(), cardState, currentPlayerId(), newCompletePlayerState, lastPlayer(), deckTicket);
    }

    /**
     *
     * @param drawnTickets tickets drawn from the deck
     * @param chosenTickets tickets chosen from the drawn tickets
     * @param newTime the time taken by the player
     * @return a new GameState with the chosen tickets added to the playerIdCurrent and the drawnTickets deleted from the game
     * @throws IllegalArgumentException if the chosen tickets aren't contained in the drawn tickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets, int newTime)//TODO var newtime
    {
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

        Deck<Ticket> newDeckTicket = deckTicket.withoutTopCards(drawnTickets.size());

        PlayerState newPlayerState = completePlayerState.get(currentPlayerId())
                .withAddedTickets(chosenTickets, newTime);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(currentPlayerId(),newPlayerState);

        return new GameState(newDeckTicket.size(), cardState, currentPlayerId(), newCompletePlayerState, lastPlayer(), newDeckTicket);
    }

    /**
     *
     * @param slot the rank of the desired face up card
     * @param newTime the time taken by the player
     * @return a new GameState with an added face up card for the playerIdCurrent
     * @throws IllegalArgumentException if canDrawCards() returns false
     */
    public GameState withDrawnFaceUpCard(int slot, int newTime)
    {
        PlayerState newPlayerState = completePlayerState.get(currentPlayerId())
                .withAddedCard(cardState.faceUpCard(slot), newTime);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(currentPlayerId(),newPlayerState);

        return new GameState(deckTicket.size(), cardState.withDrawnFaceUpCard(slot), currentPlayerId(), newCompletePlayerState, lastPlayer(), deckTicket);
    }

    /**
     * Gives  a GameState identical to (this), where the card at the top of the deck was given to current Player
     * @param newTime the time taken by the player
     * @return a new GameState with the desired change on the deck and the State of the current Player
     * @throws IllegalArgumentException if the player can't draw cards, or if the deck is empty
     */
    public GameState withBlindlyDrawnCard(int newTime)
    {


        PlayerState newPlayerState = completePlayerState.get(currentPlayerId())
                .withAddedCard(cardState.topDeckCard(), newTime);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(currentPlayerId(),newPlayerState);

        return new GameState(deckTicket.size(), cardState.withoutTopDeckCard(), currentPlayerId(), newCompletePlayerState, lastPlayer(), deckTicket);
    }

    /**
     * Gives a GameState identical to (this), where the  current player claimed the Route route
     * with the Cards cards.
     *
     * @param route the route
     * @param cards the cards
     * @param newTime the time taken by the player
     * @return a new GameState with the desired change on the State of the current Player
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards, int newTime)
    {
        PlayerState newPlayerState = completePlayerState.get(currentPlayerId())
                .withClaimedRoute(route, cards, newTime);

        Map<PlayerId,PlayerState> newCompletePlayerState = new HashMap<>(completePlayerState);
        newCompletePlayerState.put(currentPlayerId(),newPlayerState);

        return new GameState(deckTicket.size(), cardState.withMoreDiscardedCards(cards), currentPlayerId(), newCompletePlayerState, lastPlayer(), deckTicket);
    }

    /**
     *
     * @return a boolean: if the last turn Begins
     */
    public boolean lastTurnBegins()
    {

        return  MINIMUM_NUMBER_CARD >=this.completePlayerState.get(currentPlayerId()).carCount() && lastPlayer()==null;
    }

    /**
     * Gives the GameState of the next Turn, initialse the value of lastPlayer with the current player if the last turn begins
     * @return the GameState of the next turn
     */
    public GameState forNextTurn()
    {
        if(lastTurnBegins())
        {
            return new GameState(deckTicket.size(), cardState, currentPlayerId().next(), completePlayerState, currentPlayerId(), deckTicket);
        }
        else
        {
            return new GameState(deckTicket.size(), cardState, currentPlayerId().next(), completePlayerState, lastPlayer(), deckTicket);
        }

    }





}
