package it.polimi.ingsw.ServerTesting;

import it.polimi.ingsw.ServerSide.MainClasses.UpdatePackage;
import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

public class UpdatePackageTest extends TestCase {

    public void testPackageCreationAndGetters()
    {
        int[][] Board = new int[][] { {1,2},{3,4} };
        int Score = 18;
        int[] Hand = new int[] {7,8,9};
        int[] publicCards = new int[] {10,11,12};

        UpdatePackage pack = new UpdatePackage(Board, Score, Hand, publicCards);

        assertEquals(Board, pack.getChosenPlayerBoard());
        assertEquals(Score, pack.getChosenPlayerScore());
        assertEquals(Hand, pack.getChosenPlayerHand());
        assertEquals(publicCards, pack.getPublicCards());

        String expectedString =
                "18\n" +
                "[10, 11, 12]\n" +
                "[7, 8, 9]\n" +
                "[[1, 2], [3, 4]]\n";
        assertEquals(expectedString, pack.toString());

    }



}
