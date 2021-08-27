package ch.epfl.tchu.game;

import java.util.List;

public enum Card
{
    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    public static final List<Card> ALL = List.of(Card.values()); //all of the elements of Card
    public static final int COUNT = ALL.size(); //size of the List All

    //We manually added the elements in CARS as suggested for more flexibility of the code
    //Similar to All without LOCOMOTIVE
    public static final List<Card> CARS = List.of(new Card[]{BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE});

    private final Color color;

    /**
     * We assign to each Card a Color
     *
     * @param newColor the new Color
     */
    Card(Color newColor)
    {
        color = newColor;
    }

    /**
     * Gives the Card with the corresponding Color
     *
     * @param color the color
     * @return the corresponding Card for the color param
     */
    public static Card of(Color color)
    {
        for (Card card : CARS)
        {
            if (card.color().equals(color))
            {
                return card;
            }
        }
        return null;


    }

    /**
     * Gives the color of the Card
     *
     * @return this.color
     */
    public Color color()
    {
        return this.color;
    }


}
