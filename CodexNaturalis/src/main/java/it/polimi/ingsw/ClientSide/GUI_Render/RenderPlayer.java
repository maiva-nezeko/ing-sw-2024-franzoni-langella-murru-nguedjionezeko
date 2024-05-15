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
 */
public class RenderPlayer {

    private static ArrayList<Integer> VisitedID = new ArrayList<>();
    private static void FlushVisitedID(){ VisitedID = new ArrayList<>(); }


    /**
     * Renders the Points count each and every Player.
     *
     * @param gamePanel the full Game panel
     * @param g the graphics of the game
     */
    public static void render(GamePanel gamePanel, Graphics g)
    {
        GameStates scene = Client_Game.getCurrentScene();
        int playerCount = Client_IO.requestCurrentPlayerCount();

        FULL_GUI.renderGUI(g, scene);


        int[][] Grid = Client_IO.requestGrid();
        if(scene.equals(GameStates.SPECTATE_PLAYER)){ Grid = Client_IO.getCurrentPlayerGrid();  }


        if(scene.equals(GameStates.PLAY) || scene.equals(GameStates.PLACE_STARTING) || scene.equals(GameStates.SPECTATE_PLAYER)){

            if(Grid!=null){ paintPlayerGrid(g, Grid, Grid.length/2, Grid[0].length/2); FlushVisitedID();}
        }


        if(scene.equals(GameStates.PLAY) || scene.equals(GameStates.DRAW) || scene.equals(GameStates.SPECTATE_PLAYER)){

            if(playerCount == 1){
                paintPlayerToken(g, 0, scene);}
            if(playerCount==2){
                paintPlayerToken(g, 0, scene); paintPlayerToken(g, 1, scene);}
            if(playerCount==3){
                paintPlayerToken(g,0, scene); paintPlayerToken(g, 1, scene); paintPlayerToken(g, 2, scene);}
            if(playerCount==4){
                paintPlayerToken(g, 0, scene); paintPlayerToken(g,1, scene);
                paintPlayerToken(g,2, scene);
                paintPlayerToken(g, 3, scene);}}

    }

    /**
     * Paints the scene of the player who wants to place a card, rendering the Corners where the new Card can
     * be placed, the scoreboard and the color of his pawn.
     *
     * @param g the graphics of the game
     * @param player the player index int
     * @param scene the current scene as a string
     */
    public static void paintPlayerToken(Graphics g, int player, GameStates scene)
    {
        Color[] Colors= { Color.black, Color.red, Color.blue, Color.yellow};
        int score = Client_IO.requestPlayerScore()[player];


        int xPos, yPos;

        if(scene.equals(GameStates.PLAY) || scene.equals(GameStates.SPECTATE_PLAYER)){xPos = xWindowSize - 2*xWindowSize/6; yPos = 0;}
        else {xPos = xWindowSize/2 - xWindowSize/6; yPos = yWindowSize/2 - xWindowSize/6;}

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

        if(score>=30){score=29;}
        g.setColor(Colors[player]);
        g.fillOval((int) PlayerXPositions[score], (int) PlayerYPositions[score], 10, 10);
        g.setColor(Color.gray);
    }




    //PlaySceneGrid
    private static final int NumOf_Rows = Client_IO.requestGridSizes()[0];
    private static final int NumOf_Columns = Client_IO.requestGridSizes()[1];

    private static final int Width = ClientConstants.getxWindowSize() - xWindowSize/3;
    private static final int Height = ClientConstants.getyWindowSize();

    private static int Card_Width = ((xWindowSize/6) /2) - ((xWindowSize/6) /20);
    private static int Card_Height = Card_Width *10/15;

    private static final GUI_Card[][] Spaces_Coords = new GUI_Card[NumOf_Rows][NumOf_Columns];
    public static GUI_object[][] getSpaces_Coords(){return Spaces_Coords;}

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
     * Option to reduce the size of the PlayBoard as to display a bigger amount of Cards.
     */
    public static void ScaleDownGrid()
    {
        if(ScaleLevel == 4){return;}

        ScaleLevel++;

        Card_Width = Card_Width /ScaleLevel;
        Card_Height = Card_Height /ScaleLevel ;

        fillEmpty_Grid();
    }


    /**
     * Paints the PlayBoard or single Player's private Table where to play/position the Cards.
     * @param g the graphics of the game
     * @param OccupiedSpaces the Played Cards matrix
     * @param Row_pos horizontal position int
     * @param Col_pos vertical position int
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
