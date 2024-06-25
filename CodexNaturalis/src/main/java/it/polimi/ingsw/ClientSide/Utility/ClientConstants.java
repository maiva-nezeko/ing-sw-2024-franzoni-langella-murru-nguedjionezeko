package it.polimi.ingsw.ClientSide.Utility;

import java.awt.*;

/**
 * Useful Client attributes and their getters/setters methods.
 */
@SuppressWarnings("SameReturnValue")
public class ClientConstants {


    public static int SelectedCard;

    /**
     * Default port used for communication.
     */
    private static int Port = 1330;

    /**
     * Sets the communication port.
     *
     * @param port  the port int
     */
    public static void setPort(int port){Port = port;}

    /**
     * Gets port int.
     *
     * @return the int
     */
    public static int getPort(){return Port;}

    private static boolean GUI = false;

    /**
     * Gets full gui.
     *
     * @return the gui as a boolean
     */
    public static boolean getGUI() { return GUI;   }

    /**
     * Sets gui.
     *
     * @param value     the boolean value
     */
    public static void setGUI(boolean value) { GUI = value; }

    /**
     * PlayBoard fixed sizes in Text User Interface.
     */
    private static final int[] TUIGridSizes = {7*80,9*40};

    /**
     * Gets TUI matrix grid sizes for PlayBoard.
     *
     * @return the sizes as an int array
     */
    public static int[] getTUIGridSizes(){return TUIGridSizes;}

    /**
     * Gets TUI Card sizes.
     *
     * @return an int array
     * @deprecated - unnecessary method
     */
    public static int[] getTuiCardSizes(){ return new int[]{7,9};  }

    private static boolean Socket = true;

    /**
     * Sets socket.
     *
     * @param val   the boolean value
     */
    public static void SetSocket(Boolean val){Socket = val;}

    /**
     * Gets socket.
     *
     * @return the boolean
     */
    public static Boolean getSocket(){return Socket;}

    private static boolean GameStarted = false;

    /**
     * Sets the game as started.
     *
     * @param value     the boolean value
     */
    public static void setGameStarted(Boolean value){GameStarted = value;}

    /**
     * Checks if game is started.
     *
     * @return the boolean response
     */
    public static boolean isGameStarted(){return GameStarted;}

    /**
     * Fixed main directory path string.
     */
    private static final String MainDirPAth = System.getProperty("user.dir");

    /**
     * Gets main directory path as a string.
     *
     * @return the path
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
     * Gets window size width.
     *
     * @return the window size as a static int
     */
    public static int getxWindowSize() {
        return xWindowSize;
    }

    /**
     * Gets window size height.
     *
     * @return the window size as a static int
     */
    public static int getyWindowSize() {
        return yWindowSize;
    }

    /**
     * The constant ip Address.
     */
    public static String ip = "localhost";

    /**
     * Gets ip string Address.
     *
     * @return the ip
     */
    public static String getIp(){ return ip; }

    /**
     * Sets ip Address.
     *
     * @param ipString  the ip as a string
     */
    public static void setIP(String ipString) { ip = ipString;   }

    private static boolean GUIHelper = true;

    /**
     * Gets gui helper.
     *
     * @return the gui helper
     */
    public static boolean getGUIHelper() { return GUIHelper;  }

    /**
     * Toggle gui helper.
     */
    public static void toggleGUIHelper() { GUIHelper = !GUIHelper; }


}
