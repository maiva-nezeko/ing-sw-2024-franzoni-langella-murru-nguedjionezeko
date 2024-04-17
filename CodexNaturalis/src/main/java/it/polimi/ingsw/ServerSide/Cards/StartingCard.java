package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Cards.Enums.StartingPoints;

public class StartingCard extends ResourceCard {
    private final int[] AlternateCorners;
    private final StartingPoints SP;

    public StartingCard(int[] Corners, int id, int[] AlternativeCorners, StartingPoints SP)
    {
        super(Corners, id);
        this.AlternateCorners = AlternativeCorners;
        this.SP = SP;
    }

    public int[] getCorners()
    {
        if(this.isFlipped){return this.AlternateCorners;}
        return this.Corners;
    }


    public int[] addPoints(int[] OldPoints)
    {
        if(this.isFlipped){OldPoints[this.Color]++; return OldPoints;}

        for(int Value: getCorners()){ if(Value>1){OldPoints[Value-1]++;}}

        switch (this.SP) {
            case STARTING_ONE_PURPLE: OldPoints[4]++; break;
            case STARTING_ONE_RED : OldPoints[1]++; break;
            case STARTING_ONE_RED_ONE_GREEN : OldPoints[1]++; OldPoints[3]++; break;
            case STARTING_ONE_BLUE_ONE_PURPLE : OldPoints[2]++; OldPoints[4]++; break;
            case STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN : OldPoints[2]++; OldPoints[3]++; OldPoints[4]++; break;
            case STARTING_ONE_GREEN_ONE_BLUE_ONE_RED: OldPoints[1]++; OldPoints[2]++; OldPoints[3]++; break;
        }

        return OldPoints;
    }
}
