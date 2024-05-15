package main.java.it.polimi.ingsw;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import main.java.it.polimi.ingsw.ServerSide.GameServer;
import main.java.it.polimi.ingsw.ServerSide.Server_IO;
import main.java.it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

public class App
{
    public static void main( String[] args )
    {
        boolean ipSet= false;
        String response = "";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input server ip or leave it blank to host one yourself");

        //sets the server IP assuming that the client knows exactly what it is
        while(!ipSet) {
            response = scanner.nextLine();

            if(Objects.equals(response, "")){ break; }

            String[] SplitIp = response.split("\\.");
            if(SplitIp.length == 4)
            {
                ipSet = true;

                try {
                    for(String number : SplitIp){
                        if(Integer.parseInt(number)<0 || Integer.parseInt(number)>255 ){ ipSet = false; }
                    }
                }catch (NumberFormatException e){ ipSet=false; }

            }

            if(!ipSet){ System.out.println("The address you committed was not valid, try again");  }
        }

        if(!ipSet)
        {
            Server_IO.ServerRMI_impl UpdatedRMI;

            ServerConstants.setDebug(false);
            GameServer mainServer = new GameServer(1330, null);
            mainServer.start();

            try {
                Registry reg = LocateRegistry.createRegistry(1331);

                UpdatedRMI = new Server_IO.ServerRMI_impl();
                reg.bind("GetUpdates", UpdatedRMI);

            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }

            PersistenceManager.RestoreGames();
        }

        else {
            ClientConstants.setIP(response);
        }

        new Client_Game();


    }
}

