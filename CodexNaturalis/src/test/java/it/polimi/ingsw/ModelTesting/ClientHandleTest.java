package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import it.polimi.ingsw.ServerSide.Table.Player;
import it.polimi.ingsw.ServerSide.UpdateClasses.ClientHandler;
import junit.framework.TestCase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;

public class ClientHandleTest extends TestCase {

    public void testClientHandle()
    {
        String[] commands = new String[]{"SendUpdate","GameStartedStatus","SendCurrentTurn", "Flip",
        "Draw", "ChooseGoalCard", "PlaceStartingCard", "playCardByIndex", "getNewPort", "JoinPackage", "AttemptingReconnection", "CreateGame",
        "getUsernames", "getCurrentPlayerGrid", "CLi"};

        byte[] data = new byte[2048];
        DatagramPacket pack = new DatagramPacket(data, data.length);

        MultipleGameManager.CreateGame("TestPlayer", 1);
        Game testGame = MultipleGameManager.getGameInstance("TestPlayer");

        assert testGame != null;
        testGame.getPlayers().getFirst().setCard(2, 15);

        try {
            ClientHandler cl = new ClientHandler(pack, testGame, new DatagramSocket(1550));

            for(String command:commands)
            {
                assertNotEquals("", cl.getOutput(new String[]{command, "TestPlayer", "2", "2", "2"}));
            }



        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        testGame.end();

    }



}
