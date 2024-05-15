package main.java.it.polimi.ingsw.ClientSide.TUI_Render;

import main.java.it.polimi.ingsw.ClientSide.Cards.ClientCard;
import main.java.it.polimi.ingsw.ClientSide.Cards.Deck;
import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.Controller.TUI_Inputs;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Full implementation of the Text User Interface, an alternative to GUI for a Client to play
 * a game; some notable features include scaling, printing/rendering of a Table and a PlayBoard
 * without the use of images (just symbols) and a Guide to help the Player navigate his next steps.
 */
public class TUI {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private static final String ANSI_YELLOW = "\u001B[33m";

    private static final int[][] ScalingThresholdsCOL = {{15,25},{10,30},{5,35},{0,40},{0,40},{0,40},{0,40}};
    private static final int[][] ScalingThresholdsROW = {{35,45},{30,50},{25,55},{20,60},{15,65},{10,70},{0,79}};
    private static int TUI_Scale = 0;
    public static void AdjustScale(int value){
        TUI_Scale = TUI_Scale+value;

        if(TUI_Scale<0){TUI_Scale=0;}
        if(TUI_Scale>=ScalingThresholdsROW.length){TUI_Scale=ScalingThresholdsROW.length-1;}
    }



    private static final String[] Colors = new String[]{ANSI_RED, ANSI_BLUE, ANSI_GREEN, ANSI_PURPLE, ANSI_YELLOW, ANSI_RESET };


    /**
     * The TUI grid sizes.
     */
    static int[] TUIGridSizes = ClientConstants.getTUIGridSizes();
    /**
     * The Grid as a String matrix (to fill with Cards).
     */
    static String[][] GridString = new String[TUIGridSizes[0]][TUIGridSizes[1]];
    private static void fillGridString() {

        for (int RowIndex = 0; RowIndex < GridString.length; RowIndex ++) {
            for (int ColIndex = 0; ColIndex < GridString[0].length; ColIndex ++ ) {

                GridString[RowIndex][ColIndex] = " ";
            }
        }
        filled = true;
    }


    private static boolean filled = false;
    private static boolean isFilled(){return filled;}

    private static ArrayList<Integer> ExploredIDS = new ArrayList<>();
    private static void flushExploredIDS(){ ExploredIDS = new ArrayList<>(); }

    private static ArrayList<Integer> PlayableIDS = new ArrayList<>();

    /**
     * Get playable Cards IDs as an array list.
     *
     * @return the list
     */
    public static ArrayList<Integer> getPlayableIDS(){return PlayableIDS;}

    /**
     * Remove space.
     *
     * @param formattedIndex the formatted index
     */
    public static void removeSpace(int formattedIndex) { PlayableIDS.remove((Integer) formattedIndex); }

    /**
     * Prints all different parts of the Table, while also requesting private cards
     * associated with a given Client.
     */
    public static void renderTUI()
    {
        int[][] Grid = Client_IO.getCurrentPlayerGrid();

        if(Grid!=null){
            fillGridString();
            updateGrid(Grid, Grid.length * 7 / 2, Grid[0].length * 9 / 2, Grid.length / 2, Grid[0].length / 2);
            flushExploredIDS();

            paintGrid();
        }

        paintInfo();
        printSceneInfo();
        TUI_Inputs.waitForInput();

        if(Client_Game.getCurrentScene().equals(GameStates.SPECTATE_PLAYER))
        {
            try{
                TimeUnit.SECONDS.sleep(20);
                Client_IO.requestUpdate();
            }catch (InterruptedException e){ e.printStackTrace(); }

        }

    }

    /**
     *  Prints Table borders and prepares the space for Common and Private Cards Placement,
     *  that will be implemented by ClientCard class.
     */
    private static void paintInfo()
    {
        if(Client_Game.getCurrentScene().equals(GameStates.SPECTATE_PLAYER)){return;}

        int[] PublicSpaces = Client_IO.requestPublicCardsID();
        ClientCard[] PublicCards = {Deck.getCardBYid(PublicSpaces[0]), Deck.getCardBYid(PublicSpaces[1]), Deck.getCardBYid(PublicSpaces[2]), Deck.getCardBYid(PublicSpaces[3]),
                        Deck.getCardBYid(PublicSpaces[4]), Deck.getCardBYid(PublicSpaces[5]), Deck.getCardBYid(PublicSpaces[6]), Deck.getCardBYid(PublicSpaces[7])};

        int[] HandIndexes = Client_IO.requestPlayerHand();

        int[] Hand = new int[] {HandIndexes[0],HandIndexes[1],HandIndexes[2],HandIndexes[3],HandIndexes[4],HandIndexes[5], 0, 0};
        ClientCard[] HandCards = {Deck.getCardBYid(Hand[0]), Deck.getCardBYid(Hand[1]), Deck.getCardBYid(Hand[2]),
                Deck.getCardBYid(Hand[3]), Deck.getCardBYid(Hand[4]), Deck.getCardBYid(Hand[5]), null, null};

        int[] PlayerScores = Client_IO.requestPlayerScore();

        int CardIndex=0; int rowIndex=0;

        for(int line=0; line<=26; line++)
        {
            if(line==0 || line==26){System.out.println("+-------------------------------------------------+"); }
            else if(line==1 || line==25){System.out.println("|                                                 |"); }
            else if(line == 7 || line==13 || line==19){ CardIndex+=2; rowIndex=0; System.out.println("|                                                 |");   }

            else {
                for (int character = 0; character < 15; character++) {
                    switch (character) {
                        case 0 -> System.out.print("|");
                        case 1 -> System.out.print("  ");
                        case 2 -> printRow(HandCards[CardIndex], rowIndex, Hand[CardIndex] < 0);
                        case 3 -> System.out.print("  ");
                        case 4 -> printRow(HandCards[CardIndex + 1], rowIndex, Hand[CardIndex + 1] < 0);
                        case 5 -> System.out.print("  ");
                        case 6 -> System.out.print("|");

                        case 7 -> System.out.print("  ");
                        case 8 -> printRow(PublicCards[CardIndex], rowIndex, PublicSpaces[CardIndex] < 0);
                        case 9 -> System.out.print("  ");
                        case 10 -> printRow(PublicCards[CardIndex + 1], rowIndex, PublicSpaces[CardIndex + 1] < 0);
                        case 11 -> System.out.print("  ");
                        case 12 -> System.out.print("|");
                        case 13 -> System.out.print("  ");

                        case 14 -> {
                            printPlayerScores(line-2, PlayerScores);
                        }

                    }
                }
                rowIndex++;
            }

        }





    }

    /**
     * Prints Players followed by their scores; then prints Guide to Help the player
     * navigate KeyBoard inputs.
     *
     * @param line where to display message
     * @param Scores as an int array
     */
    private static void printPlayerScores(int line, int[] Scores)
    {
        if(line<0){System.out.println();}
        int playerCount = Client_IO.requestCurrentPlayerCount();
        String[] usernames = Client_IO.getGame_usernames();

        switch (line)
        {
            case 0 -> System.out.print("GameScores:\n");
            case 1 -> System.out.print(usernames[0] + ":"+ Scores[0]+"\n");

            case 2 -> { if(playerCount>=2){ System.out.print(usernames[1] + ":"+ Scores[1]+"\n");  }
            else {System.out.println();} }

            case 3 -> { if(playerCount>=3){ System.out.print(usernames[2] + ":"+ Scores[2]+"\n");  }
            else {System.out.println();} }

            case 4 -> { if(playerCount==4){ System.out.print(usernames[3]+ ":"+ Scores[3]+"\n");  }
            else {System.out.println();} }

            case 6 -> System.out.println("Draw Commands");
            case 7 -> System.out.println("Draw from Decks: 0, 3");
            case 8 -> System.out.println("Draw from Golden Spaces: 1, 2");
            case 9 -> System.out.println("Draw from Resource Spaces: 4, 5");
            case 10 -> System.out.println("Choose Goal Card: 8 for first one, 9 for second one");

            case 12 -> System.out.println("Select Cards");
            case 13 -> System.out.println("Select First: Q");
            case 14 -> System.out.println("Select Second: W");
            case 15 -> System.out.println("Select Third: E");
            case 16 -> System.out.println("Select Starting R");

            case 18 -> System.out.println("Flip Selected Card: F");
            case 19 -> System.out.println("Request Grid Update U");
            case 20 -> System.out.println("Place Starting Card S");
            case 21 -> System.out.println("Enter placement mode P");
            case 22 -> System.out.println("Scale gui up: N Down:M");


        }



    }

    /**
     * Renders and colors a single row to print of a given card (6 rows total in general);
     *
     * @param card the ID of the card to print
     * @param row the current row being printed
     * @param isFlipped a boolean stating if Card is flipped
     */
    private static void printRow(ClientCard card, int row, boolean isFlipped)
    {
        if(card == null){
            if(row==0 || row==5){System.out.print("         ");}
            else{System.out.print("         ");}
            return;
        }



        String color_code = Colors[card.getColor()];
        if(ClientConstants.SelectedCard == card.getId() && card.getId()!=0){ color_code = Colors[4]; }

        for(String character: card.getText(isFlipped)[row]){ System.out.print(color_code+character+ANSI_RESET); }
    }

    /**
     * Paints a single Player's PlayBoard where the Cards are placed once they are played.
     * It adjusts to a scaling that can be modified (up or down) by the Player.
     */
    private static void paintGrid() {

        for(int RowIndex = ScalingThresholdsROW[TUI_Scale][0]*7; RowIndex<ScalingThresholdsROW[TUI_Scale][1]*7; RowIndex++)
        {
            for(int ColIndex = ScalingThresholdsCOL[TUI_Scale][0]*9; ColIndex<ScalingThresholdsCOL[TUI_Scale][1]*9; ColIndex++)
            {
                System.out.print(GridString[RowIndex][ColIndex]);
            }
            System.out.print("\n");
        }
    }

    /**
     * Updates the PlayerBoard and insures a Card can be played in a certain position.
     * @param Grid the Cards already played
     * @param Row_pos the Row position
     * @param Col_pos the Column position
     * @param GridRow the Grid row
     * @param GridCol the Grid column
     */
    private static void updateGrid(int[][] Grid, int Row_pos, int Col_pos, int GridRow, int GridCol)
    {
       int ID = Grid[GridRow][GridCol];

       if(ID==0){ PaintNumber(Row_pos, Col_pos, GridRow,GridCol); return; }
       if(ExploredIDS.contains(ID)){ return;}

       ExploredIDS.add(ID);
       ClientCard card = Deck.getCardBYid(ID);


       if(Objects.equals(GridString[Row_pos][Col_pos], " ") && card!=null)
       {
           String[][] CardRows = card.getText(ID<0);
           int CardRow = 0; int CardCol = 0;

           for(int RowIndex = Row_pos-2; RowIndex<=Row_pos+2; RowIndex++){

               for(int ColIndex = Col_pos-4; ColIndex<=Col_pos+4; ColIndex++){
                   String color_code = Colors[card.getColor()];
                   GridString[RowIndex][ColIndex] = color_code+CardRows[CardRow][CardCol]+ANSI_RESET;
                   CardCol++;
               }

               CardRow++;
               CardCol = 0;
           }
       }

       if(GridRow-1>=0){  if(GridCol-1>=0){ updateGrid(Grid, Row_pos-3, Col_pos-6, GridRow-1, GridCol-1); }
                            if(GridCol+1<Grid[0].length){   updateGrid(Grid, Row_pos-3, Col_pos+6, GridRow-1, GridCol+1); }}

       if(GridRow+1<Grid.length){   if(GridCol-1>=0){ updateGrid(Grid, Row_pos+3, Col_pos-6, GridRow+1, GridCol-1); }
                                    if(GridCol+1<Grid[0].length){  updateGrid(Grid, Row_pos+3, Col_pos+6, GridRow+1, GridCol+1);}}

    }


    /**
     * Calculates the coordinates of the different corners where a Player is able to play a Card;
     * these coordinates will be used when in "placement mode".
     * First number is the row, second is the column.
     * @param RowPos the Row position
     * @param ColPos the Column position
     * @param RowIndex the Row index
     * @param ColIndex the Column index
     */
    private static void PaintNumber(int RowPos, int ColPos, int RowIndex, int ColIndex)
    {
        GridString[RowPos][ColPos-2] = "" + RowIndex / 10;
        GridString[RowPos][ColPos-1] = "" + RowIndex % 10;
        GridString[RowPos][ColPos+1] = "" + ColIndex / 10;
        GridString[RowPos][ColPos+2] = "" + ColIndex % 10;

        int formattedId =  (RowIndex / 10) * 1000 + (RowIndex % 10) * 100 + (ColIndex / 10)*10 + (ColIndex % 10);

        PlayableIDS.add(0, formattedId);

    }

    private static void printSceneInfo()
    {
        switch(Client_Game.getCurrentScene())
        {
            case PLAY -> System.out.println("Select a card, then press p to enter placement mode");
            case DRAW -> System.out.println("Draw a card");
            case PLACE_STARTING -> System.out.println("You may select your starting card to flip it or press S to play it");
            case CHOOSE_GOAL -> System.out.println("you may choose your goal card");
            case YOU_WIN -> System.out.println("You win, congratulations!");
            case YOU_LOSE -> System.out.println("You lost, better luck next time!");
        }
    }



}
