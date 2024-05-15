package main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GUI_HelperBanner extends GUI_object{

    private final ArrayList<Image> referenceImages = new ArrayList<>();

    /**
     * GUI image
     * @param _xSize length of the image int
     * @param _ySize width of the image int
     * @param _xCoord horizontal coordinate of the image int
     * @param _yCoord vertical coordinate of the image int
     */
    public GUI_HelperBanner(int _xSize, int _ySize, int _xCoord, int _yCoord)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);

        File path = new File(System.getProperty("user.dir")+"\\CodexNaturalis\\res\\MenuIcons\\InfoBanner.png");

        BufferedImage rI = null;
        int ySourceSize = 720;

        try {
            rI = ImageIO.read(new File(String.valueOf(path)));
        } catch (java.io.IOException e) {
            System.out.println("error");
        }


        assert rI != null;

        for( int yCoord=0; yCoord < ySourceSize; yCoord+=ySourceSize/10 )
        {
            this.referenceImages.add(rI.getSubimage(0, yCoord, 360, ySourceSize/10));
        }

    }

    /**
     * Render object
     * @param g the object that render the graphics of the game
     */
    @Override
    public void renderObject(Graphics g) {

        Image reference = null;

        switch (Client_Game.getCurrentScene())
        {
            case DRAW -> reference = referenceImages.get(0);
            case PLAY -> reference = referenceImages.get(1);
            case CHOOSE_GOAL -> reference = referenceImages.get(2);
            case PLACE_STARTING -> reference = referenceImages.get(3);
            case YOU_WIN -> reference = referenceImages.get(4);
            case YOU_LOSE -> reference = referenceImages.get(5);
        }

        if(reference!=null){g.drawImage(reference.getScaledInstance(this.xSize, this.ySize, Image.SCALE_DEFAULT), this.xCoord, this.yCoord, null); }
    }


}
