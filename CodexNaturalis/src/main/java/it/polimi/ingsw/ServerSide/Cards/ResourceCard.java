package it.polimi.ingsw.ServerSide.Cards;

public class ResourceCard extends PlayableCard{

    //Constructor
    public ResourceCard(int[] Corners, int id){
        super(Corners, id);
    }

    //Getters
    public int[] getCorners()
    {
        if(this.isFlipped){return new int[]{1, 1, 1, 1};}
        return this.Corners;
    }
    //utility Functions and Template Pattern
    public int[] addPoints(int[] OldPoints) //Points = int[] {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}
    {
        if(this.isFlipped){OldPoints[this.Color]++; return OldPoints;}

        if((this.getID()-1)%10 >=7){OldPoints[0]++;}
        for(int Value: this.Corners){ if(Value>1){OldPoints[Value-1]++;}}

        return OldPoints;
    }

}

