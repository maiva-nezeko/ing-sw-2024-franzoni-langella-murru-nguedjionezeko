package main.java.it.polimi.ingsw.ServerSide.Table;

import static java.lang.Math.abs;

public class Player {
    private final int[] PrivateCardsID = new int[6];
    private int[] ScoreBoard = new int[8];
    private final String username;

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
        if(position<0 || position>5){ return; }
        if(id!=0 && Deck.getCardBYid(id)==null && Deck.getGoalCardByID(id)==null ){ return; }
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
        if(position==3 | position==5){ return; }
        this.PrivateCardsID[position] = -this.PrivateCardsID[position];
    }
    public void consumeCard(int id){

        int index = 0; boolean found = false;

        for(int ID : this.PrivateCardsID){
            if(ID == id){  found = true; break;  }
            index++;
        }

        if(!found){ return; }
        this.setCard(index, 0);
    }
}