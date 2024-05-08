package main.java.it.polimi.ingsw.ServerSide.Utility;

import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface PersistenceManager {


    static void RestoreGames()
    {
        File path = new File(System.getProperty("user.dir")+"\\CodexNaturalis\\res\\SavedGames");

        File [] files = path.listFiles();
        if(files==null) {
            System.out.println("Saved Games Directory Empty, no games to restore");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {

                String[] formattedFile = file.getName().replace(".txt", "").split("_");

                int PlayerCount = Integer.parseInt(formattedFile[0]);
                ArrayList<Player> players = new ArrayList<>();

                //Populate correct usernames
                for(int index = 1; index<=PlayerCount; index++){ players.add(new Player(formattedFile[index])); }

                Game restoredGame = MultipleGameManager.RestoreGame(players, PlayerCount, Integer.parseInt(formattedFile[formattedFile.length-1]));
                if(restoredGame == null){return;}

                String content = " ";
                try{ content = new String(Files.readAllBytes(Path.of(file.getPath()))) ; }
                catch (Exception e){return;}

                //reset game boards
                int NumOf_Rows = ServerConstants.getNumOfRows();
                int NumOf_Columns = NumOf_Rows/2;

                int[][][] NewGameBoard = new int[PlayerCount][NumOf_Rows][NumOf_Columns];

                for(int index = 0; index< PlayerCount; index++){

                    for(int RowIndex=0; RowIndex<NumOf_Rows; RowIndex++){

                        for (int ColumnIndex=0; ColumnIndex<NumOf_Columns; ColumnIndex++)
                        {
                            NewGameBoard[index][RowIndex][ColumnIndex] =
                                    Integer.parseInt(((content.split("\n")[index]).split(":")[RowIndex]).split(",")[ColumnIndex]);
                        }

                    }
                }

                int[] RestoredPublicCards = new int[8];

                for(int index=0; index<8; index++)
                { RestoredPublicCards[index] = Integer.parseInt(content.split("\n")[PlayerCount].split(",")[index]);}


                int[][] RestoredScoreBoards = new int[PlayerCount][8];

                for(int row=PlayerCount+1; row<2*PlayerCount+1; row++){
                    for(int index=0; index<8; index++)
                    { RestoredScoreBoards[row-PlayerCount-1][index] = Integer.parseInt(content.split("\n")[row].split(",")[index]);}}

                int[][] RestoredHands = new int[PlayerCount][6];

                for(int row=2*PlayerCount+1; row<2*PlayerCount+1+PlayerCount; row++){
                    for(int index=0; index<6; index++)
                    { RestoredHands[row-2*PlayerCount-1][index] = Integer.parseInt(content.split("\n")[row].split(",")[index]);}}

                if(ServerConstants.getDebug()){
                    System.out.println(Arrays.deepToString(NewGameBoard));
                    System.out.println(Arrays.toString(RestoredPublicCards));
                    System.out.println(Arrays.deepToString(RestoredScoreBoards));
                    System.out.println(Arrays.deepToString(RestoredHands));
                }

                restoredGame.getRelatedTable().setOccupiedSpaces(NewGameBoard);
                restoredGame.getRelatedTable().setPublicSpacesID(RestoredPublicCards);

                int player_index=0;
                for(Player player: restoredGame.getPlayers()){
                    player.setScoreBoard(RestoredScoreBoards[player_index]);

                    for (int i=0; i<6; i++){player.setCard(i, RestoredHands[player_index][i]);}

                    restoredGame.modifyPlayer(player_index, player);
                    player_index++;}

                restoredGame.incrementReconnectedPlayers();
                restoredGame.setGameState(GameStates.RESTORED);
            }
        }
    }



    static void SaveGame(Game game)
    {
        if(!game.isGameStarted()){return;}

        StringBuilder GameUsernames = new StringBuilder();
        for(Player player : game.getPlayers()){GameUsernames.append(player.getUsername()).append("_");}


        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        String GameBoard = getGameBoard(game.getRelatedTable().getOccupiedSpaces(), NumOf_Rows, NumOf_Columns, game);

        try {

            String filename = game.getPlayerCount()+"_"+GameUsernames+game.getPort()+".txt";
            FileWriter FW = new FileWriter(System.getProperty("user.dir")+ "\\CodexNaturalis\\res\\SavedGames\\"+filename);
            FW.write(GameBoard);
            FW.close();

            if(ServerConstants.getDebug()){ System.out.println("Saved"); }


        } catch (Exception e){ e.printStackTrace();}

    }


    private static String getGameBoard(int[][][] GameBoard_ID, int NumOf_Rows, int NumOf_Columns, Game game) {

        List<Player> players = game.getPlayers();

        StringBuilder GameBoard= new StringBuilder();

        for(int index = 0; index< game.getPlayerCount(); index++){
            for(int RowIndex = 0; RowIndex< NumOf_Rows; RowIndex++){
                for (int ColumnIndex = 0; ColumnIndex< NumOf_Columns; ColumnIndex++){
                    GameBoard.append(GameBoard_ID[index][RowIndex][ColumnIndex]).append(",");  }

                GameBoard.append(":");
            }
            GameBoard.append("\n");
        }

        int[] PublicID  = game.getRelatedTable().getPublicSpacesID();

        for(int ID : PublicID){ GameBoard.append(ID).append(",");}
        GameBoard.append("\n");

        int[] ScoreBoard;

        for(int index = 0; index< game.getPlayerCount(); index++){
            ScoreBoard = players.get(index).getScoreBoard();
            for(int ID : ScoreBoard){ GameBoard.append(ID).append(",");}
            GameBoard.append("\n"); }


        int[] Hand;

        for(int index = 0; index< game.getPlayerCount(); index++){
            Hand = players.get(index).getPrivateCardsID();
            for(int ID : Hand){ GameBoard.append(ID).append(",");}
            GameBoard.append("\n"); }

        return GameBoard.toString();
    }
}





