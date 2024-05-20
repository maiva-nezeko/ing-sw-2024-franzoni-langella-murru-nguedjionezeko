package main.java.it.polimi.ingsw.ClientSide.Utility;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_Card;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;

/**
 * The Helper methods for Game basic settings and operations such as "Select Card",
 * or getting the exact position for a Card placement.
 */
public class HelperMethods {


    private static final int Rows = Client_IO.requestGridSizes()[0];
    private static final int Columns = Client_IO.requestGridSizes()[1];

    private static int Scale = 0;
    private static final int[][] ScalingThresholds = new int[][]{{6,5}, {12,10}};


    /**
     * Select card.
     *
     * @param Border the border as a GUI_object
     * @return the id
     */
    public static int SelectCard(GUI_object Border)
    {
        int result = 0;
        if(Border instanceof GUI_Card)
        {
            if(((GUI_Card) Border).getCard()!=null){
                result = ((GUI_Card) Border).getCard().getId();
                if(((GUI_Card) Border).isFlipped()){result = -result;}}

        }

        return result;
    }


    /**
     * The Boolean to check if boundaries and coordinates are respected.
     *
     * @param xPos   the horizontal position
     * @param yPos   the vertical position
     * @param Border the border
     * @return the boolean
     */
    public static boolean is_Inside(int xPos, int yPos, GUI_object Border){

        int[] Coords = Border.getCoords();
        int[] Size = Border.getSizes();

        int[] Boundaries = {Coords[0], Coords[1],Coords[0] + Size[0], Coords[1] + Size[1]};
        boolean xCheck=false, yCheck=false;

        if(xPos> Boundaries[0] && xPos<Boundaries[2]){ xCheck = true;}
        if(yPos> Boundaries[1] && yPos<Boundaries[3]){ yCheck = true;}

        return (xCheck && yCheck);

    }

    /**
     * Get grid position as an array.
     *
     * @param xPos the x horizontal position
     * @param yPos the y vertical position
     * @return the int [ ]
     */
    public static int[] GetGridPosition(int xPos, int yPos)
    {
        GUI_object[][] SpaceCoords = RenderPlayer.getSpaces_Coords();
        int[] Indexes = {0,0};

        for(int Row_index = 0; Row_index < Rows; Row_index++) {
            for (int Columns_index = 0; Columns_index < Columns; Columns_index++) {
                if (is_Inside(xPos, yPos, SpaceCoords[Row_index][Columns_index])) {
                    Indexes[0] = Row_index; Indexes[1] = Columns_index;
                    return Indexes;
                }
            }
        }

        return null;
    }

    /**
     * Format a string of infos into an array of ints.
     *
     * @param Array the string
     * @return the int [ ] array
     */
    public static int[] FormattedStringToArray( String Array )
    {
        String[] SplitArray = Array.replace("[", "").replace("]", "").replace(" ", "").split(",");
        int[] result = new int[SplitArray.length];

        for(int index=0; index<SplitArray.length; index++){ result[index] = Integer.parseInt(SplitArray[index]); }

        return result;
    }

    /**
     * Formatted string to matrix int [ ] [ ].
     *
     * @param Array the array
     * @return the int [ ] [ ]
     */
    public static int[][] FormattedStringToMatrix( String Array )
    {
        if(Array.length()<=1){return null;}

        String[] SplitArray = Array.replace("[", "").replace("]", "").replace(" ", "")
                .replace(";", "").split(":");
        int Columns = SplitArray.length/2;

        int[][] result = new int[SplitArray.length][Columns];

        for(int index=0; index<SplitArray.length; index++){ result[index] = FormattedStringToArray(SplitArray[index]); }

        return result;
    }



}
