package it.polimi.ingsw.ServerSide.Cards;

//import it.polimi.ingsw.ServerSide.Cards.Card;
/**
 * The Card type Playable card. A Card that the Players are able to play on their board.
 */
public abstract class PlayableCard extends Card{

    /**
     * The Corners listed clockwise from the top left. The numbers indicate the resource or,
     * if not present, the type of corner (NO corner, White/Blank).
     */
    protected final int[] Corners; //CornerValues 0=No Corner, 1=Blank, 2=Red, 3=Blue, 4=Green, 5=Purple, 6=Feather, 7=Salt, 8=Paper
    /**
     * The Color associated with a card, useful for checking "same-as-card" PlayConditions.
     */
    protected int Color;

    /**
     * Instantiates a new Playable card.
     *
     * @param Corners the corners that characterize the card
     * @param id      the unique id
     *                The id can also be used to identify the color of the Card
     *                because of the way they are listed in Table.Deck.
     */
    public PlayableCard(int[] Corners, int id){
        super(id);
        this.Corners = Corners;
        this.Color =  (id-1)/10; //1-10,41-50 Red==0, 11-20,51-60 Blue==1, 21-30,61-70 Green==2, 31-40,71-80 Purple==3
        if(this.Color>=4) {this.Color -= 4;}
    }

//Getters

    /**
     * Get corners method gets the Corner's array.
     * Different Card types have different implementations for this method.
     *
     * @return the corners for a specific card as an int [ ]
     */
    public abstract int[] getCorners();

    /**
     * Get color is able to determine a Card's color.
     *
     * @return the int representing the Color (as seen above)
     */
    public int getColor(){return this.Color;}

    /**
     * Add points method adds or subtracts resources points after a Card is played.
     * Different Card types have different implementations for this method.
     *
     * @param OldPoints the different resources visible on the Player's board (listed)
     *                  in order: {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}
     * @return the updated visible resources array as an int [ ]
     */
    public abstract int[] addPoints(int[] OldPoints); //Points = int[] {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}

    /**
     * Is Playable method questions whether a Card is playable in a certain position.
     *
     * @param OldPoints the different resources visible on the Player's board (listed)
     * @return the answer as a boolean
     */
    public Boolean isPlayable(int[] OldPoints){return true;}

    /**
     * Set Card as flipped.
     *
     * @param isFlipped the boolean that indicates whether the Card is flipped or not
     */
    public void setFlipped(boolean isFlipped){this.isFlipped = isFlipped;}

    /**
     * isFlipped returns the current flipped status of the selected card
     *
     * @return the boolean value representing the flipped status
     */
    public boolean isFlipped(){return this.isFlipped;}
}
