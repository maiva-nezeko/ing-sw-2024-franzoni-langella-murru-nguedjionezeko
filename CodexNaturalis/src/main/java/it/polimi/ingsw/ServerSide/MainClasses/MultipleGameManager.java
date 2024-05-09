package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import main.java.it.polimi.ingsw.ServerSide.Server_IO;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;
import main.java.it.polimi.ingsw.ServerSide.Utility.GameStates;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * manage the multiple game in the server to keep track opf all the information in every game started  */
public class MultipleGameManager {

    static ArrayList<Game> CurrentGames = new ArrayList<>();

    /**
     * crate a new game in a new port
     * @param playerCount number of players in the game
     * @param Players list of player that populates the game */

    public static Game addGame(int playerCount, List<Player> Players)
    {

        CurrentGames.add( CurrentGames.size(), new Game(playerCount, Players, getFreePort()));

        Game game = CurrentGames.get(CurrentGames.size()-1);


        game.startGameLoop();

        game.getRelatedTable().AutoFillSpaces();
        System.out.println("Spaces filled");

        System.out.println("Waiting for players to join..");
        System.out.println(game.getEmptySlot());

        double timePerUpdate = (10000000000.0); long previousTime = System.nanoTime();

        while (game.getEmptySlot()!=5)
        {
            if(System.nanoTime() == previousTime+timePerUpdate){
                System.out.println("Waiting"); previousTime=System.nanoTime();}

        }

        System.out.println("All players joined");
        System.out.println("Dealing Cards");
        game.getRelatedTable().DealCards();

        game.start();




        return CurrentGames.get(CurrentGames.size()-1);
    }

    private static int getFreePort() {
        int port = 1332;
        for(Game game: CurrentGames){ if(game!=null){ if(game.getPort() >= port){ port= game.getPort()+2; } }  }

        return port;
    }

    public static Game getGameInstance(String userName) {
        for (Game currentGame : CurrentGames) {
            if (Server_IO.getUsernames(currentGame).contains(userName)) {
                return currentGame;
            }
        }

        return null;
    }

    public static Game getInstanceByPort(int port){
        for (Game currentGame : CurrentGames) {
            if (currentGame.getPort() == port) {
                return currentGame;
            }
        }

        return null;
    }
    /**
     * player joins a new game for the first time
     * @param username it's unique for the player*/
    public static boolean JoinGame(String username){
        for (Game currentGame : CurrentGames){ if(!currentGame.isGameStarted() && (currentGame.getGameState() != GameStates.RESTORED) &&
                !Server_IO.getUsernames(currentGame).contains(username))
        {currentGame.addPlayer(username); return true;} }

        return false;
    }

    public static boolean CreateGame(String username, int playerCount){
        for (Game currentGame : CurrentGames){ if(
                Server_IO.getUsernames(currentGame).contains(username)){return false;}}


        List<Player> Players = new ArrayList<>();
        Players.add(new Player(username));
        if(playerCount >=1 && playerCount<=4){ addGame(playerCount, Players); return true; }
        return false;
    }

    /**
     * function is called when the game end, it closes the game,
     * set gameStarted false, delete the saved file and release the associated server
     * @param game the game finished*/

    public static void end(Game game) {

        if(!CurrentGames.contains(game) || !game.isGameStarted()){return;}

        StringBuilder GameUsernames = new StringBuilder();
        for(Player player : game.getPlayers()){GameUsernames.append(player.getUsername()).append("_");}

        String filename = game.getPlayerCount() + "_" + GameUsernames + game.getPort() + ".txt";
        Path SavePath = Path.of(System.getProperty("user.dir")+ "\\CodexNaturalis\\res\\SavedGames\\"+filename);

        for(int index=0; index<CurrentGames.size(); index++){ if(CurrentGames.get(index).equals(game)){ CurrentGames.remove(index); break; } }


        if(ServerConstants.getNoSaveDelete()){return;}

        if(!Files.exists(SavePath)){ if(ServerConstants.getDebug()){ System.out.println("File not existent at: \t" + SavePath);} return;}

        try{ if(Files.deleteIfExists( SavePath ) )
            {if(ServerConstants.getDebug()){ System.out.println("SaveFile successfully deleted at: \t" + SavePath); } }
            else{  System.out.println("Failed to delete SaveFile at: \t" + SavePath); }
        }catch (Exception e){ e.printStackTrace(); }
    }



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

    public static Game RestoreGame(List<Player> Players , int playerCount, int port){
        if(getInstanceByPort(port)!= null){return null;}

        CurrentGames.add( CurrentGames.size(), new Game(playerCount, Players, port));
        Game restoredGame = getInstanceByPort(port);
        if(restoredGame == null){return null;}

        restoredGame.startGameLoop();
        return getInstanceByPort(port);
    }
}
