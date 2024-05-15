package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import main.java.it.polimi.ingsw.ClientSide.Utility.HelperMethods;
import main.java.it.polimi.ingsw.Rmi.ServerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * The type Client io.
 */
public class Client_IO {


    /**
     * The constant reg.
     */
    protected static Registry reg;
    /**
     * The constant UpdateObject.
     *
     */
    protected static ServerRMI UpdateObject;
    /**
     * The constant RMI_Set.
     */
    protected static boolean RMI_Set=false;
    protected static boolean MyTurn=false;
    private static int[][] lastUpdatedGrid = new int[80][40];
    private static int[] lastUpdatedScore = new int[4];
    private static int[] lastUpdatedPublicCards = new int[28];
    private static int[] lastUpdatedHand = new int[6];

    private static int playerCount;

    private static String username;

    /**
     * Set username.
     *
     * @param user the user
     */
    public static void setUsername(String user){username=user;}

    /**
     * Get username string.
     *
     * @return the string
     */
    public static String getUsername(){return username;}



    private static String[] game_usernames;

    /**
     * Set username.
     *
     * @param usernames current game usernames list
     */
    public static void setGame_usernames(String[] usernames){game_usernames=usernames;}

    /**
     * Get username string.
     *
     * @return the string
     */
    public static String[] getGame_usernames(){return game_usernames;}

    /**
     * Request grid sizes int [ ].
     *
     * @return the int [ ]
     */
//OnlyLocalGame
    public static int[] requestGridSizes(){return new int[]{80,40};}

    /**
     * Request current player count int.
     *
     * @return the int
     */
    public static int requestCurrentPlayerCount(){ return playerCount ;}

    private static void setRMI()
    {
        try{
            //int RMI_port = ClientConstants.getPort()+1;
            int RMI_port = 1331;
            reg = LocateRegistry.getRegistry(ClientConstants.getIp(), RMI_port);
            UpdateObject = (ServerRMI) reg.lookup("GetUpdates");
            RMI_Set=true;
        }
        catch (Exception e){e.printStackTrace();}
    }


    /**
     * Request update.
     */
//updaters
     public static void requestUpdate()
    {
        if(ClientConstants.getSocket()) {

            if(!ClientConstants.isGameStarted()){
                ClientConstants.setGameStarted(GameClient.listenForResponse("GameStartedStatus,"+ username).contains("true"));
                System.out.print("GameStarted: "+ ClientConstants.isGameStarted());

                if(ClientConstants.isGameStarted()){
                    setGame_usernames(getUsernamesString().split(","));
                }

            }

            else {

                MyTurn = GameClient.listenForResponse("SendCurrentTurn," + username).equals("true");
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
                    ClientConstants.setGameStarted(UpdateObject.GameStarted(username)); System.out.println(" GameStarted: "+ ClientConstants.isGameStarted() + " ");
                    if(ClientConstants.isGameStarted()){
                        setGame_usernames(getUsernamesString().split(","));
                    }
                }
                else{

                    UpdateObject.update(username);
                    MyTurn = UpdateObject.isTurn(username); System.out.println("IsTurn: " + MyTurn);


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


    /**
     * Request public cards id int [ ].
     *
     * @return the int [ ]
     */
    public static int[] requestPublicCardsID(){return lastUpdatedPublicCards;}

    /**
     * Request grid int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    public static int[][] requestGrid(){return lastUpdatedGrid;}

    /**
     * Request player score int [ ].
     *
     * @return the int [ ]
     */
    public static int[] requestPlayerScore(){ return lastUpdatedScore;}

    /**
     * Request player hand int [ ].
     *
     * @return the int [ ]
     */
    public static int[] requestPlayerHand() { return lastUpdatedHand;}


    /**
     * Flip card in pos.
     *
     * @param position the position
     */
//Modifiers
    public static void FlipCard_inPos(int position)
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.PLAY) && !Client_Game.getCurrentScene().equals(GameStates.PLACE_STARTING)){return;}

        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Flip," +username +","+ position);}
        else{ try { UpdateObject.RMI_Flip(position); }catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }
    //Server_IO.Flip(position, assignedNumber); requestUpdate();}

    /**
     * Draw card.
     *
     * @param position the position
     */
    public static void DrawCard(int position)
    //{Server_IO.DrawCard(position, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.DRAW)){return;}

        System.out.println("Requesting Card Draw at position "+position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Draw," +username +","+ position);}
        else{try {UpdateObject.RMI_DrawCard(position);} catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
        Client_Game.ChangeScene(GameStates.SPECTATE_PLAYER);
    }

    /**
     * Place starting card.
     *
     * @param selectedCard the selected card
     */
    public static void PlaceStartingCard(int selectedCard)
    //{ Server_IO.PlaceStartingCard(selectedCard, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.PLACE_STARTING)){return;}

        System.out.println("PlaceStartingCard," +username +","+ selectedCard);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("PlaceStartingCard," +username +","+ selectedCard);}
        else{try { UpdateObject.RMI_PlaceStartingCard(selectedCard);}catch (RemoteException e){e.printStackTrace();}}

        Client_Game.ChangeScene(GameStates.PLAY);
        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Choose goal card.
     *
     * @param position the position
     */
    public static void ChooseGoalCard(int position) {

        if(!Client_Game.getCurrentScene().equals(GameStates.CHOOSE_GOAL)){return;}
        if(position != 3 && position != 5){return;}

        System.out.println("ChooseGoalCard," +username +","+ position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("ChooseGoalCard," +username +","+ position);}
        else{try { UpdateObject.RMI_ChooseGoalCard(position);}catch (RemoteException e){e.printStackTrace();}}

        Client_Game.ChangeScene(GameStates.PLACE_STARTING);
        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Play card by index boolean.
     *
     * @param Row_index     the row index
     * @param Columns_index the columns index
     * @param id            the id
     * @return the boolean
     */
    public static boolean playCardByIndex(int Row_index, int Columns_index, int id) {
        //System.out.println("RequestedPlay," + Row_index + "," + Columns_index + "," + id);

        if(!Client_Game.getCurrentScene().equals(GameStates.PLAY)){return false;}

        boolean returnValue = false;

        HelperMethods.resize(Row_index, Columns_index);

        System.out.println("PlayCardByIndex," +username +","+ id+ " ("+Row_index+","+Columns_index+ ")");
        if (ClientConstants.getSocket())
        { returnValue = GameClient.listenForResponse("playCardByIndex," + username +","+ Row_index + "," + Columns_index + "," + id).equals("true"); }

        else{ try { returnValue = UpdateObject.RMI_PlayCardByIndex(Row_index, Columns_index, id); }catch (RemoteException e){e.printStackTrace();}}

        if(returnValue){ if(ClientConstants.getGUI()){ requestUpdate(); }}
        return returnValue;
    }

    /**
     * Get new port.
     */
    public static void getNewPort(){
        int NewPort = 0;
        if(ClientConstants.getSocket()) { NewPort = Integer.parseInt(GameClient.listenForResponse("getNewPort," +username)); }
        else{ try { NewPort = UpdateObject.RMI_getNewPort(username); setRMI(); }catch (RemoteException e){e.printStackTrace();}}
        ClientConstants.setPort(NewPort);
        System.out.println("NewPort = " + NewPort);

        RMI_Set = false;
    }

    /**
     * Join game string.
     *
     * @return the string
     */
    public static String JoinGame()
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("JoinPackage,"+username);}
        else{ if(!RMI_Set){setRMI();}
            try{ return UpdateObject.JoinGame(username);} catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    /**
     * Reconnect string.
     *
     * @param port last port assigned before disconnection
     * @return the string
     */
    public static String Reconnect(int port)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("AttemptingReconnection,"+username+","+port);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.Reconnect(username, port); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    /**
     * Create game string.
     *
     * @param playerCount the player count
     * @return the string
     */
    public static String CreateGame(int playerCount)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("CreateGame,"+username+','+playerCount);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.CreateGame(username, playerCount); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    public static String getUsernamesString()
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("getUsernames,"+username);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.RMI_getUsernames(); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    private static final double second = (1000000000.0);
    private static final double timePerUpdate = 20*second;
    private static long lastUpdateTime = System.nanoTime();
    private static int[][] lastPlayerGrid = null;

    public static int[][] getCurrentPlayerGrid()
    {
        if((System.nanoTime() > lastUpdateTime + timePerUpdate))
        {
            lastUpdateTime = System.nanoTime();
            requestUpdate();

            if(!MyTurn){
                if(ClientConstants.getSocket()) { lastPlayerGrid = HelperMethods.FormattedStringToMatrix(GameClient.listenForResponse("getCurrentPlayerGrid,"+username));}
                else{ if(!RMI_Set){setRMI();} try{ lastPlayerGrid = UpdateObject.RMI_getCurrentPlayerGrid(); } catch (RemoteException e ){e.printStackTrace();}}
                return lastPlayerGrid;
            }

            Client_Game.ChangeScene(GameStates.PLAY);
            lastPlayerGrid = null;
        }
        return lastPlayerGrid;

    }

}
