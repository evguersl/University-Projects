package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;



import static java.lang.Math.max;



import java.util.*;
import java.util.stream.Collectors;

public final class PlayerState extends PublicPlayerState
{
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final int time;

    /**
     * Constructs a PlayerState
     * @param tickets the tickets of the player
     * @param cards the cards of the player
     * @param routes the routes captured by the player
     * @param newTime the time taken by the player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes, int newTime)
    {
        super (tickets.size(), cards.size(), routes);
        this.cards=cards;
        this.tickets=tickets;
        this.time=newTime;
    }

    /**
     *
     * @return the time taken by the player
     */
    public int getTime()
    {
        return this.time;
    }

    /**
     * Builds the initial state of the player
     * @param initialCards the initial cards distributed to the player
     * @return the requested PlayerState
     * @throws IllegalArgumentException if initialCards size isn't equal to 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards)
    {

        Preconditions.checkArgument(initialCards.size()==Constants.INITIAL_CARDS_COUNT);
        return new PlayerState(SortedBag.of(),initialCards,List.of(), 0);
    }

    /**
     * @return the tickets of the player
     */
    public SortedBag<Ticket> tickets()
    {
        return this.tickets;
    }

    /**
     * Adds tickets to the player's tickets
     * @param newTickets the tickets to add
     * @param newTime the time taken by the player
     * @return the new PlayerState
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets, int newTime)
    {
        SortedBag<Ticket> withNewTickets = this.tickets.union(newTickets);
        return new PlayerState(withNewTickets, this.cards,this.routes(), newTime);
    }

    /**
     *
     * @return the player's cards
     */
    public SortedBag<Card> cards()
    {
        return this.cards;
    }

    /**
     * Adds a card to the player's cards
     * @param card the card to add
     * @param newTime the time taken by the player
     * @return the new PlayerState
     */
    public PlayerState withAddedCard(Card card, int newTime)
    {
        SortedBag<Card> withNewCard = this.cards.union(SortedBag.of(card));
        return new PlayerState(this.tickets,withNewCard,this.routes(), newTime);
    }


    /**
     * Checks if a player can claim a Route
     * @param route the route that a player wants to take
     * @return true if he can otherwise false
     */
    public boolean canClaimRoute(Route route)
    {
        List<SortedBag<Card>> possibleCards = route.possibleClaimCards();

        for(SortedBag<Card> sb : possibleCards)
        {
            if (this.cards.contains(sb) && carCount()>= route.length())
            {
                return true;
            }
        }
        return  false;
    }

    /**
     * Gives a List of all the cards the player can use to capture the Route
     * @param route the Route to capture
     * @return the requested List
     * @throws IllegalArgumentException if there isn't enough cars to take the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route)
    {
        Preconditions.checkArgument(carCount()>= route.length());
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        List<SortedBag<Card>> allPossibleClaimCards = route.possibleClaimCards();

        allPossibleClaimCards.forEach(sb ->
        {
            if (this.cards.contains(sb))
            {
                possibleClaimCards.add(sb);
            }
        });


        return possibleClaimCards;
    }

    /**
     * Gives the List of all the possible cards the player could use to take control of a tunnel
     * Sorted in the increasing number of LOCOMOTIVES
     *
     * @param additionalCardsCount number of cards to add
     * @param initialCards the initial cards to take possession of a tunnel
     * @param drawnCards the additional cards drawn
     * @return the requested List
     * @throws IllegalArgumentException if additionalCardsCount<=0 or
     *                                  additionalCardsCount>4 or
     *                                  initialCards empty or
     *                                  initialCards has more than two types or
     *                                  there isn't exactly 3 drawnCards
     *
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards)
    {
        Preconditions.checkArgument(1<=additionalCardsCount && additionalCardsCount<= Constants.ADDITIONAL_TUNNEL_CARDS && !initialCards.isEmpty() && initialCards.toSet().size()<=2 && drawnCards.size()==Constants.ADDITIONAL_TUNNEL_CARDS);

        List<Card> stream = cards.stream()
                .filter(c-> initialCards.contains(c) || c==Card.LOCOMOTIVE)
                .collect(Collectors.toList());

        SortedBag<Card> allPossibleAdditionalCards = SortedBag.of(stream).difference(initialCards);

        Set<SortedBag<Card>> optionsSet = new HashSet<>();

        if(additionalCardsCount<=allPossibleAdditionalCards.size())
        {
            optionsSet = allPossibleAdditionalCards.subsetsOfSize(additionalCardsCount);
        }

        List<SortedBag<Card>> options = new ArrayList<>(optionsSet);

        options.sort( Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)) );

        return options;
    }

    /**
     * Creates a new PlayerState with an added Route that was captured
     *
     * @param route the Route taken
     * @param claimCards cards used to take control of the Route
     * @param newTime the time taken by the player
     * @return the requested PlayerState
     *
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards, int newTime)
    {
        List<Route> newRoute = new ArrayList<>(this.routes());
        newRoute.add(route);
        SortedBag<Card> newCards = this.cards.difference(claimCards);
        return new PlayerState(this.tickets(),newCards,newRoute, newTime);
    }

    /**
     * @return Gives the points of all the tickets
     */
    public int ticketPoints()
    {
        int max =0;
        int points=0;
        for (Route r1 : this.routes())
        {
            Station s1 = r1.station1();
            Station s2 = r1.station2();
            max = max( max, max(s1.id(),s2.id()) );
        }

        max++;
        StationPartition.Builder builder = new StationPartition.Builder(max);

        for (Route r1 : this.routes())
        {
            builder.connect(r1.station1(),r1.station2());
        }

        StationPartition partition = builder.build();
        for (Ticket t : this.tickets)
        {
            points+= t.points(partition);
        }

        return points;
    }

    /**
     *
     * @return the total points : claimPoints + ticketPoints
     */
    public int finalPoints()
    {
        return claimPoints()+ticketPoints();
    }

}
