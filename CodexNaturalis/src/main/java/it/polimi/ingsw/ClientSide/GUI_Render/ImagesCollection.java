package main.java.it.polimi.ingsw.ClientSide.GUI_Render;

import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants.xWindowSize;

public class ImagesCollection {

    private static final Image PointBoardImage = getBoardIMG();
    public static Image getPointBoardImage() { return PointBoardImage; }



    private static final Image[] Menu_GUI_Images = LoadMenuImages();
    public static Image[] getMenu_GUI_Images() { return Menu_GUI_Images; }


    private static Image getBoardIMG(){

        Image rI = null;

        try {rI = ImageIO.read(new File(ClientConstants.getMainDirPAth() + "/res/Board.png")); }
        catch (java.io.IOException e) {System.out.println("error");}

        assert rI != null;
        return rI.getScaledInstance(xWindowSize/6,xWindowSize/3,Image.SCALE_DEFAULT);
    }

    private static Image[] LoadMenuImages()
    {
        BufferedImage rI = null;
        Image[] Menu_GUI_Images = new Image[7];

        try {
            rI = ImageIO.read(new File(ClientConstants.getMainDirPAth() + "/res/MenuIcons/IconeMenuProvvisorie.png"));
        } catch (java.io.IOException e) {
            System.out.println("error");
        }

        assert rI != null;
        Menu_GUI_Images[0] = rI.getSubimage(0, 0, 960, 216); //NewGame
        Menu_GUI_Images[1] = rI.getSubimage(0, 216, 960, 216); //NewLocalGame
        Menu_GUI_Images[2] = rI.getSubimage(0, 432, 384, 216); //Options
        Menu_GUI_Images[3] = rI.getSubimage(384, 432, 384, 216); //Quit

        Menu_GUI_Images[4] = rI.getSubimage(0, 648, 384, 216);
        Menu_GUI_Images[5] = rI.getSubimage(384, 648, 384, 216);
        Menu_GUI_Images[6] = rI.getSubimage(768, 648, 384, 216);

        return Menu_GUI_Images;
    }


}
