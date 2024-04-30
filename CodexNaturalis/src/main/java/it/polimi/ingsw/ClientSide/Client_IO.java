package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import main.java.it.polimi.ingsw.ClientSide.Utility.HelperMethods;
import main.java.it.polimi.ingsw.Rmi.ServerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client_IO {


    protected static Registry reg;
    protected static ServerRMI UpdateObject;
    protected static boolean RMI_Set=false;

    private static int[][] lastUpdatedGrid = new int[20][10];
    private static int[] lastUpdatedScore = new int[4];
    private static int[] lastUpdatedPublicCards = new int[28];
    private static int[] lastUpdatedHand = new int[6];

    private static int playerCount;

    private static String username;
    public static void setUsername(String user){username=user;}
    public static String getUsername(){return username;}

    //OnlyLocalGame
    public static int[] requestGridSizes(){return new int[]{20,10};}

    public static int requestCurrentPlayerCount(){ return playerCount ;}

    private static void setRMI()
    {
        try{
            int RMI_port = ClientConstants.getPort()+1;
            reg = LocateRegistry.getRegistry("localhost", RMI_port);
            UpdateObject = (ServerRMI) reg.lookup("GetUpdates");
            RMI_Set=true;
        }
        catch (Exception e){e.printStackTrace();}
    }



    //updaters
     public static void requestUpdate()
    {
        if(ClientConstants.getSocket()) {

            if(!ClientConstants.isGameStarted()){
                ClientConstants.setGameStarted(GameClient.listenForResponse("GameStartedStatus,"+ username).contains("true"));
                System.out.print("GameStarted: "+ ClientConstants.isGameStarted());}

            else {

                boolean MyTurn = GameClient.listenForResponse("SendCurrentTurn," + username).equals("true");
                System.out.println("IsTurn: " + MyTurn + " ");

                if(MyTurn){  System.out.println("RequestingSocketUpdate");
                    requestSocketUpdate();}

                if(ClientConstants.getGUI()){
                    FULL_GUI.updateGUI();}
                else{ TUI.renderTUI(); }
            }
        }

        else
        {
            if(!RMI_Set){setRMI();}
            try {
                if(!ClientConstants.isGameStarted()){
                    ClientConstants.setGameStarted(UpdateObject.GameStarted(username)); System.out.println(" GameStarted: "+ ClientConstants.isGameStarted() + " ");}
                else{

                    UpdateObject.update(username);
                    boolean MyTurn = UpdateObject.isTurn(username); System.out.println("IsTurn: " + MyTurn);


                    playerCount = UpdateObject.RMI_getCurrentPlayerCount();

                    lastUpdatedPublicCards = UpdateObject.RMI_getPublicCardsID();
                    lastUpdatedHand = UpdateObject.RMI_getPlayerHand();

                    lastUpdatedGrid = UpdateObject.RMI_getPlayerBoard();
                    lastUpdatedScore = UpdateObject.RMI_getPlayersScores();

                    if(ClientConstants.getGUI()){
                        FULL_GUI.updateGUI();}
                    else{ TUI.renderTUI(); }
                }

            }catch (RemoteException e){e.printStackTrace();}

        }

    }

    private static void requestSocketUpdate() {

        String[] SocketUpdate = GameClient.listenForResponse("SendUpdate," + username).split(";");
        System.out.println("UpdateReceived");

        if(SocketUpdate[0].equals("null") || SocketUpdate.length!=5){return;}

        playerCount = Integer.parseInt(SocketUpdate[0]);
        System.out.println("updatedPlayerCount: " +playerCount);

        lastUpdatedPublicCards = HelperMethods.FormattedStringToArray(SocketUpdate[1]);
        System.out.println("lastUpdatedPublicCards: " + Arrays.toString(lastUpdatedPublicCards));

        lastUpdatedHand = HelperMethods.FormattedStringToArray(SocketUpdate[2]);
        System.out.println("lastUpdatedHand: " + Arrays.toString(lastUpdatedHand));

        lastUpdatedGrid = HelperMethods.FormattedStringToMatrix(SocketUpdate[3]);
        System.out.println("lastUpdatedGrid: " + Arrays.deepToString(lastUpdatedGrid));

        lastUpdatedScore = HelperMethods.FormattedStringToArray(SocketUpdate[4]);
        System.out.println("lastUpdatedScore: " + Arrays.toString(lastUpdatedScore));

    }


    public static int[] requestPublicCardsID(){return lastUpdatedPublicCards;}
    public static int[][] requestGrid(){return lastUpdatedGrid;}
    public static int[] requestPlayerScore(){ return lastUpdatedScore;}

    public static int[] requestPlayerHand() { return lastUpdatedHand;}



    //Modifiers
    public static void FlipCard_inPos(int position)
    {
        if(!Client_Game.getCurrentScene().equals("Play")){return;}

        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Flip," +username +","+ position);}
        else{ try { UpdateObject.RMI_Flip(position); }catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }
    //Server_IO.Flip(position, assignedNumber); requestUpdate();}

    public static void DrawCard(int position)
    //{Server_IO.DrawCard(position, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals("Draw")){return;}

        System.out.println("Requesting Card Draw at position "+position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Draw," +username +","+ position);}
        else{try {UpdateObject.RMI_DrawCard(position);} catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    public static void PlaceStartingCard(int selectedCard)
    //{ Server_IO.PlaceStartingCard(selectedCard, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals("Play")){return;}

        System.out.println("PlaceStartingCard," +username +","+ selectedCard);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("PlaceStartingCard," +username +","+ selectedCard);}
        else{try { UpdateObject.RMI_PlaceStartingCard(selectedCard);}catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    public static void ChooseGoalCard(int position) {

        if(!Client_Game.getCurrentScene().equals("Choose_Goal")){return;}
        if(position != 3 && position != 5){return;}

        System.out.println("ChooseGoalCard," +username +","+ position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("ChooseGoalCard," +username +","+ position);}
        else{try { UpdateObject.RMI_ChooseGoalCard(position);}catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    public static boolean playCardByIndex(int Row_index, int Columns_index, int id) {
        //System.out.println("RequestedPlay," + Row_index + "," + Columns_index + "," + id);

        if(!Client_Game.getCurrentScene().equals("Play")){return false;}

        boolean returnValue = false;

        System.out.println("PlayCardByIndex," +username +","+ id);
        if (ClientConstants.getSocket())
        { returnValue = GameClient.listenForResponse("playCardByIndex," + username +","+ Row_index + "," + Columns_index + "," + id).equals("true"); }

        else{ try { returnValue = UpdateObject.RMI_PlayCardByIndex(Row_index, Columns_index, id); }catch (RemoteException e){e.printStackTrace();}}

        if(returnValue){ if(ClientConstants.getGUI()){ requestUpdate(); }}
        return returnValue;
    }

    public static void getNewPort(){
        int NewPort = 0;
        if(ClientConstants.getSocket()) { NewPort = Integer.parseInt(GameClient.listenForResponse("getNewPort," +username)); }
        else{ try { NewPort = UpdateObject.RMI_getNewPort(username); setRMI(); }catch (RemoteException e){e.printStackTrace();}}
        ClientConstants.setPort(NewPort);
        System.out.println("NewPort = " + NewPort);

        RMI_Set = false;
    }
    public static String JoinGame(String Username)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("JoinPackage,"+Username);}
        else{ if(!RMI_Set){setRMI();}
            try{ return UpdateObject.JoinGame(Username);} catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    public static String Reconnect(String Username)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("AttemptingReconnection,"+Username);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.Reconnect(Username); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    public static String CreateGame(String Username, int playerCount)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("CreateGame,"+Username+','+playerCount);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.CreateGame(username, playerCount); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

}
