package main.java.it.polimi.ingsw.ClientSide.Controller;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.util.Scanner;
/**
 * all the command a player can play are manage here, like drawing cards, choosing the position in the board
 * select goal and starting cards and check if the space in the board you chose are valid and if you can play the card */
public interface TUI_Inputs {
    static void waitForInput() {

        boolean InputResolved = false; boolean playedFlag;
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        int SelectedSpace = -1;

        while(!InputResolved) {

            System.out.println("Type one of the commands listed above, please use the single character identifier");
            String command = scanner.nextLine();

            switch (command.toLowerCase())
            {
                //draw
                case "0" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(0); InputResolved=true;}}
                case "1" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(1); InputResolved=true;}}
                case "2" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(2); InputResolved=true;}}
                case "3" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(3); InputResolved=true;}}
                case "4" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(4); InputResolved=true;}}
                case "5" -> {if(Client_Game.getCurrentScene().equals(GameStates.DRAW)){Client_IO.DrawCard(5); InputResolved=true;}}

                //Goal cards
                case "8" -> {Client_IO.ChooseGoalCard(3); Client_Game.ChangeScene(GameStates.PLACE_STARTING);  InputResolved=true;}
                case "9" -> {Client_IO.ChooseGoalCard(5); Client_Game.ChangeScene(GameStates.PLACE_STARTING);  InputResolved=true;}

                //select
                case "q" -> {ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[0]; SelectedSpace = 0;}
                case "w" -> {ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[1]; SelectedSpace = 1;}
                case "e" -> {ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[2]; SelectedSpace = 2;}
                case "r" -> {ClientConstants.SelectedCard = Client_IO.requestPlayerHand()[4]; SelectedSpace = 4;}

                //misc
                case "u" -> {Client_IO.requestUpdate(); InputResolved=true;}
                case "s" -> {
                    if(Client_Game.getCurrentScene().equals(GameStates.PLACE_STARTING)){
                    Client_IO.PlaceStartingCard(Client_IO.requestPlayerHand()[4]); Client_Game.ChangeScene(GameStates.PLAY);  InputResolved=true;}
                }
                case "f" -> {if(SelectedSpace!=-1){Client_IO.FlipCard_inPos(SelectedSpace);  InputResolved=true;}}

                case "n" -> {TUI.AdjustScale(1); InputResolved=true;}
                case "m" -> {TUI.AdjustScale(-1); InputResolved=true;}

                case "p" -> {
                    if(Client_Game.getCurrentScene().equals(GameStates.PLAY)){
                    if (SelectedSpace != 0 && SelectedSpace != 1 && SelectedSpace != 2) {
                        System.out.println("Please select a card first");
                        continue;
                    }

                    System.out.println("Write down the number of the space you would like to place your card into");
                    String SelectedGridSpace = scanner.nextLine();

                    if(SelectedGridSpace.length()!=4){ System.out.println("Space not valid, wrong numbers of char"); continue; }

                    int formattedIndex = Integer.parseInt(SelectedGridSpace);

                    int SelectedRow = formattedIndex/100;
                    int SelectedCol = formattedIndex%100;

                    if(!TUI.getPlayableIDS().contains(formattedIndex)){ System.out.println("Space not valid"); continue;}

                    System.out.println("PlayCard "+ ClientConstants.SelectedCard + " in: " + SelectedRow + "," + SelectedCol);

                    if(!Client_IO.playCardByIndex(SelectedRow, SelectedCol, ClientConstants.SelectedCard))
                    { System.out.println("This card can't be played in this space"); continue; }

                    TUI.removeSpace(formattedIndex);
                    ClientConstants.SelectedCard=0;
                    SelectedSpace=-1;
                    Client_Game.ChangeScene(GameStates.DRAW);
                    InputResolved=true;

                }}

                default -> System.out.println("Command not recognized");

            }

        }

        Client_IO.requestUpdate();
    }
}
