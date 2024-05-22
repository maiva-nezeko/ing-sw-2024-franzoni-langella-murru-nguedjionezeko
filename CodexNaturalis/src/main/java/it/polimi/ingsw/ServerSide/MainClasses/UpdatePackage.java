package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import java.util.Arrays;

/**
 * Contains all the information to update correctly the game after every change of the status of the game.
 * @author Edoardo Carlo Murru, Darelle Maiva Nguedjio Nezeko
 */
public class UpdatePackage {

    private int[][] chosenPlayerBoard;
    /**
     * Gets selected Player Board as an int matrix.
     * @return the Player Board
     */
    public int[][] getChosenPlayerBoard(){return this.chosenPlayerBoard;}

    private int chosenPlayerScore;
    /**
     * Gets selected Player Score as an int.
     * @return the score number
     * @deprecated - Client_IO has a 'requestPlayerScore' method
     */
    public int getChosenPlayerScore(){return this.chosenPlayerScore;}

    private int[] chosenPlayerHand;
    /**
     * Gets selected Player Hand as an int array.
     * @return the hand
     */
    public int[] getChosenPlayerHand(){return this.chosenPlayerHand;}

    private int[] publicCards;
    /**
     * Gets Public or Common Cards updated after a turn.
     * @return the Cards as an int array
     */
    public int[] getPublicCards(){return this.publicCards;}

    /**
     * Instantiates a new UpdatePackage that server uses to communicate with the client.
     *
     * @param chosenPlayerBoard     personal player board where the Cards are played
     * @param chosenPlayerHand      personal player hand (resource/gold/personal/goal)
     * @param chosenPlayerScore     personal player score
     * @param publicCards the cards in the table that players can draw
     * */
    UpdatePackage(int[][] chosenPlayerBoard, int chosenPlayerScore, int[] chosenPlayerHand, int[] publicCards)
    {
        this.chosenPlayerBoard = chosenPlayerBoard; this.chosenPlayerScore = chosenPlayerScore;
        this.chosenPlayerHand = chosenPlayerHand; this.publicCards = publicCards;
    }

    /**
     * Formats an array or a list to a string.
     * @return a string
     */
    public String toString()
    {
        String result = "";

        result += chosenPlayerScore + "\n";
        for (String s : Arrays.asList(Arrays.toString(publicCards), Arrays.toString(chosenPlayerHand), Arrays.deepToString(chosenPlayerBoard))) {
            result += s + "\n";
        }

        return result;
    }



}
