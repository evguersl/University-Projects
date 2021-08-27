package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class who creates all the properties needed to Update the GUI
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */
public final class ObservableGameState
{
    private final IntegerProperty time;
    private final IntegerProperty points;

    private final PlayerId myPlayer;
    private final IntegerProperty ticketPercentage;
    private final IntegerProperty cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCardsInfo;
    private final List<ObjectProperty<PlayerId>> routeInfo;
    private final ObservableList<Ticket> ticketPlayer;
    private final List<IntegerProperty> cardInfo;
    private final List<BooleanProperty> canCaptureRouteInfo;

    private final Map<PlayerId, IntegerProperty> numberTicketPlayer;
    private final Map<PlayerId, IntegerProperty> numberCardPlayer;
    private final Map<PlayerId, IntegerProperty> numberCarPlayer;
    private final Map<PlayerId, IntegerProperty> numberConstructPointPlayer;

    public PublicGameState myPublicGameState;
    private PlayerState myPlayersState;

    private final int TOTAL_ROUTE_LENGTH = ChMap.routes().size();
    private final int TOTAL_TICKET_SIZE = ChMap.tickets().size();

    private final List<BooleanProperty> stationSelected;

    private final  SimpleObjectProperty<Card> topCardProperty;



    /**
     * Creates an ObservableGameState
     * @param myPlayer the id of the corresponding player
     */
    public ObservableGameState(PlayerId myPlayer)
    {
        this.myPlayer = myPlayer;

        this.time = new SimpleIntegerProperty();
        this.points = new SimpleIntegerProperty();

        this.ticketPercentage = new SimpleIntegerProperty();
        this.cardPercentage = new SimpleIntegerProperty();

        this.numberTicketPlayer = initializeHashMap();
        this.numberCardPlayer = initializeHashMap();
        this.numberCarPlayer = initializeHashMap();
        this.numberConstructPointPlayer = initializeHashMap();

        this.topCardProperty = new SimpleObjectProperty<>();

        this.faceUpCardsInfo = new ArrayList<>();

        for(int i=0; i<Constants.FACE_UP_CARDS_COUNT; i++)
        {
            this.faceUpCardsInfo.add(new SimpleObjectProperty<>());
        }

        this.routeInfo = new ArrayList<>();
        this.canCaptureRouteInfo = new ArrayList<>();

        for(int i = 0; i<TOTAL_ROUTE_LENGTH; i++)
        {
            this.routeInfo.add(new SimpleObjectProperty<>());
            this.canCaptureRouteInfo.add(new SimpleBooleanProperty());
        }

        this.ticketPlayer = FXCollections.observableArrayList();

        this.cardInfo = new ArrayList<>();

        for(int i=0; i<Card.COUNT; i++)
        {
            this.cardInfo.add(new SimpleIntegerProperty());
        }

        this.stationSelected = new ArrayList<>();

        for (int i = 0; i < ChMap.getAllStationsWithoutCountries().size(); i++)
        {
            this.stationSelected.add(new SimpleBooleanProperty());
        }

        this.myPublicGameState = null;
        this.myPlayersState = null;
    }

    private Map<PlayerId, IntegerProperty> initializeHashMap()
    {
        Map<PlayerId, IntegerProperty> myMap = new HashMap<>();
        myMap.put(PlayerId.PLAYER_1, new SimpleIntegerProperty());
        myMap.put(PlayerId.PLAYER_2, new SimpleIntegerProperty());
        return myMap;
    }

    /**
     * Called every time to set a new state
     * @param publicGameState the current publicGameState
     * @param myPlayersState the current player's state
     */
    public void setState(PublicGameState publicGameState, PlayerState myPlayersState, Card card)
    {
        this.myPublicGameState = publicGameState;
        this.myPlayersState = myPlayersState;
        this.time.set(myPlayersState.getTime());
        this.points.set(myPlayersState.finalPoints());

        this.topCardProperty.set(card);

        this.ticketPercentage.set(  (publicGameState.ticketsCount()*100 / TOTAL_TICKET_SIZE)  );
        this.cardPercentage.set(  publicGameState.cardState().deckSize()*100 / Constants.TOTAL_CARDS_COUNT);

        this.numberTicketPlayer.get(PlayerId.PLAYER_1).setValue(publicGameState.playerState(PlayerId.PLAYER_1).ticketCount());
        this.numberTicketPlayer.get(PlayerId.PLAYER_2).setValue(publicGameState.playerState(PlayerId.PLAYER_2).ticketCount());

        this.numberCardPlayer.get(PlayerId.PLAYER_1).setValue(publicGameState.playerState(PlayerId.PLAYER_1).cardCount());
        this.numberCardPlayer.get(PlayerId.PLAYER_2).setValue(publicGameState.playerState(PlayerId.PLAYER_2).cardCount());

        this.numberCarPlayer.get(PlayerId.PLAYER_1).setValue(publicGameState.playerState(PlayerId.PLAYER_1).carCount());
        this.numberCarPlayer.get(PlayerId.PLAYER_2).setValue(publicGameState.playerState(PlayerId.PLAYER_2).carCount());

        this.numberConstructPointPlayer.get(PlayerId.PLAYER_1).setValue(publicGameState.playerState(PlayerId.PLAYER_1).claimPoints());
        this.numberConstructPointPlayer.get(PlayerId.PLAYER_2).setValue(publicGameState.playerState(PlayerId.PLAYER_2).claimPoints());

        for (int slot : Constants.FACE_UP_CARD_SLOTS)
        {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCardsInfo.get(slot).set(newCard);
        }

        for (int i=0; i<TOTAL_ROUTE_LENGTH; i++ )
        {
            //routeInfo
            if( routeInfo.get(i).get()==null && publicGameState.playerState(PlayerId.PLAYER_1).routes().contains(ChMap.routes().get(i)) )
            {
                routeInfo.get(i).set(PlayerId.PLAYER_1);
            }
            else if( routeInfo.get(i).get()==null && publicGameState.playerState(PlayerId.PLAYER_2).routes().contains(ChMap.routes().get(i)) )
            {
                routeInfo.get(i).set(PlayerId.PLAYER_2);
            }

            //capturedRouteInfo
            boolean canCaptureTheRoute = publicGameState.currentPlayerId()==this.myPlayer
                                         && routeInfo.get(i).get()==null
                                         && !routeHasTakenNeighbour(ChMap.routes().get(i))
                                         && myPlayersState.canClaimRoute(ChMap.routes().get(i));

            canCaptureRouteInfo.get(i).set(canCaptureTheRoute);
        }

        for(int i=0; i<TOTAL_TICKET_SIZE; i++)
        {
            Ticket tempTicket = ChMap.tickets().get(i);

            if( !ticketPlayer.contains(tempTicket) && myPlayersState.tickets().contains(tempTicket) )
            {
                ticketPlayer.add(tempTicket);
            }
        }

        for(int i=0; i<Card.COUNT; i++)
        {
            cardInfo.get(i).set( myPlayersState.cards().countOf(Card.ALL.get(i)) );
        }


    }

    private boolean routeHasTakenNeighbour(Route route)
    {
        for(Route rt : ChMap.routes())
        {
            if(  !rt.equals(route) && rt.stations().equals(route.stations()) && myPublicGameState !=null && myPublicGameState.claimedRoutes().contains(rt)  )
            {
                return true;
            }
        }
        return false;
    }

    public void updateSelectedStation(ObservableList<Ticket> ticketList)
    {
        this.stationSelected.forEach(t->t.setValue(false));
        for(Ticket ti :  ticketList)
        {
            for (Trip tr : ti.getTrips() )
            {
                if (ChMap.getAllStationsWithoutCountries().contains(tr.from()))
                {
                    int indexFrom = ChMap.getAllStationsWithoutCountries().indexOf(tr.from());
                    this.stationSelected.get(indexFrom).set(true);
                }
                if (ChMap.getAllStationsWithoutCountries().contains(tr.to()))
                {
                    int indexTo = ChMap.getAllStationsWithoutCountries().indexOf(tr.to());
                    this.stationSelected.get(indexTo).set(true);
                }
            }
        }

    }


    /**
     *
     * @return the time property
     */

    public ReadOnlyIntegerProperty timeProperty()
    {
        return time;
    }

    /**
     *
     * @return the points property
     */

    public ReadOnlyIntegerProperty pointsProperty()
    {
        return points;
    }

    /**
     *
     * @return the ticketPercentage property
     */
    public ReadOnlyIntegerProperty ticketPercentageProperty()
    {
        return ticketPercentage;
    }

    /**
     *
     * @return the numberTicketPlayer property of the requested Player
     */
    public ReadOnlyIntegerProperty numberTicketPlayerProperty(PlayerId myPLayerId)
    {
        return this.numberTicketPlayer.get(myPLayerId);
    }

    /**
     *
     * @return the numberCardPlayer property of the requested Player
     */
    public ReadOnlyIntegerProperty numberCardPlayerProperty(PlayerId myPLayerId)
    {
        return this.numberCardPlayer.get(myPLayerId);
    }

    /**
     *
     * @return the numberCarPlayer property of the requested Player
     */
    public ReadOnlyIntegerProperty numberCarPlayerProperty(PlayerId myPLayerId)
    {
        return this.numberCarPlayer.get(myPLayerId);
    }

    /**
     *
     * @return the numberConstructPointPlayer property of the requested Player
     */
    public ReadOnlyIntegerProperty numberConstructPointPlayerProperty(PlayerId myPLayerId)
    {
        return this.numberConstructPointPlayer.get(myPLayerId);
    }

    /**
     *
     * @return the cardPercentage property
     */
    public ReadOnlyIntegerProperty cardPercentageProperty()
    {
        return cardPercentage;
    }

    /**
     *
     * @param slot the slot of the card
     * @return the face up card property of the given slot
     */
    public ReadOnlyObjectProperty<Card> faceUpCardsInfoProperty(int slot)
    {
        return faceUpCardsInfo.get(slot);
    }

    /**
     *
     * @param route that we wish to know if it's taken
     * @return the PlayerId property of the given route index
     */
    public ReadOnlyObjectProperty<PlayerId> routeInfoProperty(Route route)
    {
        return routeInfo.get(ChMap.routes().indexOf(route));
    }

    /**
     *
     * @return the ticketPlayer ObservableList
     */
    public ObservableList<Ticket> ticketPlayer()
    {
        return FXCollections.unmodifiableObservableList(this.ticketPlayer);
    }


    /**
     *
     * @param index of the card
     * @return the cardInfo property of the card at the index position
     */
    public ReadOnlyIntegerProperty cardInfoProperty(int index)
    {
        return cardInfo.get(index);
    }

    /**
     *
     * @param route that we want to get the canCaptureRouteInfo
     * @return the canCaptureRouteInfo property of the route at the index position
     */
    public ReadOnlyBooleanProperty canCaptureRouteInfoProperty(Route route)
    {
        return canCaptureRouteInfo.get(ChMap.routes().indexOf(route));
    }

    /**
     *
     * @param station that we want to get the property (selected or not)
     * @return the ReadOnlyBooleanProperty, containing a boolean, true if a ticket with this station is actually selected
     */

    public ReadOnlyBooleanProperty stationSelectedProperty(Station station)
    {
        return this.stationSelected.get(ChMap.getAllStationsWithoutCountries().indexOf(station));
    }

    /**
     *
     * @return the canDrawTicket method of PublicGameState
     */
    public boolean canDrawTickets()
    {
        return this.myPublicGameState.canDrawTickets();
    }

    /**
     *
     * @return the canDrawCards of PublicGameState
     */
    public boolean canDrawCards()
    {
        return this.myPublicGameState.canDrawCards();
    }

    /**
     *
     * @param route the desired route
     * @return the possibleClaimCards method of PlayerState
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route)
    {
        return this.myPlayersState.possibleClaimCards(route);
    }

    /**
     *
     * @return the property containing the top card
     */

    public ReadOnlyObjectProperty<Card> topCardProperty ()
    {
        return this.topCardProperty;
    }
}
