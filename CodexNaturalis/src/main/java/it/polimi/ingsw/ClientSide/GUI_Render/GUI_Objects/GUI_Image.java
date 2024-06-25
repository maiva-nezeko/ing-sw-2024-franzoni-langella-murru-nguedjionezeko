package it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;


import java.awt.*;

/**
 * The GUI_Image that extends the GUI object. It's used for the ScoreBoard display on screen.
 */
public class GUI_Image extends GUI_object{

    private final Image referenceImg;

    /**
     *
     * @param _xSize length of the image int
     * @param _ySize width of the image int
     * @param _xCoord horizontal coordinate of the image int
     * @param _yCoord vertical coordinate of the image int
     * @param referenceImg the referenced image
     */
    public GUI_Image(int _xSize, int _ySize, int _xCoord, int _yCoord, Image referenceImg)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);

        this.referenceImg = referenceImg;
    }

    /**
     * Gets the reference of the Image.
     *
     * @return the reference
     * @deprecated - from a previous version, no longer necessary
     */
    public Image getImage(){return this.referenceImg;}

    /**
     * Renders the object.
     * @param g the graphics where render the object
     */
    @Override
    public void renderObject(Graphics g) {
        if(this.referenceImg!=null){ g.drawImage(referenceImg.getScaledInstance(this.xSize, this.ySize, Image.SCALE_DEFAULT), this.xCoord, this.yCoord, null); }
        else {g.drawRect(this.xCoord, this.yCoord, this.xSize, this.ySize);}
    }
}
