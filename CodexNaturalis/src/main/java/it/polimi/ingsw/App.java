package main.java.it.polimi.ingsw;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Server_IO;
import main.java.it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App
{
    public static void main( String[] args )
    {

        Server_IO.ServerRMI_impl UpdatedRMI;

        ServerConstants.setDebug(false);
        GameServer mainServer = new GameServer(1330, null); mainServer.start();

        try {
            Registry reg = LocateRegistry.createRegistry(1331);

            UpdatedRMI = new Server_IO.ServerRMI_impl();
            reg.bind("GetUpdates", UpdatedRMI);

        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        PersistenceManager.RestoreGames();

        new Client_Game();


    }
}

