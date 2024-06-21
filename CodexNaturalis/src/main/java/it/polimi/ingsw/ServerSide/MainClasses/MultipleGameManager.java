package it.polimi.ingsw.ServerSide.MainClasses;

import it.polimi.ingsw.ServerSide.Server_IO;
import it.polimi.ingsw.ServerSide.Table.Player;
import it.polimi.ingsw.ServerSide.Utility.GameStates;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages multiple games in the server to keep track of all the information in every game started  */
public class MultipleGameManager {

    /**
     * List of all current Games.
     */
    static final ArrayList<Game> CurrentGames = new ArrayList<>();

    /**
     * Creates a new game in a new port.
     *
     * @param playerCount number of players in the game
     * @param Players list of player that populates the game
     */
    public static Game addGame(int playerCount, List<Player> Players)
    {

        CurrentGames.add( CurrentGames.size(), new Game(playerCount, Players, getFreePort()));

        Game game = CurrentGames.get(CurrentGames.size()-1);


        game.startGameLoop();

        game.getRelatedTable().AutoFillSpaces();
        ServerConstants.printMessageLn("Spaces filled");

        ServerConstants.printMessageLn("Waiting for players to join..");
        ServerConstants.printMessageLn(""+game.getEmptySlot());

        double timePerUpdate = (10000000000.0); long previousTime = System.nanoTime();

        while (game.getEmptySlot()!=5)
        {
            if(System.nanoTime() == previousTime+timePerUpdate){
                ServerConstants.printMessageLn("Waiting"); previousTime=System.nanoTime();}

        }

        ServerConstants.printMessageLn("All players joined");
        ServerConstants.printMessageLn("Dealing Cards");
        game.getRelatedTable().DealCards();

        game.start();




        return CurrentGames.get(CurrentGames.size()-1);
    }

    /**
     * Gets the next free port.
     * @return the port number
     */
    private static int getFreePort() {
        int port = 1332;
        for(Game game: CurrentGames){ if(game!=null){ if(game.getPort() >= port){ port= game.getPort()+2; } }  }

        return port;
    }

    /**
     * Gets the current Game instance for a specified Player.
     * @param userName the Player username
     * @return the Game
     */
    public static Game getGameInstance(String userName) {
        for (Game currentGame : CurrentGames) {
            if (Server_IO.getUsernames(currentGame).contains(userName)) {
                return currentGame;
            }
        }

        return null;
    }

    /**
     * Gets Game instance by port.
     * @param port      the port number
     * @return the searched Game
     */
    public static Game getInstanceByPort(int port){
        for (Game currentGame : CurrentGames) {
            if (currentGame.getPort() == port) {
                return currentGame;
            }
        }

        return null;
    }
    /**
     * Player joins a new game for the first time.
     *
     * @param username      the unique Player's username
     */
    public static boolean JoinGame(String username){
        for (Game currentGame : CurrentGames){ if(!currentGame.isGameStarted() && (currentGame.getGameState() != GameStates.RESTORED) &&
                !Server_IO.getUsernames(currentGame).contains(username))
        {currentGame.addPlayer(username); return true;} }

        return false;
    }

    /**
     * Creates a new Game - instead of Joining an existing one.
     * @param username      the unique username
     * @param playerCount   the desired Player count
     * @return the new Game
     */
    public static boolean CreateGame(String username, int playerCount){
        for (Game currentGame : CurrentGames){ if(
                Server_IO.getUsernames(currentGame).contains(username)){return false;}}


        List<Player> Players = new ArrayList<>();
        Players.add(new Player(username));
        if(playerCount >=1 && playerCount<=4){ addGame(playerCount, Players); return true; }
        return false;
    }

    /**
     * Method is called when the Game ends: it closes the Game, sets isGameStarted as false,
     * deletes the saved file and releases the associated server.
     * @param game      the game that just ended
     */

    public static void end(Game game) {

        if(!CurrentGames.contains(game) || !game.isGameStarted()){return;}

        StringBuilder GameUsernames = new StringBuilder();
        for(Player player : game.getPlayers()){GameUsernames.append(player.getUsername()).append("_");}

        String filename = game.getPlayerCount() + "_" + GameUsernames + game.getPort() + ".txt";
        Path SavePath = Path.of(System.getProperty("user.dir")+ "\\res\\SavedGames\\"+filename);

        for(int index=0; index<CurrentGames.size(); index++){ if(CurrentGames.get(index).equals(game)){ CurrentGames.remove(index); break; } }


        if(ServerConstants.getNoSaveDelete()){return;}

        if(!Files.exists(SavePath)){ if(ServerConstants.getDebug()){ ServerConstants.printMessageLn("File not existent at: \t" + SavePath);} return;}

        try{ if(Files.deleteIfExists( SavePath ) )
            {if(ServerConstants.getDebug()){ ServerConstants.printMessageLn("SaveFile successfully deleted at: \t" + SavePath); } }
            else{  ServerConstants.printMessageLn("Failed to delete SaveFile at: \t" + SavePath); }
        }catch (Exception e){ e.printStackTrace(); }
    }

    /**
     * Reconnects a Player to an existing Game after lost connection.
     *
     * @param username the username of the Player requesting a reconnection
     * @param lastPort the port used for the communication
     * @return the searched Game
     */
    public static Game Reconnect(String username, int lastPort) {
        Game lastGame = getInstanceByPort(lastPort);
        if(lastGame!= null &&
                Server_IO.getUsernames(lastGame).contains(username))
        {
            lastGame.incrementReconnectedPlayers();

            if( ( lastGame.getReconnectedPlayers() >= lastGame.getPlayerCount() ) && !lastGame.isGameStarted()){  lastGame.start(); }
            return lastGame;
        }

        return null;
    }

    /**
     * Restores a Past game after Server error.
     *
     * @param Players       the list of Players
     * @param playerCount   the number of Players
     * @param port          the port number used
     * @return the searched Game
     */
    public static Game RestoreGame(List<Player> Players , int playerCount, int port){
        if(getInstanceByPort(port)!= null){return null;}

        CurrentGames.add( CurrentGames.size(), new Game(playerCount, Players, port));
        Game restoredGame = getInstanceByPort(port);
        if(restoredGame == null){return null;}

        restoredGame.startGameLoop();
        return getInstanceByPort(port);
    }
}
