package it.polimi.ingsw.ServerSide.Table;

public class Player {
    private int[] PrivateCardsID = new int[6];
    private int[] ScoreBoard = new int[8];
    private String username;

    public Player(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public int[] getPrivateCardsID() {
        return PrivateCardsID;
    }
    public void setCard(int position, int id){
        this.PrivateCardsID[position]=id;
    }
    public int getEmptySlot(){
        for(int index=0; index<3; index++){
            if(this.PrivateCardsID[index]==0){
                return index;}
        }
        return -1;
    }
    public int[] getScoreBoard() {
        return this.ScoreBoard;
    }

    public void setScoreBoard(int[] NewScore) {
        this.ScoreBoard = NewScore;
    }
    public void FlipCard(int position){
        this.PrivateCardsID[position] = -this.PrivateCardsID[position];
    }
    public void consumeCard(int id){
        int index = 0;
        for(int ID : this.getPrivateCardsID()){
            if(ID == id){
                break;
            }
            index++;
        }
        this.setCard(index, 0);
    }
}