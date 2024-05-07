package main.java.it.polimi.ingsw.ServerSide.Cards;

/**
 * The PlayableCard type Resource card. It's just like a PlayableCard, but it's not abstract.
 */
public class ResourceCard extends PlayableCard{

    //Constructor

    /**
     * Instantiates a new Resource Card.
     *
     * @param Corners the corners that characterize the card
     * @param id      the unique id
     */
    public ResourceCard(int[] Corners, int id){
        super(Corners, id);
    }

    //Getters

    /**
     * ResourceCard.getCorners first checks if the Card is flipped, which would result in the
     * corners all being set to Blank;
     * Otherwise keeps the corners as set in PlayableCard (also see Table.Deck)
     *
     * @return the corners for a specific card as an int [ ]
     */
    public int[] getCorners()
    {
        if(this.isFlipped){return new int[]{1, 1, 1, 1};}
        return this.Corners;
    }
    //utility Functions and Template Pattern

    /**
     * ResourceCard.addPoints first checks if the Card is flipped, which would result in only
     * having one resource in the center and none at the corners;
     * Otherwise, we check for every corner and update OldPoints accordingly, keeping in mind
     * the +1 offset between values assigned to the corners (that include No corner and Blank)
     * and the Points index (starting from 1 = Red).
     *
     * @param OldPoints the different resources visible on the Player's board (listed)
     * @return the updated visible resources array as an int [ ]
     */
    public int[] addPoints(int[] OldPoints) //Points = int[] {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}
    {
        if(this.isFlipped){OldPoints[this.Color]++; return OldPoints;}

        if((this.getID()-1)%10 >=7){OldPoints[0]++;}
        for(int Value: this.Corners){ if(Value>1){OldPoints[Value-1]++;}}

        return OldPoints;
    }

}

