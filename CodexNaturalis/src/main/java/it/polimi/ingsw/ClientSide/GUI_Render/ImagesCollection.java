package it.polimi.ingsw.ClientSide.GUI_Render;

import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static it.polimi.ingsw.ClientSide.Utility.ClientConstants.xWindowSize;
/**
 * Collects all images for GUI rendering excluding Cards and Helper messages;
 * these images include the main menu, the secondary menu with the options for the
 * players number, the PointBoard.
 * @author Edoardo Murru, Maiva Nezeko.
 */
public class ImagesCollection {

    private static final Image PointBoardImage = getBoardIMG();
    public static Image getPointBoardImage() { return PointBoardImage; }



    private static final Image[] Menu_GUI_Images = LoadMenuImages();

    /**
     * Gets all images to render the initial menu.
     *
     * @return the images as an array
     */
    public static Image[] getMenu_GUI_Images() { return Menu_GUI_Images; }

    /**
     * Gets the image of the PointBoard.
     *
     * @return  a scaled version of this image
     */
    private static Image getBoardIMG(){

        Image rI = null;

        try {rI = ImageIO.read(new File(ClientConstants.getMainDirPAth() + "/res/Board.png")); }
        catch (java.io.IOException e) {System.out.println("error");}

        assert rI != null;
        return rI.getScaledInstance(xWindowSize/6,xWindowSize/3,Image.SCALE_DEFAULT);
    }


    /**
     * Loads the Images in the rightful place on the menu: "New Game", "New Local Game", "Options",
     * "Quit Game"; "2 Players", "3 Players", "4 Players".
     *
     * @return the images as an array
     */
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
