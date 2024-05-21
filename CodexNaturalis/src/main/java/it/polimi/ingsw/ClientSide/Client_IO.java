package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import main.java.it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import main.java.it.polimi.ingsw.ClientSide.Utility.HelperMethods;
import main.java.it.polimi.ingsw.Rmi.ServerRMI;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Manages all messages sent and received when a Client communicates with the Server, but from a Clien't point of view;
 * these messages include the Update request (and reply), the Card flipping request and the Place Starting Card request,
 * amongst many others.
 * @author Darelle Maiva Nguedjio Nezeko, Edoardo Carlo Murru
 */
public class Client_IO {


    /**
     * The registry protected constant.
     */
    protected static Registry reg;
    /**
     * The ServerRMI type UpdateObject.
     */
    protected static ServerRMI UpdateObject;

    protected static boolean RMI_Set=false;
    protected static boolean MyTurn=false;
    private static int[][] lastUpdatedGrid = new int[80][40];
    private static int[] lastUpdatedScore = new int[4];
    private static int[] lastUpdatedPublicCards = new int[28];
    private static int[] lastUpdatedHand = new int[6];

    private static int playerCount;
    private static String username;
    private static String currentPlayer;
    public static String requestCurrentPlayerName() { return currentPlayer;  }

    /**
     * Sets username.
     * @param user      the username
     */
    public static void setUsername(String user){username=user;}

    /**
     * Gets username as a string.
     * @return the username
     */
    public static String getUsername(){return username;}



    private static String[] game_usernames;

    /**
     * Sets all usernames for a Game in a string array.
     * @param usernames     current game usernames list
     */
    public static void setGame_usernames(String[] usernames){game_usernames=usernames;}

    /**
     * Gets all us username for a Game.
     * @return the string array containing the usernames
     */
    public static String[] getGame_usernames(){return game_usernames;}

    /**
     * Request grid sizes.
     * @return the sizes array
     */
    //OnlyLocalGame
    public static int[] requestGridSizes(){return new int[]{80,40};}

    /**
     * Request current player count.
     * @return the count as an int
     */
    public static int requestCurrentPlayerCount(){ return playerCount ;}

    /**
     * Sets RMI communication.
     */
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

    //updaters

    /**
     * Requests un update.
     */
     public static void requestUpdate()
    {
        if(ClientConstants.getSocket()) {

            if(!ClientConstants.isGameStarted()){
                ClientConstants.setGameStarted(GameClient.listenForResponse("GameStartedStatus,"+ username).contains("true"));
                System.out.print("GameStarted: "+ ClientConstants.isGameStarted());

                if(ClientConstants.isGameStarted()){
                    setGame_usernames(getUsernamesString().split(","));
                    requestUpdate();
                }

            }

            else {

                MyTurn = getCurrentTurn();
                System.out.println("IsTurn: " + MyTurn + " ");

                System.out.println("RequestingSocketUpdate");
                requestSocketUpdate();

                if(ClientConstants.getGUI()){
                    FULL_GUI.updateGUI();}
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
                        requestUpdate();
                    }
                }
                else{

                    UpdateObject.update(username);
                    MyTurn = getCurrentTurn();
                    System.out.println("IsTurn: " + MyTurn);


                    playerCount = UpdateObject.RMI_getCurrentPlayerCount();

                    lastUpdatedPublicCards = UpdateObject.RMI_getPublicCardsID();
                    lastUpdatedHand = UpdateObject.RMI_getPlayerHand();

                    lastUpdatedGrid = UpdateObject.RMI_getPlayerBoard();
                    lastUpdatedScore = UpdateObject.RMI_getPlayersScores();

                    if(ClientConstants.getGUI()){
                        FULL_GUI.updateGUI();}
                }

            }catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}

        }

    }

    /**
     * Requests for a socket update with updated Playercount, Public Cards, Hand, PlayBoard and Scores.
     */
    private static void requestSocketUpdate(){

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

    private static boolean getCurrentTurn() {
        if(ClientConstants.getSocket()) { currentPlayer = GameClient.listenForResponse("SendCurrentTurn," + username); }
        else{ try { currentPlayer = UpdateObject.isTurn(username);}catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}


        return currentPlayer.equals(username);
    }


    /**
     * Requests public cards ids in a list.
     * @return the int array of last updated Cards
     */
    public static int[] requestPublicCardsID(){return lastUpdatedPublicCards;}

    /**
     * Requests Player's grid.
     * @return the last updated grid int matrix.
     */
    public static int[][] requestGrid(){return lastUpdatedGrid;}

    /**
     * Requests player score int [ ].
     * @return the int [ ]
     */
    public static int[] requestPlayerScore(){ return lastUpdatedScore;}

    /**
     * Request player hand int [ ].
     * @return the int [ ]
     */
    public static int[] requestPlayerHand() { return lastUpdatedHand;}



    //Modifiers
    /**
     * Flips card in a given position.
     * @param position   the Card position
     */
    public static void FlipCard_inPos(int position)
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.PLAY) && !Client_Game.getCurrentScene().equals(GameStates.PLACE_STARTING)){return;}

        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Flip," +username +","+ position);}
        else{ try { UpdateObject.RMI_Flip(position, username); }catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    //Server_IO.Flip(position, assignedNumber); requestUpdate();}

    /**
     * Draws a Card from desired position.
     * @param position       the position to draw the Card from
     */
    public static void DrawCard(int position)
    //{Server_IO.DrawCard(position, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.DRAW)){return;}

        System.out.println("Requesting Card Draw at position "+position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Draw," +username +","+ position);}
        else{try {UpdateObject.RMI_DrawCard(position, username);} catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
        Client_Game.ChangeScene(GameStates.SPECTATE_PLAYER);
    }

    /**
     * Places Starting Card in PlayBoard.
     * @param selectedCard the selected card
     */
    public static void PlaceStartingCard(int selectedCard)
    //{ Server_IO.PlaceStartingCard(selectedCard, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.PLACE_STARTING)){return;}

        System.out.println("PlaceStartingCard," +username +","+ selectedCard);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("PlaceStartingCard," +username +","+ selectedCard);}
        else{try { UpdateObject.RMI_PlaceStartingCard(selectedCard, username);}catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        if(ClientConstants.getGUI()){ requestUpdate(); }

        if(MyTurn){Client_Game.ChangeScene(GameStates.PLAY);}
        else {Client_Game.ChangeScene(GameStates.SPECTATE_PLAYER);}

    }

    /**
     * Chooses goal card.
     * @param position      the position of the chosen Card
     */
    public static void ChooseGoalCard(int position) {

        if(!Client_Game.getCurrentScene().equals(GameStates.CHOOSE_GOAL)){return;}
        if(position != 3 && position != 5){return;}

        System.out.println("ChooseGoalCard," +username +","+ position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("ChooseGoalCard," +username +","+ position);}
        else{try { UpdateObject.RMI_ChooseGoalCard(position, username);}catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        Client_Game.ChangeScene(GameStates.PLACE_STARTING);
        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Plays a card by using their Deck index.
     *
     * @param Row_index     the row index
     * @param Columns_index the columns index
     * @param id            the unique Card id
     * @return the boolean indicating if the Card has been placed successfully
     */
    public static boolean playCardByIndex(int Row_index, int Columns_index, int id) {
        //System.out.println("RequestedPlay," + Row_index + "," + Columns_index + "," + id);

        if(!Client_Game.getCurrentScene().equals(GameStates.PLAY)){return false;}

        boolean returnValue = false;

        //System.out.println("PlayCardByIndex," +username +","+ id+ " ("+Row_index+","+Columns_index+ ")");
        if (ClientConstants.getSocket())
        { returnValue = GameClient.listenForResponse("playCardByIndex," + username +","+ Row_index + "," + Columns_index + "," + id).equals("true"); }
        else{
            try { returnValue = UpdateObject.RMI_PlayCardByIndex(Row_index, Columns_index, id, username);
                if(!returnValue){ if( UpdateObject.isClosed( ClientConstants.getPort() )){ ClientExceptionHandler.CalculateWinner(); } }
            }
            catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        if(returnValue){ if(ClientConstants.getGUI()){ requestUpdate(); }}

        return returnValue;
    }

    /**
     * Gets a new communication port.
     */
    public static void getNewPort(){
        int NewPort = 0;
        if(ClientConstants.getSocket()) { NewPort = Integer.parseInt(GameClient.listenForResponse("getNewPort," +username)); }
        else{ try { NewPort = UpdateObject.RMI_getNewPort(username); setRMI(); }catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}
        ClientConstants.setPort(NewPort);
        System.out.println("NewPort = " + NewPort);

        RMI_Set = false;
    }

    /**
     * Joins a game.
     * @return the string reply message
     */
    public static String JoinGame()
    {
        if(ClientConstants.getSocket())
        { return GameClient.listenForResponse("JoinPackage,"+username);}
        else{ if(!RMI_Set){setRMI();}
            try{ return UpdateObject.JoinGame(username);} catch (RemoteException e ){ClientExceptionHandler.ServerUnreachable(e);}}
        return "Client_Failed";
    }

    /**
     * Reconnects the Client to an existing Game.
     * @param port   the last port assigned before disconnection
     * @return the string reply message
     */
    public static String Reconnect(int port)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("AttemptingReconnection,"+username+","+port);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.Reconnect(username, port); } catch (RemoteException e ){ClientExceptionHandler.ServerUnreachable(e);}}
        return "Client_Failed";
    }

    /**
     * Creates a new Game.
     * @param playerCount   the number of Players
     * @return the string reply message
     */
    public static String CreateGame(int playerCount)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("CreateGame,"+username+','+playerCount);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.CreateGame(username, playerCount); } catch (RemoteException e ){ ClientExceptionHandler.ServerUnreachable(e);}}
        return "Client_Failed";
    }

    /**
     * Gets usernames as a string.
     * @return reply message string
     */
    public static String getUsernamesString()
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("getUsernames,"+username);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.RMI_getUsernames(); } catch (RemoteException e ){ClientExceptionHandler.ServerUnreachable(e);}}
        return "Client_Failed";
    }

    /**
     * Definition of a second converted from system.nanotime()
     */
    private static final double second = (1000000000.0);
    /**
     * The update time interval: un Update is send automatically every 10 seconds.
     */
    private static final double timePerUpdate = 10*second;
    private static long lastUpdateTime = System.nanoTime();
    private static int[][] lastPlayerGrid = null;

    /**
     * Gets current Player PlayBoard or grid to place their Cards.
     * @return updated Player Grid
     */
    public static int[][] getCurrentPlayerGrid()
    {
        if(!Client_Game.getCurrentScene().equals(GameStates.SPECTATE_PLAYER)){return lastUpdatedGrid;}

        if((System.nanoTime() >= lastUpdateTime + timePerUpdate))
        {
            lastUpdateTime = System.nanoTime();
            if(ClientConstants.getGUI()){ requestUpdate(); }

            if(!MyTurn){
                if(ClientConstants.getSocket())
                { lastPlayerGrid = HelperMethods.FormattedStringToMatrix(GameClient.listenForResponse("getCurrentPlayerGrid,"+username));}
                else{ if(!RMI_Set){setRMI();}
                    try{ lastPlayerGrid = UpdateObject.RMI_getCurrentPlayerGrid(); } catch (RemoteException e ){ClientExceptionHandler.ServerUnreachable(e);}}
                return lastPlayerGrid;
            }

            Client_Game.ChangeScene(GameStates.PLAY);
            lastPlayerGrid = null;
        }

        if(ClientConstants.getGUI()){ return lastPlayerGrid; }


        try
        {
            TimeUnit.SECONDS.sleep((long) (timePerUpdate/second)/2);
            requestUpdate();
            if(MyTurn){ Client_Game.ChangeScene(GameStates.PLAY); return lastUpdatedGrid; }

        }catch (InterruptedException e){e.printStackTrace();}

        return null;

    }

}
