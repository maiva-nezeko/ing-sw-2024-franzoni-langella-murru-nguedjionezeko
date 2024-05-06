package it.polimi.ingsw.ClientSide.Controller;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

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

            case KeyEvent.VK_DOWN:
                if(ClientConstants.getGUI()){
                    System.out.println("Down key was pressed");
                    Client_Game.ChangeScene(2);}
                break;
            case KeyEvent.VK_UP:
                if(ClientConstants.getGUI()){
                    System.out.println("Up key was pressed");
                    Client_Game.ChangeScene(3);}
                break;

            case KeyEvent.VK_ESCAPE:
                if(ClientConstants.getGUI()){
                    System.out.println("Menu key was pressed");
                    Client_Game.ChangeScene(1);}
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
            //keys to play
            //DRAW
            case KeyEvent.VK_1: //Draw From GoldDeck
                Client_IO.DrawCard(0);
                break;
            case KeyEvent.VK_2: //Draw From Gold1
                Client_IO.DrawCard(1);
                break;
            case KeyEvent.VK_3: //Draw From Gold2
                Client_IO.DrawCard(2);
                break;
            case KeyEvent.VK_4: //Draw From ResourceDeck
                Client_IO.DrawCard(3);
                break;
            case KeyEvent.VK_5: //Draw From Resource1
                Client_IO.DrawCard(4);
                break;
            case KeyEvent.VK_6: //Draw From Resource2
                Client_IO.DrawCard(5);
                break;
            //SELECT
            case KeyEvent.VK_Q: //SELECT1
                SelectedSpace = 0; ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[0];
                break;
            case KeyEvent.VK_W: //SELECT2
                SelectedSpace = 1; ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[1];
                break;
            case KeyEvent.VK_E: //SELECT3
                SelectedSpace = 2; ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[2];
                break;
            case KeyEvent.VK_R: //SELECT_Starting
                SelectedSpace = 4; ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[4];
                break;
            case KeyEvent.VK_U: //RequestUpdate
                Client_IO.requestUpdate();
                break;
            case KeyEvent.VK_S: //PlaceStartingCard
                Client_IO.PlaceStartingCard(Client_IO.requestPlayerHand()[4]);
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