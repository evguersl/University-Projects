package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;



public final class CardState extends PublicCardState
{


    private final Deck<Card> deck;

    private final SortedBag<Card> discard;

    private CardState( List<Card> faceup,Deck<Card> pioche, SortedBag<Card> defausse)
    {
        super(faceup,pioche.size(), defausse.size());
        this.deck = pioche;
        this.discard = defausse;

    }

    /**
     * Initialise the CardState with the FACE_UP_CARDS_COUNT first cards of the deck as face up cards, The deck is filed with the remaining Cards
     *
     * @param deck the initial deck of Cards
     * @return A CardState
     * @throws IllegalArgumentException if the deck size is less than FACE_UP_CARDS_COUNT
     */

    public static CardState of(Deck<Card> deck)
    {
    Preconditions.checkArgument(deck.size()>= Constants.FACE_UP_CARDS_COUNT);
    List<Card> faceUpCards = deck.topCards(Constants.FACE_UP_CARDS_COUNT)
                                .toList();
    Deck<Card> deckCards = deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT);

    return new CardState(faceUpCards,deckCards,SortedBag.of());
    
    }

    /**
     * Replace the face up card index "slot" by the card at the top of the Deck
     * @param slot, the index of the cards we want to replace in the face up cards
     * @return A CarState (identical to this) with the new face up cards
     * @throws IndexOutOfBoundsException if the parameter slot is not between 0(include) and FACE_UP_CARDS_COUNT;
     * @throws IllegalArgumentException if the deck is not empty
     */

    public CardState withDrawnFaceUpCard(int slot)
    {
        if (slot<0 || slot>=Constants.FACE_UP_CARDS_COUNT)
        {
            throw new IndexOutOfBoundsException();
        }
        Preconditions.checkArgument(deck.size()!=0);

        Card carte = this.deck.topCard();
        Deck<Card> nouvellePioche = this.deck.withoutTopCard();
        List<Card> faceUpCard= new ArrayList<>(this.faceUpCards());
        faceUpCard.set(slot,carte);
        return new CardState(faceUpCard,nouvellePioche,this.discard);
    }

    /**
     * Gives the card at the top of the deck
     * @return The Card at the top of the deck
     * @throws IllegalArgumentException if the deck is empty
     */
    public Card topDeckCard()
    {
        Preconditions.checkArgument(this.deck.size()!=0);
        return this.deck.topCard();
    }

    /**
     * Gives a CardState, without the first card of the deck
     * @return a  CardState (identical to this) with the desired change on the deck
     * @throws IllegalArgumentException if the deck is empty
     */
    public CardState withoutTopDeckCard()
    {
        Preconditions.checkArgument(this.deck.size()!=0);
        Deck<Card> nouvellePioche= this.deck.withoutTopCard();
        return new CardState(this.faceUpCards(),nouvellePioche,this.discard);
    }

    /**
     * Gives a CardState replacing the empty deck by a random distribution of the discards
     * @param rng an Random object
     * @return a CardState(identical to this) with no discards, and a new deck made with the old discards
     * @throws IllegalArgumentException if the deck is not empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng)
    {
        Preconditions.checkArgument(this.deck.size()==0);
        return new CardState(this.faceUpCards(),Deck.of(this.discard,rng),SortedBag.of());
    }

    /**
     *  Add all the cards of a SortedBag of Cards to the discards
      * @param additionalDiscards SortedBag filed with the cards you want to add to the discards
     * @return a CardState (identical to this) with new discards
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards)
    {
        SortedBag<Card> newDiscard = SortedBag.of(additionalDiscards.union(this.discard));
        return new CardState(this.faceUpCards(),this.deck,newDiscard);
    }



}
