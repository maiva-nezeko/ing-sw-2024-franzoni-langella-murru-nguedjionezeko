package it.polimi.ingsw.ServerSide.Cards;


import it.polimi.ingsw.ServerSide.Cards.Enums.PlayCondition;
import it.polimi.ingsw.ServerSide.Cards.Enums.PointCondition;

public class GoldCard extends PlayableCard {
    /**
     * The Play Condition as seen in Cards.Enums.PlayCondition.
     */
    protected final PlayCondition PlayCond;
    /**
     * The Point Condition as seen in Cards.Enums.PointCondition.
     */
    protected final PointCondition PointCond;

    /**
     * Instantiates a new Gold card.
     *
     * @param Corners   the corners that characterize the card
     * @param id        the unique id
     * @param PointCond the condition to assign the points
     * @param PlayCond  the condition to play the card
     */
    public GoldCard(int[] Corners, int id, PointCondition PointCond, PlayCondition PlayCond) {
        super(Corners, id);
        this.PlayCond = PlayCond;
        this.PointCond = PointCond;
    }

    /**
     * Gets the points condition called PointCond as type PointCondition.
     *
     * @return the point condition
     */
    public PointCondition getPointCond(){return this.PointCond;}

    /**
     * GoldCard.getCorners first checks if the Card is flipped, which would result in the
     * corners all being set to Blank;
     * Otherwise keeps the corners as set in PlayableCard (also see Table.Deck)
     *
     * @return the corners for a specific card as an int [ ]
     */
    public int[] getCorners() {
        if(this.isFlipped){return new int[]{1, 1, 1, 1};}
        return this.Corners;
    }

    //Utility Function
    public int[] addPoints(int[] OldPoints)
    {
        if(this.isFlipped){OldPoints[this.Color]++; return OldPoints;}

        for(int Value: this.Corners){ if(Value>1){OldPoints[Value-1]++;}}

        switch (this.PointCond)
        {
            case ONE_POINT_FLAT: OldPoints[0]++; break;

            case THREE_POINT_FLAT: OldPoints[0]+=3; break;

            case FIVE_POINT_FLAT: OldPoints[0]+=5; break;

            case ONE_FOR_FEATHER: OldPoints[0]+=OldPoints[5]; break;

            case ONE_FOR_SALT: OldPoints[0]+=OldPoints[6]; break;

            case ONE_FOR_PAPER: OldPoints[0]+=OldPoints[7]; break;

            //checked after calling addPoints in TableManager
            // case TWO_FOR_CORNER: OldPoints[0]+=OldPoints[5]; break;

        }

        return OldPoints;

    }

    public Boolean isPlayable(int[] OldPoints)
    {
        /* if the card is flipped there is no need to further check if is playable */
        if(this.isFlipped){return true;}

        return switch (this.PlayCond) {
            case TWO_SAME_ONE_RED -> (OldPoints[1] > 0 && OldPoints[this.Color] >= 2);
            case TWO_SAME_ONE_BLUE -> (OldPoints[2] > 0 && OldPoints[this.Color] >= 2);
            case TWO_SAME_ONE_GREEN -> (OldPoints[3] > 0 && OldPoints[this.Color] >= 2);
            case TWO_SAME_ONE_PURPLE -> (OldPoints[4] > 0 && OldPoints[this.Color] >= 2);
            case THREE_SAME -> (OldPoints[this.Color] >= 3);
            case THREE_SAME_ONE_RED -> (OldPoints[1] > 0 && OldPoints[this.Color] >= 3);
            case THREE_SAME_ONE_BLUE -> (OldPoints[2] > 0 && OldPoints[this.Color] >= 3);
            case THREE_SAME_ONE_GREEN -> (OldPoints[3] > 0 && OldPoints[this.Color] >= 3);
            case THREE_SAME_ONE_PURPLE -> (OldPoints[4] > 0 && OldPoints[this.Color] >= 3);
            case FIVE_SAME -> (OldPoints[this.Color] >= 5);
        };
    }

}
