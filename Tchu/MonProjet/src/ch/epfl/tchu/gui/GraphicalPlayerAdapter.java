package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * An adapter of the class Graphical Player, implementing Player
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public final class GraphicalPlayerAdapter implements Player
    {
        private GraphicalPlayer graphicalPlayer;
        private final BlockingQueue<SortedBag<Ticket>> initialTicketChoice;
        private final BlockingQueue<TurnKind> turnKind;
        private final BlockingQueue<Integer> cardSlot;
        private final BlockingQueue<Route> routeToClaim;
        private final BlockingQueue<SortedBag<Card>> cardToClaimRoute;

        public GraphicalPlayerAdapter()
        {

            this.initialTicketChoice = new ArrayBlockingQueue<>(1);
            this.turnKind = new ArrayBlockingQueue<>(1);
            this.cardSlot = new ArrayBlockingQueue<>(1);
            this.routeToClaim = new ArrayBlockingQueue<>(1);
            this.cardToClaimRoute = new ArrayBlockingQueue<>(1);
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames)
        {
            Platform.runLater(()->

            this.graphicalPlayer= new GraphicalPlayer(ownId,playerNames));

        }

        @Override
        public void receiveInfo(String info)
        {
            Platform.runLater(() -> graphicalPlayer.receiveInfo(info));
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState,Card card)
        {
            Platform.runLater(()->graphicalPlayer.setState(newState,ownState, card));
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets)
        {
            ActionHandler.ChooseTicketsHandler handler = (tickets1)->
            {
                try
                {
                    initialTicketChoice.put(tickets1);
                }
                catch (InterruptedException e)
                {
                    throw new Error();
                }

            };
            Platform.runLater(()-> graphicalPlayer.chooseTickets(tickets,handler));
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets()
        {
            try
            {
                return initialTicketChoice.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }

        @Override
        public TurnKind nextTurn() {
            ActionHandler.DrawCardHandler cardHandler =
                    (slot) ->
                    {
                        try
                        {
                            //TODO NEW CODE

                            cardSlot.put(slot);
                            turnKind.put(TurnKind.DRAW_CARDS);
                        }
                        catch (InterruptedException e)
                        {
                            throw new Error();
                        }
                    };

            ActionHandler.DrawTicketsHandler ticketsHandler =
                    () ->
                    {
                        try
                        {
                            turnKind.put(TurnKind.DRAW_TICKETS);
                        }
                        catch (InterruptedException e)
                        {
                            throw new Error();
                        }
                    };
            ActionHandler.ClaimRouteHandler routeHandler =
                    (route,sortedBag) ->
                    {
                        try
                        {
                            routeToClaim.put(route);
                            cardToClaimRoute.put(sortedBag);
                            turnKind.put(TurnKind.CLAIM_ROUTE);
                        }
                        catch (InterruptedException e)
                        {
                            throw new Error();
                        }
                    };

            Platform.runLater(()-> graphicalPlayer.startTurn(ticketsHandler,cardHandler,routeHandler) );
            try
            {
                return turnKind.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options)
        {
            setInitialTicketChoice(options);
            return chooseInitialTickets();
        }

        @Override
        public int drawSlot()
        {
            ActionHandler.DrawCardHandler cardHandler2 =
                    (slot) ->
                    {
                        try
                        {
                            cardSlot.put(slot);

                        }
                        catch (InterruptedException e)
                        {
                            throw new Error();
                        }
                    };

            if (cardSlot.isEmpty())
            {
                Platform.runLater(() -> graphicalPlayer.drawCard(cardHandler2));
            }
            try
            {
                return cardSlot.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }

        @Override
        public Route claimedRoute() {
            try
            {
                return routeToClaim.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            try
            {
                return cardToClaimRoute.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options)
        {
            ActionHandler.ChooseCardsHandler chooseCardsHandler =
                    (choosenCards) ->
                    {
                        try
                        {
                            cardToClaimRoute.put(choosenCards);
                        }
                        catch (InterruptedException e)
                        {
                            throw new Error();
                        }
                    };

            Platform.runLater(()->graphicalPlayer.chooseAdditionalCards(options,chooseCardsHandler));

            try
            {
                return cardToClaimRoute.take();
            }
            catch (InterruptedException e)
            {
                throw new Error();
            }
        }
    }
