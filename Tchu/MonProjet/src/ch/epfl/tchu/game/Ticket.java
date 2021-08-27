package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.*;

public final class Ticket implements Comparable<Ticket>
{
    private final List<Trip> trips;
    private final String text;

    /**
     * Ticket constructor
     *
     * @param trips List of all the Trips
     * @throws IllegalArgumentException if the size of the argument List is equal to zero or
     *                                  if the from Stations don't all have the same name
     */
    public Ticket(List<Trip> trips)
    {
        Preconditions.checkArgument(trips.size()!=0 && allSameNames(trips));
        this.trips = List.copyOf(trips);
        text = computeText(trips);
    }

    /**
     * Create a single Ticket and calling the Ticket(List<Trip> trips) constructor
     *
     * @param from Station1
     * @param to Station2
     * @param points if the two Stations are successfully connected
     */
    public Ticket(Station from, Station to, int points)
    {
        this( Collections.singletonList(new Trip(from, to, points)) );
    }

    /**
     * Gives the text display a Ticket
     *
     * @return text
     */
    public String text()
    {
        return text;
    }

    /**
     * Builds the text display of a given Ticket
     *
     * @param trips List of the trips
     * @return String display
     */
    private static String computeText(List<Trip> trips)
    {
        int tripsSize = trips.size();

        String startName = trips.get(0).from().name();
        TreeSet<String> s = new TreeSet<>();

        //Station to Station
        if (tripsSize == 1)
        {
            String arrival = trips.get(0).to().name();
            int nbPoints = trips.get(0).points();

            return String.format("%s - %s (%s)", startName, arrival, nbPoints);
        }
        //Station to Country or Country to Country
        else
        {
            for (Trip value : trips)
            {
                String to = value.to().name();
                int points = value.points();
                s.add(String.format("%s (%s)", to, points));
            }

            String trip = String.join(", ", s);
            return String.format("%s - {%s}",startName, trip);
        }
    }

    /**
     * Checks if all the departure stations have the same name
     *
     * @param trips List of the trips
     * @return boolean true if the departures all have the same name, false otherwise
     */
    private static boolean allSameNames(List<Trip> trips)
    {
        Set<String> nameSet = new HashSet<>();
        trips.forEach(trip -> nameSet.add(trip.from().name()));
        return nameSet.size() == 1;
    }

    /**
     * Gives the number of points of a Ticket
     *
     * @param connectivity of the interface StationConnectivity
     * @return corresponfing points
     */
    public int points (StationConnectivity connectivity)
    {
        int tripSize = trips.size();

        //Station to Station
        if(1 == tripSize)
        {
            return trips.get(0).points(connectivity);
        }
        //Station to Country or Country to Country
        else
        {
            int max = 0;
            int min = 0;

            //for loop to determine the max and the min
            for (Trip trip : trips)
            {
                int nbPoints = trip.points(connectivity);

                if (nbPoints < 0 && (nbPoints > min || 0 == min))
                {
                    min = nbPoints;
                }
                else if (nbPoints > 0 && nbPoints > max)
                {
                    max = nbPoints;
                }
            }
            return 0==max ? min : max;
        }

    }

    /**
     *
     * @return the List<Trip></Trip> of a Tiket
     */

    public List<Trip> getTrips() {
        return List.copyOf(trips);
    }

    /**
     * Compares the text between two Tickets
     * @param that argument Ticket
     * @return <0 if this.text before that.text 0 if this.text==that.text >0 otherwise
     */
    @Override
    public int compareTo(Ticket that)
    {
        return this.text.compareTo(that.text);
    }

    @Override
    public String toString()
    {
        return text();
    }
}
