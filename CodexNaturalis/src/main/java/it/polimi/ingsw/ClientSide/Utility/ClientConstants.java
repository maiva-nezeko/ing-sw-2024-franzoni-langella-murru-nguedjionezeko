package main.java.it.polimi.ingsw.ClientSide.Utility;

import java.awt.*;
import java.nio.file.FileSystems;

public class ClientConstants {

    public static int SelectedCard;

    private static int Port = 1330;
    public static void setPort(int port){Port = port;}
    public static int getPort(){return Port;}

    private static boolean GUI = false;
    public static boolean getGUI() { return GUI;   }
    public static void setGUI(boolean value) { GUI = value; }

    private static final int[] TUIGridSizes = {7*80,9*40};
    public static int[] getTUIGridSizes(){return TUIGridSizes;}
    public static int[] getTuiCardSizes(){ return new int[]{7,9};  }

    private static boolean Socket = false;
    public void SetSocket(Boolean val){Socket = val;}
    public static Boolean getSocket(){return Socket;}

    private static boolean GameStarted = false;
    public static void setGameStarted(Boolean value){GameStarted = value;}
    public static boolean isGameStarted(){return GameStarted;}

    private static final String MainDirPAth = "CodexNaturalis";
    public static String getMainDirPAth(){return MainDirPAth;}


    public static final int xWindowSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int yWindowSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static int getxWindowSize() {
        return xWindowSize;
    }
    public static int getyWindowSize() {
        return yWindowSize;
    }


}
