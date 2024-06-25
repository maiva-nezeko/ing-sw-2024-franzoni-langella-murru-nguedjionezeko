package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import junit.framework.TestCase;

public class MultipleGameTest extends TestCase {

    public void testCreateGame() {

        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));
        assertNotNull(MultipleGameManager.getGameInstance("TestGame"));

        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        assertNotNull(testGame.getPlayers());

        testGame.end();

    }


    public void testJoinGame()
    {
        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));
        assertFalse(MultipleGameManager.JoinGame("TestGame")); //player already present
        assertFalse(MultipleGameManager.JoinGame("TestGame2")); //game full

        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        testGame.end();

    }

    public void testReconnect()
    {
        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));


        assertNotNull(MultipleGameManager.getGameInstance("TestGame"));

        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame!= null;

        assertNotNull( MultipleGameManager.Reconnect("TestGame", testGame.getPort()) );
        assertEquals( testGame, MultipleGameManager.Reconnect("TestGame", testGame.getPort()) );

        testGame.end();


    }



}
