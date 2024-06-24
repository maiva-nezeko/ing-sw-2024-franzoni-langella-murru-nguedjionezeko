package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.swing.*;
import java.rmi.RemoteException;

public class ClientExceptionHandler {

    public static void ServerUnreachable(RemoteException e){ HandleServerUnreachable(e); }
    public static void ServerUnreachable(RuntimeException e){ HandleServerUnreachable(e); }


    private static void HandleServerUnreachable(Exception ignoredE) {

        try {
            if(ClientConstants.getSocket()){
                String ServerCheck = GameClient.checkIfClosed("isClosed," + Client_IO.getUsername(), 1330);
                if( !ServerCheck.contains("Unable to reach") && !ServerCheck.contains("no")){ CalculateWinner(); }
            }
        }
        finally {

            if(ClientConstants.getGUI()){
                JOptionPane.showMessageDialog(Client_Game.getGamePanel(), "the servers are currently unreachable, " +
                        "please try our reconnection function once they are back up, note that your current port was:"+
                        ClientConstants.getPort());

            }
            else{
                System.out.println("the servers are currently unreachable, " +
                        "please try our reconnection function once they are back up, note that your current port was:"+
                        ClientConstants.getPort()
                );
            }

            Client_Game.ChangeScene(GameStates.MAIN_MENU);
        }

    }

    static void CalculateWinner() {

        String[] Usernames = Client_IO.getGame_usernames();
        int[] Scores = Client_IO.requestPlayerScore();
        int myScore=0;
        int maxScore=0;

        for(int index =0; index < Usernames.length; index++)
        {
            if(Scores[index]>maxScore){ maxScore = Scores[index]; }
            if(Usernames[index].equals(Client_IO.getUsername())){ myScore = Scores[index]; }
        }

        if(myScore >= maxScore){Client_Game.endGame(GameStates.YOU_WIN); return;}
        Client_Game.endGame(GameStates.YOU_LOSE);

    }

}
