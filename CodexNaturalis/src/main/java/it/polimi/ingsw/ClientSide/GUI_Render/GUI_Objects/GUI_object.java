package main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import java.awt.*;

public abstract class GUI_object {

    protected int xSize, ySize;
    protected int xCoord, yCoord;


    public GUI_object(int _xSize, int _ySize, int _xCoord, int _yCoord)
    {
        this.xSize = _xSize;
        this.ySize = _ySize;
        this.xCoord = _xCoord;
        this.yCoord = _yCoord;
    }
    public int[] getSizes() {return new int[]{xSize, ySize};}
    public int[] getCoords() {return new int[]{xCoord, yCoord};}

    public abstract void renderObject(Graphics g);
}
