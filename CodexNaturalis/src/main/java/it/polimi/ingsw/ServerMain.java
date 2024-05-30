package it.polimi.ingsw;

import it.polimi.ingsw.ServerSide.GameServer;
import it.polimi.ingsw.ServerSide.Server_IO;
import it.polimi.ingsw.ServerSide.Utility.PersistenceManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args)
    {
        Server_IO.ServerRMI_impl UpdatedRMI;

        GameServer mainServer = new GameServer(1330, null);
        Thread mainGameThread = new Thread(mainServer);
        mainGameThread.start();

        try {
            Registry reg = LocateRegistry.createRegistry(1331);

            UpdatedRMI = new Server_IO.ServerRMI_impl();
            reg.bind("GetUpdates", UpdatedRMI);

        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        PersistenceManager.RestoreGames();

    }



}
