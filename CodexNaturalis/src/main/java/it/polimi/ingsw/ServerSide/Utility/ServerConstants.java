package main.java.it.polimi.ingsw.ServerSide.Utility;

import java.nio.file.FileSystems;

public class ServerConstants {
    private  static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    public static String getMainDirPAth(){return MainDirPAth;}
    private static final int UPS_SET = 100;
    public static int getUPS_SET() {
        return UPS_SET;
    }

    private static final long Time_Per_Turn = 60;
    public static long getTime_Per_Turn(){ return Time_Per_Turn; }

    public static int getNumOfRows(){ return 80; }

    private static boolean debug=false;
    public static void setDebug(Boolean value) { debug = value; }
    public static boolean getDebug() { return debug; }

    private static boolean noSaveDelete=false;
    public static void setNoSaveDelete(Boolean value) { noSaveDelete = value; }
    public static boolean getNoSaveDelete() { return noSaveDelete; }
    
    
    public static void printMessageLn(String message) {   if(debug){System.out.println(message);}  }
    public static void printMessage(String message){ if(debug){System.out.print(message);} }


}
