package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Manages all the input from the Player through the keyboard.
 * Lets the player play from the keyboard combining inputs with actions.
 * @author Edoardo Carlo MUrru.
 */

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;

    /**
     * Instantiates the keyboard event.
     * @param gamePanel the game panel 
     */

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Indicates the pressed or typed key from the keyboard.
     * @param e the event to be processed
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Manages all the input of the keyboard such us: DOWN, UP, TAB, J,...;
     * Changes scenes or performs actions depending on the inputs of the keyboard.
     * @param e the occurred event
     */
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {

            //Only for Debug Purposes
            case KeyEvent.VK_O:
                if(ClientConstants.getGUI() && Client_Game.getCurrentScene().equals(GameStates.PLAYER_SELECTION)){
                    Shortcuts.CreateLoop(1, gamePanel);
                }
                break;

            case KeyEvent.VK_TAB: //not implemented
                System.out.println("TAB key pressed, Chat toggled");
                break;


            case KeyEvent.VK_U: //RequestUpdate
                Client_IO.requestUpdate();
                break;
            case KeyEvent.VK_N: //ScaleUP grid
                RenderPlayer.ScaleGrid(true);
                break;
            case KeyEvent.VK_M: //ScaleDOWN grid
                RenderPlayer.ScaleGrid(false);
                break;
            case KeyEvent.VK_H: //toggles the gui help banner
                ClientConstants.toggleGUIHelper();
                break;

        }

    }

    /**
     * Releases previously pressed key.
     * @param e the event to be processed
     */
    public void keyReleased(KeyEvent e) {
    }
}