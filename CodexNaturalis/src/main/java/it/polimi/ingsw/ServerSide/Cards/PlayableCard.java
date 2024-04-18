package it.polimi.ingsw.ServerSide.Cards;

public abstract class PlayableCard extends Card{

    protected int[] Corners; //CornerValues 0=No Corner, 1=Blank, 2=Red, 3=Blue, 4=Green, 5=Purple, 6=Feather, 7=Salt, 8=Paper
    protected int Color;
    public PlayableCard(int[] Corners, int id){
        super(id);
        this.Corners = Corners;
        this.Color =  (id-1)/20 + 1; //0-19 Red==1, 20-39 Blue==2, 40-59 Green==3, 60-79 Purple==4
    }
    //Getters
    public abstract int[] getCorners();
    public int getColor(){return this.Color;}

    public abstract int[] addPoints(int[] OldPoints); //Points = int[] {Total, Red, Blue, Green, Purple, Feather, Salt, Paper}
    public Boolean isPlayable(int[] OldPoints){return true;}
    public void setFlipped(boolean isFlipped){this.isFlipped = isFlipped;}
}
