package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;

import javax.swing.*;

public interface Shortcuts {

    static void JoinLoop()
    {
        requestUsername("No username selected");
        String JoinStatus = Client_IO.JoinGame();
        if(JoinStatus.contains("username")){ requestUsername("Server already has that name connected, try a new one"); }
        else if(!JoinStatus.contains("Joining")){ Client_Game.ChangeScene(5); }
        else {  Client_Game.ChangeScene(6);    }
    }

    static void CreateLoop(int playerCount, GamePanel gamePanel)
    {
        if(!Client_IO.CreateGame(playerCount).contains("Joining")){
            BackToMenu("Creation request", gamePanel);}
        else{
            Client_IO.getNewPort();
            Client_Game.ChangeScene(6);
            Client_IO.requestUpdate();
        }
    }


    static void BackToMenu(String Reason, GamePanel gamePanel)
    {
        JOptionPane.showMessageDialog(gamePanel, "The server has not accepted your "+Reason);
        Client_Game.ChangeScene(0);
    }


    static void requestUsername(String additionalInfo) {

        String userName = "";
        userName = JOptionPane.showInputDialog(additionalInfo+": you may select a new username");

        boolean correctUsername = false;
        while (!correctUsername) {



            if(userName!= null && userName.matches("^[a-zA-Z0-9]+$")){ correctUsername = true; }
            else{  userName = JOptionPane.showInputDialog("Invalid Username, please select a new one");}

        }

        Client_IO.setUsername(userName);

    }

    static void requestPort(GamePanel gamePanel) {

        if(Client_IO.getUsername()== null){requestUsername("No username selected");}

        String port = null;
        port = JOptionPane.showInputDialog("Enter the port assigned to your previous game");

        boolean correctPort = false;
        while (!correctPort) {



            if(port!= null && Integer.parseInt(port)>0){ correctPort = true; }
            else{  port = JOptionPane.showInputDialog("Port number can't be empty or negative");}

        }

        if(Client_IO.Reconnect(Integer.parseInt(port)).contains("Reconnecting")){ FULL_GUI.updateGUI(); Client_Game.ChangeScene(3);  return; }

        Shortcuts.BackToMenu("Reconnection attempt", gamePanel);

    }

}
