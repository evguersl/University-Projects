package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class  Deck<C extends Comparable<C>>
{

    /**
     * All of the cards of the Deck
     */
    private final List<C> multiensembleCartes;

    private Deck(List<C> cards)
    {
        multiensembleCartes=List.copyOf(cards);
    }

    /**
     * Shuffles a given SortedBag
     *
     * @param cards the SortedBag to shuffle
     * @param rng the source of randomness to use to shuffle the list
     * @param <C> must extends a Comparable
     * @return a Deck with the initial SortedBag shuffled
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng)
    {
        List<C> tempList =cards.toList();
        Collections.shuffle(tempList,rng);
        return new Deck<>(tempList);
    }

    /**
     * Gives the size of the Deck
     *
     * @return the requested size
     */
    public int size()
    {
        return this.multiensembleCartes.size();
    }

    /**
     * Checks if the Deck is empty or not
     *
     * @return true if the Deck is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return 0==size();
    }

    /**
     * Gives the top element of our Deck
     *
     * @return the requested element
     * @throws IllegalArgumentException if the deck is empty
     */
    public C topCard()
    {
        Preconditions.checkArgument(0!=size());

        return this.multiensembleCartes.get(this.multiensembleCartes.size()-1);
    }

    /**
     * A new Deck without it's previous top element
     *
     * @return the requested new Deck
     * @throws IllegalArgumentException if the deck is empty
     */
    public Deck<C> withoutTopCard()
    {
        Preconditions.checkArgument(0!=size());
        return new Deck<>(this.multiensembleCartes.subList(0,size()-1));
    }

    /**
     * Gives the "count" first elements of the Deck
     *
     * @param count number of the top elements of the Deck requested
     * @return the requested SortedBag
     * @throws IllegalArgumentException if count is not between 0 (included) and the deck size(included)
     */
    public SortedBag<C> topCards(int count)
    {
        Preconditions.checkArgument(0<=count && count<=size());

        List<C> l1 = this.multiensembleCartes.subList(size()-count,size());

        return SortedBag.of(l1);
    }

    /**
     * Gives a new Deck without the "count" first elements of the old Deck
     *
     * @param count number of the top elements of the Deck requested
     * @return the requested new Deck
     * @throws  IllegalArgumentException if count is not between 0 (included) and the deck size(included)
     */
    public Deck<C> withoutTopCards(int count)
    {
        Preconditions.checkArgument(0<=count && count<=size());
        return new Deck<>(this.multiensembleCartes.subList(0,size()-count));
    }

}
