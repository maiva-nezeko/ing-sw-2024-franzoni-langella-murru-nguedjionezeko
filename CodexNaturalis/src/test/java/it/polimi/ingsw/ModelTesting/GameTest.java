package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.UpdatePackage;
import it.polimi.ingsw.ServerSide.Table.Player;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GameTest extends TestCase {

    public void testAddPlayer()
    {

        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        testGame.addPlayer("TestPlayer");

        assertTrue(testGame.isGameStarted());
        assertNotNull(testGame.getPlayers().getFirst());

        testGame.end();
    }

    public void testGetPlayerByUsername()
    {

        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        testGame.addPlayer("TestPlayer");

        assertTrue(testGame.isGameStarted());
        assertNotNull(testGame.getPlayerByUsername("TestPlayer"));
        assertNull(testGame.getPlayerByUsername("Test2"));

        testGame.end();

    }

    public void testSendUpdatePackage()
    {

        ArrayList<Player> playerList = new ArrayList<>();
        Game testGame = new Game(1, playerList, 1335);

        testGame.addPlayer("TestPlayer");
        UpdatePackage pack = testGame.sendUpdatePackage("TestPlayer");

        assertNotNull(pack);
        assertNotNull(pack.getChosenPlayerBoard());
        assertNotNull(pack.getChosenPlayerHand());
        assertNotNull(pack.getPublicCards());


        testGame.end();

    }

}
