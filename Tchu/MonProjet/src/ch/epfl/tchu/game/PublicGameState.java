package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class PublicGameState
{
    private final int ticketCount;
    private final PublicCardState cardState;

    private final PlayerId playerIdCurrent;
    private final PlayerId playerIdLast;
    private final Map<PlayerId,PublicPlayerState> playerState;
    private final int MAX_NUMBER_IN_ONE_TURN = 2;

    /**
     *Public game state constructor
     * @param ticketsCount the tickets count
     * @param cardState the card state
     * @param currentPlayerId the current Player Id
     * @param playerState the player State
     * @param lastPlayer the last Player
     * @return a PublicGameState initialise with the given parameters
     * @throws IllegalArgumentException if the cardstate's deck size is stricly negative, or if map playerState doesn't contains exactly two elements
     * @throws NullPointerException if cardstate or  currentPlayerId, is null
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer)
    {
        this.cardState = Objects.requireNonNull(cardState);
        Preconditions.checkArgument(cardState.deckSize()>=0 && playerState.size()== PlayerId.COUNT && ticketsCount>=0);
        this.ticketCount = ticketsCount;
        this.playerIdCurrent=Objects.requireNonNull(currentPlayerId);
        this.playerIdLast = lastPlayer;
        this.playerState = Map.copyOf(playerState);
    }

    /**
     *
     * @return the number of tickets in the ticket deck
     */
    public int ticketsCount()
    {
        return this.ticketCount;
    }

    /**
     * True if the ticket deck has at least one element
     * @return a boolean :if a player can draw a ticket
     */
    public boolean canDrawTickets()
    {
        return this.ticketCount>=1;
    }

    /**
     * Gives the public card state linked to this gameState
     * @return the PublicCardState of (this)
     */
    public PublicCardState cardState()
    {
        return this.cardState;
    }

    /**
     * True if there are more or 5 cards in the deck and the discards added.
     * @return a boolean : if a player can draw cards
     */
    public boolean canDrawCards()
    {
        return ( this.cardState.deckSize() + this.cardState.discardsSize() ) >= Constants.ADDITIONAL_TUNNEL_CARDS +MAX_NUMBER_IN_ONE_TURN;
    }

    /**
     *
     * @return the PlayerId of the current Player
     */
    public PlayerId currentPlayerId()
    {
        return this.playerIdCurrent;
    }

    /**
     *
     * @param playerId the player's Id
     * @return the PublicPlayerState of the "playerId" player;
     */
    public PublicPlayerState playerState(PlayerId playerId)
    {
        return this.playerState.get(playerId);
    }

    /**
     *
     * @return the PublicPlayerState of the current player
     */
    public PublicPlayerState currentPlayerState()
    {
        return this.playerState.get(currentPlayerId());
    }

    /**
     *
     * @return a List<Route> of all the Route claimed by the players
     */
    public List<Route> claimedRoutes()
    {
        List<Route> tempRoute= new ArrayList<>(currentPlayerState().routes()) ;

        tempRoute.addAll(playerState(currentPlayerId().next()).routes());

        return tempRoute;
    }

    /**
     * Gives the PlayerId of the last player who will play, (may be null if not the last turn)
     * @return the PlayerId of the last player
     */
    public PlayerId lastPlayer()
    {
        return this.playerIdLast;
    }

}
