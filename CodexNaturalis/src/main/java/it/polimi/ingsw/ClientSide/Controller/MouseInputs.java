package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;

/**
 * class manages all the inputs from the player through the mouse
 * */

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;
    private boolean chosenGoal_flag = false;
    GUI_object[] GUI_Spaces = FULL_GUI.getGUI();

    /**
     * instantiates the mouse event
     *
     * @param gamePanel
     */


    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * this class takes the chosen coordinates of the event.
     * based on the state of the game it allows players to play specific actions
     * respecting the function of every state
     *
     * @param e the event caused by the player
     */
    public void mouseClicked(MouseEvent e) {
        int xPos = e.getX();
        int yPos = e.getY();

        //C1C3 Gold, C2C4Normal, 0 Gold_Deck, 1 Deck
        switch (Client_Game.getCurrentScene()) {
            case "Main_Menu":
                if (e.getButton() == BUTTON1) {

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[34])) {
                        JoinLoop();
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[35])) { requestPort();
                        System.out.println("Reconnecting");
                    }

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[36])) {
                        Client_Game.ChangeScene(4);
                    } //Options
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[37])) {
                        System.exit(0);
                    } //Quit

                }
                break;

            case "Player_Selection":
                if (e.getButton() == BUTTON1) {

                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[38])) {
                        if(!Client_IO.CreateGame(2).contains("Joining")){ BackToMenu("Creation request");}
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[39])) {
                        if(!Client_IO.CreateGame(3).contains("Joining")){ BackToMenu("Creation request");}
                    }
                    if (HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[40])) {
                        if(!Client_IO.CreateGame(4).contains("Joining")){ BackToMenu("Creation request");}
                    }

                }
                break;

            case "Choose_Goal":
                if (e.getButton() == BUTTON1) {
                    if (!chosenGoal_flag && HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[31])) {
                        Client_IO.ChooseGoalCard(3);
                        chosenGoal_flag = true;
                    }
                    if (!chosenGoal_flag && HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[32])) {
                        Client_IO.ChooseGoalCard(5);
                        chosenGoal_flag = true;
                    }
                }
                break;

            case "Place_Starting":

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

            case "Play":
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
                                        Client_Game.ChangeScene(2);
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

            case "Draw":
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

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }


    private void requestUsername(String additionalInfo) {

        String userName = "";
        userName = JOptionPane.showInputDialog(additionalInfo+": you may select a new username");

        boolean correctUsername = false;
        while (!correctUsername) {



            if(userName!= null && userName.matches("^[a-zA-Z0-9]+$")){ correctUsername = true; }
            else{  userName = JOptionPane.showInputDialog("Invalid Username, please select a new one");}

        }

        Client_IO.setUsername(userName);

    }

    private void requestPort() {

        if(Client_IO.getUsername()== null){requestUsername("No username selected");}

        String port = null;
        port = JOptionPane.showInputDialog("Enter the port assigned to your previous game");

        boolean correctPort = false;
        while (!correctPort) {



            if(port!= null && Integer.parseInt(port)>0){ correctPort = true; }
            else{  port = JOptionPane.showInputDialog("Port number can't be empty or negative");}

        }

        if(Client_IO.Reconnect(Integer.parseInt(port)).contains("Reconnecting")){ FULL_GUI.updateGUI(); Client_Game.ChangeScene(3);  return; }

        BackToMenu("Reconnection attempt");

    }


    private void JoinLoop()
    {
        requestUsername("No username selected");
        String JoinStatus = Client_IO.JoinGame();
        if(JoinStatus.contains("username")){ requestUsername("Server already has that name connected, try a new one"); }
        else if(!JoinStatus.contains("Joining")){ Client_Game.ChangeScene(5); }
        else {Client_Game.ChangeScene(6);}
    }

    private void BackToMenu(String Reason)
    {
        JOptionPane.showMessageDialog(gamePanel, "The server has not accepted your "+Reason);
        Client_Game.ChangeScene(0);
    }
}