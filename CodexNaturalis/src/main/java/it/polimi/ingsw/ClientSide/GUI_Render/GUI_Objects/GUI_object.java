package main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import java.awt.*;

/**
 * the type GUI_object
 */
public abstract class GUI_object {

    protected int xSize, ySize;
    protected int xCoord, yCoord;


    /**
     * GUI object
     * @param _xSize length of the image int
     * @param _ySize width of the image int
     * @param _xCoord horizontal coordinate of the image int
     * @param _yCoord vertical coordinate of the image int
     */
    public GUI_object(int _xSize, int _ySize, int _xCoord, int _yCoord)
    {
        this.xSize = _xSize;
        this.ySize = _ySize;
        this.xCoord = _xCoord;
        this.yCoord = _yCoord;
    }

    /**
     * Get size of the object int[]
     *
     * @return the int []
     */
    public int[] getSizes() {return new int[]{xSize, ySize};}

    /**
     * Get coordinate of the object int []
     *
     * @return the int[]
     */
    public int[] getCoords() {return new int[]{xCoord, yCoord};}

    /**
     * Render Object
     * @param g graphics, the object that render the graphics of the game
     */
    public abstract void renderObject(Graphics g);
}
