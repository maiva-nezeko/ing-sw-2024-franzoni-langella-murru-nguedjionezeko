package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.Table.Player;
import it.polimi.ingsw.ServerSide.UpdateClasses.TableManager;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import junit.framework.TestCase;

import java.util.ArrayList;

public class TAbleManagerTest extends TestCase {

    public void testAddGoalPoints()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        testGame.addPlayer("TestPlayer");
        Player testPlayer = testGame.getPlayers().getFirst();

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 108);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 109, 110});

        TableManager.AddGoalPoints(testPlayer, 1, testGame);

        assertEquals(2, testPlayer.getScoreBoard()[0]);

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 111);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 112, 113});

        TableManager.AddGoalPoints(testPlayer, 1, testGame);

        assertEquals(2+15+6, testPlayer.getScoreBoard()[0]);

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 111);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 114, 115});

        TableManager.AddGoalPoints(testPlayer, 1, testGame);

        assertEquals(2+6+4, testPlayer.getScoreBoard()[0]);

        testGame.end();
    }

    public void testStairPoints()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        int[][][] newSpaces = new int[1][ServerConstants.getNumOfRows()][ServerConstants.getNumOfRows()/2];

        //red stair
        newSpaces[0][2][0] = 1; newSpaces[0][1][1] = 1; newSpaces[0][0][2] = 1;
        //blue stair
        newSpaces[0][5][0] = 11;  newSpaces[0][4][1] = 11; newSpaces[0][3][2] = 11;
        //green stair
        newSpaces[0][6][0] = 21;  newSpaces[0][7][1] = 21; newSpaces[0][8][2] = 21;
        //purple stair
        newSpaces[0][9][0] = 31;  newSpaces[0][10][1] = 31; newSpaces[0][11][2] = 31;


        testGame.getRelatedTable().setOccupiedSpaces(newSpaces);
        testGame.addPlayer("TestPlayer");
        Player testPlayer = testGame.getPlayers().getFirst();

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 100);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 101, 102});

        TableManager.AddGoalPoints(testPlayer, 0, testGame);

        assertEquals(6, testPlayer.getScoreBoard()[0]);

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 103);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 101, 102});

        TableManager.AddGoalPoints(testPlayer, 0, testGame);

        assertEquals(6, testPlayer.getScoreBoard()[0]);


        testGame.end();
    }

    public void testLPoints()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        int[][][] newSpaces = new int[1][ServerConstants.getNumOfRows()][ServerConstants.getNumOfRows()/2];

        //red L
        newSpaces[0][0][0] = 1; newSpaces[0][2][0] = 1; newSpaces[0][3][1] = 21;
        //blue L
        newSpaces[0][10][0] = 11;  newSpaces[0][8][0] = 11; newSpaces[0][7][1] = 1;
        //green L
        newSpaces[0][0][10] = 21;  newSpaces[0][2][10] = 21; newSpaces[0][3][9] = 31;
        //purple L
        newSpaces[0][15][1] = 31;  newSpaces[0][13][1] = 31; newSpaces[0][12][0] = 11;


        testGame.getRelatedTable().setOccupiedSpaces(newSpaces);
        testGame.addPlayer("TestPlayer");
        Player testPlayer = testGame.getPlayers().getFirst();

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 104);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 105, 106});

        TableManager.AddGoalPoints(testPlayer, 0, testGame);

        assertEquals(9, testPlayer.getScoreBoard()[0]);

        testPlayer.setScoreBoard( new int[]{0, 1,2,3,4 ,5,6,7} );
        testPlayer.setCard(3, 107);
        testGame.getRelatedTable().setPublicSpacesID(new int[]{0,0,0,0,0,0, 105, 106});

        TableManager.AddGoalPoints(testPlayer, 0, testGame);

        assertEquals(9, testPlayer.getScoreBoard()[0]);


        testGame.end();
    }
}
