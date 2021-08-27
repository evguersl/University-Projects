package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.List;

public class PublicPlayerState
{
    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    /**
     * Builds the PublicPlayerState
     *
     * @param ticketCount number of tickets of the player
     * @param cardCount number of cards of the player
     * @param routes captured routes by the player
     * @throws IllegalArgumentException if ticketCount or cardCount is negative
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes)
    {
        Preconditions.checkArgument(ticketCount>=0 && cardCount>=0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);

        int tempCarCount = 0;
        int tempClaimPoints = 0;

        for(Route rt : routes)
        {
            tempCarCount += rt.length();
            tempClaimPoints += rt.claimPoints();
        }

        this.carCount = Constants.INITIAL_CAR_COUNT - tempCarCount;
        this.claimPoints = tempClaimPoints;
    }

    /**
     *
     * @return the ticketCount
     */
    public int ticketCount()
    {
        return ticketCount;
    }

    /**
     *
     * @return the cardCount
     */
    public int cardCount()
    {
        return cardCount;
    }

    /**
     *
     * @return the routes captured by the player
     */
    public List<Route> routes()
    {
        return routes;
    }

    /**
     *
     * @return the remaining carCount of the player
     */
    public int carCount()
    {
        return carCount;
    }

    /**
     *
     * @return the claimPoints of the player
     */
    public int claimPoints()
    {
        return claimPoints;
    }

}
