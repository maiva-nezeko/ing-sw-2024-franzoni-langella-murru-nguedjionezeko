package main.java.it.polimi.ingsw.ServerSide.Utility;

import java.nio.file.FileSystems;

/**
 * Contains constants used by the Server during a Game, such as the Number of Rows of a PlayBoard grid,
 * or a standard message to print.
 */
@SuppressWarnings("SameReturnValue")
public class ServerConstants {
    private  static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();

    /**
     * Gets main directory path.
     * @return the path as a string
     * @deprecated - unnecessary method
     */
    public static String getMainDirPAth(){return MainDirPAth;}
    private static final int UPS_SET = 100;

    /**
     * Gets UPS_SET.
     * @return the UPS_SET as an int
     * @deprecated from a previous version, no longer necessary
     */
    public static int getUPS_SET() {
        return UPS_SET;
    }

    /**
     * A turn has an assigned maximum time limit of 60 seconds.
     */
    private static final long Time_Per_Turn = 60;

    /**
     * Gets time limit for each turn.
     * @return the time as a long type
     * @deprecated as Time per Turn is present in GameServer.
     */
    public static long getTime_Per_Turn(){ return Time_Per_Turn; }

    public static int getNumOfRows(){ return 80; }

    private static boolean debug=true;

    /**
     * Sets the debug actions.
     * @param value  the boolean value
     * @deprecated unnecessary method
     */
    public static void setDebug(Boolean value) { debug = value; }
    public static boolean getDebug() { return debug; }

    private static boolean noSaveDelete=false;
    public static void setNoSaveDelete(Boolean value) { noSaveDelete = value; }
    public static boolean getNoSaveDelete() { return noSaveDelete; }

    /**
     * Prints Client messages, for example when a Card is placed successfully or when the last turn starts.
     * @param message   the message to print
     */
    public static void printMessageLn(String message) {   if(debug){System.out.println(message);}  }

    /**
     * Prints Server messages, for example when a new Player joins or when an update package is sent.
     * @param message   the message to print
     */
    public static void printMessage(String message){ if(debug){System.out.print(message);} }


}
