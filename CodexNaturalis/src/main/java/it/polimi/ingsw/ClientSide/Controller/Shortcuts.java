package it.polimi.ingsw.ClientSide.Controller;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.MainClasses.GameStates;

import javax.swing.*;

/**
 * This interface contains useful methods in order for the controller to manage all the actions before the game  */

public interface Shortcuts {

    /**
     * Loop where the controller waits for the player to join the game.
     * Player has to select the username and the controller checks if it is already taken or not.
     * In the first case, it notifies the player that the username is unavailable, in the latter it takes
     * the Player to the choose goal scene if the join status is 'joining', otherwise the Player is taken
     * to the Player selection screen.
     * **/
    static void JoinLoop()
    {
        requestUsername("No username selected");
        String JoinStatus = Client_IO.JoinGame();
        if(JoinStatus.contains("username")){ requestUsername("Server already has that name connected, try a new one"); }
        else if(!JoinStatus.contains("Joining")){ Client_Game.ChangeScene(GameStates.PLAYER_SELECTION); }
        else {
            Client_IO.getNewPort();
            Client_Game.ChangeScene(GameStates.CHOOSE_GOAL);
        }
    }

    /**
     * The loop of the create function: it waits for a successful game creation, which would cause
     * the game to get a new port, change scene and request an update, or remain in the loop
     * in case of unsuccessful create game request.
     *
     * @param gamePanel     the panel of the game
     * @param playerCount   number of players in the game
     *
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
     * Command to get back to the main menu.
     *
     * @param gamePanel   the panel of the game
     * @param Reason      message that appears on the screen
     * */
    static void BackToMenu(String Reason, GamePanel gamePanel)
    {
        gamePanel.showMessage(Reason);
        Client_Game.ChangeScene(GameStates.MAIN_MENU);
    }

    /**
     * Checks, after the player inserts his username, if it's valid , in which case it sets the username
     * or otherwise asking for a new one.
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
     * Manages the request of the port from the player;
     * After entering the port number, method checks if the port is correct (not empty, negative value)
     * and either reconnects the player to the game or takes him back to the menu.
     *
     */
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
