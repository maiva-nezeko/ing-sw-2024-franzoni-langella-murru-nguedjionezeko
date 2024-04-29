package main.java.it.polimi.ingsw.ServerSide.Table;


import main.java.it.polimi.ingsw.ServerSide.Cards.GoalCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.Utility.CardRandomizer;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Table {

    private final Game relatedGame;


    private final List<PlayableCard> ShuffledDeck = CardRandomizer.ShuffleDeck();
    private final List<PlayableCard> ShuffledGoldDeck = CardRandomizer.ShuffleGoldDeck();
    private final List<GoalCard> ShuffledGoalDeck = CardRandomizer.ShuffleGoalDeck();
    private final List<PlayableCard> ShuffledStartingDeck = CardRandomizer.ShuffleStartingDeck();

    public PlayableCard drawResourceCard(boolean isGold)
    {
        PlayableCard returnCard = null;

        if (isGold){   if(!ShuffledGoldDeck.isEmpty()){ returnCard = ShuffledGoldDeck.remove(0);}}
        else  { if(!ShuffledDeck.isEmpty()){ returnCard = ShuffledDeck.remove(0);}}

        return returnCard;
    }


    public PlayableCard drawStartingCard()
    { PlayableCard returnCard = null;  if(!ShuffledStartingDeck.isEmpty()){ returnCard= ShuffledStartingDeck.remove(0);} return returnCard;}
    public GoalCard drawGoalCard()
    {GoalCard returnCard = null; if(!ShuffledGoalDeck.isEmpty()){ returnCard = ShuffledGoalDeck.remove(0);} return returnCard;}


    private int[][][] OccupiedSpaces;
    public int[][][] getOccupiedSpaces(){return OccupiedSpaces;}
    public void setOccupiedSpaces(int[][][] newOccupiedSpaces){ this.OccupiedSpaces=newOccupiedSpaces; }

    private int[] PublicSpacesID;
    public int[] getPublicSpacesID(){ return this.PublicSpacesID; }
    public void setPublicSpacesID(int[] newPublicSpaces){ this.PublicSpacesID = newPublicSpaces; }


    public Table(int PlayerCount, Game relatedGame)
    {
        int numOf_Rows = ServerConstants.getNumOfRows();
        int numOf_Columns = numOf_Rows / 2;

        this.OccupiedSpaces = new int[PlayerCount][numOf_Rows][numOf_Columns];
        this.PublicSpacesID = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        this.relatedGame = relatedGame;
    }

    public GoalCard[] getGoalCards(){
        return new GoalCard[] { Deck.getGoalCardByID(PublicSpacesID[6]), Deck.getGoalCardByID(PublicSpacesID[7])}; }



    public void AutoFillSpaces(){

        if(PublicSpacesID[0]==0){
            PlayableCard Card = drawResourceCard(true);
            if(Card != null){ PublicSpacesID[0] = -Card.getID(); }
        }

        if(PublicSpacesID[1]==0){
            PlayableCard Card = drawResourceCard(false);
            if(Card != null){ PublicSpacesID[1] = -Card.getID(); }
        }

        if(PublicSpacesID[2]==0){  if(PublicSpacesID[0]!=0){MoveCard(0,2);  AutoFillSpaces();} else if(PublicSpacesID[1]!=0){MoveCard(1,2);  AutoFillSpaces();}    }
        if(PublicSpacesID[3]==0){  if(PublicSpacesID[1]!=0){MoveCard(1,3);  AutoFillSpaces();} else if(PublicSpacesID[0]!=0){MoveCard(0,3);  AutoFillSpaces();}    }
        if(PublicSpacesID[4]==0){  if(PublicSpacesID[0]!=0){MoveCard(0,4);  AutoFillSpaces();} else if(PublicSpacesID[1]!=0){MoveCard(1,4);  AutoFillSpaces();}    }
        if(PublicSpacesID[5]==0){  if(PublicSpacesID[1]!=0){MoveCard(1,5);  AutoFillSpaces();} else if(PublicSpacesID[0]!=0){MoveCard(0,5);  AutoFillSpaces();}    }

        if(PublicSpacesID[0]==0 && PublicSpacesID[1]==0){ relatedGame.end(); }
    }

    public void MoveCard(int from, int to)
    {
        if(PublicSpacesID[from] != 0) { PublicSpacesID[to] = -PublicSpacesID[from];  PublicSpacesID[from] = 0;    }
    }

    public void DrawCard(int position, String username)
    {
        Player chosenPlayer = relatedGame.getPlayerByUsername(username);
        if(chosenPlayer==null){return;}

        int EmptyHandSlot = chosenPlayer.getEmptySlot();
        if(EmptyHandSlot != -1)
        {
            chosenPlayer.setCard(EmptyHandSlot, abs(PublicSpacesID[position]));
            PublicSpacesID[position]=0;
            AutoFillSpaces();
            if(ServerConstants.getDebug()){ System.out.println("Player: "+username+" draw: "+PublicSpacesID[position] + " in position: " +position); }

        }

    }


    public void DealCards()
    {
        this.PublicSpacesID[6] = drawGoalCard().getID();
        this.PublicSpacesID[7] = drawGoalCard().getID();

        List<Player> Players = this.relatedGame.getPlayers();

        for(Player player: Players)
        {
            player.setCard(0, drawResourceCard(true).getID());
            player.setCard(1, drawResourceCard(false).getID());
            player.setCard(2, drawResourceCard(false).getID());
            player.setCard(4, drawStartingCard().getID());
            player.setCard(3, drawGoalCard().getID());
            player.setCard(5, drawGoalCard().getID());

            this.relatedGame.modifyPlayer(this.relatedGame.getPlayerNumber(player.getUsername()), player);

            if(ServerConstants.getDebug()){ System.out.println(this.relatedGame.getPlayerByUsername(player.getUsername()).getUsername()
                    +" got hand: "+ Arrays.toString(this.relatedGame.getPlayerByUsername(player.getUsername()).getPrivateCardsID()));}
        }

    }

    public void emptyGrid(int player)
    {for(int Rowindex=0; Rowindex<ServerConstants.getNumOfRows(); Rowindex++){
        for(int ColumnIndex=0; ColumnIndex<ServerConstants.getNumOfRows()/2; ColumnIndex++)
        {this.OccupiedSpaces[player][Rowindex][ColumnIndex] = 0;}}}

}

