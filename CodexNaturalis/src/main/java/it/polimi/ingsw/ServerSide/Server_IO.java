package main.java.it.polimi.ingsw.ServerSide;


import  main.java.it.polimi.ingsw.ServerSide.Rmi.ServerRMI;
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

public class Server_IO {

    //Modifiers
    public static void Flip(int position, String username){
        Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPlayerByUsername(username) .FlipCard(position);}

    public static boolean PlayCardByIndex(int rowIndex, int columnsIndex, int id, String username) {
       return TableManager.playCardByIndex(rowIndex, columnsIndex, id, username);   }
    public static void DrawCard(int position, String username){ Table table = Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getRelatedTable();
     table.DrawCard(position, username);  }
    public static void PlaceStartingCard(int selectedCard, String username){ Game game = Objects.requireNonNull(MultipleGameManager.getGameInstance(username));
        TableManager.PlaceStartingCard(selectedCard, game.getPlayerNumber(username) ,username); }
    public static void ChooseGoalCard(int position, String username){
        Player player = Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPlayerByUsername(username);
        if(position==3){ player.setCard(5, 0); }
        if(position==5){ player.setCard(3,  player.getPrivateCardsID()[5]); player.setCard(5, 0);}
    }



    static double timePerTurn = (100000000.0); static long previousTime = System.nanoTime();

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




    public static class ServerRMI_impl extends UnicastRemoteObject implements ServerRMI
    {

        private int RMI_PlayerCount; private String username; private int RMI_CurrentTurn;
        private UpdatePackage RMI_UpdatePackage; private Game game;

        protected ServerRMI_impl() throws RemoteException{super();}


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
        public String Reconnect(String username) throws RemoteException
        {
            if(MultipleGameManager.Reconnect(username) != null){ this.game = MultipleGameManager.getGameInstance(username);
                return "Reconnecting to previous game"; }
            return "Reconnection attempt failed: Username not found";
        }

        public String CreateGame(String username, int playerCount) throws RemoteException
        {
            if(MultipleGameManager.CreateGame(username, playerCount)){ this.game = MultipleGameManager.getGameInstance(username);
                return "Joining new Game"; }
            return "Creation attempt failed: Server Error or wrong PlayerCount";
        }
        public int RMI_getNewPort(String username) throws RemoteException{  return getNewPort(username)  ;   }

        public void update(String username) throws RemoteException {

            if(System.nanoTime() == previousTime+timePerTurn){  System.out.println("Turn timer Expired"); previousTime=System.nanoTime();
                game.changePlayerTurn();}


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

    }


    //OnlyServerSide
    public static int[] sendGridDimensions(){ return new int[]{20, 10};}


}
