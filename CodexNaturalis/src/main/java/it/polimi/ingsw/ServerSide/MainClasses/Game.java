package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;
import main.java.it.polimi.ingsw.ServerSide.Table.Table;
import main.java.it.polimi.ingsw.ServerSide.Utility.GameStates;

import java.util.Arrays;
import java.util.List;

/**
 * Keeps track of the features of the game like the number of player, list of player.
 * It's used to send update to the server and communicate about the state of the game and player turns.
 */
public class  Game {
    private final int PlayerCount;

    public int getPlayerCount() {
        return PlayerCount;
    }

    private int lastPlayer;
    public void setLastPlayer(int player){ lastPlayer = player; }
    public int getLastPlayer(){return lastPlayer;}


    private int reconnectedPlayers=-1;

    /**
     * Reconnected Players count.
     */
    public void incrementReconnectedPlayers(){reconnectedPlayers++;}

    /**
     * Checks for and gets reconnected Players.
     * @return an int
     */
    public int getReconnectedPlayers(){return reconnectedPlayers;}
    
    private GameServer gameServer;

    private GameStates GameState;

    /**
     *
     * @return GameState
     */
    public GameStates getGameState(){return GameState;}

    /**
     * Switches sequentially between GameStates.
     */
    public void nextPhase(){ if(GameStates.advanceState(GameState) != null){GameState = GameStates.advanceState(GameState);} }

    /**
     * Sets Game State.
     * @param GS    the new GameState
     */
    public void setGameState(GameStates GS){GameState = GS;}
    private final int port;

    /**
     *
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    private final List<Player> Players;

    /**
     *
     * @return Players
     */
    public List<Player> getPlayers() {
        return this.Players;
    }

    /**
     * Add a player to a game through the username.
     *
     * @param username the username to add
     */
    public void addPlayer(String username) {
        this.Players.add(new Player(username));

        ServerConstants.printMessage("New player joined:\n"+Players);
        for (Player player : Players) {
            ServerConstants.printMessage(player.getUsername());
        }

        if (this.getPlayers().size() == this.PlayerCount) {
            this.start();
        }
    }

    /**
     * Modifies an existing player by adding a new one
     *
     * @param index     the position of the player in the list of player
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
     * Reports the game started: GameState is changed to 'PLAYING' and the GameStarted boolean is set as true.
     */
    public void start() {
        GameState = GameStates.PLAYING;
        GameStarted = true;
    }

    /**
     * Reports the game ended: a notification is sent and it calls for the method that shuts down GameServer.
     *
     * @see GameServer#shutDown()
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

    /**
     * Gets Table related to the Game.
     *
     * @return the table
     */
    public Table getRelatedTable() {
        return relatedTable;
    }


    /**
     * Starts actual Game: initializes a GameServer, initializes a gameThread and calls for the thread to start.
     */
    void startGameLoop() {
        gameServer = new GameServer(this.port, this);

        Thread gameThread = new Thread(gameServer);
        gameThread.start();
    }

    /**
     * Instantiates a new Game.
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

    /**
     * Gets the empty slot as in how many Players we are expecting for the game to start.
     *
     * @return the remaining Players
     */
    public int getEmptySlot() {
        if (this.Players.size() == this.PlayerCount) {
            return 5;
        } else return this.Players.size();
    }

    /**
     * Gets a Player object through is username - as a unique identifier.
     *
     * @param username the username of the Player to get
     * @return the Player
     */
    public Player getPlayerByUsername(String username) {
        for (Player player : this.Players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets number associated with a specified Player.
     * @param username the username of the Player
     * @return the int
     */
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
     * Send update package to the server in order to update the game based on the moves of the player during the turn
     *
     * @param username      the username of the Player to update
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

    /**
     *
     * @return turn
     */
    public int getCurrentPlayerTurn() {
        return CurrentPlayerTurn;
    }

    /**
     * Changes turn to the rightful Player.
     */
    public void changePlayerTurn(){
        //player disconnected before draw phase, we draw a card for him
        // drawCard automatically ignore the request if the player has a full hand
        Player currentPlayer = getPlayers().get(CurrentPlayerTurn);
        getRelatedTable().drawRandom(currentPlayer.getUsername());


        if(CurrentPlayerTurn+1== this.getPlayerCount())
        {
            if(GameState == GameStates.LAST_TURN){ GameState = GameStates.GAME_ENDED; this.end(); }
            CurrentPlayerTurn=0;
        }

        else{CurrentPlayerTurn++;} }

    public void resetTimer() { gameServer.resetTimer(false); }
}
