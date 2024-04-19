package main.java.it.polimi.ingsw.ServerSide.Rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMI extends Remote
    {
        //updaters
        int RMI_getCurrentPlayerCount() throws RemoteException; //{return ServerConstants.getPlayerCount();}
        int RMI_getCurrentPlayerTurn() throws RemoteException;

        int[] RMI_getPublicCardsID() throws RemoteException;
        int[][] RMI_getPlayerBoard() throws RemoteException;
        int[] RMI_getPlayersScores() throws RemoteException;
        int[] RMI_getPlayerHand() throws RemoteException;


        //modifiers
        void RMI_Flip(int position) throws RemoteException; //{CardDealer.FlipCard_inPos(position, assignedNumber);}
        void RMI_DrawCard(int position) throws RemoteException;
        boolean RMI_PlayCardByIndex(int rowIndex, int columnsIndex, int id) throws RemoteException; //{return TableManager.playCardByIndex(rowIndex, columnsIndex, id, AssignedNumber);}
        void RMI_PlaceStartingCard(int selectedCard) throws RemoteException; //{ TableManager.PlaceStartingCard(selectedCard, assignedNumber); }
        void RMI_ChooseGoalCard(int position) throws RemoteException;

        //ServerConnection
        void update(String username) throws RemoteException;
        String JoinGame(String username) throws RemoteException;
        String Reconnect(String username)throws RemoteException;
        String CreateGame(String username, int playerCount) throws RemoteException;
        int RMI_getNewPort(String username) throws RemoteException;

        boolean GameStarted(String username)throws RemoteException;
        boolean isTurn(String username) throws RemoteException;

    }


