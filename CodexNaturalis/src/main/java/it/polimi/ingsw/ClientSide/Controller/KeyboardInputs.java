package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import main.java.it.polimi.ingsw.ClientSide.Utility.HelperMethods;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * class manages all the input from the player through the keyboard
 * */

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;
    private int SelectedSpace;

    /**
     * instantiates the keyboard event
     * @param gamePanel */

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * manage all the input of the keyboard like: DOWN, UP, TAB, J,...;
     * the class changes the scenes or performs the actions depending on the inputs of the keyboard
     * @param e the event occurred
     * */
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            //Only for Debug Purposes
            case KeyEvent.VK_O:
                if(ClientConstants.getGUI() && Client_Game.getCurrentScene().equals(GameStates.PLAYER_SELECTION)){
                    Shortcuts.CreateLoop(1, gamePanel);
                }
                break;
            case KeyEvent.VK_DOWN:
                if(ClientConstants.getGUI()){
                    System.out.println("Down key was pressed");
                    Client_Game.ChangeScene(GameStates.DRAW);}
                break;
            case KeyEvent.VK_UP:
                if(ClientConstants.getGUI()){
                    System.out.println("Up key was pressed");
                    Client_Game.ChangeScene(GameStates.PLAY);}
                break;
            //END OF _Only for Debug Purposes

            case KeyEvent.VK_ESCAPE:
                if(ClientConstants.getGUI()){
                    System.out.println("Menu key was pressed");
                    Client_Game.ChangeScene(GameStates.MAIN_MENU);}
                break;

            case KeyEvent.VK_J:
                if(ClientConstants.getGUI()){
                    System.out.println("Joining Game");

                    Client_Game.JoinGame();
                    Client_IO.requestUpdate();}
                break;

            case KeyEvent.VK_TAB:
                System.out.println("TAB key pressed, Chat toggled");
                break;


            case KeyEvent.VK_U: //RequestUpdate
                Client_IO.requestUpdate();
                break;
            case KeyEvent.VK_N: //RequestUpdate
                RenderPlayer.ScaleUpGrid();
                break;
            case KeyEvent.VK_M: //RequestUpdate
                RenderPlayer.ScaleDownGrid();
                break;
            case KeyEvent.VK_S: //PlaceStartingCard
                Client_IO.PlaceStartingCard(Client_IO.requestPlayerHand()[4]);
                break;
            case KeyEvent.VK_H: //toggles the gui help banner
                ClientConstants.toggleGUIHelper();
                break;
            //FLIP & PLAY
            case KeyEvent.VK_F: //FLIP
                if(SelectedSpace != -1){Client_IO.FlipCard_inPos(SelectedSpace);}
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
    }
}