package it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Constructs and renders a banner in the top part of the screen to help
 * the player navigate his turn.
 */
public class GUI_HelperBanner extends GUI_object{

    /**
     * List of different images with text matching the GameStates, in an array:
     * "Draw", "Play", "Choose Goal", "Place Starting Card".
     * An image for the winner and one for the loser once the Game is over is also
     * included.
     */
    private final ArrayList<Image> referenceImages = new ArrayList<>();

    /**
     * GUI banner images coordinates and size.
     *
     * @param _xSize length of the image
     * @param _ySize width of the image
     * @param _xCoord horizontal coordinate
     * @param _yCoord vertical coordinate
     */
    public GUI_HelperBanner(int _xSize, int _ySize, int _xCoord, int _yCoord)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);

        File path = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\MenuIcons\\AllBanners.png");

        BufferedImage rI = null;
        int ySourceSize = 2160;

        try {
            rI = ImageIO.read(new File(String.valueOf(path)));
        } catch (java.io.IOException e) {
            System.out.println("error");
        }


        assert rI != null;

        for( int yCoord=0; yCoord < ySourceSize; yCoord+=ySourceSize/10 )
        {
            this.referenceImages.add(rI.getSubimage(0, yCoord, 1280, ySourceSize/10));
        }

    }

    /**
     * Renders all specific objects to Draw, PLay, etc. in the right order according to the GameState,
     * the phase of the turn in which the player is.
     *
     * @param g the object that render the graphics of the game
     */
    @Override
    public void renderObject(Graphics g) {

        Image reference = null;

        switch (Client_Game.getCurrentScene())
        {
            case MAIN_MENU -> reference = referenceImages.get(0);
            case DRAW -> reference = referenceImages.get(1);
            case PLAY -> reference = referenceImages.get(2);

            case PLAYER_SELECTION -> reference = referenceImages.get(4);

            case CHOOSE_GOAL -> reference = referenceImages.get(5);
            case PLACE_STARTING -> reference = referenceImages.get(6);
            case YOU_WIN -> reference = referenceImages.get(7);
            case YOU_LOSE -> reference = referenceImages.get(8);

            case SPECTATE_PLAYER -> reference = referenceImages.get(9);
        }

        if(reference!=null){g.drawImage(reference.getScaledInstance(this.xSize, this.ySize, Image.SCALE_DEFAULT), this.xCoord, this.yCoord, null); }
    }


}
