package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;
/**
 * Interface who manages the actions the User can do during a game.
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public interface ActionHandler
{
    @FunctionalInterface
    interface DrawTicketsHandler
    {
        /**
         * Called to draw tickets
         */
        void onDrawTickets();
    }

    @FunctionalInterface
    interface DrawCardHandler
    {
        /**
         * Called to draw cards
         * @param slot -1 for the deck otherwise the slot of the corresponding faceupCard
         */
        void onDrawCard(int slot);
    }

    @FunctionalInterface
    interface ClaimRouteHandler
    {
        /**
         * Called to claim a route
         * @param claimRoute the route to claim
         * @param cardsToClaimRoute the cards to claim the route
         */
        void onClaimRoute(Route claimRoute, SortedBag<Card> cardsToClaimRoute);
    }
    @FunctionalInterface
    interface ChooseTicketsHandler
    {
        /**
         * Called when the player has to choose tickets
         * @param chosenTickets the chosen tickets
         */
        void onChooseTickets(SortedBag<Ticket> chosenTickets);
    }
    @FunctionalInterface
    interface ChooseCardsHandler
    {
        /**
         * Called when the player has to choose cards
         * @param chosenCards the chosen cards
         */
        void onChooseCards(SortedBag<Card> chosenCards);
    }
}
