package it.polimi.ingsw;

import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import it.polimi.ingsw.ServerSide.GameServer;
import it.polimi.ingsw.ServerSide.Server_IO;
import it.polimi.ingsw.ServerSide.Utility.PersistenceManager;

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

        //Allow to enter the ip in configuration window

        if(args.length>0 && args[0] != null)
        {
            ipSet = true;
            ClientConstants.setIP(args[0]);
        }

        //sets the server IP assuming that the client knows exactly what it is
        while(!ipSet) {
            System.out.println("Input server ip ");
            response = scanner.nextLine();

            if(Objects.equals(response, "")){ break; }

            String[] SplitIp = response.split("\\.");
            if(SplitIp.length == 4)
            {
                ipSet = true;

                try {
                    for(String number : SplitIp){
                        if (Integer.parseInt(number) < 0 || Integer.parseInt(number) > 255) {
                            ipSet = false;
                            break;
                        }
                    }
                }catch (NumberFormatException e){ ipSet=false; }

            }

            if(!ipSet){ System.out.println("The address you committed was not valid, try again");  }
            else {
                ClientConstants.setIP(response);
            }
        }


        new Client_Game();


    }
}

