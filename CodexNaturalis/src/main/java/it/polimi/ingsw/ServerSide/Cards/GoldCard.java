package main.java.it.polimi.ingsw.ServerSide.Cards;


import main.java.it.polimi.ingsw.ServerSide.Cards.Enums.PlayCondition;
import main.java.it.polimi.ingsw.ServerSide.Cards.Enums.PointCondition;

public class GoldCard extends PlayableCard {
    protected final PlayCondition PlayCond;
    protected final PointCondition PointCond;

    public GoldCard(int[] Corners, int id, PointCondition PointCond, PlayCondition PlayCond) {
        super(Corners, id);
        this.PlayCond = PlayCond;
        this.PointCond = PointCond;
    }

    public PointCondition getPointCond(){return this.PointCond;}

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
