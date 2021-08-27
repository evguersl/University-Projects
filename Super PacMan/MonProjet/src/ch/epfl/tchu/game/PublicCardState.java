package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;


import java.util.List;
import java.util.Objects;



public class PublicCardState
{
    private final int deckSize;
    private final int discardsSize;
    private final List<Card> faceUpCards;

    /**
     * Creates a PublicCardState instance with the given parameters
     *
     * @param faceUpCards a List of the face up Cards
     * @param deckSize the size of the deck
     * @param discardsSize the size of the discards
     * @throws IllegalArgumentException if there aren't 5 cards in the faceUpCards List,
     *          and if the deck size or the discard size is strictly less than 0
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize)
    {
        Preconditions.checkArgument(Constants.FACE_UP_CARDS_COUNT==faceUpCards.size() && 0<=deckSize && 0<=discardsSize);

        this.deckSize = deckSize;
        this.discardsSize = discardsSize;

        this.faceUpCards= List.copyOf(faceUpCards);
    }



    /**
     * Gives the List of the faceUpCards
     *
     * @return the requested List of faceUpCards
     */
    public List<Card> faceUpCards()
    {
        return this.faceUpCards;
    }

    /**
     * Gives a requested Card of the faceUpCard List
     *
     * @param slot the index of the requested Card
     * @return the requested Card
     * @throws IndexOutOfBoundsException if slot isn't between 0 (included) and 5 (excluded)
     */

    public Card faceUpCard(int slot)
    {
        Objects.checkIndex(slot,Constants.FACE_UP_CARDS_COUNT);
        return faceUpCards.get(slot);
    }

    /**
     * Gives the size od the Deck
     *
     * @return the requested size
     */
    public int deckSize()
    {
        return this.deckSize;
    }

    /**
     * Checks if the Deck is empty or not
     *
     * @return true if it is empty and false otherwise
     */
    public boolean isDeckEmpty()
    {
        return 0==deckSize();

    }

    /**
     * Gives the size of the discard
     *
     * @return the requested size
     */
    public int discardsSize()
    {
        return discardsSize;
    }

}
