package it.polimi.ingsw.ModelTesting;

import junit.framework.TestCase;
import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import it.polimi.ingsw.ServerSide.Table.Player;
import it.polimi.ingsw.ServerSide.Table.Table;
import it.polimi.ingsw.ServerSide.UpdateClasses.TableManager;
import it.polimi.ingsw.ServerSide.Utility.GameStates;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class UpdateTest extends TestCase {

    public UpdateTest(String testName)
    {
        super(testName);

    }

    public void testPlaceStartingCard()
    {
        ArrayList<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("TestPlayer"));
        Game testGame = new Game(1, testPlayers, 1331);
        Table testTable = testGame.getRelatedTable();


        int[][] gameBoard = new int[ServerConstants.getNumOfRows()][ServerConstants.getNumOfRows()/2];

        //checks if the gameBoard starts empty
        assertArrayEquals(gameBoard, testTable.getOccupiedSpaces()[0]);

        //checks if the card is correctly placed
        TableManager.PlaceStartingCard(205, testGame, "TestPlayer");
        gameBoard[ServerConstants.getNumOfRows()/2][ServerConstants.getNumOfRows()/4] = 205;

        assertArrayEquals(gameBoard, testTable.getOccupiedSpaces()[0]);

        testGame.end();

    }

    public void testPlayCard()
    {
        //By construction of PlayCardByIndex Method this also tests for AdjustScore and AddGoalPoints
        ArrayList<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("TestPlayer"));
        Game testGame = new Game(1, testPlayers, 1331);





        //TestPlays formatted like => (StartingCard, FirstCardToPlace, whereToPlaceFirstCard, SecondCardToPlace, whereToPlaceSecondCard...)
        int[][] TestPlays = {
                {201, 24, /*TR*/1, 6, /*BL*/ 2},
                {-201, 24, /*BR*/3, 6, /*TR*/ 1, 19,/*BL*/ 2},
                {203, 27,/*Tl*/0, 8,/*BL*/2}
        };

        //Results return -1 if the set of moves should not be possible or the correct scoreboard otherwise
        int[][] Results = {{-1}, {1, 2, 3, 2, 0, 0, 1, 0}, {-1}};
        boolean cantPlayFlag;

        int StartingRow = ServerConstants.getNumOfRows()/2;
        int StartingColumn = ServerConstants.getNumOfRows()/4;



        //Correct Set of indexes for the corners displayed in TestPlays
        int[][] Corners = {
                {StartingRow-1, StartingColumn-1},
                {StartingRow-1, StartingColumn+1},
                {StartingRow+1, StartingColumn-1},
                {StartingRow+1, StartingColumn+1}
        };

        int playIndex=0;
        for (int[] test : TestPlays) {

            cantPlayFlag = false;

            //play
            for (int index = 0; index < test.length-1; index++) {

                if (index == 0) { TableManager.PlaceStartingCard(test[index], testGame, testGame.getPlayers().get(0).getUsername()); }

                else {
                    cantPlayFlag = !TableManager.playCardByIndex(Corners[test[index+1]][0], Corners[test[index+1]][1], test[index], "TestPlayer" ); index++;}

                if(cantPlayFlag){ break; }
            }

            //check
            if(Results[playIndex][0] == -1){ assertTrue(cantPlayFlag);  }
            else
            { assertArrayEquals(Results[playIndex], testGame.getPlayerByUsername("TestPlayer").getScoreBoard()); }

            //reset
            testGame.getRelatedTable().emptyGrid(0);
            testGame.getPlayers().get(0).setScoreBoard(new int[8]);
        }

        testGame.end();

    }

    //Test for Game End Condition by reaching 20 points
    public void testEndGame_EmptyDecks()
    {
        ArrayList<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("TestPlayer"));
        Game testGame = new Game(1, testPlayers, 1331);
        Table testTable = testGame.getRelatedTable();
        testGame.setGameState(GameStates.LAST_TURN);


        for(int cardsDrawn = 0; cardsDrawn<80; cardsDrawn++){
            testTable.DrawCard(3, "TestPlayer" );
            testGame.getPlayers().get(0).setCard(1, 0);
        }

        //check for game advanced
        assertEquals(GameStates.LAST_TURN, testGame.getGameState());
        testGame.changePlayerTurn();

        assertFalse(testGame.isGameStarted());
    }

    //Test for Game End Condition by empty decks
    public void testEndGame_20Points()
    {
        //check for game creation
        ArrayList<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("TestPlayer"));
        testPlayers.add(new Player("TestPlayer2"));

        Game testGame = MultipleGameManager.addGame(2, testPlayers);
        Table testTable = testGame.getRelatedTable();

        //Setting up the correct scenery
        testTable.DealCards();
        testGame.getPlayers().get(0).setScoreBoard(new int[]{ 19, 0,0,0,0, 0,0,0 });
        TableManager.PlaceStartingCard(201, testGame, testGame.getPlayers().get(0).getUsername());


        //Playing a card to get over 20 points
        assertTrue(
                TableManager.playCardByIndex( ServerConstants.getNumOfRows()/2 -1, ServerConstants.getNumOfRows()/4 -1, 10, testGame.getPlayers().get(0).getUsername() )
        );


        //check if game has advanced
        assertEquals(GameStates.LAST_TURN, testGame.getGameState());
        testGame.changePlayerTurn();

        assertNotNull(MultipleGameManager.getGameInstance("TestPlayer"));

        assertEquals(GameStates.LAST_TURN, testGame.getGameState());
        testGame.changePlayerTurn();

        assertFalse(testGame.isGameStarted());
        assertNull(MultipleGameManager.getGameInstance("TestPlayer"));
    }
}
