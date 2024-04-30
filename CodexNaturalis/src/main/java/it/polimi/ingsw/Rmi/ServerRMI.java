package main.java.it.polimi.ingsw.Rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * The interface Server RMI.
 */
public interface ServerRMI extends Remote
{
    /**
     * Get current player count.
     *
     * @return the count as an int
     * @throws RemoteException the remote exception in case any errors occur
     */

    //updaters
    int RMI_getCurrentPlayerCount() throws RemoteException; //{return ServerConstants.getPlayerCount();}

    /**
     * Rmi gets the player who is in charge of the current turn as an int.
     *
     * @return the int associated with a player
     * @throws RemoteException the remote exception in case any errors occur
     */
    int RMI_getCurrentPlayerTurn() throws RemoteException;

    /**
     * Gets public cards as in the cards that are in everyone's view to draw.
     * They are updated at every turn in case a players draws from the public cards
     * instead of from the Resource Deck or the Gold Deck
     *
     * @return the changing list of public cards as an int [ ]
     * @throws RemoteException the remote exception in case any errors occur
     */
    int[] RMI_getPublicCardsID() throws RemoteException;

    /**
     * Gets player board as in the place where each player can play their cards.
     *
     * @return the player board as an int [ ] [ ]
     * @throws RemoteException the remote exception in case any errors occur
     */
    int[][] RMI_getPlayerBoard() throws RemoteException;

    /**
     * Gets players scores updated to the last card played, included the single player's
     * PointBoard in the first index [0].
     *
     * @return the OldPoints of all resources present in the player's board as an int [ ];
     * @throws RemoteException the remote exception in case any errors occur
     */
    int[] RMI_getPlayersScores() throws RemoteException;

    /**
     * Gets player hand as in the three private cards each player can choose to play.
     *
     * @return the hand as int [ ]
     * @throws RemoteException the remote exception in case any errors occur
     */
    int[] RMI_getPlayerHand() throws RemoteException;


    /**
     * Flips the card in a given position.
     *
     * @param position the position of the card to be flipped.
     * @throws RemoteException the remote exception in case any errors occur
     */

    //modifiers
    void RMI_Flip(int position) throws RemoteException; //{CardDealer.FlipCard_inPos(position, assignedNumber);}

    /**
     * Draws a new card.
     *
     * @param position the position to draw from
     * @throws RemoteException the remote exception in case any errors occur
     */
    void RMI_DrawCard(int position) throws RemoteException;

    /**
     * Plays card by index.
     *
     * @param rowIndex     the number indicating the row index
     * @param columnsIndex the number indicating the columns index
     * @param id           the unique card id
     * @return the value as true or false - a boolean
     * @throws RemoteException the remote exception in case any errors occur
     */
    boolean RMI_PlayCardByIndex(int rowIndex, int columnsIndex, int id) throws RemoteException; //{return TableManager.playCardByIndex(rowIndex, columnsIndex, id, AssignedNumber);}

    /**
     * Place starting card.
     *
     * @param selectedCard the selected starting card
     * @throws RemoteException the remote exception in case any errors occur
     */
    void RMI_PlaceStartingCard(int selectedCard) throws RemoteException; //{ TableManager.PlaceStartingCard(selectedCard, assignedNumber); }

    /**
     * Choose goal card.
     *
     * @param position the position chosen (between 3 and 5)
     * @throws RemoteException the remote exception in case any errors occur
     */
    void RMI_ChooseGoalCard(int position) throws RemoteException;

    /**
     * Calls for Update.
     *
     * @param username the username of the client to update
     * @throws RemoteException the remote exception in case any errors occur
     */

    //ServerConnection
    void update(String username) throws RemoteException;

    /**
     * Join game.
     *
     * @param username the username of the player that wants to join
     * @return the response message to display as a string
     * @throws RemoteException the remote exception in case any errors occur
     */
    String JoinGame(String username) throws RemoteException;

    /**
     * Reconnect to an existing game.
     *
     * @param username the username used to reconnect
     * @return the response message as a string
     * @throws RemoteException the remote exception in case any errors occur
     */
    String Reconnect(String username)throws RemoteException;

    /**
     * Creates new game in case no games exist in the server.
     *
     * @param username    the username used to create and join the game
     * @param playerCount the desired player count
     * @return the response message as a string
     * @throws RemoteException the remote exception in case any errors occur
     */
    String CreateGame(String username, int playerCount) throws RemoteException;

    /**
     * Get new port for a game and a client to communicate .
     *
     * @param username the player's username
     * @return the port as an int
     * @throws RemoteException the remote exception in case any errors occur
     */
    int RMI_getNewPort(String username) throws RemoteException;

    /**
     * Indicates whether a game has or hasn't started for a certain client as we always act
     * as all 4 players are present, but we skip the 3rd and/or 4th turn if only 2 or 3 players
     * are actually present as set in the current Playercount
     *
     * @param username the username to question
     * @return the true or false with a boolean
     * @throws RemoteException the remote exception in case any errors occur
     */
    boolean GameStarted(String username)throws RemoteException;

    /**
     * Determines whether it's a given player's turn.
     *
     * @param username the username to question
     * @return the true a or false with a boolean
     * @throws RemoteException the remote exception in case any errors occur
     */
    boolean isTurn(String username) throws RemoteException;

}