package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;
import main.java.it.polimi.ingsw.ServerSide.Table.Table;

import java.util.Arrays;
import java.util.List;

public class Game {
    private final int PlayerCount;
    public int getPlayerCount(){return PlayerCount;}


    private GameServer gameServer;
    private Thread gameThread;

    private final int port;
    public int getPort(){return this.port;}

    private final List<Player> Players;
    public List<Player> getPlayers(){return this.Players;}
    public void addPlayer(String username)
    { this.Players.add(new Player(username)); if(this.getPlayers().size() == this.PlayerCount){ this.start(); } }
    public void modifyPlayer(int index, Player newPlayer){ this.Players.set(index, newPlayer);}

    private boolean GameStarted = false;
    public boolean isGameStarted() { return GameStarted;}
    public void start() { GameStarted = true;  }
    public void end(){ MultipleGameManager.end(this); this.GameStarted = false;  }


    private final Table relatedTable;
    public Table getRelatedTable(){ return relatedTable; }


    void startGameLoop(int Port)
    {
        gameServer = new GameServer(Port);
        gameServer.start();
    }



    public Game(int playerCount, List<Player> Players, int port)
    {
        this.PlayerCount = playerCount;
        this.Players = Players;
        this.relatedTable = new Table(playerCount, this);
        this.port = port;
    }




    public int getEmptySlot()
    {
        if(this.Players.size() == this.PlayerCount){return 5;}
        else return this.Players.size();
    }

    public Player getPlayerByUsername(String username)
    {
        for (Player player : this.Players)
        {  if (player.getUsername().equals(username)) {   return player;   }    }
        return null;
    }

    public int getPlayerNumber(String username)
    {
        for (int index = 0; index<this.Players.size(); index++)
        { if(this.Players.get(index).getUsername().equals(username)){ return index;}}
        return 5;
    }

   public UpdatePackage sendUpdatePackage(String username)
   {
       int playerIndex = getPlayerNumber(username);

       System.out.println("Updating package for " + username + " PlayerHand = " + Arrays.toString(this.Players.get(playerIndex).getPrivateCardsID())
        +" Score:" + this.Players.get(playerIndex).getScoreBoard()[0] + " PublicSpaces" + Arrays.toString(this.relatedTable.getPublicSpacesID()));
       return new UpdatePackage(
               this.relatedTable.getOccupiedSpaces()[playerIndex], this.Players.get(playerIndex).getScoreBoard()[0],
               this.Players.get(playerIndex).getPrivateCardsID(), this.relatedTable.getPublicSpacesID());

   }


    //TurnManager
    private static int CurrentPlayerTurn=0;
    public int getCurrentPlayerTurn(){return CurrentPlayerTurn;}
    public void changePlayerTurn(){
        if(CurrentPlayerTurn+1== this.getPlayerCount()){CurrentPlayerTurn=0;}else{CurrentPlayerTurn++;} }




}
