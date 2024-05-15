package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;
import main.java.it.polimi.ingsw.ServerSide.Table.Table;
import main.java.it.polimi.ingsw.ServerSide.Utility.GameStates;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * keeps track of the features of the game like the number of player, list of player.
 * it's used to send update to the server and communicate about the state of the game and player turns
 * */
public class  Game {
    private final int PlayerCount;

    public int getPlayerCount() {
        return PlayerCount;
    }


    private int reconnectedPlayers=-1;
    public void incrementReconnectedPlayers(){reconnectedPlayers++;}
    public int getReconnectedPlayers(){return reconnectedPlayers;}
    
    private GameServer gameServer;
    private Thread gameThread;

    private GameStates GameState;
    public GameStates getGameState(){return GameState;}
    public void nextPhase(){ if(GameStates.advanceState(GameState) != null){GameState = GameStates.advanceState(GameState);} }
    public void setGameState(GameStates GS){GameState = GS;}
    private final int port;

    public int getPort() {
        return this.port;
    }

    private final List<Player> Players;

    public List<Player> getPlayers() {
        return this.Players;
    }

    /**
     * add a player to a game by his username
     *
     * @param username
     */
    public void addPlayer(String username) {
        this.Players.add(new Player(username));

        ServerConstants.printMessage("New player joined:\n"+Players);
        for(int i =0; i< Players.size(); i++){ ServerConstants.printMessage(Players.get(i).getUsername());}

        if (this.getPlayers().size() == this.PlayerCount) {
            this.start();
        }
    }

    /**
     * modify an existing player by adding a new one
     *
     * @param index     the postion of the player in the list of player
     * @param newPlayer the player that replace the old one
     */
    public void modifyPlayer(int index, Player newPlayer) {
        this.Players.set(index, newPlayer);
    }

    private boolean GameStarted = false;

    public boolean isGameStarted() {
        return GameStarted;
    }

    /**
     * report the game ended
     */
    public void start() {
        GameState = GameStates.PLAYING;
        GameStarted = true;
    }

    /**
     * report the game ended
     */
    public void end() {

        if(this.gameServer!=null){
            ServerConstants.printMessage("Shutting down GameServer");
            gameServer.shutDown();
        }

        MultipleGameManager.end(this);
        this.GameStarted = false;
    }


    private final Table relatedTable;

    public Table getRelatedTable() {
        return relatedTable;
    }


    /**
     *
     */
    void startGameLoop() {
        gameServer = new GameServer(this.port, this);
        gameServer.start();
    }

    /**
     * Instantiates a new Game
     *
     * @param playerCount number of player that constitute the game
     * @param port        server port where the game is instantiated
     * @param Players     list of the player in the game
     */

    public Game(int playerCount, List<Player> Players, int port) {
        this.PlayerCount = playerCount;
        this.Players = Players;
        this.relatedTable = new Table(playerCount, this);
        this.port = port;
        this.GameState = GameStates.PLAYER_JOINING;
    }



    public int getEmptySlot() {
        if (this.Players.size() == this.PlayerCount) {
            return 5;
        } else return this.Players.size();
    }

    public Player getPlayerByUsername(String username) {
        for (Player player : this.Players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerNumber(String username) {

        ServerConstants.printMessage("looking for playerNumber: " +username+ "\t");


        for (int index = 0; index < this.Players.size(); index++) {
            ServerConstants.printMessage(Players.get(index).getUsername()+", ");
            if (this.Players.get(index).getUsername().equals(username)) {
                return index;
            }
        }

        ServerConstants.printMessage("\n");
        return 5;
    }

    /**
     * send update package to the server in order to update the game based on the moves of the player during the turn
     *
     * @param username
     * @return UpdatePackage contains all the information to change the status of the game
     */
    public UpdatePackage sendUpdatePackage(String username) {
        int playerIndex = getPlayerNumber(username);

        ServerConstants.printMessage("Updating package for " + username + " PlayerHand = " + Arrays.toString(this.Players.get(playerIndex).getPrivateCardsID())
                + " Score:" + this.Players.get(playerIndex).getScoreBoard()[0] + " PublicSpaces" + Arrays.toString(this.relatedTable.getPublicSpacesID()));
        return new UpdatePackage(
                this.relatedTable.getOccupiedSpaces()[playerIndex], this.Players.get(playerIndex).getScoreBoard()[0],
                this.Players.get(playerIndex).getPrivateCardsID(), this.relatedTable.getPublicSpacesID());

    }


    //TurnManager
    private static int CurrentPlayerTurn = 0;

    public int getCurrentPlayerTurn() {
        return CurrentPlayerTurn;
    }

    public void changePlayerTurn(){
        if(CurrentPlayerTurn+1== this.getPlayerCount())
        {
            if(GameState == GameStates.LAST_TURN){ GameState = GameStates.GAME_ENDED; this.end(); }
            CurrentPlayerTurn=0;
        }

        else{CurrentPlayerTurn++;} }

    public void resetTimer() { gameServer.resetTimer(false); }
}
