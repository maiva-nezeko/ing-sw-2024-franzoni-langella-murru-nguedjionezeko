package it.polimi.ingsw.ModelTesting;

import junit.framework.TestCase;
import it.polimi.ingsw.ServerSide.Cards.GoalCard;
import it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import it.polimi.ingsw.ServerSide.Table.Player;
import it.polimi.ingsw.ServerSide.Utility.CardRandomizer;
import it.polimi.ingsw.ServerSide.Utility.GameStates;
import it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class UtilityTest extends TestCase{

    public UtilityTest(String testName)
    {
        super(testName);

        testCardRandomizerTest();
    }

    public void testCardRandomizerTest()
    {
        List<Integer> ContainedID = new ArrayList<>();

        List<PlayableCard> ResDeck = CardRandomizer.ShuffleDeck();

        //Checks if the deck has the correct size
        assertEquals(ResDeck.size(), 40);

        //Checks if the deck causes some null pointer exceptions
        for(int index = 0; index<40; index++)
        {
            assertNotNull(ResDeck.get(index));
            ContainedID.add(ResDeck.get(index).getID());
        }

        //checks if the deck contains all required ids
        for(int ID = 1; ID<=40; ID++)
        {
            assertTrue(ContainedID.contains(ID));
            ContainedID.remove((Integer) ID);
        }

        //double-check the size
        assertTrue(ContainedID.isEmpty());


        //the process is repeated for all decks

        List<PlayableCard> GoldDeck = CardRandomizer.ShuffleGoldDeck();

        //Checks if the deck has the correct size
        assertEquals(GoldDeck.size(), 40);

        //Checks if the deck causes some null pointer exceptions
        for(int index = 0; index<40; index++)
        {
            assertNotNull(GoldDeck.get(index));
            ContainedID.add(GoldDeck.get(index).getID());
        }

        //checks if the deck contains all required ids
        for(int ID = 41; ID<=80; ID++)
        {
            assertTrue(ContainedID.contains(ID));
            ContainedID.remove((Integer) ID);
        }

        //double-check the size
        assertTrue(ContainedID.isEmpty());

        List<PlayableCard> StartingDeck = CardRandomizer.ShuffleStartingDeck();

        //Checks if the deck has the correct size
        assertEquals(StartingDeck.size(), 6);

        //Checks if the deck causes some null pointer exceptions
        for(int index = 0; index<6; index++)
        {
            assertNotNull(StartingDeck.get(index));
            ContainedID.add(StartingDeck.get(index).getID());
        }

        //checks if the deck contains all required ids
        for(int ID = 200; ID<=205; ID++)
        {
            assertTrue(ContainedID.contains(ID));
            ContainedID.remove((Integer) ID);
        }

        //double-check the size
        assertTrue(ContainedID.isEmpty());


        List<GoalCard> GoalDeck = CardRandomizer.ShuffleGoalDeck();

        //Checks if the deck has the correct size
        assertEquals(GoalDeck.size(), 16);

        //Checks if the deck causes some null pointer exceptions
        for(int index = 0; index<16; index++)
        {
            assertNotNull(GoalDeck.get(index));
            ContainedID.add(GoalDeck.get(index).getID());
        }

        //checks if the deck contains all required ids
        for(int ID = 100; ID<=115; ID++)
        {
            assertTrue(ContainedID.contains(ID));
            ContainedID.remove((Integer) ID);
        }

        //double-check the size
        assertTrue(ContainedID.isEmpty());
    }

    public void testPersistenceTest()
    {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("PersistenceTest"));
        players.add(new Player("PersistenceTest2"));

        int[][][] gameBoard = new int[2][ServerConstants.getNumOfRows()][ServerConstants.getNumOfRows()/2];
        //fill the GameBoard with random numbers to later check if it's the same
        for (int i=0; i<gameBoard[0].length; i++) {
            for (int j=0; j<gameBoard[0][i].length; j++) {
                gameBoard[0][i][j] = (int) (Math.random()*10);
            }
        }

        int[] publicCards = new int[8];
        for (int i=0; i<publicCards.length; i++) {
            publicCards[i] = (int) (Math.random()*10);       }

        int[] scoreBoard = new int[8];
        for (int i=0; i<scoreBoard.length; i++) {
            scoreBoard[i] = (int) (Math.random()*10);        }

        int[] Hand = new int[6];
        for (int i=0; i<Hand.length; i++) {
            Hand[i] = (int) (Math.random()*10);        }


        Game testGame = MultipleGameManager.addGame(2, players);

        testGame.getRelatedTable().setOccupiedSpaces(gameBoard);
        testGame.getRelatedTable().setPublicSpacesID(publicCards);
        testGame.getPlayers().getFirst().setScoreBoard(scoreBoard);

        for(int i=0; i<Hand.length; i++){testGame.getPlayers().getFirst().setCard(i, Hand[i]);}


        testGame.start();
        PersistenceManager.SaveGame(testGame);
        ServerConstants.setNoSaveDelete(true);
        testGame.end();

        assertNull(MultipleGameManager.getGameInstance("PersistenceTest"));

        PersistenceManager.RestoreGames();
        testGame =  MultipleGameManager.getGameInstance("PersistenceTest");

        assertNotNull(testGame);

        assertEquals(GameStates.RESTORED, testGame.getGameState());

        assertArrayEquals(gameBoard, testGame.getRelatedTable().getOccupiedSpaces());
        assertArrayEquals(scoreBoard, testGame.getPlayers().getFirst().getScoreBoard());

        ServerConstants.setNoSaveDelete(false);
        testGame.start();
        testGame.end();

    }




}
