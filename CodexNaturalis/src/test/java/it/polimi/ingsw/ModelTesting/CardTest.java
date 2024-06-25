package it.polimi.ingsw.ModelTesting;


import junit.framework.TestCase;
import it.polimi.ingsw.ServerSide.Cards.*;
import it.polimi.ingsw.ServerSide.Cards.Enums.*;
import it.polimi.ingsw.ServerSide.Table.Deck;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;


public class CardTest extends TestCase {

    public CardTest(String testName)
    {
        super(testName);

        testGoalCard_correctGoalState();
        testPlayableCards_CorrectColors();

        testGoldDeck_Playable();
        testGoldDeck_Points();
        testGoldCard_correctReturnValues();


        testResource_Points();
        testResourceCards_correctReturnValues();

        testStartingDeck_Points();
        testStartingCards_correctReturnValues();
    }

    public void testGoalCard_correctGoalState()
    {
        GoalCard testCard = new GoalCard(GoalStates.THREE_FOR_COMBO, 10);
        assertEquals(GoalStates.THREE_FOR_COMBO, testCard.getGoalState());
    }

    public void testPlayableCards_CorrectColors() {
        ResourceCard[] testCards = {new ResourceCard(new int[] {1,2,3,4} , 10), new ResourceCard(new int[] {1,2,3,4} , 21),
                new ResourceCard(new int[] {1,2,3,4} , 33), new ResourceCard(new int[] {1,2,3,4} , 47), new ResourceCard(new int[] {1,2,3,4} , 79)};

        int[] returnedColors = {testCards[0].getColor(), testCards[1].getColor(), testCards[2].getColor(), testCards[3].getColor(), testCards[4].getColor()};
        int[] actualColors = {0,2,3,0,3};

        assertArrayEquals(returnedColors, actualColors);

    }


    public void testGoldCard_correctReturnValues()
    {
        GoldCard testCard = new GoldCard(new int[]{0,1,2,3},10, PointCondition.ONE_FOR_SALT, PlayCondition.THREE_SAME);

        //Check if the card returns the correct Corner Array;
        assertArrayEquals(testCard.getCorners(), new int[]{0,1,2,3});

        //Check if the card Flips and then returns the correct Corners
        testCard.setFlipped(true);
        assertTrue(testCard.isFlipped());

        //Check if the card returns the correct Corners once Flipped
        assertArrayEquals(testCard.getCorners(), new int[]{1,1,1,1});
    }

    public void testGoldDeck_Playable()
    {
        //Load the entire Gold Deck
        ArrayList<GoldCard> testCards = new ArrayList<>();
        for(int index=41; index<=80; index++){ testCards.add( (GoldCard) Deck.getCardBYid(index));}

        //int Arrays specific to test each condition for both true and false
        int[] existingPoints1 = new int[]{0, 1,1,1,1, 1,1,1 };
        int[] existingPoints2 = new int[]{0, 5,5,5,5, 2,2,2 };

        for(GoldCard currentCard : testCards){

            assertFalse(currentCard.isPlayable(existingPoints1));
            assertTrue(currentCard.isPlayable(existingPoints2));

        }
    }

    public void testGoldDeck_Points()
    {
        GoldCard[] testGoldenDeck = {
                new GoldCard(new int[]{0,1,1,6},41, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_BLUE),
                new GoldCard(new int[]{1,7,0,1},52, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_GREEN),
                new GoldCard(new int[]{8,1,1,0},53, PointCondition.ONE_FOR_PAPER, PlayCondition.THREE_SAME_ONE_PURPLE),
                new GoldCard(new int[]{1,1,0,1},44, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),
                new GoldCard(new int[]{1,0,7,0},67, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),
                new GoldCard(new int[]{1,0,1,0},71, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME)    };


        //check the AddPoints() method, the condition TwoForCorner is ignored and calculated in PlayCardByIndex;
        int[][] finalPoints = { {3, 3,5,7,6, 3,5,7}, {6, 3,5,7,6, 2,6,7}, {8, 3,5,7,6, 2,5,8}, {0, 3,5,7,6, 2,5,7}, {3, 3,5,7,6, 2,6,7}, {5, 3,5,7,6, 2,5,7}};

        //check the AddPoints() method, in the flipped condition
        int[][] flippedFinalPoints = { {0, 4,5,7,6, 2,5,7}, {0, 3,6,7,6, 2,5,7}, {0, 3,6,7,6, 2,5,7}, {0, 4,5,7,6, 2,5,7}, {0, 3,5,8,6, 2,5,7}, {0, 3,5,7,7, 2,5,7}};

        for(int index=0; index<testGoldenDeck.length; index++){
            assertArrayEquals( testGoldenDeck[index].addPoints(new int[] {0, 3,5,7,6, 2,5,7}), finalPoints[index] );

            testGoldenDeck[index].setFlipped(true);
            assertArrayEquals( testGoldenDeck[index].addPoints(new int[] {0, 3,5,7,6, 2,5,7}), flippedFinalPoints[index] );
        }


    }

    public void testResourceCards_correctReturnValues()
    {
        ResourceCard testCard = new ResourceCard(new int[]{0,1,2,3},10);

        //Check if the card returns the correct Corner Array;
        assertArrayEquals(testCard.getCorners(), new int[]{0,1,2,3});

        //Check if the card Flips and then returns the correct Corners
        testCard.setFlipped(true);
        assertTrue(testCard.isFlipped());

        //Check if the card returns the correct Corners once Flipped
        assertArrayEquals(testCard.getCorners(), new int[]{1,1,1,1});
    }

    public void testResource_Points()
    {
        ResourceCard[] testResourceDeck = {
                new ResourceCard(new int[]{0,2,4,6},1),
                new ResourceCard(new int[]{1,3,6,2},12),
                new ResourceCard(new int[]{8,6,4,2},13),
                new ResourceCard(new int[]{1,4,2,1},4),
                new ResourceCard(new int[]{1,7,3,0},28),
                new ResourceCard(new int[]{1,4,5,0},31)    };


        //check the AddPoints() method, the fifth card also tests for resource cards giving one free point
        int[][] finalPoints = { {0, 1,0,1,0, 1,0,0}, {0, 1,1,0,0, 1,0,0}, {0, 1,0,1,0, 1,0,1}, {0, 1,0,1,0, 0,0,0}, {1, 0,1,0,0, 0,1,0}, {0, 0,0,1,1, 0,0,0}};
        //check the AddPoints() method, in the flipped condition
        int[][] flippedFinalPoints = { {0, 1,0,0,0, 0,0,0},{0, 0,1,0,0, 0,0,0}, {0, 0,1,0,0, 0,0,0}, {0, 1,0,0,0, 0,0,0}, {0, 0,0,1,0, 0,0,0}, {0, 0,0,0,1, 0,0,0}};

        for(int index=0; index<testResourceDeck.length; index++){
            assertArrayEquals( testResourceDeck[index].addPoints(new int[] {0, 0,0,0,0, 0,0,0}), finalPoints[index] );

            testResourceDeck[index].setFlipped(true);
            assertTrue(testResourceDeck[index].isFlipped());
            assertTrue(testResourceDeck[index].isPlayable(new int[] {0, 0,0,0,0, 0,0,0 }));

            assertArrayEquals( testResourceDeck[index].addPoints(new int[] {0, 0,0,0,0, 0,0,0}), flippedFinalPoints[index] );
        }


    }


    public void testStartingCards_correctReturnValues()
    {
        StartingCard testCard = new StartingCard(new int[]{0,1,2,3},10, new int[]{0,2,3,4}, StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE);

        //Check if the card returns the correct Corner Array;
        assertArrayEquals(testCard.getCorners(), new int[]{0,1,2,3});

        //Check if the card Flips and then returns the correct Corners
        testCard.setFlipped(true);
        assertTrue(testCard.isFlipped());

        //Check if the card returns the correct Corners once Flipped
        assertArrayEquals(testCard.getCorners(), new int[]{0,2,3,4});
    }

    public void testStartingDeck_Points()
    {
        StartingCard[] testStartingDeck = {
                new StartingCard(new int[]{2,4,5,3},200,new int[]{1,4,5,1}, StartingPoints.STARTING_ONE_PURPLE),//check
                new StartingCard(new int[]{4,3,2,5},201,new int[]{3,1,1,2}, StartingPoints.STARTING_ONE_RED),//check
                new StartingCard(new int[]{5,3,2,4},202,new int[]{1,1,1,1}, StartingPoints.STARTING_ONE_RED_ONE_GREEN),//check
                new StartingCard(new int[]{4,5,3,2},203,new int[]{1,1,1,1}, StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE),
                new StartingCard(new int[]{5,2,4,3},204,new int[]{1,1,0,0}, StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN),
                new StartingCard(new int[]{2,3,4,5},205,new int[]{1,1,0,0}, StartingPoints.STARTING_ONE_GREEN_ONE_BLUE_ONE_RED) };


        //check the AddPoints() method, the condition TwoForCorner is ignored and calculated in PlayCardByIndex;
        int[] finalPoints = {0, 1,1,1,1, 0,0,0};
        //check the AddPoints() method, in the flipped condition
        int[][] flippedFinalPoints = { {0, 0,0,1,2, 0,0,0}, {0, 2,1,0,0, 0,0,0}, {0, 1,0,1,0, 0,0,0}, {0, 0,1,0,1, 0,0,0}, {0, 0,1,1,1, 0,0,0}, {0, 1,1,1,0, 0,0,0}};

        for(int index=0; index<testStartingDeck.length; index++){
            assertArrayEquals( testStartingDeck[index].addPoints(new int[] {0, 0,0,0,0, 0,0,0}), finalPoints );

            testStartingDeck[index].setFlipped(true);
            assertTrue(testStartingDeck[index].isFlipped());

            assertArrayEquals( testStartingDeck[index].addPoints(new int[] {0, 0,0,0,0, 0,0,0}), flippedFinalPoints[index] );
        }


    }


}
