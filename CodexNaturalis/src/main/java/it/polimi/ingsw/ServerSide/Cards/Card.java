package it.polimi.ingsw.Cards;

public abstract class Card {
    protected boolean isFlipped;
    protected int ID;

    //Constructor
    public Card(int ID){  this.ID = ID; this.isFlipped = false; }


    public Boolean getFlipStatus(){return this.isFlipped;}
    public void Flip(){this.isFlipped = !this.isFlipped;}
    public void SetFlipped(Boolean value){this.isFlipped = value;}



    public int getID(){return this.ID;}


}
