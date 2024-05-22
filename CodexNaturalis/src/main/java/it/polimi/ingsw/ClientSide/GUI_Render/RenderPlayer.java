package main.java.it.polimi.ingsw.ClientSide.GUI_Render;

import main.java.it.polimi.ingsw.ClientSide.Cards.Deck;
import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_Card;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.awt.*;
import java.util.ArrayList;

import static main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants.xWindowSize;
import static main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants.yWindowSize;

/**
 * Renders player's view, details the secondary scene, where the previously placed Cards can be seen.
 * @author Edoardo Carlo Murru
 */
public class RenderPlayer {

    /**
     * Keeps track of the Card that has been placed on the PlayBoard, while the Card
     * is about to be rendered on the grid and erased from the PLayer's Hand.
     */
    private static ArrayList<Integer> VisitedID = new ArrayList<>();

    /**
     * Substitutes old VisitedID with a new empty one, to prepare for next turn.
     */
    private static void FlushVisitedID(){ VisitedID = new ArrayList<>(); }


    /**
     * Renders the Points count for each and every Player.
     *
     * @param ignoredGamePanel the full Game panel
     * @param g the graphics of the game
     */
    public static void render(GamePanel ignoredGamePanel, Graphics g)
    {
        GameStates scene = Client_Game.getCurrentScene();
        //int playerCount = Client_IO.requestCurrentPlayerCount();

        FULL_GUI.renderGUI(g, scene);


        int[][] Grid = Client_IO.requestGrid();
        if(scene.equals(GameStates.SPECTATE_PLAYER)){ Grid = Client_IO.getCurrentPlayerGrid();  }


        if(scene.equals(GameStates.PLAY) || scene.equals(GameStates.PLACE_STARTING) || scene.equals(GameStates.SPECTATE_PLAYER)){

            if(Grid!=null){
                fillEmpty_Grid();
                paintPlayerGrid(g, Grid, Grid.length/2, Grid[0].length/2);
                FlushVisitedID();}
        }


        if(scene.equals(GameStates.PLAY) || scene.equals(GameStates.DRAW) || scene.equals(GameStates.SPECTATE_PLAYER)){ paintPlayerToken(g, scene);}


    }

    /**
     * Paints the scene of the player who wants to place a card, rendering the Corners where the new Card can
     * be placed, the scoreboard and the color of his pawn.
     *
     * @param g the graphics of the game
     * @param scene the current scene as a string
     */
    public static void paintPlayerToken(Graphics g, GameStates scene)
    {
        boolean playOrSpectate = scene.equals(GameStates.PLAY) || scene.equals(GameStates.SPECTATE_PLAYER);

        Color[] Colors= { Color.black, Color.red, Color.blue, Color.yellow};
        String[] Usernames = Client_IO.getGame_usernames();
        int[] scores = Client_IO.requestPlayerScore();

        int xPos, yPos;

        if(playOrSpectate){ xPos = xWindowSize - 2*xWindowSize/6; yPos = 0; }
        else { xPos = xWindowSize/2 - xWindowSize/6; yPos = yWindowSize/2 - xWindowSize/6; }

        final double BoardHeight = ((double) xWindowSize /3);
        final double BoardWidth = ((double) xWindowSize /6);

        final double Zero_yPos = yPos + BoardHeight - BoardHeight/10.5;
        final double Zero_xPos = xPos + BoardWidth/4.4;
        final double Six_xPos = xPos + BoardWidth/9.0;

        final double lateralDistance = BoardWidth/4.3;
        final double verticalDistance = BoardHeight/9.5;

        final double[] PlayerXPositions = { Zero_xPos, Zero_xPos+lateralDistance, Zero_xPos+2*lateralDistance,
                Six_xPos+3*lateralDistance, Six_xPos+2*lateralDistance, Six_xPos+lateralDistance,  Six_xPos,
                Six_xPos, Six_xPos+lateralDistance, Six_xPos+2*lateralDistance, Six_xPos+3*lateralDistance,
                Six_xPos+3*lateralDistance, Six_xPos+2*lateralDistance, Six_xPos+lateralDistance,  Six_xPos,
                Six_xPos, Six_xPos+lateralDistance, Six_xPos+2*lateralDistance, Six_xPos+3*lateralDistance,
                /*19*/Six_xPos+3*lateralDistance,/*20*/xPos+BoardWidth/2.0,/*21*/Six_xPos,/*22*/Six_xPos,
                /*23*/Six_xPos,/*24*/Six_xPos+lateralDistance/2,/*25*/xPos+BoardWidth/2.0,/*26*/Six_xPos+3*lateralDistance-lateralDistance/2,
                /*27*/Six_xPos+3*lateralDistance,/*28*/Six_xPos+3*lateralDistance,/*29*/xPos+BoardWidth/2.0

        };
        final double[] PlayerYPositions = {Zero_yPos, Zero_yPos, Zero_yPos,
                Zero_yPos-verticalDistance,Zero_yPos-verticalDistance,Zero_yPos-verticalDistance,Zero_yPos-verticalDistance,
                Zero_yPos-2*verticalDistance,Zero_yPos-2*verticalDistance,Zero_yPos-2*verticalDistance,Zero_yPos-2*verticalDistance,
                Zero_yPos-3*verticalDistance,Zero_yPos-3*verticalDistance,Zero_yPos-3*verticalDistance,Zero_yPos-3*verticalDistance,
                Zero_yPos-4*verticalDistance,Zero_yPos-4*verticalDistance,Zero_yPos-4*verticalDistance,Zero_yPos-4*verticalDistance,
                /*19*/Zero_yPos-5*verticalDistance,/*20*/Zero_yPos-5.5*verticalDistance,/*21*/Zero_yPos-5*verticalDistance,/*22*/Zero_yPos-6*verticalDistance,
                /*23*/Zero_yPos-7*verticalDistance,/*24*/Zero_yPos-8*verticalDistance,/*25*/Zero_yPos-8*verticalDistance,/*26*/Zero_yPos-8*verticalDistance,
                /*27*/Zero_yPos-7*verticalDistance,/*28*/Zero_yPos-6*verticalDistance,/*29*/Zero_yPos-6.5*verticalDistance

        };

        for(int playerIndex=0; playerIndex<Client_IO.requestCurrentPlayerCount(); playerIndex++)
        {
            int maxPrintableScore = scores[playerIndex];
            if(maxPrintableScore>=30){maxPrintableScore=29;}
            g.setColor(Colors[playerIndex]);

            g.fillOval((int) PlayerXPositions[maxPrintableScore], (int) PlayerYPositions[maxPrintableScore], 10, 10);

            if( playOrSpectate
                    && Usernames[playerIndex].equals(Client_IO.requestCurrentPlayerName()))
            {
                g.fillOval(Width/2, Height/2, 10, 10);
            }

            g.setColor(Color.gray);


        }

    }


    //PlaySceneGrid
    private static final int NumOf_Rows = Client_IO.requestGridSizes()[0];
    private static final int NumOf_Columns = Client_IO.requestGridSizes()[1];

    private static final int Width = ClientConstants.getxWindowSize() - xWindowSize/3;
    private static final int Height = ClientConstants.getyWindowSize();

    private static final int originalCardWidth = ((xWindowSize/6) /2) - ((xWindowSize/6) /20);
    private static final int originalCardHeight = originalCardWidth *10/15;
    private static int Card_Width = originalCardWidth;
    private static int Card_Height = originalCardHeight;

    private static final GUI_Card[][] Spaces_Coords = new GUI_Card[NumOf_Rows][NumOf_Columns];
    public static GUI_object[][] getSpaces_Coords(){return Spaces_Coords;}

    /**
     * Starting scale level.
     */
    private static int ScaleLevel=1;

    /**
     *  Fill the empty space of the grid/PlayBoard with new GUI Card placed.
     */
    public static void fillEmpty_Grid()
    {
        int xCenter = Width/2 - Card_Width/2;
        int yCenter = Height/2 - Card_Height/2;

        for(int Row_index = 0; Row_index < NumOf_Rows; Row_index++){
            for(int Columns_index = 0; Columns_index < NumOf_Columns; Columns_index++)
            {
                Spaces_Coords[Row_index][Columns_index]
                        = new GUI_Card(Card_Width, Card_Height,
                        (xCenter -(NumOf_Columns/2 - Columns_index)*(Card_Width-Card_Width/4)),
                        (yCenter - (NumOf_Rows/2 - Row_index)*(Card_Height-Card_Height/4)), null );

            }
        }

    }

    /**
     * Option to reduce the size of the PlayBoard as to display a larger amount of Cards
     * simultaneously on Player's window screen.
     */
    public static void ScaleGrid(boolean up)
    {
        if(up){
            if(ScaleLevel >= 4){return;}
            ScaleLevel++;
        }
        else
        {
            if(ScaleLevel <= 1){return;}
            ScaleLevel--;
        }

        Card_Width = originalCardWidth /ScaleLevel;
        Card_Height = originalCardHeight /ScaleLevel ;

        fillEmpty_Grid();
    }



    /**
     * Paints the PlayBoard or single Player's private Table where to play/position the Cards.
     * @param g the graphics of the game
     * @param OccupiedSpaces the Played Cards matrix
     * @param Row_pos the int for horizontal position
     * @param Col_pos the int for vertical position
     */
    private static void paintPlayerGrid(Graphics g, int[][] OccupiedSpaces,int Row_pos, int Col_pos)
    {
        int ID = OccupiedSpaces[Row_pos][Col_pos];

        if(ID==0){Spaces_Coords[Row_pos][Col_pos].renderObject(g); return;}
        if(VisitedID.contains(ID)){return;}

        VisitedID.add(ID);

        Spaces_Coords[Row_pos][Col_pos].setCard(Deck.getCardBYid(ID), ID<0);
        Spaces_Coords[Row_pos][Col_pos].renderObject(g);

        if(Row_pos-1>=0){
            if(Col_pos-1>=0){ paintPlayerGrid(g, OccupiedSpaces, Row_pos-1, Col_pos-1); }
            if(Col_pos+1<OccupiedSpaces[0].length){ paintPlayerGrid(g, OccupiedSpaces, Row_pos-1, Col_pos+1); }
        }
        if(Row_pos+1<OccupiedSpaces.length){
            if(Col_pos-1>=0){paintPlayerGrid(g, OccupiedSpaces, Row_pos+1, Col_pos-1);}
            if(Col_pos+1<OccupiedSpaces[0].length){paintPlayerGrid(g, OccupiedSpaces, Row_pos+1, Col_pos+1);}
        }

    }
}
