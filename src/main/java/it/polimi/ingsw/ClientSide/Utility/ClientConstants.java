package it.polimi.ingsw.ClientSide.Utility;

import java.awt.*;
import java.nio.file.FileSystems;

/**
 * The different types of Client constants used during the course of the program.
 */
public class ClientConstants {

    /**
     * The constant SelectedCard which refers to a Card chosen by the Player.
     */
    public static int SelectedCard;

    private static int Port = 1330;

    /**
     * Set port for communication.
     *
     * @param port the port used
     */
    public static void setPort(int port){Port = port;}

    /**
     * Get port for communication as an int.
     *
     * @return the int
     */
    public static int getPort(){return Port;}

    private static boolean GUI = false;

    /**
     * Gets gui.
     *
     * @return the gui
     */
    public static boolean getGUI() { return GUI;   }

    /**
     * Sets gui.
     *
     * @param value the value
     */
    public static void setGUI(boolean value) { GUI = value; }

    private static final int[] TUIGridSizes = {5*20,9*10};

    /**
     * Get tui grid sizes int [ ].
     *
     * @return the int [ ]
     */
    public static int[] getTUIGridSizes(){return TUIGridSizes;}

    private static boolean Socket = false;

    /**
     * Set socket.
     *
     * @param val the value
     */
    public void SetSocket(Boolean val){Socket = val;}

    /**
     * Get socket boolean.
     *
     * @return the boolean
     */
    public static Boolean getSocket(){return Socket;}

    private static boolean GameStarted = false;

    /**
     * Set game started.
     *
     * @param value the value
     */
    public static void setGameStarted(Boolean value){GameStarted = value;}

    /**
     * Is game started boolean.
     *
     * @return the boolean
     */
    public static boolean isGameStarted(){return GameStarted;}

    private static final String MainDirPAth = "CodexNaturalis";

    /**
     * Get main directory path as a string.
     *
     * @return the string MainDirPath
     */
    public static String getMainDirPAth(){return MainDirPAth;}


    /**
     * The constant xWindowSize.
     */
    public static final int xWindowSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    /**
     * The constant yWindowSize.
     */
    public static final int yWindowSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    /**
     * Gets window size.
     *
     * @return the window size
     */
    public static int getxWindowSize() {
        return xWindowSize;
    }

    /**
     * Gets window size.
     *
     * @return the window size
     */
    public static int getyWindowSize() {
        return yWindowSize;
    }

}
