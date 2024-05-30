package it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import java.awt.*;

/**
 * The abstract GUI_object, the blueprint for both the GUI Card and the GUI Image(s).
 */
public abstract class GUI_object {

    protected final int xSize;
    protected final int ySize;
    protected final int xCoord;
    protected final int yCoord;


    /**
     * Sets the size of the GUI object from the parameters.
     *
     * @param _xSize the length int
     * @param _ySize the height int
     * @param _xCoord the horizontal coordinates
     * @param _yCoord the vertical coordinates
     */
    public GUI_object(int _xSize, int _ySize, int _xCoord, int _yCoord)
    {
        this.xSize = _xSize;
        this.ySize = _ySize;
        this.xCoord = _xCoord;
        this.yCoord = _yCoord;
    }

    /**
     * Get size of the object.
     *
     * @return the size as an int []
     */
    public int[] getSizes() {return new int[]{xSize, ySize};}

    /**
     * Get coordinates of the object as an array: position 0 is x, position 1 is y.
     *
     * @return the coordinates as an int[]
     */
    public int[] getCoords() {return new int[]{xCoord, yCoord};}

    /**
     * Renders Object in graphics.
     *
     * @param g the graphics where to render the object
     */
    public abstract void renderObject(Graphics g);
}
