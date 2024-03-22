package it.polimi.ingsw.Cards;

public class ResourceCard extends Card{

    protected int[] Corners; //CornerValues 0=No Corner, 1=Blank, 2=Red, 3=Blue, 4=Green, 5=Purple, 6=Feather, 7=Salt, 8=Paper
    protected int Color;

    //Constructor
    public ResourceCard(int[] Corners, int id){
        super(id);
        this.Corners = Corners;
        this.Color =  id/20 + 1; //0-19 Red==1, 20-39 Blue==2, 40-59 Green==3, 60-79 Purple==4
    }


    //Getters
    public int[] getCorners()
    {
        if(this.isFlipped){return new int[]{1, 1, 1, 1};}
        return this.Corners;
    }
    public int getColor(){return this.Color;}

    //utility Functions and Template Pattern
    public int[] addPoints(int[] OldPoints) //Points = int[] {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}
    {
        if(this.isFlipped){OldPoints[this.Color]++; return OldPoints;}

        for(int Value: this.Corners){ if(Value>1){OldPoints[Value-1]++;}}

        return OldPoints;
    }

    public Boolean isPlayable(int[] OldPoints){return true;}
}

