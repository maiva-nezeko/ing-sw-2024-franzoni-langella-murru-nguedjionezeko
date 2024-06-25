package it.polimi.ingsw.ServerSide;


import  it.polimi.ingsw.Rmi.ServerRMI;
import  it.polimi.ingsw.ServerSide.MainClasses.Game;
import  it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import  it.polimi.ingsw.ServerSide.MainClasses.UpdatePackage;
import  it.polimi.ingsw.ServerSide.Table.Player;
import  it.polimi.ingsw.ServerSide.Table.Table;
import  it.polimi.ingsw.ServerSide.UpdateClasses.TableManager;
import  it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The Server IO class serves the communication between Server and Client.
 * For complete implementations of the functions instead of the messages,
 * @see TableManager
 * @author Edoardo Carlo Murru
 */
public class Server_IO {

    /**
     * Gets usernames for every player and associates them with it unambiguously.
     * @param game  the game requesting the usernames
     * @return the username string
     */
    public static String getUsernames(Game game)
    {
        if(game==null){return "";}

        StringBuilder usernames = new StringBuilder();

        for(Player player : game.getPlayers())
        {
            usernames.append(player.getUsername()).append(",");
        }

        return usernames.toString();
    }

    //Modifiers
    /**
     * Flips the card located in a given position.
     * In fact, calls for the homonymous method in Player.
     *
     * @param position the position of the card to flip
     * @param username the username of the player making the action
     * @see Player#FlipCard(int)
     */
    public static void Flip(int position, String username){
        Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPlayerByUsername(username) .FlipCard(position);}

    /**
     * Plays a card by using the index for the final position.
     * In fact, calls for the homonymous method in TableManager.
     *
     * @param rowIndex     the number indicating the row index
     * @param columnsIndex the number indicating the columns index
     * @param id           the unique card id
     * @param username     the username of the player requesting the action
     * @return the boolean indicating if a Card has been played or not
     */
    public static boolean PlayCardByIndex(int rowIndex, int columnsIndex, int id, String username) {
        return TableManager.playCardByIndex(rowIndex, columnsIndex, id, username);   }

    /**
     * Drawing a new card.
     * In fact, calls for the homonymous method in Table, after getting the Game Instance from MultipleGameManager and
     * the Table for the game. The turn timer is then reset because drawing is the last action in a turn.
     *
     * @param position the position of the Deck to draw from
     * @param username the username of the player requesting the action
     */
    public static void DrawCard(int position, String username){
        Game game = Objects.requireNonNull(MultipleGameManager.getGameInstance(username));
        Table table = game.getRelatedTable();
        table.DrawCard(position, username);
        game.resetTimer();
    }

    /**
     * Places the starting card (flipped or not flipped) as the first card on the Player's board.
     * In fact, calls for the homonymous method in TableManager, after getting the Game Instance from MultipleGameManager.
     *
     * @param selectedCard the selected starting card
     * @param username     the username of the player requesting the action
     */
    public static void PlaceStartingCard(int selectedCard, String username){
        Game game = Objects.requireNonNull(MultipleGameManager.getGameInstance(username));
        TableManager.PlaceStartingCard(selectedCard, game ,username); }

    /**
     * Chooses goal card from the once displayed in position 3 and 5.
     * First, it gets the Game Instance from MultipleGameManager and the Player (required not null) from Game.
     * No matter the choice, the Card will be set in position 3, while position 5 will remain empty.
     *
     * @param position the position in which the chosen card is situated
     * @param username the username of the player requesting the action
     */
    public static void ChooseGoalCard(int position, String username){
        Player player = Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPlayerByUsername(username);
        if(position==3){ player.setCard(5, 0); }
        if(position==5){ player.setCard(3,  player.getPrivateCardsID()[5]); player.setCard(5, 0);}
    }


    /**
     * Socket update string.
     * As per usual, first it makes sure that neither the Game nor the Player are null.
     * @param username the username of the player associated with the update
     * @return the update as a string
     */
    public static String SocketUpdate(String username)
    {
        Game gameInstance = MultipleGameManager.getGameInstance(username);
        if(gameInstance==null){ return "null\n"; }

        Player currentPlayer = gameInstance.getPlayerByUsername(username);
        if(currentPlayer==null){ return "null\n"; }


        StringBuilder UpdatedGame = new StringBuilder();

        UpdatedGame.append(gameInstance.getPlayerCount()).append(";");
        UpdatedGame.append(Arrays.toString(gameInstance.getRelatedTable().getPublicSpacesID())).append(";");
        UpdatedGame.append(Arrays.toString(currentPlayer.getPrivateCardsID())).append(";");



        UpdatedGame.append(getGameBoard(gameInstance, gameInstance.getPlayerNumber(username)));


        for(Player player : gameInstance.getPlayers()){ UpdatedGame.append(player.getScoreBoard()[0]).append(",");}


        return UpdatedGame.toString();
    }

    /**
     * Gets full GameBoard for a given game.
     *
     * @param game          the game requesting a GameBoard
     * @param PlayerNumber  the number of Players
     * @return the GameBoard formatted as a string
     */
    public static String getGameBoard(Game game, int PlayerNumber)
    {
        assert game!=null;

        StringBuilder GameBoard= new StringBuilder();
        int[][] GameBoard_ID = game.getRelatedTable().getOccupiedSpaces()[PlayerNumber];

        for(int RowIndex = 0; RowIndex< ServerConstants.getNumOfRows(); RowIndex++){
            GameBoard.append(Arrays.toString(GameBoard_ID[RowIndex])).append(":");  }
        GameBoard.append(";");

        return GameBoard.toString();

    }

    /**
     * Gets a new port for a chosen player.
     * In fact, it calls for getPort method from Game.
     *
     * @param username  the player identifier
     * @return the port int number
     *
     * @see Game#getPort()
     */
    private static int getNewPort(String username)
    { return  Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPort();  }




    public static boolean ReconnectCheck(String username, int port) {
        boolean drawFlag = false;

        if (MultipleGameManager.Reconnect(username, port) != null) {

            Game game = MultipleGameManager.getGameInstance(username);
            assert game != null;

            int[] hand = game.getPlayerByUsername(username).getPrivateCardsID();

            for (int i = 0; i < 3; i++) {
                if (hand[i] == 0) {
                    game.getRelatedTable().drawRandom(username);
                    drawFlag = true;
                }
            }

            if (drawFlag && game.getCurrentPlayerTurn() == game.getPlayerNumber(username)) {
                game.changePlayerTurn();
            }

            return true;
        }

        return false;
    }





    /**
     * The implementation of the Server rmi. Further documentations is found in ServerRMI interface.
     */
    public static class ServerRMI_impl extends UnicastRemoteObject implements ServerRMI
    {

        private int RMI_PlayerCount; private int RMI_CurrentTurn;
        private UpdatePackage RMI_UpdatePackage; private Game game;


        public ServerRMI_impl() throws RemoteException{super();}


        public int RMI_getCurrentPlayerCount() throws RemoteException{return this.RMI_PlayerCount;}
        public int RMI_getCurrentPlayerTurn() throws RemoteException{return this.RMI_CurrentTurn;}

        public int[] RMI_getPublicCardsID() throws RemoteException
        {return this.RMI_UpdatePackage.getPublicCards();}
        public int[][] RMI_getPlayerBoard() throws RemoteException
        {return this.RMI_UpdatePackage.getChosenPlayerBoard();}

        public int[] RMI_getPlayerHand() throws RemoteException
        {return this.RMI_UpdatePackage.getChosenPlayerHand();}

        public int[] RMI_getPlayersScores() throws RemoteException
        {
            List<Player> players = game.getPlayers();
            int[] Scores = new int[game.getPlayerCount()];

            for(int index=0; index<game.getPlayerCount(); index++)
            { Scores[index] = players.get(index).getScoreBoard()[0]; }

            return Scores;
        }


        //modifiers
        public void RMI_Flip(int position, String username) throws RemoteException{
            Server_IO.Flip(position, username);}
        public void RMI_DrawCard(int position, String username) throws RemoteException{
            Server_IO.DrawCard(position, username);}
        public boolean RMI_PlayCardByIndex(int rowIndex, int columnsIndex, int id, String username) throws RemoteException{
            return Server_IO.PlayCardByIndex(rowIndex, columnsIndex, id, username);}
        public void RMI_ChooseGoalCard(int position, String username){ ChooseGoalCard(position , username);}
        public void RMI_PlaceStartingCard(int selectedCard, String username) throws RemoteException{
            Server_IO.PlaceStartingCard(selectedCard, username); }


        /**
         * Prints 'Join Game' response messages.
         * It calls for the homonymous method in MultipleGameManager; according to the boolean reply to that method,
         * a different message is printed from this method.
         *
         * @param username  the username of the player that wants to join
         * @return a reply message according to a successful or unsuccessful attempt
         * @throws RemoteException the remote exception in case any errors occur
         *
         * @see MultipleGameManager#JoinGame(String)
         */
        public String JoinGame(String username) throws RemoteException
        {
            ServerConstants.printMessageLn(username);
            if(MultipleGameManager.JoinGame(username)){ this.game = MultipleGameManager.getGameInstance(username);
                return "Connection attempt was successful, Joining"; }
            if(MultipleGameManager.getGameInstance(username)!=null)
            {
                return "Connection failed, username already present";
            }
            return "Connection failed";
        }

        /**
         * Prints 'Reconnect' response messages and gets desired Game Instance (if applicable).
         * It calls for the homonymous method in MultipleGameManager; according to the boolean reply to that method,
         * a different message is printed from this method.
         * 
         * @param username  the username used to reconnect
         * @param port      last port assigned before disconnection
         * @return a reply message according to a successful or unsuccessful attempt
         * @throws RemoteException the remote exception in case any errors occur
         * 
         * @see MultipleGameManager#Reconnect(String, int)
         * @see MultipleGameManager#getGameInstance(String) 
         */
        public String Reconnect(String username, int port) throws RemoteException
        {
            if(ReconnectCheck(username, port)){
                this.game = MultipleGameManager.getGameInstance(username);
                return "Reconnecting to previous game";
            }
            return "Reconnection attempt failed: Username not found";
        }

        /**
         * Prints 'Reconnect' response messages and gets a new Game Instance (if applicable).
         * It calls for the homonymous method in MultipleGameManager; according to the boolean reply to that method,
         * a different message is printed from this method.
         * 
         * @param username    the username used to create and join the game
         * @param playerCount the desired player count
         * @return a reply message according to a successful or unsuccessful attempt
         * @throws RemoteException the remote exception in case any errors occur
         */
        public String CreateGame(String username, int playerCount) throws RemoteException
        {
            if(MultipleGameManager.CreateGame(username, playerCount)){ this.game = MultipleGameManager.getGameInstance(username);
                return "Joining new Game"; }
            return "Creation attempt failed: Username Already present in Server list";
        }
        public int RMI_getNewPort(String username) throws RemoteException{  return getNewPort(username)  ;   }

        /**
         * Updates current turn, player count and UpdatePackage for rmi with respective getter methods in Game.
         * @see Game#getCurrentPlayerTurn() 
         * @see Game#getPlayerCount() 
         * @see Game#sendUpdatePackage(String) 
         * 
         * @param username   the username of the client to update
         * @throws RemoteException the remote exception in case any errors occur
         */
        public void update(String username) throws RemoteException {

            ServerConstants.printMessageLn("Updating RMI at "+username);
            this.RMI_CurrentTurn = this.game.getCurrentPlayerTurn();
            this.RMI_PlayerCount = this.game.getPlayerCount();
            this.RMI_UpdatePackage = this.game.sendUpdatePackage(username);

        }

        /**
         * First it gets the Game Instance from MultiplePlayerManager, then checks if the game is null and finally
         * returns the same value from isGameStarted in Game.
         * @see MultipleGameManager#getGameInstance(String) 
         * 
         * @param username  the username to question
         * @return a boolean indicating if the Game has started
         * @see Game#isGameStarted() 
         */
        public boolean GameStarted(String username){ this.game = MultipleGameManager.getGameInstance(username);
            assert this.game != null;
            return (this.game.isGameStarted());}

        /**
         * Gets what Player has the turn currently.
         * First it gets Player's list from game, then it gets the currentPlayerTurn still from Game,
         * and finally it gets the Player's username from Player class.
         *
         * @param username  the username of the Player to question
         * @return the username as a string
         * @throws RemoteException the remote exception in case any errors occur
         */
        public String isTurn(String username) throws RemoteException{return this.game.getPlayers().get(game.getCurrentPlayerTurn()).getUsername();}

        /**
         * Gets usernames for rmi.
         * It calls for the homonymous method in Server_IO;
         * @see Server_IO#getUsernames(Game)
         *
         * @return the usernames list
         * @throws RemoteException the remote exception in case any errors occur
         */
        public String RMI_getUsernames() throws RemoteException{ return getUsernames(this.game);}

        /**
         * Gets usernames for rmi.
         * It first seeks for the Table related to the Game, then points to a particular player's grid calling for 'getOccupiedSpaces'
         * in Table class.
         *
         * @see Game#getRelatedTable()
         * @see Table#getOccupiedSpaces()
         *
         * @return the current Player's grid or PlayBoard or Table
         * @throws RemoteException the remote exception in case any errors occur
         */
        public int[][] RMI_getCurrentPlayerGrid() throws RemoteException{ return this.game.getRelatedTable().getOccupiedSpaces()[this.game.getCurrentPlayerTurn()]; }


        @Override
        public boolean isClosed(int port) throws RemoteException { return MultipleGameManager.getInstanceByPort(port) == null; }
    }


    //OnlyServerSide

    /**
     * Send grid dimensions through a two size array of int;
     * dimension are already fixed.
     *
     * @return the dimensions as an int [ ]
     * @deprecated
     */
    public static int[] sendGridDimensions(){ return new int[]{20, 10};}


}
