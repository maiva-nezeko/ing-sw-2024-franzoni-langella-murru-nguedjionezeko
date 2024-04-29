package test.java.it.polimi.ingsw;

import main.java.it.polimi.ingsw.ServerSide.Cards.*;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.Table.*;


import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;
import static org.testng.Assert.*;

public class TableTest {

    @Test
    public void CorrectDeckCreation() {
        for (int index = -250; index < 250; index++)
        {
            int ID = abs(index);

            Card card = Deck.getCardBYid(index);
            Card goalCard = Deck.getGoalCardByID(index);


            if(ID>0 && ID<=40){
                assertNotNull(card);
                assertTrue( card instanceof ResourceCard);

                assertNull(goalCard);
            }
            else if(ID>40 && ID <= 80){
                assertNotNull(card);
                assertTrue( card instanceof GoldCard);

                assertNull(goalCard);
            }
            else if(index>=100 && index<=115){

                assertNotNull(goalCard);
                assertNull(card);
            }
            else if(ID>=200 && ID <= 205){
                assertNotNull(card);
                assertTrue( card instanceof StartingCard);

                assertNull(goalCard);
            }
            else
            {
                assertNull(card);
                assertNull(goalCard);
            }


        }
    }

    @Test
    public void Player_CorrectReturnValuesAndSetters()
    {
        Player testPlayer = new Player("TestPlayer");

        //checks correct creation of the Player Object
        assertEquals(testPlayer.getUsername(), "TestPlayer");

        testPlayer.setCard(0, 41);

        //test for getEmptySlot
        assertEquals(testPlayer.getEmptySlot(), 1);

        testPlayer.setCard(1, 20);
        testPlayer.setCard(2, -30);

        //getEmptySlot ignores the specials Slot, so should return -1
        assertEquals(testPlayer.getEmptySlot(), -1);

        testPlayer.setCard(3,205);
        testPlayer.setCard(4,105);
        testPlayer.setCard(5, 102);

        //wrong Calls to setCard that should not return any changes
        testPlayer.setCard(9, 10);
        testPlayer.setCard(3, 250);

        //expected result for getPrivateCards() after the setters are called;
        assertEquals(testPlayer.getPrivateCardsID(), new int[] {41, 20, -30, 205, 105, 102});

        //test for the flip function, the second call should not produce any changes
        testPlayer.FlipCard(1);
        testPlayer.FlipCard(3);
        assertEquals(testPlayer.getPrivateCardsID(), new int[] {41, -20, -30, 205, 105, 102});

        //test for the consumeCard() function, the second call should not produce any changes
        testPlayer.consumeCard(41);
        testPlayer.consumeCard(12);
        assertEquals(testPlayer.getPrivateCardsID(), new int[] {0, -20, -30, 205, 105, 102});

        //test for correct ScoreBoard setting and interaction
        testPlayer.setScoreBoard(new int[]{0, 1,2,3,4, 5,6,7});
        assertEquals(testPlayer.getScoreBoard(), new int[]{0, 1,2,3,4, 5,6,7});

    }

    @Test
    public void Table_CorrectDecksCreations()
    {
        ArrayList<Player> Players = new ArrayList<Player>();
        Players.add(new Player("TestPlayer"));

        Game testgame = new Game(1,Players, 1331);
        Table testTable = testgame.getRelatedTable();

        assertNotNull(testTable.getOccupiedSpaces());
        int[][][] tableSpaces = testTable.getOccupiedSpaces();

        assertEquals(tableSpaces.length, 1);
        assertEquals(tableSpaces[0].length, ServerConstants.getNumOfRows());
        assertEquals(tableSpaces[0][0].length, ServerConstants.getNumOfRows()/2);

        assertNotNull(testTable.getPublicSpacesID());
        int[] tablePublicSpaces = testTable.getPublicSpacesID();

        //we fill the table spaces
        testTable.AutoFillSpaces();

        //the two deck spaces contain flipped cards
        assertTrue((tablePublicSpaces[0] >= -80) && (tablePublicSpaces[0]<-40));
        assertTrue((tablePublicSpaces[1] >= -40) && (tablePublicSpaces[1]<-0));

        //space 2 and 4 have precedence to gold cards
        assertTrue((tablePublicSpaces[2] > 40) && (tablePublicSpaces[2]<=80));
        assertTrue((tablePublicSpaces[4] > 40) && (tablePublicSpaces[4]<=80));

        //space 3 and 5 have precedence for resource cards
        assertTrue((tablePublicSpaces[3] > 0) && (tablePublicSpaces[3]<=40));
        assertTrue((tablePublicSpaces[5] > 0) && (tablePublicSpaces[5]<=40));

        //Testing DealCards function
        testTable.DealCards();
        assertNotNull(testTable.getGoalCards());

        Player testPlayer = testgame.getPlayerByUsername("TestPlayer");


        int specialCardID = 0;
        for(int ID : testPlayer.getPrivateCardsID())
        {
            assertTrue(ID>0);

            if(specialCardID == 3){ assertTrue(ID>=100); }
            if(specialCardID == 4){ assertTrue(ID>=200); }
            if(specialCardID == 5){ assertTrue(ID>=100); }

            specialCardID++;
        }

        //TestingDrawCards
        int[] OldHand = testPlayer.getPrivateCardsID();

        //with a full hand, this method should not produce any changes
        testTable.DrawCard(0, "TestPlayer");
        testPlayer = testgame.getPlayerByUsername("TestPlayer");

        assertEquals(testPlayer.getPrivateCardsID(), OldHand);

        //now trying after removing the first card
        testPlayer.setCard(0,0);
        assertEquals(testPlayer.getEmptySlot(), 0);

        int CardInPos0 = testTable.getPublicSpacesID()[0];


        testTable.DrawCard(0, "TestPlayer");
        testPlayer = testgame.getPlayerByUsername("TestPlayer");

        assertNotEquals(testPlayer.getPrivateCardsID()[0], 0);

        assertEquals(testgame.getPlayerByUsername("TestPlayer").getPrivateCardsID()[0], -CardInPos0);

        int oldFromSpace = testTable.getPublicSpacesID()[2];

        testTable.MoveCard(2, 3);

        assertNotEquals(oldFromSpace, 0);
        assertEquals(testTable.getPublicSpacesID()[3], -oldFromSpace);
    }

}
