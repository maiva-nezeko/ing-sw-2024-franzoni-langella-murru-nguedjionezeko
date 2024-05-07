package main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;


import java.awt.*;

public class GUI_Image extends GUI_object{

    private final Image referenceImg;

    public GUI_Image(int _xSize, int _ySize, int _xCoord, int _yCoord, Image referenceImg)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);

        this.referenceImg = referenceImg;
    }

    public Image getImage(){return this.referenceImg;}

    @Override
    public void renderObject(Graphics g) {
        if(this.referenceImg!=null){ g.drawImage(referenceImg.getScaledInstance(this.xSize, this.ySize, Image.SCALE_DEFAULT), this.xCoord, this.yCoord, null); }
        else {g.drawRect(this.xCoord, this.yCoord, this.xSize, this.ySize);}
    }
}
