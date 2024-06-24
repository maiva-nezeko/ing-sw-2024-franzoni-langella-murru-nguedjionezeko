package main.java.it.polimi.ingsw.ServerSide.Cards;

/**
 * The abstract type Card, the "template" for all different
 * types of cards in Deck.
 */
public abstract class Card {
    /**
     * The boolean indicating if the Player wants to play the front or
     * the back of the card.
     */
    protected boolean isFlipped;
    /**
     * The unique ID that identifies a Card among all the cards in the Deck.
     */
    protected final int ID;

    //Constructor
    /**
     * Instantiates a new Card.
     *
     * @param ID the unique id
     */
    public Card(int ID){  this.ID = ID; this.isFlipped = false; }

    /**
     * Gets a specific Card id.
     *
     * @return the id as an int
     */
    public int getID(){return this.ID;}


}