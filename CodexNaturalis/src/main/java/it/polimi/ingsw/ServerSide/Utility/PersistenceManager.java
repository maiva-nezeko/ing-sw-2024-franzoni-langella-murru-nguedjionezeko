package main.java.it.polimi.ingsw.ServerSide.Utility;

import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public interface PersistenceManager {

    static void RestoreGame(Game game)
    {

        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        StringBuilder GameUsernames = new StringBuilder();
        for(Player player : game.getPlayers()){GameUsernames.append(player.getUsername());}

        String content = " ";
        int playerCount = game.getPlayerCount();

        try{ content = new String(Files.readAllBytes(Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath()+"/res/SavedGames/"+GameUsernames+playerCount+".txt"))); }
        catch (Exception e){return;}

        int[][][] NewGameBoard = new int[playerCount][NumOf_Rows][NumOf_Columns];

        for(int index = 0; index< playerCount; index++){

            for(int RowIndex=0; RowIndex<NumOf_Rows; RowIndex++){

                for (int ColumnIndex=0; ColumnIndex<NumOf_Columns; ColumnIndex++)
                {
                    NewGameBoard[index][RowIndex][ColumnIndex] =
                            Integer.parseInt(((content.split("\n")[index]).split(":")[RowIndex]).split(",")[ColumnIndex]);
                }

            }
        }

        game.getRelatedTable().setOccupiedSpaces(NewGameBoard);


        int[] RestoredPublicCards = new int[28];

        for(int index=0; index<8; index++)
        { RestoredPublicCards[index] = Integer.parseInt(content.split("\n")[playerCount].split(",")[index]);}


        int[][] RestoredScoreBoards = new int[playerCount][8];

        for(int row=playerCount+1; row<2*playerCount+1; row++){
            for(int index=0; index<8; index++)
            { RestoredScoreBoards[row-playerCount-1][index] = Integer.parseInt(content.split("\n")[row].split(",")[index]);}}


        if(ServerConstants.getDebug()){
            System.out.println(Arrays.deepToString(NewGameBoard));
            System.out.println(Arrays.toString(RestoredPublicCards));
            System.out.println(Arrays.deepToString(RestoredScoreBoards));}

        game.getRelatedTable().setPublicSpacesID(RestoredPublicCards);

        int player_index=0;
        for(Player player: game.getPlayers()){player.setScoreBoard(RestoredScoreBoards[player_index]); game.modifyPlayer(player_index, player); player_index++;}


    }

    static void SaveGame(int[][][] GameBoard_ID, Game game)
    {
        if(!game.isGameStarted()){return;}

        StringBuilder GameUsernames = new StringBuilder();
        for(Player player : game.getPlayers()){GameUsernames.append(player.getUsername());}


        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        String GameBoard = getGameBoard(GameBoard_ID, NumOf_Rows, NumOf_Columns, game);

        try {
            FileOutputStream fos = new FileOutputStream( FileSystems.getDefault().getPath("").toAbsolutePath()+"/res/SavedGames/"+GameUsernames+game.getPlayerCount()+".txt");
            OutputStreamWriter outWrite = new OutputStreamWriter(fos);
            outWrite.write(GameBoard);
            outWrite.flush();
            outWrite.close();
            fos.close();
            if(ServerConstants.getDebug()){ System.out.println("Saved"); }

        } catch (Exception e){ e.printStackTrace();}

    }

    private static String getGameBoard(int[][][] GameBoard_ID, int NumOf_Rows, int NumOf_Columns, Game game) {

        List<Player> players = game.getPlayers();

        StringBuilder GameBoard= new StringBuilder();

        for(int index = 0; index< game.getPlayerCount(); index++){
            for(int RowIndex = 0; RowIndex< NumOf_Rows; RowIndex++){
                for (int ColumnIndex = 0; ColumnIndex< NumOf_Columns; ColumnIndex++){ GameBoard.append(GameBoard_ID[index][RowIndex][ColumnIndex]).append(",");  }

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
        return GameBoard.toString();
    }
}


