package main.java.it.polimi.ingsw.ServerSide;


import  main.java.it.polimi.ingsw.Rmi.ServerRMI;
import  main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import  main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import  main.java.it.polimi.ingsw.ServerSide.MainClasses.UpdatePackage;
import  main.java.it.polimi.ingsw.ServerSide.Table.Player;
import  main.java.it.polimi.ingsw.ServerSide.Table.Table;
import  main.java.it.polimi.ingsw.ServerSide.UpdateClasses.TableManager;
import  main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The Server IO class serves the communication between Server and Client.
 * For complete implementations of the functions instead of the messages, go
 * to UpdateClasses.TableManager.
 */
public class Server_IO {

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
     *
     * @param position the position of the card to flip
     * @param username the username of the player making the action
     */

    public static void Flip(int position, String username){
        Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPlayerByUsername(username) .FlipCard(position);}

    /**
     * Plays a card by using the index for the final position.
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
     *
     * @param position the position of the Deck to draw from
     * @param username the username of the player requesting the action
     */
    public static void DrawCard(int position, String username){ Table table = Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getRelatedTable();
        table.DrawCard(position, username);  }

    /**
     * Places the starting card (flipped or not flipped) as the first card on the Player's board.
     *
     * @param selectedCard the selected starting card
     * @param username     the username of the player requesting the action
     */
    public static void PlaceStartingCard(int selectedCard, String username){
        Game game = Objects.requireNonNull(MultipleGameManager.getGameInstance(username));
        TableManager.PlaceStartingCard(selectedCard, game ,username); }

    /**
     * Choose goal card from the once displayed in position 3 and 5.
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
     *
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

        StringBuilder GameBoard= new StringBuilder();
        int[][] GameBoard_ID = gameInstance.getRelatedTable().getOccupiedSpaces()[gameInstance.getPlayerNumber(username)];

        for(int RowIndex = 0; RowIndex< ServerConstants.getNumOfRows(); RowIndex++){
            GameBoard.append(Arrays.toString(GameBoard_ID[RowIndex])).append(":");  }
        GameBoard.append(";");

        UpdatedGame.append(GameBoard);


        for(Player player : gameInstance.getPlayers()){ UpdatedGame.append(player.getScoreBoard()[0]).append(",");}


        return UpdatedGame.toString();
    }

    private static int getNewPort(String username)
    { return  Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPort();  }


    /**
     * The implementation of the Server rmi. Further documentations is found in ServerRMI interface.
     */
    public static class ServerRMI_impl extends UnicastRemoteObject implements ServerRMI
    {

        private int RMI_PlayerCount; private String username; private int RMI_CurrentTurn;
        private UpdatePackage RMI_UpdatePackage; private Game game;

        /**
         * Instantiates a new Server rmi.
         *
         * @throws RemoteException in case any errors occur
         */
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
        public void RMI_Flip(int position) throws RemoteException{
            Server_IO.Flip(position, this.username);}
        public void RMI_DrawCard(int position) throws RemoteException{
            Server_IO.DrawCard(position, this.username); this.game.changePlayerTurn();}
        public boolean RMI_PlayCardByIndex(int rowIndex, int columnsIndex, int id) throws RemoteException{
            return Server_IO.PlayCardByIndex(rowIndex, columnsIndex, id, this.username);}
        public void RMI_ChooseGoalCard(int position){ ChooseGoalCard(position, this.username);}
        public void RMI_PlaceStartingCard(int selectedCard) throws RemoteException{
            Server_IO.PlaceStartingCard(selectedCard, this.username); }




        public String JoinGame(String username) throws RemoteException
        {
            if(MultipleGameManager.JoinGame(username)){ this.game = MultipleGameManager.getGameInstance(username);
                return "Connection attempt was successful"; }
            return "Connection failed";
        }
        public String Reconnect(String username, int port) throws RemoteException
        {
            if(MultipleGameManager.Reconnect(username, port) != null){ this.game = MultipleGameManager.getGameInstance(username);
                return "Reconnecting to previous game"; }
            return "Reconnection attempt failed: Username not found";
        }

        public String CreateGame(String username, int playerCount) throws RemoteException
        {
            if(MultipleGameManager.CreateGame(username, playerCount)){ this.game = MultipleGameManager.getGameInstance(username);
                return "Joining new Game"; }
            return "Creation attempt failed: Username Already present in Server list";
        }
        public int RMI_getNewPort(String username) throws RemoteException{  return getNewPort(username)  ;   }

        public void update(String username) throws RemoteException {

            System.out.println("Updating RMI at "+username);
            this.username = username;
            this.RMI_CurrentTurn = this.game.getCurrentPlayerTurn();
            this.RMI_PlayerCount = this.game.getPlayerCount();
            this.RMI_UpdatePackage = this.game.sendUpdatePackage(username);

        }

        public boolean GameStarted(String username){ this.game = MultipleGameManager.getGameInstance(username);
            assert this.game != null;
            return (this.game.isGameStarted());}

        public boolean isTurn(String username) throws RemoteException{return this.game.getCurrentPlayerTurn() == this.game.getPlayerNumber(username);}

        public String RMI_getUsernames() throws RemoteException{ return getUsernames(this.game);}

    }


    //OnlyServerSide

    /**
     * Send grid dimensions.
     *
     * @return the dimensions as an int [ ]
     */
    public static int[] sendGridDimensions(){ return new int[]{20, 10};}


}
