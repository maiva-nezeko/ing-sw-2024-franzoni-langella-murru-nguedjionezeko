package it.polimi.ingsw.ClientSide.GUI_Render;

import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static it.polimi.ingsw.ClientSide.Utility.ClientConstants.*;

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

    private static final Image[] GUIBackgrounds = LoadBackgrounds();
    public static Image[] getGUIBackgrounds() { return GUIBackgrounds; }

    /**
     * Gets the image of the PointBoard.
     *
     * @return  a scaled version of this image
     */
    private static Image getBoardIMG(){

        Image rI = null;

        rI = loadImage("/src/main/resources/Board.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/Board.png");}
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

        rI = loadImage("/src/main/resources/MenuIcons/buttons.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/buttons.png");}

        assert rI != null;


        Menu_GUI_Images[0] = rI.getSubimage(0, 0, 960, 216); //NewGame
        Menu_GUI_Images[1] = rI.getSubimage(0, 216, 960, 216); //NewLocalGame
        Menu_GUI_Images[2] = rI.getSubimage(0, 432, 384, 216); //Options
        Menu_GUI_Images[3] = rI.getSubimage(384, 432, 384, 216); //Quit

        rI = loadImage("/src/main/resources/MenuIcons/players.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/players.png");}

        assert rI != null;


        Menu_GUI_Images[4] = rI.getSubimage(0, 0, 384, 216);
        Menu_GUI_Images[5] = rI.getSubimage(0, 216, 384, 216);
        Menu_GUI_Images[6] = rI.getSubimage(0, 432, 384, 216);

        return Menu_GUI_Images;
    }

    private static Image[] LoadBackgrounds() {

        BufferedImage rI = null;
        Image[] GUI_Backgrounds = new Image[7];

        rI = loadImage("/src/main/resources/MenuIcons/MainMenu.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/MainMenu.png");}
        assert rI != null;

        GUI_Backgrounds[0] = rI.getScaledInstance(xWindowSize,yWindowSize,Image.SCALE_DEFAULT);

        rI = loadImage("/src/main/resources/MenuIcons/Backgrounds/DeckBackground.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/Backgrounds/DeckBackground.png");}
        assert rI != null;

        GUI_Backgrounds[1] = rI.getScaledInstance(640,1920,Image.SCALE_DEFAULT);

        rI = loadImage("/src/main/resources/MenuIcons/Backgrounds/HandBackgroundDraw.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/Backgrounds/HandBackgroundDraw.png");}
        assert rI != null;

        GUI_Backgrounds[2] = rI.getScaledInstance(1280,880,Image.SCALE_DEFAULT);

        rI = loadImage("/src/main/resources/MenuIcons/Backgrounds/HandBackgroundPlay.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/Backgrounds/HandBackgroundPlay.png");}
        assert rI != null;

        GUI_Backgrounds[3] = rI.getScaledInstance(1920,640,Image.SCALE_DEFAULT);

        rI = loadImage("/src/main/resources/MenuIcons/Backgrounds/HandBackground.png");
        if(rI == null){ rI = loadImage("/CodexNaturalis/src/main/resources/MenuIcons/Backgrounds/HandBackground.png");}
        assert rI != null;

        GUI_Backgrounds[4] = rI.getScaledInstance(2560,2160,Image.SCALE_DEFAULT);

        return GUI_Backgrounds;
    }

    public static BufferedImage loadImage(String RelativePath)
    {
        BufferedImage rI = null;

        try {
            rI = ImageIO.read(new File(ClientConstants.getMainDirPAth() + RelativePath));
        } catch (java.io.IOException e) {
            System.out.println("fetching correct path");
        }

        return rI;
    }

}
