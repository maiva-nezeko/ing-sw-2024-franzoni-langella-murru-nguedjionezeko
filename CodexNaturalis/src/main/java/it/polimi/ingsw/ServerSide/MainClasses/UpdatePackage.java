package main.java.it.polimi.ingsw.ServerSide.MainClasses;

import java.util.Arrays;

public class UpdatePackage {

    private int[][] chosenPlayerBoard;
    public int[][] getChosenPlayerBoard(){return this.chosenPlayerBoard;}

    private int chosenPlayerScore;
    public int getChosenPlayerScore(){return this.chosenPlayerScore;}

    private int[] chosenPlayerHand;
    public int[] getChosenPlayerHand(){return this.chosenPlayerHand;}

    private int[] publicCards;
    public int[] getPublicCards(){return this.publicCards;}



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
