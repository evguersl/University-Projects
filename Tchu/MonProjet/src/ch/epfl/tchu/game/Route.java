package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;



public final class Route
{

    /**
     * Whether the Route is OVERGROUND or UNDERGROUND
     */
    public enum Level
    {
        OVERGROUND,
        UNDERGROUND
    }

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * Creates a Route between two Stations with a specified id, length, level and color
     *
     * @param id must be non null
     * @param station1 must be non null and different name than station2
     * @param station2 must be non null and different name than station1
     * @param length between MIN_ROUTE_LENGTH and MAX_ROUTE_LENGTH
     * @param level must be non null
     * @param color of the class Color
     * @throws IllegalArgumentException see conditions above
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color)
    {
        Preconditions.checkArgument( !( station1.id()==station2.id() ) && Constants.MIN_ROUTE_LENGTH<=length && length<=Constants.MAX_ROUTE_LENGTH);

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.level = Objects.requireNonNull(level);

        this.length = length;
        this.color = color;
    }

    /**
     * Gives the Route id
     * @return id
     */
    public String id()
    {
        return id;
    }

    /**
     * Gives station1 of the Route
     *
     * @return station1
     */
    public Station station1()
    {
        return station1;
    }

    /**
     * Gives station2 of the Route
     *
     * @return station2
     */
    public Station station2()
    {
        return station2;
    }

    /**
     * Gives the length of the Route
     *
     * @return length
     */
    public int length()
    {
        return length;
    }

    /**
     * Gives the Route Level
     *
     * @return level
     */
    public Level level()
    {
        return level;
    }

    /**
     * Gives the color of the Route
     *
     * @return color
     */
    public Color color()
    {
        return color;
    }

    /**
     * ArrayList with station1 and station2
     *
     * @return the corresponding List
     */
    public List<Station> stations()
    {
        return List.of(station1,station2);
    }

    /**
     * Gives the opposite Station of the specified Station (Route has two Stations)
     *
     * @param station the Station we wish to have the opposite
     * @return the opposite Station of the argument
     * @throws IllegalArgumentException if the station param isn't in the Route
     */
    public Station stationOpposite(Station station)
    {
        boolean isStation1 = station==station1;
        boolean isStation2 = station==station2 ;
        Preconditions.checkArgument(isStation1 || isStation2);
        return isStation1 ? station2:station1;

    }

    /**
     * Gives all the possible Card combinations to capture the Route
     *
     * @return a List with all the possible combinations
     */
    public List<SortedBag<Card>> possibleClaimCards()
    {
        //OVERGROUND
        if( 0==this.level.compareTo(Level.OVERGROUND) )
        {
            //Color not null
            if(this.color!=null)
            {
                return new ArrayList<>(Collections.singleton(SortedBag.of(this.length,Card.of(this.color))));
            }
            //Color null
            else
            {
                return overgroundColorNull();
            }
        }
        //UNDERGROUND
        else
        {
            //Color not null
            if(this.color!=null)
            {
                return undergroundColorNotNull();
            }
            //Color null
            else
            {
                return undergroundColorNull();
            }
        }
    }

    private List<SortedBag<Card>> overgroundColorNull()
    {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();

        for(Card card : Card.CARS)
        {
            possibleClaimCards.add(SortedBag.of(this.length,card));
        }

        return possibleClaimCards;
    }

    private List<SortedBag<Card>> undergroundColorNotNull()
    {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();

        for(int i=0; i<this.length; i++)
        {
            if(0==i)
            {
                possibleClaimCards.add(SortedBag.of(length-i,Card.of(this.color)));
                continue;
            }
            possibleClaimCards.add(SortedBag.of(length-i,Card.of(this.color),i,Card.LOCOMOTIVE));
        }

        possibleClaimCards.add(SortedBag.of(this.length,Card.LOCOMOTIVE));

        return possibleClaimCards;
    }

    private List<SortedBag<Card>> undergroundColorNull()
    {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();

        for(int i=0; i<this.length; i++)
        {
            for(Card card : Card.CARS)
            {
                if(0==i)
                {
                    possibleClaimCards.add(SortedBag.of(length-i,card));
                    continue;
                }
                possibleClaimCards.add(SortedBag.of(length-i,card,i,Card.LOCOMOTIVE));
            }
        }

        possibleClaimCards.add(SortedBag.of(this.length,Card.LOCOMOTIVE));

        return possibleClaimCards;
    }

    /**
     * Gives the additional Cards to play if you want to capture a tunnel Route
     *
     * @param claimCards the Cards you initially chose to capture the tunnel Route
     * @param drawnCards the Cards picked in the deck
     *
     * @return the additional Cards to play
     * @throws IllegalArgumentException if the Route isn't a tunnel or there aren't 3 Cards in the drawnCards param
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards)
    {
        boolean checkIfTunnel = ( 0==this.level.compareTo(Level.UNDERGROUND) );
        boolean check3Cards = ( Constants.ADDITIONAL_TUNNEL_CARDS==drawnCards.size() );

        Preconditions.checkArgument(checkIfTunnel && check3Cards);

        int compteur=0;

        if(claimCards.countOf(Card.LOCOMOTIVE)!=this.length)
        {
            int i=0;
            while(claimCards.countOf(Card.CARS.get(i))==0)
            {
                i++;
            }
            compteur+=drawnCards.countOf(Card.CARS.get(i));
        }
        compteur+=drawnCards.countOf(Card.LOCOMOTIVE);

        return compteur;
    }

    /**
     * Gives the points associated to the Route
     *
     * @return the requested points
     */
    public int claimPoints()
    {
        return Constants.ROUTE_CLAIM_POINTS.get(this.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, station1, station2);
    }




}