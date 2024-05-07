package test.java.it.polimi.ingsw.ServerTesting;

import junit.framework.TestCase;
import main.java.it.polimi.ingsw.ServerSide.Cards.GoalCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import main.java.it.polimi.ingsw.ServerSide.Utility.CardRandomizer;

import java.util.ArrayList;
import java.util.List;

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

    }




}
