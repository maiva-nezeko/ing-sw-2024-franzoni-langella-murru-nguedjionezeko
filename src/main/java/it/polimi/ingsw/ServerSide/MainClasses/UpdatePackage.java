package it.polimi.ingsw.ServerSide.MainClasses;

import java.util.Arrays;

/**
 * contains all the information to update correctly the game after every change of the status of the game */
public class UpdatePackage {

    private int[][] chosenPlayerBoard;
    public int[][] getChosenPlayerBoard(){return this.chosenPlayerBoard;}

    private int chosenPlayerScore;
    public int getChosenPlayerScore(){return this.chosenPlayerScore;}

    private int[] chosenPlayerHand;
    public int[] getChosenPlayerHand(){return this.chosenPlayerHand;}

    private int[] publicCards;
    public int[] getPublicCards(){return this.publicCards;}

    /**
     * instantiates a new UpdatePackage that server uses to communicate with the client
     * @param chosenPlayerBoard personal player board where it plays the cards
     * @param chosenPlayerHand personal player hand where it has its card (resource/gold/personal/goal)
     * @param chosenPlayerScore personal player score
     * @param publicCards the cards in the table that players can draw
     * */

    UpdatePackage(int[][] chosenPlayerBoard, int chosenPlayerScore, int[] chosenPlayerHand, int[] publicCards)
    {
        this.chosenPlayerBoard = chosenPlayerBoard; this.chosenPlayerScore = chosenPlayerScore;
        this.chosenPlayerHand = chosenPlayerHand; this.publicCards = publicCards;
    }

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
