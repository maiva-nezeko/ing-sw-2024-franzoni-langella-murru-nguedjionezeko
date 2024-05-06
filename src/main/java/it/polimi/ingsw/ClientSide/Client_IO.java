package it.polimi.ingsw.ClientSide;

import it.polimi.ingsw.ClientSide.GUI_Render.FULL_GUI;
import it.polimi.ingsw.ClientSide.GameClient;
import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;
import it.polimi.ingsw.ClientSide.Utility.HelperMethods;
import it.polimi.ingsw.Rmi.ServerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * Client IO the class to manage or collect all the input that needs to be sent to the server
 * including pre written statements as a reply to Update/Connection attempts.
 */
public class Client_IO {


    /**
     * The constant registry where every message will be saved while the game exists.
     */
    protected static Registry reg;
    /**
     * The constant UpdateObject.
     */
    protected static ServerRMI UpdateObject;
    /**
     * The constant boolean RMI_Set............................. WHAT
     */
    protected static boolean RMI_Set=false;

    private static int[][] lastUpdatedGrid = new int[20][10];
    private static int[] lastUpdatedScore = new int[4];
    private static int[] lastUpdatedPublicCards = new int[28];
    private static int[] lastUpdatedHand = new int[6];

    private static int playerCount;

    private static String username;

    /**
     * Set username.
     *
     * @param user the username set
     */
    public static void setUsername(String user){username=user;}

    /**
     * Get username, used in get player by username.
     *
     * @return the username as a string
     */
    public static String getUsername(){return username;}

    /**
     * Request grid sizes.
     *
     * @return the int [ ] of sizes
     */
//OnlyLocalGame
    public static int[] requestGridSizes(){return new int[]{20,10};}

    /**
     * Request current player count.
     *
     * @return the numbers of players as an int
     */
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


    /**
     * Request update in player's turn.
     */
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


    /**
     * Request public cards for everyone to draw as an array.
     *
     * @return the cards as a list of int [ ]
     */
    public static int[] requestPublicCardsID(){return lastUpdatedPublicCards;}

    /**
     * Request the grid of PlayerTable cards played as a matrix.
     *
     * @return the spreadsheet of placed cards as an int [ ] [ ]
     */
    public static int[][] requestGrid(){return lastUpdatedGrid;}

    /**
     * Request player score including the list of resources present on his Player's Board.
     *
     * @return the OldPoints array of ints
     */
    public static int[] requestPlayerScore(){ return lastUpdatedScore;}

    /**
     * Request player hand.
     *
     * @return the list of a Player's private Cards as an int [ ]
     */
    public static int[] requestPlayerHand() { return lastUpdatedHand;}


    /**
     * Flip card.
     *
     * @param position the position of the card to be fliped
     */
//Modifiers
    public static void FlipCard_inPos(int position)
    {
        if(!Client_Game.getCurrentScene().equals("Play")){return;}

        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Flip," +username +","+ position);}
        else{ try { UpdateObject.RMI_Flip(position); }catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }
    //Server_IO.Flip(position, assignedNumber); requestUpdate();}

    /**
     * Draw a card at the end of the turn.
     *
     * @param position the position of the card to draw
     */
    public static void DrawCard(int position)
    //{Server_IO.DrawCard(position, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals("Draw")){return;}

        System.out.println("Requesting Card Draw at position "+position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("Draw," +username +","+ position);}
        else{try {UpdateObject.RMI_DrawCard(position);} catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Place starting card.
     *
     * @param selectedCard the randomly sorted starting card to place
     */
    public static void PlaceStartingCard(int selectedCard)
    //{ Server_IO.PlaceStartingCard(selectedCard, assignedNumber);  requestUpdate();}
    {
        if(!Client_Game.getCurrentScene().equals("Play")){return;}

        System.out.println("PlaceStartingCard," +username +","+ selectedCard);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("PlaceStartingCard," +username +","+ selectedCard);}
        else{try { UpdateObject.RMI_PlaceStartingCard(selectedCard);}catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Choose persnal goal card from the two randomly sorted.
     *
     * @param position the chosen card (either in position 3 or 5)
     */
    public static void ChooseGoalCard(int position) {

        if(!Client_Game.getCurrentScene().equals("Choose_Goal")){return;}
        if(position != 3 && position != 5){return;}

        System.out.println("ChooseGoalCard," +username +","+ position);
        if(ClientConstants.getSocket()) {GameClient.listenForResponse("ChooseGoalCard," +username +","+ position);}
        else{try { UpdateObject.RMI_ChooseGoalCard(position);}catch (RemoteException e){e.printStackTrace();}}

        if(ClientConstants.getGUI()){ requestUpdate(); }
    }

    /**
     * Play card by id and position.
     *
     * @param Row_index     the row index (orizontal coordinates position)
     * @param Columns_index the columns index (vertical coordinates position)
     * @param id            the Card id
     * @return the boolean indicating if the chosen card is playable in a specific position
     */
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

    /**
     * Get new port for socket communication.
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
     * Join game - when a game already exists.
     *
     * @param Username the username of the player that wants to join a game
     * @return the reply message as a string
     */
    public static String JoinGame(String Username)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("JoinPackage,"+Username);}
        else{ if(!RMI_Set){setRMI();}
            try{ return UpdateObject.JoinGame(Username);} catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    /**
     * Reconnect to an existing game through a username.
     *
     * @param Username the username used to reconnect
     * @return the reply message as a string
     */
    public static String Reconnect(String Username)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("AttemptingReconnection,"+Username);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.Reconnect(Username); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

    /**
     * Create games a bew game given a username (the first Player who set up the game) and
     * the desired number of players - the PLayerCount.
     *
     * @param Username    the first client
     * @param playerCount the number of players
     * @return the reply message as a string
     */
    public static String CreateGame(String Username, int playerCount)
    {
        if(ClientConstants.getSocket()) { return GameClient.listenForResponse("CreateGame,"+Username+','+playerCount);}
        else{ if(!RMI_Set){setRMI();} try{ return UpdateObject.CreateGame(username, playerCount); } catch (RemoteException e ){e.printStackTrace();}}
        return "Client_Failed";
    }

}
