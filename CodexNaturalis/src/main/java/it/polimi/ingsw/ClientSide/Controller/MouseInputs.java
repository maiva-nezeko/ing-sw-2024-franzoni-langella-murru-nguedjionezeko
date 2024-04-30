package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;
    private boolean chosenGoal_flag=false;
    GUI_object[] GUI_Spaces = FULL_GUI.getGUI();

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void mouseClicked(MouseEvent e) {
        int xPos = e.getX();
        int yPos = e.getY();

        //C1C3 Gold, C2C4Normal, 0 Gold_Deck, 1 Deck
        switch (Client_Game.getCurrentScene())
        {
            case "Main_Menu":
                if(e.getButton() == BUTTON1){

                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[34])){Client_Game.JoinGame();}
                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[35])){Client_Game.ChangeScene(2);
                        System.out.println("NewLocalGame"); Client_Game.ChangeScene(5);} //NewLocalGame

                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[36])){Client_Game.ChangeScene(4);} //Options
                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[37])){System.exit(0);} //Quit

                }
                break;

            case "NewLocalGame_Player_Selection":
                if(e.getButton() == BUTTON1){

                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[38])){
                        System.out.println("NewLocalGame, 2Players");} //NewLocalGame_2Players
                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[39])){
                        System.out.println("NewLocalGame, 2Players");} //NewLocalGame_3Players
                    if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[40 ])){
                        System.out.println("NewLocalGame, 2Players");} //NewLocalGame_4Players

                }
                break;

            case "Game_Menu": break;

            case "WaitForTurn": break;

            case "Play":
                    switch (e.getButton()){

                        case BUTTON1: //left click

                            if(ClientConstants.isGameStarted()){
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[28])){ ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[28]); }
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[29])){ ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[29]); }
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[30])){ ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[30]); }
                                //placeStartingCard
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[33])){ ClientConstants.SelectedCard = HelperMethods.SelectCard(GUI_Spaces[33]); }
                                //chooseGoalCard
                                if(!chosenGoal_flag && HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[31])){ Client_IO.ChooseGoalCard(3); chosenGoal_flag=true; }
                                if(!chosenGoal_flag && HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[32])){ Client_IO.ChooseGoalCard(5); chosenGoal_flag=true; }

                                //play Card
                                if( ClientConstants.SelectedCard != 0 ){

                                    if(ClientConstants.SelectedCard>=200 || ClientConstants.SelectedCard<=-200)
                                        { Client_IO.PlaceStartingCard(ClientConstants.SelectedCard); ClientConstants.SelectedCard = 0; }

                                    else {
                                        int[] Grid_Indexes = HelperMethods.GetGridPosition(xPos, yPos);
                                        if (Grid_Indexes != null) {
                                            if(Client_IO.playCardByIndex( Grid_Indexes[0], Grid_Indexes[1], ClientConstants.SelectedCard)){
                                                ClientConstants.SelectedCard = 0; Client_Game.ChangeScene(2);}}

                                    }
                                }}

                            break;

                        case BUTTON3: //right click

                            if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[28])){ Client_IO.FlipCard_inPos(0);}
                            if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[29])){ Client_IO.FlipCard_inPos(1);}
                            if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[30])){ Client_IO.FlipCard_inPos(2);}

                            if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[33])){ Client_IO.FlipCard_inPos(4);
                                System.out.println("Flipped Starting Card");}

                            break;
                    }

                break;

            case "Draw":
                    switch(e.getButton()){
                        case BUTTON1: //left click
                            if(ClientConstants.isGameStarted()){
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[4])){{System.out.println("Clicked on gold deck"); Client_IO.DrawCard(0);}}
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[5])){System.out.println("Clicked on deck");  Client_IO.DrawCard(1);}
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[6])){ Client_IO.DrawCard(2); System.out.println("Clicked on C1");}
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[7])){ Client_IO.DrawCard(3); System.out.println("Clicked on C2"); }
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[8])){ Client_IO.DrawCard(4); System.out.println("Clicked on C3"); }
                                if(HelperMethods.is_Inside(xPos, yPos, GUI_Spaces[9])){ Client_IO.DrawCard(5); System.out.println("Clicked on C4"); }}

                                break;

                        case BUTTON3: //right click

                                System.out.println("Right mouse Button pressed");

                                if(HelperMethods.is_Inside(xPos, yPos,  GUI_Spaces[12])){ Client_IO.FlipCard_inPos(0); }
                                if(HelperMethods.is_Inside(xPos, yPos,  GUI_Spaces[13])){ Client_IO.FlipCard_inPos(1); }
                                if(HelperMethods.is_Inside(xPos, yPos,  GUI_Spaces[14])){ Client_IO.FlipCard_inPos(2);}

                                break;
                }


                break;

            default: break;

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

}