package main.java.it.polimi.ingsw.ServerSide.Table;

/**
 * This class represent a player. Each has private attributes.
 */
public class Player {
    /**
     * The array of Private Cards, including the Private Goals.
     */
    private final int[] PrivateCardsID = new int[6];
    /**
     * The ScoreBoard with number of resources listed for each cell;
     * At 0 index the Player's points can be found.
     */
    private int[] ScoreBoard = new int[8];
    private final String username;

    /**
     * Instantiates a new Player.
     *
     * @param username  the unique name of the player
     */

    public Player(String username){
        this.username = username;
    }

    /**
     * Gets this Player's username.
     * @return the username as a string
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Gets this Player's private Cards.
     * @return the private Hand and Goal Cards
     */
    public int[] getPrivateCardsID() {
        return PrivateCardsID;
    }

    /**
     * Sets Card in a given position.
     * @param position  the position to set the Card in
     * @param id        the Card id
     */
    public void setCard(int position, int id){
        if(position<0 || position>5){ return; }
        if(id!=0 && Deck.getCardBYid(id)==null && Deck.getGoalCardByID(id)==null ){ return; }
        this.PrivateCardsID[position]=id;
    }

    /**
     * Gets empty Card slot from private hand Cards.
     * @return the int slot
     */
    public int getEmptySlot(){
        for(int index=0; index<3; index++){
            if(this.PrivateCardsID[index]==0){
                return index;}
        }
        return -1;
    }

    /**
     * Gets ScoreBoard.
     * @return the selected ScoreBoard
     */
    public int[] getScoreBoard() {
        return this.ScoreBoard;
    }

    /**
     * Sets ScoreBoard.
     * @param NewScore an int array
     */
    public void setScoreBoard(int[] NewScore) {
        this.ScoreBoard = NewScore;
    }

    /**
     * It flips a card in your score board, if you want to play it flipped
     * @param position the position of the card in your score board
     *
     * */
    public void FlipCard(int position){
        if(position==3 | position==5){ return; }
        this.PrivateCardsID[position] = -this.PrivateCardsID[position];
    }

    /**
     * Sets the card id to the default value of "0".
     * @param id    the card identifier
     */
    public void consumeCard(int id){

        int index = 0; boolean found = false;

        for(int ID : this.PrivateCardsID){
            if(ID == id){  found = true; break;  }
            index++;
        }

        if(!found){ return; }
        this.setCard(index, 0);
    }
}