package it.polimi.ingsw;

import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ServerSide.GameServer;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

public class App
{
    public static void main( String[] args )
    {


        ServerConstants.setDebug(false);
        GameServer mainServer = new GameServer(1330); mainServer.start();
        new Client_Game();


    }
}

