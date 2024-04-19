package main.java.it.polimi.ingsw;

import main.java.it.polimi.ingsw.ServerSide.Cards.ResourceCard;
import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import main.java.it.polimi.ingsw.Server_Testing.TestAggregator;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        boolean test = false;
        ServerConstants.setDebug(false);

        if(test){ TestAggregator.RunTest(); }
        else {  GameServer mainServer = new GameServer(1330); mainServer.start();
            //MultipleGameManager.addGame(1, new ArrayList<>());
        }
    }
}
