package it.polimi.ingsw.ClientSide.Controller;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import it.polimi.ingsw.ClientSide.Utility.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;

/**
 * Manages all the inputs from the Player through the mouse.
 * Lets the player play from the mouse combining inputs with actions
 * @author Edoardo Carlo Murru
 */

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;
    private boolean chosenGoal_flag = false;
    final GUI_object[] GUI_Spaces = FULL_GUI.getGUI();

    /**
     * Instantiates the mouse event.
     *
     * @param gamePanel the game panel
     */
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * The chosen coordinates of an event are pointed out here.
     * Based on the state of the game, Players are allowed to play specific actions
     * according to the task of every GameState. The server is also shown a message accordingly,
     * such as "starting cards flipped" or "clicked on deck xy".
     *
     * @param e the event caused by the player
     */
    public void mouseClicked(MouseEvent e) {
        int xPos = e.getX();
        int yPos = e.getY();

        //C1C3 Gold, C2C4Normal, 0 Gold_Deck, 1 Deck
        switch (Client_Game.getCurrentScene()) {
            case MAIN_MENU:
                if (e.getButton() == BUTTON1) {

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[34])) {
                        Shortcuts.JoinLoop();
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[35])) {
                        Shortcuts.requestPort(gamePanel);
                        System.out.println("Reconnecting");
                    }

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[36])) {
                        Client_Game.ChangeScene(GameStates.OPTIONS);
                    } //Options
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[37])) {
                        System.exit(0);
                    } //Quit

                }
                break;

            case PLAYER_SELECTION:
                if (e.getButton() == BUTTON1) {

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[38])) {
                        Shortcuts.CreateLoop(2, gamePanel);
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[39])) {
                        Shortcuts.CreateLoop(3, gamePanel);
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[40])) {
                        Shortcuts.CreateLoop(4, gamePanel);
                    }

                }
                break;

            case CHOOSE_GOAL:
                if (e.getButton() == BUTTON1) {
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[31])) {
                        Client_IO.ChooseGoalCard(3);
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[32])) {
                        Client_IO.ChooseGoalCard(5);
                    }
                }
                break;

            case PLACE_STARTING:

                switch ((e.getButton())) {
                    case BUTTON1:
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[33])) {
                            ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[33]);
                        }

                        if (ClientConstants.SelectedCard >= 200 || ClientConstants.SelectedCard <= -200) {
                            Client_IO.PlaceStartingCard(ClientConstants.SelectedCard);
                            ClientConstants.SelectedCard = 0;
                        }

                        break;
                    case BUTTON3:
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[33])) {
                            Client_IO.FlipCard_inPos(4);
                            System.out.println("Flipped Starting Card");
                        }
                        break;
                }

                break;

            case PLAY:
                switch (e.getButton()) {

                    case BUTTON1: //left click

                        if (ClientConstants.isGameStarted()) {
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[28])) {
                                ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[28]);
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[29])) {
                                ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[29]);
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[30])) {
                                ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[30]);
                            }

                            //play Card
                            if (ClientConstants.SelectedCard != 0) {
                                int[] Grid_Indexes = HelperMethods.GetGridPosition(xPos, yPos);
                                if (Grid_Indexes != null) {
                                    if (Client_IO.playCardByIndex(Grid_Indexes[0], Grid_Indexes[1], ClientConstants.SelectedCard)) {
                                        ClientConstants.SelectedCard = 0;
                                        Client_Game.ChangeScene(GameStates.DRAW);
                                    }
                                }

                            }
                        }


                        break;

                    case BUTTON3: //right click

                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[28])) {
                            Client_IO.FlipCard_inPos(0);
                        }
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[29])) {
                            Client_IO.FlipCard_inPos(1);
                        }
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[30])) {
                            Client_IO.FlipCard_inPos(2);
                        }
                        break;
                }

                break;

            case DRAW:
                switch (e.getButton()) {
                    case BUTTON1: //left click
                        if (ClientConstants.isGameStarted()) {
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[4])) {
                                {
                                    System.out.println("Clicked on gold deck");
                                    Client_IO.DrawCard(0);
                                }
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[5])) {
                                System.out.println("Clicked on deck");
                                Client_IO.DrawCard(1);
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[6])) {
                                Client_IO.DrawCard(2);
                                System.out.println("Clicked on C1");
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[7])) {
                                Client_IO.DrawCard(3);
                                System.out.println("Clicked on C2");
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[8])) {
                                Client_IO.DrawCard(4);
                                System.out.println("Clicked on C3");
                            }
                            if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[9])) {
                                Client_IO.DrawCard(5);
                                System.out.println("Clicked on C4");
                            }
                        }

                        break;

                    case BUTTON3: //right click

                        System.out.println("Right mouse Button pressed");

                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[12])) {
                            Client_IO.FlipCard_inPos(0);
                        }
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[13])) {
                            Client_IO.FlipCard_inPos(1);
                        }
                        if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[14])) {
                            Client_IO.FlipCard_inPos(2);
                        }

                        break;
                }


                break;

            default:
                break;

        }

    }

    /**
     * Spots the movement of the mouse with no button pushed.
     * @param e  the event generated by the player
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Spots the mouse pressure (or click).
     * @param e  the event generated by the player
     * */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Spots the mouse release.
     * @param e  the event generated by the player
     * */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Spots the mouse entrance in an area.
     * @param e  the event generated by the player
     * */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Spots the mouse exiting an area.
     * @param e  the event generated by the player
     * */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Spots the mouse being dragged across the screen.
     * @param e  the event generated by the player
     * */
    public void mouseDragged(MouseEvent e) {
    }






}