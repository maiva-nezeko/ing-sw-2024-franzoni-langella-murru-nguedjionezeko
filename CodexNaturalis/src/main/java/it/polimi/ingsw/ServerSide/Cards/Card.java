
package main.java.it.polimi.ingsw.ServerSide.Cards;

public abstract class Card {
    protected boolean isFlipped;
    protected int ID;

    //Constructor
    public Card(int ID){  this.ID = ID; this.isFlipped = false; }
    public int getID(){return this.ID;}


}
