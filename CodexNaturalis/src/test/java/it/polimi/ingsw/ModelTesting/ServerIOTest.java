package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import it.polimi.ingsw.ServerSide.Server_IO;
import junit.framework.TestCase;

public class ServerIOTest extends TestCase {

    public void testGetUsernames()
    {
        MultipleGameManager.CreateGame("TestGame", 1);
        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        assertEquals("TestGame,", Server_IO.getUsernames(testGame));


        testGame.end();

    }

    public void testFlip()
    {

        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));
        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        testGame.getRelatedTable().AutoFillSpaces();
        int card = testGame.getPlayers().getFirst().getPrivateCardsID()[0];

        Server_IO.Flip(0, "TestGame");
        assertEquals(-card, testGame.getPlayers().getFirst().getPrivateCardsID()[0]);

        testGame.end();


    }

    public void testChooseGoal()
    {
        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));
        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        testGame.getRelatedTable().AutoFillSpaces();
        int card = testGame.getPlayers().getFirst().getPrivateCardsID()[5];

        Server_IO.ChooseGoalCard(5, "TestGame");
        assertEquals(card, testGame.getPlayers().getFirst().getPrivateCardsID()[3]);

        testGame.end();
    }

    public void testSocketUpdate()
    {
        assertTrue(MultipleGameManager.CreateGame("TestGame", 1));
        Game testGame  = MultipleGameManager.getGameInstance("TestGame");
        assert testGame != null;

        assertEquals(testGame.sendUpdatePackage("TestGame").toString().split("\n")[1],
                Server_IO.SocketUpdate("TestGame").split(";")[1]);




        testGame.end();
    }

}
