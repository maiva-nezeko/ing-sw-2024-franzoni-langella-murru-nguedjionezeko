package it.polimi.ingsw.ClientSide.Controller;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.MainClasses.GameStates;

import javax.swing.*;

/**
 * interface contains useful methods for the controller
 * to manage all the action before the game  */

public interface Shortcuts {

    /**
     * loop where the controller wait the player to join in the game.
     * player has to select the username and the controller
     * check if it is already taken or not.
     * ok: the state changes
     * not ok: insert a new username
     * **/
    static void JoinLoop()
    {
        requestUsername("No username selected");
        String JoinStatus = Client_IO.JoinGame();
        if(JoinStatus.contains("username")){ requestUsername("Server already has that name connected, try a new one"); }
        else if(!JoinStatus.contains("Joining")){ Client_Game.ChangeScene(GameStates.PLAYER_SELECTION); }
        else {  Client_Game.ChangeScene(GameStates.CHOOSE_GOAL);    }
    }

    /**
     * method create a loop;
     * it waits during the dialog between client and server about
     * create a new game of a certain number of player.
     * method based on the answer decides if change the state or remaining in the loop
     *
     * @param gamePanel     the panel of the game
     * @param playerCount   number of player in the game
     */
    static void CreateLoop(int playerCount, GamePanel gamePanel)
    {
        if(!Client_IO.CreateGame(playerCount).contains("Joining")){
            BackToMenu("The server has not accepted your Creation request", gamePanel);}
        else{
            Client_IO.getNewPort();
            Client_Game.ChangeScene(GameStates.CHOOSE_GOAL);
            Client_IO.requestUpdate();
        }
    }

    /**
     * command to get back to the main menu
     * @param gamePanel   the panel of the game
     * @param Reason      message that appears on the screen
     * */
    static void BackToMenu(String Reason, GamePanel gamePanel)
    {
        gamePanel.showMessage(Reason);
        Client_Game.ChangeScene(GameStates.MAIN_MENU);
    }

    /**
     * check, after the player inserts his username, if it's correct or not:
     * asking for a new one if it is invalid or
     * set the username for the player if it is valid
     *
     * @param additionalInfo   a string containing info
     * */

    static void requestUsername(String additionalInfo) {

        String userName;
        userName = JOptionPane.showInputDialog(additionalInfo+": you may select a new username");

        boolean correctUsername = false;
        while (!correctUsername) {



            if(userName!= null && userName.matches("^[a-zA-Z0-9]+$")){ correctUsername = true; }
            else{  userName = JOptionPane.showInputDialog("Invalid Username, please select a new one");}

        }

        Client_IO.setUsername(userName);

    }

    /**
     * manage the request of the port by the player;
     * after entering the port name, method checks if the port is correct (not empty, negative value)
     * and chose to reconnect the player to the game or get him back to the menu*/

    static void requestPort(GamePanel gamePanel) {

        if(Client_IO.getUsername()== null){requestUsername("No username selected");}

        String port;
        port = JOptionPane.showInputDialog("Enter the port assigned to your previous game");

        boolean correctPort = false;
        while (!correctPort) {



            if(port!= null && Integer.parseInt(port)>0){ correctPort = true; }
            else{  port = JOptionPane.showInputDialog("Port number can't be empty or negative");}

        }

        if( !Client_IO.Reconnect(Integer.parseInt(port)).contains("failed") ){ FULL_GUI.updateGUI(); return; }

        Shortcuts.BackToMenu("The server has not accepted your reconnection attempt", gamePanel);

    }

}
