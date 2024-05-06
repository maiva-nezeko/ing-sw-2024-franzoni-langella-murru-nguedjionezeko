package it.polimi.ingsw.Server_Testing_TOFIX;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.ArrayList;
import java.util.Arrays;

public interface PersistenceTest {

    static boolean runPersistenceTest()
    {
        return SaveLoad_Test();
    }


    private static boolean SaveLoad_Test() {

        //TestPlays (player0, player1): {{StartingCard, Tl, Tr, Bl, Br}, {StartingCard, Tl, Tr, Bl, Br}}
        int[][] TestPlays = {{201, 10, 17, 15, 13},{-203, -10, 17, 63, 34}};

        Game game = MultipleGameManager.addGame(2, new ArrayList<>());
        MultipleGameManager.JoinGame("SaveLoad_Test0", game);
        MultipleGameManager.JoinGame("SaveLoad_Test1", game);

        int numOfRows = ServerConstants.getNumOfRows();
        int numOfColumns = numOfRows/2;
        int[][][] OccupiedSpaces = new int[game.getPlayerCount()][numOfRows][numOfColumns];

        for(int playerIndex=0; playerIndex<TestPlays.length; playerIndex++)
        {
            OccupiedSpaces[playerIndex][numOfRows/2][numOfColumns/2] = TestPlays[playerIndex][0];
            OccupiedSpaces[playerIndex][numOfRows/2 -1][numOfColumns/2 -1] = TestPlays[playerIndex][1];
            OccupiedSpaces[playerIndex][numOfRows/2 -1][numOfColumns/2 +1] = TestPlays[playerIndex][2];
            OccupiedSpaces[playerIndex][numOfRows/2 +1][numOfColumns/2 -1] = TestPlays[playerIndex][3];
            OccupiedSpaces[playerIndex][numOfRows/2 +1][numOfColumns/2 +1] = TestPlays[playerIndex][4];

        }

        PersistenceManager.SaveGame(OccupiedSpaces, game);

        PersistenceManager.RestoreGame(game);

        if(ServerConstants.getDebug())
        {   System.out.println(Arrays.deepToString(new int[][][]{game.getRelatedTable().getOccupiedSpaces()[0], game.getRelatedTable().getOccupiedSpaces()[1]}));
            System.out.println(Arrays.deepToString(OccupiedSpaces));    }

        boolean failedFlag = !Arrays.deepEquals(new int[][][]{game.getRelatedTable().getOccupiedSpaces()[0], game.getRelatedTable().getOccupiedSpaces()[1]}, OccupiedSpaces);

        if(!failedFlag){System.out.println("PersistenceTest-> Passed");}
        if(failedFlag){System.out.println("PersistenceTest-> Failed");}

        game.end();
        return failedFlag;

    }




}
