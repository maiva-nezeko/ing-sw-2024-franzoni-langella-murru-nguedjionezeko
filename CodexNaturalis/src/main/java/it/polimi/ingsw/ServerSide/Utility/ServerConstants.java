package main.java.it.polimi.ingsw.ServerSide.Utility;

import java.nio.file.FileSystems;

/**
 * Contains constants used by the Server during a Game, such as the Number of Rows of a PlayBoard grid,
 * or a standard message to print.
 */
public class ServerConstants {
    private  static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    public static String getMainDirPAth(){return MainDirPAth;}
    private static final int UPS_SET = 100;
    public static int getUPS_SET() {
        return UPS_SET;
    }

    /**
     * A turn has an assigned maximum time limit of 60 seconds.
     * @deprecated as Time per Turn is present in GameServer.
     */
    private static final long Time_Per_Turn = 60;
    public static long getTime_Per_Turn(){ return Time_Per_Turn; }

    public static int getNumOfRows(){ return 80; }

    private static boolean debug=true;
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
