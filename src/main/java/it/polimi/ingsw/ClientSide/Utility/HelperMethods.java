package it.polimi.ingsw.ClientSide.Utility;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_Card;
import it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.GUI_object;
import it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;

/**
 * The Helper methods class, containing definitions of Rows, Columns, SpaceCoords,
 * SelectCard, among others.
 */
public class HelperMethods {


    private static final GUI_object[][] SpaceCoords = RenderPlayer.getSpaces_Coords();
    private static final int Rows = Client_IO.requestGridSizes()[0];
    private static final int Columns = Client_IO.requestGridSizes()[1];


    /**
     * Select card as an int.
     *
     * @param Border the border of the Card object
     * @return the int
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
     * Is inside boolean to check whether an object is in an area.
     *
     * @param xPos   the x position
     * @param yPos   the y position
     * @param Border the border for display
     * @return the boolean response
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
     * Get grid position int [ ].............................WHAT
     *
     * @param xPos the x position
     * @param yPos the y position
     * @return the int [ ]
     */
    public static int[] GetGridPosition(int xPos, int yPos)
    {
        int[] Indexes = {0,0};

        for(int Row_index = 0; Row_index < Rows; Row_index++) {
            for (int Columns_index = 0; Columns_index < Columns; Columns_index++) {
                if (is_Inside(xPos, yPos, SpaceCoords[Row_index][Columns_index])) {
                    Indexes[0] = Row_index; Indexes[1] = Columns_index; return Indexes;
                }
            }
        }

        return null;
    }

    /**
     * Formatted string to array int [ ].
     *
     * @param Array the string we want to transform
     * @return the int [ ]
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
     * @param Array the array that we want to transform
     * @return the matrix as an int [ ] [ ]
     */
    public static int[][] FormattedStringToMatrix( String Array )
    {
        String[] SplitArray = Array.replace("[", "").replace("]", "").replace(" ", "").split(":");
        int Columns = SplitArray.length/2;

        int[][] result = new int[SplitArray.length][Columns];

        for(int index=0; index<SplitArray.length; index++){ result[index] = FormattedStringToArray(SplitArray[index]); }

        return result;
    }



}
