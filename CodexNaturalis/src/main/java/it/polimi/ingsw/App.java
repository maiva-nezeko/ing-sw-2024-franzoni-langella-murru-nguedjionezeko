package it.polimi.ingsw;

import it.polimi.ingsw.ServerSide.Cards.ResourceCard;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        ResourceCard card = new ResourceCard(new int[]{7, 1, 1, 1}, 15);
        System.out.println(card.getClass() + " " + Arrays.toString(card.getCorners()));

        System.out.println( "Hello World!" );
    }
}
