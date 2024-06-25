package it.polimi.ingsw.ClientSide;

import it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.MainClasses.GameStates;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import it.polimi.ingsw.ClientSide.Utility.HelperMethods;
import it.polimi.ingsw.Rmi.ServerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

/**
 * Manages all messages sent and received when a Client communicates with the Server, but from a Client's point of view;
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

    /**
     * RMI boolean used when socket communication isn't being used by the Client to check if RMI communication
     * has already been set.
     */
    protected static boolean RMI_Set=false;
    /**
     * Boolean that is set as true when it's the current Client's turn, therefore allowing him to play.
     */
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
     * Sets username as a string.
     * @param user      the username
     */
    public static void setUsername(String user){username=user;}

    /**
     * Gets username as a string.
     * @return the username
     */
    public static String getUsername(){return username;}

    /**
     * Collects game's usernames list.
     */
    private static String[] game_usernames;

    /**
     * Sets all usernames for a Game in a string array.
     * @param usernames     current game usernames list
     */
    public static void setGame_usernames(String[] usernames){game_usernames=usernames;}

    /**
     * Gets all username for a Game.
     * @return the string array containing the usernames
     */
    public static String[] getGame_usernames(){return game_usernames;}


    //OnlyLocalGame
    /**
     * Requests grid or PlayBoard sizes.
     * @return the fixed grid sizes array
     */
    public static int[] requestGridSizes(){return new int[]{80,40};}

    /**
     * Requests current player count.
     * @return the count as an int
     */
    public static int requestCurrentPlayerCount(){ return playerCount ;}

    /**
     * Sets RMI communication in port 1331, gets a new registry, initializes UpdateObject to allow for RMI
     * updates and lastly modifies the boolean RMI_Set to true, to indicate that the RMI has been set.
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
     * Requests an update: ensures game has started and hasn't ended, then gets current turn and updates GUI -
     * if present. If the used communication is Socket, calls for a socket update instead and exits this method,
     * otherwise we proceed with the RMI update in this method (as a standard).
     * @see Client_IO#requestSocketUpdate()
     * The update request gets the new players count, the public cards, the current player's hand,
     * the PlayBoard and the Player's score along with visible resources count.
     * In the end, the gui render is also updated - if applicable.
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
     * Prints the response of a Socket Update request, specifically the new Playercount, the Public Cards, the Hand,
     * the PlayBoard and the Scores along with visible resources count.
     * Note: if the assigned Socket Update is null we simply exit the method.
     */
    private static void requestSocketUpdate(){

        String[] SocketUpdate = GameClient.listenForResponse("SendUpdate," + username).split(";");

        if(SocketUpdate[0].equals("null") || SocketUpdate.length!=5){return;}

        playerCount = Integer.parseInt(SocketUpdate[0]);

        lastUpdatedPublicCards = HelperMethods.FormattedStringToArray(SocketUpdate[1]);

        lastUpdatedHand = HelperMethods.FormattedStringToArray(SocketUpdate[2]);

        lastUpdatedGrid = HelperMethods.FormattedStringToMatrix(SocketUpdate[3]);

        lastUpdatedScore = HelperMethods.FormattedStringToArray(SocketUpdate[4]);

    }

    /**
     * Gets current turn. In particular, requests the current Player through GameClient for Socket communication;
     * Alternatively, in case of RMI communication requests the current Player from the UpdateObject.
     *
     * @return the username of the current player turn
     * @see GameClient#listenForResponse
     */
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
     * Requests Player's grid or PlayBoard.
     * @return the last updated grid int matrix.
     */
    public static int[][] requestGrid(){return lastUpdatedGrid;}

    /**
     * Requests current Player's score, including visible resources count.
     * @return the scores array int[ ]
     */
    public static int[] requestPlayerScore(){ return lastUpdatedScore;}

    /**
     * Request player hand cards list.
     * @return the cards array int [ ]
     */
    public static int[] requestPlayerHand() { return lastUpdatedHand;}



    //Modifiers
    /**
     * Flips card in a given position: first changes the current GameState accordingly, then requests the flip either
     * for Socket or RMI communication - therefore calling for RMI_Flip - depending on ClientConstants, finally it
     * requests un update in GUI (if applicable).
     * Note: if the current scene is 'PLACE_STARTING' we simply exit the method.
     *
     * @param position      the position of the Card to flip
     * @see ServerRMI#RMI_Flip(int, String)
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
     * Prints Drawing Card (from desired position) messages: first changes the current GameState accordingly, then requests
     * the draw either for Socket or RMI communication - therefore calling for RMI_DrawCard - depending on ClientConstants,
     * finally it requests un update in GUI (if applicable) and changes the GameState to 'SPECTATE_PLAYER' to allow a Client
     * to see the other Players PlayBoards when it's their turn.
     * Note: if the current scene is 'DRAW' we simply exit the method.
     *
     * @param position      the position to draw the Card from
     * @see ServerRMI#RMI_DrawCard(int, String)
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
     * Places Starting Card in PlayBoard: first if the current GameState isn't 'PLACE_STARTING' we simply exit the method,
     * otherwise the Server is notified that a Starting Card is being placed, and we proceed with placing the Card through
     * Socket or RMI communication - therefore calling for RMI_PlaceStartingCard - depending on ClientConstants; finally it
     * requests un update in GUI (if applicable) and then changes scene to 'PLAY' or 'SPECTATE_PLAYER' if it's not our turn.
     *
     * @param selectedCard   the starting card
     * @see ServerRMI#RMI_PlaceStartingCard(int, String)
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
     * Chooses goal card: first if the current GameState isn't 'CHOOSE_GOAL' we simply exit the method, and the same goes
     * if the chosen Card isn't in one of the two position where Personal Goal cards are places by default (position 3 or 5);
     * otherwise the Server is notified that a GoalCard is being chosen, and we proceed with setting the Card through
     * Socket or RMI communication - in which case RMI_ChooseGoalCard is called - depending on ClientConstants; finally it
     * changes scene to 'PLACE_STARTING' and then requests un update in GUI (if applicable).
     *
     * @param position      the position of the chosen Goal Card
     *
     * @see ServerRMI#RMI_ChooseGoalCard(int, String)
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
     * Prints response for Playing a card by using its Deck index: first if the current GameState isn't 'PLAY' we simply exit
     * the method, returning false, and the same goes if the Card can't be played, as the default return would be set as false
     * - after calling for RMI_PlayCardByIndex, but eventually is later turned to true if playable. In that case, un update
     * request is sent, and then a GUI update (if applicable). On the other hand, if the Card is calculated non-playable,
     * the method checks if the game has ended and therefore proceeds to Calculate te winner.
     *
     * @param Row_index     the row index
     * @param Columns_index the columns index
     * @param id            the unique Card id
     * @return the boolean indicating if the Card has been placed successfully
     *
     * @see ServerRMI#RMI_PlayCardByIndex(int, int, int, String)
     */
    public static boolean playCardByIndex(int Row_index, int Columns_index, int id) {
        //System.out.println("RequestedPlay," + Row_index + "," + Columns_index + "," + id);

        if(!Client_Game.getCurrentScene().equals(GameStates.PLAY)){return false;}

        boolean returnValue = false;

        //System.out.println("PlayCardByIndex," +username +","+ id+ " ("+Row_index+","+Columns_index+ ")");
        if (ClientConstants.getSocket())
        { returnValue = GameClient.listenForResponse("playCardByIndex," + username +","+ Row_index + "," + Columns_index + "," + id).contains("true"); }
        else{
            try {
                returnValue = UpdateObject.RMI_PlayCardByIndex(Row_index, Columns_index, id, username);

                if(!returnValue){
                    if( UpdateObject.isClosed( ClientConstants.getPort() )){ ClientExceptionHandler.CalculateWinner(); } }
            }
            catch (RemoteException e){ClientExceptionHandler.ServerUnreachable(e);}}

        if(returnValue){ if(ClientConstants.getGUI()){ requestUpdate(); }}

        return returnValue;
    }

    /**
     * Prints response for method that gets a new communication port, either Socket or RMI, in which case RMI_getNewPort is called
     * then sets RMI_Set boolean accordingly. The Client is notified through a simple message.
     *
     * @see ServerRMI#RMI_getNewPort(String)
     * @see ClientConstants#setPort(int)
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
     * Prints messages for when a Player joins a game, either via Socket or RMI. In the latter case, it checks if RMI_Set
     * value is true, otherwise we call for setRMI, then either the reply from 'JoinGame' in ServerRMI interface is returned, or an
     * error message is sent.
     *
     * @return the string reply message
     *
     * @see ServerRMI#JoinGame(String)
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
     * Reconnects the Client to an existing Game, either via Socket or RMI. In the latter case, it checks if RMI_Set
     * value is true, otherwise we call for setRMI, then either the reply from 'Reconnect' in ServerRMI interface is returned, or an
     * error message is sent.
     *
     * @param port   the last port assigned before disconnection
     * @return the string reply message
     *
     * @see ServerRMI#Reconnect(String, int)
     */
    public static String Reconnect(int port)
    {
        String returnValue = "Client_Failed";
        if(ClientConstants.getSocket()) { returnValue = GameClient.listenForResponse("AttemptingReconnection,"+username+","+port);}
        else{ if(!RMI_Set){setRMI();} try{ returnValue = UpdateObject.Reconnect(username, port); } catch (RemoteException e ){ClientExceptionHandler.ServerUnreachable(e);}}

        System.out.println(returnValue);

        if(!returnValue.contains("failed"))
        {
            ClientConstants.setPort(port);

            requestUpdate();

            if(lastUpdatedHand[5]!=0){ Client_Game.ChangeScene(GameStates.CHOOSE_GOAL);}
            else if(lastUpdatedHand[4]!=0){ Client_Game.ChangeScene(GameStates.PLACE_STARTING);}
            else if(MyTurn){  Client_Game.ChangeScene(GameStates.PLAY); }
            else{Client_Game.ChangeScene(GameStates.SPECTATE_PLAYER);}
        }

        return returnValue;
    }

    /**
     * Creates a new Game, either via Socket or RMI. In the latter case, it checks if RMI_Set value is true, otherwise it calls
     * for setRMI, then either the reply from 'CreateGame' in ServerRMI interface is returned, or an error message is sent.
     *
     * @param playerCount   the number of Players
     * @return the string reply message
     *
     * @see ServerRMI#CreateGame(String, int)
     */
    public static String CreateGame(int playerCount)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("CreateGame,"+username+','+playerCount);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.CreateGame(username, playerCount); } catch (RemoteException e ){ ClientExceptionHandler.ServerUnreachable(e);}}
        return "Client_Failed";
    }

    /**
     * Gets usernames as a string, either via Socket or RMI. In the latter case, it checks if RMI_Set value is true, otherwise it calls
     * for setRMI, then either the reply from 'RMI_getUsernames' in ServerRMI interface is returned, or an error message is sent.
     *
     * @return reply message string
     *
     * @see ServerRMI#RMI_getUsernames()
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
     * Gets current Player PlayBoard or grid to place their Cards: first it makes sure that the current scene isn't
     * 'SPECTATE_PLAYER', to be able to immediately return lastPlayerGrid, otherwise it checks
     * if the update time has been exceeded, in which case the time is reset and a new update is requested; if it's no longer
     * current Player's turn, it requests the grid either through Socket or RMI communication - in which case it calls for
     * 'RMI_getCurrentPlayerGrid' - depending on ClientConstants, finally it requests un update in GUI (if applicable),
     * changes scene to 'PLAY' and return lastPlayerGrid.
     * Afterwards, a new update is requested and the GameState is changed to 'PLAY'.
     *
     * @return updated Player Grid
     *
     * @see ServerRMI#RMI_getCurrentPlayerGrid()
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
