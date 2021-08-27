package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

public interface Player
{
    /**
     * The three types of actions a player can do
     */
    enum TurnKind
    {
        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        public static final List<TurnKind> ALL = List.of(TurnKind.values()); //all of the elements of TurnKind
    }

    /**
     * Communicates the player's own identity and the other player's identity
     *
     * @param ownId the player's own identity
     * @param playerNames the other player's identity
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Called when an information must be communicated to the player
     * @param info the info to communicate
     */
    void receiveInfo(String info);

    /**
     * Called every time the game state changes
     * @param newState the new Public Game State
     * @param ownState the new state of the player
     * @param topCard  the card at the top of the deck
     */
    void updateState(PublicGameState newState, PlayerState ownState,Card topCard);

    /**
     * Called in the beginning of the game to communicate to the player the five tickets he received
     * @param tickets the five received tickets
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Called in the beginning of the game to ask which tickets does the player wants to keep
     * @return the chosen tickets
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * to ask the player which action he wants to do
     * @return the chosen action
     */
    TurnKind nextTurn();

    /**
     * adds new tickets that the player chose
     * @param options the tickets from which the player must choose
     * @return the chosen tickets
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * A method to call when the player want to draw cards, to know were he wants to draw them
     * @return The int corresponding to the index of the card he want to draw (face up), or Constants.DECK_SLOT if he wants to draw in the hidden cards
     */
    int drawSlot();

    /**
     * A method to call when the player want to claim a Route to know which one he wants to Claim
     * @return the Route the player want to claim
     */
    Route claimedRoute();

    /**
     * A method to call when the Player want to claim a Route, return the cards he wants to play
     * @return the cards the player want to play to claim a Route
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Gives the SortedBag of the cards the player wants to use to claim a tunnel, can be empty if the player don't want/have the possibility to claim the tunnel
     * @param options the List of SortedBag containing all the possible cards the player can play to claim a tunnel
     * @return a SortedBag of the Cards the player want to play to claim the tunnel
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
