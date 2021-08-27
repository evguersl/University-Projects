package ch.epfl.tchu.game;
import ch.epfl.tchu.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip
{
    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Construct a trip between two stations
     *
     * @param from Station1
     * @param to Station2
     * @param points if the two Stations are successfully connected
     * @throws IllegalArgumentException if points<=0
     * @throws NullPointerException if from or to null
     */
    public Trip(Station from, Station to, int points)
    {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        Preconditions.checkArgument(points>0);
        this.points=points;
    }

    /**
     * Builds a List of Trips between a List departures and a List of arrivals assigning n points if
     * they are later successfully connected
     *
     * @param from departures
     * @param to arrivals
     * @param points in case of successful connection
     * @return the List with all the trips
     * @throws IllegalArgumentException if the size of one of the List is equal to 0 or if points is <=0
     *
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points)
    {
        Preconditions.checkArgument(from.size()!=0 && to.size()!=0 && points>0);
        List<Trip> all = new ArrayList<>();

        for (Station s1 : from)
        {
            for (Station s2 : to)
            {
                all.add(new Trip (s1,s2,points));
            }
        }
        return all;
    }

    /**
     * Gives the departure Station
     *
     * @return from
     */
    public Station from()
    {
        return from;
    }

    /**
     * Gives the arrival Station
     *
     * @return to
     */
    public Station to ()
    {
        return to;
    }

    /**
     * Gives the number of points in case of successful connection
     *
     * @return points
     */
    public int points ()
    {
        return points;
    }

    /**
     * Gives a positive or negative nummber of points depending on whether the from and to stations are connected
     *
     * @param connectivity of the interface StationConnectivity
     * @return positive or negative number of points
     */
    public int points(StationConnectivity connectivity)
    {
        if (connectivity.connected(from,to))
        {
            return points;
        }
        return -points;
    }

}






