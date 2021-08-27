package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.List;

public final class Info
{

    private final String playerName;


    /**
     * Creates an Info instance with the given parameters
     *
     * @param playerName the player's name
     */
    public Info(String playerName)
    {
        this.playerName = playerName;

    }

    /**
     * Gives the french name of a Card with plural or not
     *
     * @param card we seek the name of this Card
     * @param count the number of appearances of this Card
     * @return the requested name String
     */
    public static String cardName(Card card, int count)
    {
        switch (card) {
            case BLACK:
                return StringsFr.BLACK_CARD + StringsFr.plural(count);
            case BLUE:
                return StringsFr.BLUE_CARD + StringsFr.plural(count);
            case GREEN:
                return StringsFr.GREEN_CARD + StringsFr.plural(count);
            case VIOLET:
                return StringsFr.VIOLET_CARD + StringsFr.plural(count);
            case YELLOW:
                return StringsFr.YELLOW_CARD + StringsFr.plural(count);
            case ORANGE:
                return StringsFr.ORANGE_CARD + StringsFr.plural(count);
            case RED:
                return StringsFr.RED_CARD + StringsFr.plural(count);
            case WHITE:
                return StringsFr.WHITE_CARD + StringsFr.plural(count);
            default:
                return StringsFr.LOCOMOTIVE_CARD + StringsFr.plural(count);
        }

    }

    /**
     * Display when there's a DRAW
     *
     * @param playerNames a List of the players name
     * @param points the number of the points that each player got
     * @return the requested display
     */
    public static String draw(List<String> playerNames, int points)
    {
        String names = playerNames.get(0)+StringsFr.AND_SEPARATOR+playerNames.get(1);
        return String.format(StringsFr.DRAW, names, points);
    }

    /**
     *
     * @return The WILL_PLAY_FIRST String display
     */
    public String willPlayFirst()
    {
        return String.format(StringsFr.WILL_PLAY_FIRST, this.playerName);
    }

    /**
     *
     * @param count the number of kept Tickets
     * @return The KEPT_N_TICKETS String display
     */
    public String keptTickets(int count)
    {
        return String.format(StringsFr.KEPT_N_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     *
     * @return the CAN_PLAY String display
     */
    public String canPlay()
    {
        return String.format(StringsFr.CAN_PLAY, this.playerName);
    }

    /**
     *
     * @param count the number of drew Tickets
     * @return the DREW_TICKETS String display
     */
    public String drewTickets(int count)
    {
        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     *
     * @return the DREW_BLIND_CARD String display
     */
    public String drewBlindCard()
    {
        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);
    }

    /**
     *
     * @param card the card to display
     * @return the DREW_VISIBLE_CARD String display
     */
    public String drewVisibleCard(Card card)
    {
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, cardName(card,1));
    }

    /**
     *
     * @param route the route to claim
     * @param cards the cards to take control of the route
     * @return the CLAIMED_ROUTE String display
     */
    public String claimedRoute(Route route, SortedBag<Card> cards)
    {
        return String.format(StringsFr.CLAIMED_ROUTE, this.playerName, routeName(route), cardDescription(cards));
    }

    /**
     *
     * @param route the route to claim
     * @param initialCards the cards to take control of the tunnel
     * @return the ATTEMPTS_TUNNEL_CLAIM String display
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards)
    {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName, routeName(route), cardDescription(initialCards));
    }

    /**
     *
     * @param drawnCards the additional cards to play
     * @param additionalCost the additional cost
     * @return the ADDITIONAL_CARDS_ARE String display with the NO_ADDITIONAL_COST or the SOME_ADDITIONAL_COST display
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost)
    {
        String message = String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardDescription(drawnCards));

        if(0==additionalCost)
        {
            return message + StringsFr.NO_ADDITIONAL_COST;
        }
        else
        {
            return message + String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
        }
    }

    /**
     *
     * @param route the route that hasn't been claimed
     * @return the DID_NOT_CLAIM_ROUTE String display
     */
    public String didNotClaimRoute(Route route)
    {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playerName, routeName(route));
    }

    /**
     *
     * @param carCount the remaining car count of the player
     * @return the LAST_TURN_BEGINS String display
     */
    public String lastTurnBegins(int carCount)
    {
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     *
     * @param longestTrail the longest Trail of the game
     * @return the GETS_BONUS String display
     */
    public String getsLongestTrailBonus(Trail longestTrail)
    {
        String trailName = longestTrail.station1().name() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().name();

        return String.format(StringsFr.GETS_BONUS, this.playerName, trailName);
    }

    /**
     *
     * @param points the winner points
     * @param loserPoints the looser points
     * @return the WINS String display
     */
    public String won(int points, int loserPoints)
    {
        return String.format(StringsFr.WINS, this.playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    private static String routeName(Route route)
    {
        return route.station1().name()+StringsFr.EN_DASH_SEPARATOR+route.station2().name();
    }

    /**
     *
     * @param time the shortest time of the game
     * @return the GETS_TIME_BONUS String display
     */
    public String getTimeBonus(int time)
    {
        return String.format(StringsFr.GETS_TIME_BONUS, this.playerName, time);
    }

    /**
     *
     * @param cards the sorted bag of cards you want to display
     * @return A covinient way to to represent a sorted bag of cards in a string
     */
     public static String cardDescription(SortedBag<Card> cards)
    {
        StringBuilder description = new StringBuilder();
        int counter = 0;

        for (Card c: cards.toSet())
        {
            int n = cards.countOf(c);
            if(0==counter)
            {
                description.append(n).append(" ").append(cardName(c, n));
                counter++;
            }
            else if(cards.toSet().size()-1!=counter)
            {
                description.append(", ").append(n).append(" ").append(cardName(c, n));
                counter++;
            }
            else
            {
                description.append(StringsFr.AND_SEPARATOR).append(n).append(" ").append(cardName(c, n));
            }
        }

        return description.toString();
    }


}
