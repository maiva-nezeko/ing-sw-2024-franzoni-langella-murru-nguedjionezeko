package it.polimi.ingsw.ClientSide.Cards;

import it.polimi.ingsw.ClientSide.Cards.Enums.*;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import static java.lang.Math.abs;

/**
 * All the different Cards listed present in the Game, forming the Deck.
 * Corners are an int array for each Card going from Left Upper Corner, to Right Upper Corner,
 * then Left Down Corner and Finally Right Down Corner. Each card is also associated with enums
 * if required:
 *  GoalStates for GoalCards
 *  PlayCondition and PointCondition for GoldenCards
 *  StartingPoints for StartingCards
 *  @see ClientCard
 */
public class Deck {

    private static final String RedImagePath = "/src/main/resources/Cards/RedResources-1.png";
    private static final String Red_GoldImagePath = "/src/main/resources/Cards/RedGolden-1.png";

    private static final String BlueImagePath = "/src/main/resources/Cards/BlueResources-1.png";
    private static final String Blue_GoldImagePath = "/src/main/resources/Cards/BlueGolden-1.png";

    private static final String GreenImagePath = "/src/main/resources/Cards/GreenResources-1.png";
    private static final String Green_GoldImagePath = "/src/main/resources/Cards/GreenGolden-1.png";

    private static final String PurpleImagePath = "/src/main/resources/Cards/PurpleResources-1.png";
    private static final String Purple_GoldImagePath =  "/src/main/resources/Cards/PurpleGolden-1.png";

    private static final String GoalCards_ImagePath = "/src/main/resources/Cards/GoalDeck-1.png";
    private static final String StartingCards_ImagePath = "/src/main/resources/Cards/Front-Back-Start-2.png";


    /**
     * Complete Resource and Golden Cards list as ClientCards.
     */
    private static final ClientCard[] Deck = {
            //RED CARDS
            new ClientCard(RedImagePath, new int[]{2,1,2,0}, null,null,null,null,null, 1),
            new ClientCard(RedImagePath, new int[]{2,2,0,1}, null,null,null,null,null, 2), //check
            new ClientCard(RedImagePath, new int[]{1,0,2,2}, null,null,null,null,null, 3),
            new ClientCard(RedImagePath, new int[]{0,2,1,2}, null,null,null,null,null,4), //check
            new ClientCard(RedImagePath, new int[]{0,6,4,2}, null,null,null,null,null,5),
            new ClientCard(RedImagePath, new int[]{7,2,0,3}, null,null,null,null,null,6), //check
            new ClientCard(RedImagePath, new int[]{2,5,8,0}, null,null,null,null,null,7),
            new ClientCard(RedImagePath, new int[]{1,2,1,0}, null,null,null,null,null,8), //check
            new ClientCard(RedImagePath, new int[]{2,0,1,1}, null,null,null,null,null,9),
            new ClientCard(RedImagePath, new int[]{0,1,2,1}, null,null,null,null,null,10), //check

            //BLUE
            new ClientCard(BlueImagePath, new int[]{3,3,1,0}, null,null,null,null,null,11),
            new ClientCard(BlueImagePath, new int[]{0,1,3,3}, null,null,null,null,null,12), //check
            new ClientCard(BlueImagePath, new int[]{3,0,3,1}, null,null,null,null,null,13),
            new ClientCard(BlueImagePath, new int[]{1,3,0,3}, null,null,null,null,null,14),//check
            new ClientCard(BlueImagePath, new int[]{0,5,7,3}, null,null,null,null,null,15),
            new ClientCard(BlueImagePath, new int[]{4,3,0,8}, null,null,null,null,null,16),//check
            new ClientCard(BlueImagePath, new int[]{6,0,3,2}, null,null,null,null,null,17),
            new ClientCard(BlueImagePath, new int[]{0,1,3,1}, null,null,null,null,null,18),//check
            new ClientCard(BlueImagePath, new int[]{1,0,1,3}, null,null,null,null,null,19),
            new ClientCard(BlueImagePath, new int[]{1,3,1,0}, null,null,null,null,null,20),//check

            //GREEN
            new ClientCard(GreenImagePath, new int[]{4,1,4,0}, null,null,null,null,null,21),
            new ClientCard(GreenImagePath, new int[]{4,4,0,1}, null,null,null,null,null,22),
            new ClientCard(GreenImagePath, new int[]{1,0,4,4}, null,null,null,null,null,23),
            new ClientCard(GreenImagePath,new int[]{0,1,4,4}, null,null,null,null,null,24),
            new ClientCard(GreenImagePath, new int[]{0,5,6,4}, null,null,null,null,null,25),
            new ClientCard(GreenImagePath, new int[]{2,4,0,7}, null,null,null,null,null,26),
            new ClientCard(GreenImagePath, new int[]{8,0,4,3}, null,null,null,null,null,27),
            new ClientCard(GreenImagePath, new int[]{1,1,4,0}, null,null,null,null,null,28),
            new ClientCard(GreenImagePath, new int[]{1,1,0,4}, null,null,null,null,null,29),
            new ClientCard(GreenImagePath, new int[]{0,4,1,1}, null,null,null,null,null,30),

            //PURPLE
            new ClientCard(PurpleImagePath, new int[]{5,5,1,0}, null,null,null,null,null,31),
            new ClientCard(PurpleImagePath, new int[]{0,1,5,5}, null,null,null,null,null,32),//checked
            new ClientCard(PurpleImagePath, new int[]{5,0,5,1}, null,null,null,null,null,33),
            new ClientCard(PurpleImagePath, new int[]{1,5,0,5}, null,null,null,null,null,34),//checked
            new ClientCard(PurpleImagePath, new int[]{0,6,3,5}, null,null,null,null,null,35),
            new ClientCard(PurpleImagePath, new int[]{8,5,0,2}, null,null,null,null,null,36),//checked
            new ClientCard(PurpleImagePath, new int[]{5,4,7,0}, null,null,null,null,null,37),
            new ClientCard(PurpleImagePath, new int[]{5,0,1,1}, null,null,null,null,null,38),//checked
            new ClientCard(PurpleImagePath, new int[]{1,1,0,5}, null,null,null,null,null,39),
            new ClientCard(PurpleImagePath, new int[]{0,5,1,1}, null,null,null,null,null,40),//checked

            // RED GOLDEN
            new ClientCard(Red_GoldImagePath, new int[]{0,1,1,6},null, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_BLUE, null,null,41),
            new ClientCard(Red_GoldImagePath, new int[]{1,7,0,1},null, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_GREEN,null,null,42), //check
            new ClientCard(Red_GoldImagePath, new int[]{8,1,1,0},null, PointCondition.ONE_FOR_PAPER, PlayCondition.THREE_SAME_ONE_PURPLE,null,null,43),
            new ClientCard(Red_GoldImagePath, new int[]{1,1,0,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE,null,null,44),//check
            new ClientCard(Red_GoldImagePath, new int[]{1,1,1,0},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN,null,null,45),
            new ClientCard(Red_GoldImagePath, new int[]{0,1,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE,null,null,46),//check
            new ClientCard(Red_GoldImagePath, new int[]{1,0,7,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,47),
            new ClientCard(Red_GoldImagePath, new int[]{6,1,0,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,48),//check
            new ClientCard(Red_GoldImagePath, new int[]{8,0,1,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,49),
            new ClientCard(Red_GoldImagePath, new int[]{1,0,1,0},null, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME,null,null,50),//check

            //BLUE GOLDEN
            new ClientCard(Blue_GoldImagePath, new int[]{7,1,1,0},null, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_PURPLE,null,null,51),
            new ClientCard(Blue_GoldImagePath, new int[]{0,1,1,8},null, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_GREEN,null,null,52),  //check
            new ClientCard(Blue_GoldImagePath, new int[]{1,0,6,1},null, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_RED,null,null,53),
            new ClientCard(Blue_GoldImagePath, new int[]{1,1,0,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE,null,null,54),//check
            new ClientCard(Blue_GoldImagePath, new int[]{1,0,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED,null,null,55),
            new ClientCard(Blue_GoldImagePath, new int[]{0,1,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN,null,null,56),//check
            new ClientCard(Blue_GoldImagePath, new int[]{1,0,8,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,57),
            new ClientCard(Blue_GoldImagePath, new int[]{1,7,0,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,58),//check
            new ClientCard(Blue_GoldImagePath, new int[]{0,1,0,6},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,59),
            new ClientCard(Blue_GoldImagePath, new int[]{0,1,0,1},null, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME,null,null,60),//check

            //GREEN GOLDEN
            new ClientCard(Green_GoldImagePath, new int[]{6,1,1,0},null, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_PURPLE, null,null,61),
            new ClientCard(Green_GoldImagePath, new int[]{1,8,0,1},null, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_RED, null,null,62),
            new ClientCard(Green_GoldImagePath, new int[]{1,0,7,1},null, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_BLUE, null,null,63),
            new ClientCard(Green_GoldImagePath, new int[]{0,1,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE, null,null,64),
            new ClientCard(Green_GoldImagePath, new int[]{1,1,1,0},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE, null,null,65),
            new ClientCard(Green_GoldImagePath, new int[]{1,0,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED, null,null,66),
            new ClientCard(Green_GoldImagePath, new int[]{1,0,6,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME, null,null,67),
            new ClientCard(Green_GoldImagePath, new int[]{8,1,0,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME, null,null,68),
            new ClientCard(Green_GoldImagePath, new int[]{0,7,0,1},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME, null,null,69),
            new ClientCard(Green_GoldImagePath, new int[]{1,1,0,0},null, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME, null,null,70),

            //PURPLE GOLDEN
            new ClientCard(Purple_GoldImagePath, new int[]{1,6,0,1},null, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_GREEN,null,null,71),
            new ClientCard(Purple_GoldImagePath, new int[]{1, 0,8,1},null, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_BLUE,null,null,72),
            new ClientCard(Purple_GoldImagePath, new int[]{0,1,1,7},null, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_RED,null,null,73),
            new ClientCard(Purple_GoldImagePath, new int[]{1, 1,0,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE,null,null,74),
            new ClientCard(Purple_GoldImagePath, new int[]{1,1,1,0},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN,null,null,75),
            new ClientCard(Purple_GoldImagePath, new int[]{1, 0,1,1},null, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED,null,null,76),
            new ClientCard(Purple_GoldImagePath, new int[]{7,0,1,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,77),
            new ClientCard(Purple_GoldImagePath, new int[]{1, 8,0,0},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,78),
            new ClientCard(Purple_GoldImagePath, new int[]{0,0,6,1},null, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME,null,null,79),
            new ClientCard(Purple_GoldImagePath, new int[]{1, 1,0,0},null, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME,null,null,80)};
    //checked


    /**
     * Complete GoalCards list as ClientCards.
     */
    private static final ClientCard[] GoalDeck = {
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_RED_STAIRCASE,100),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_GREEN_STAIRCASE, 101),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_BLUE_STAIRCASE,102),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_BLUE_STAIRCASE, 103),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.THREE_FOR_RED_L,104),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.THREE_FOR_GREEN_L, 105),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.THREE_FOR_BLUE_L,106),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.THREE_FOR_PURPLE_L, 107),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_THREE_FUNGUS, 108),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_THREE_LEAF, 109),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_THREE_WOLF, 110),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_THREE_BUTTERFLY,111),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.THREE_FOR_COMBO, 112),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_TWO_PAPER, 113),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_TWO_SALT, 114),
            new ClientCard(GoalCards_ImagePath, null, null, null, null, null, GoalStates.TWO_FOR_TWO_FEATHERS, 115)
    };


    //Back, Front
    /**
     * Complete Starting Cards list as ClientCards.
     */
    private static final ClientCard[] StartingDeck = {
            //fronts
            new ClientCard(StartingCards_ImagePath, new int[]{2,4,5,3},new int[]{1,4,5,1}, null, null, StartingPoints.STARTING_ONE_PURPLE,null,200),//check
            new ClientCard(StartingCards_ImagePath, new int[]{4,3,2,5},new int[]{3,1,1,2}, null, null, StartingPoints.STARTING_ONE_RED,null,201),//check
            new ClientCard(StartingCards_ImagePath, new int[]{5,3,2,4},new int[]{1,1,1,1}, null, null, StartingPoints.STARTING_ONE_RED_ONE_GREEN,null,202),//check
            new ClientCard(StartingCards_ImagePath, new int[]{4,5,3,2},new int[]{1,1,1,1}, null, null,StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE,null,203),
            new ClientCard(StartingCards_ImagePath, new int[]{5,2,4,3},new int[]{1,1,0,0}, null, null,StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN,null,204),
            new ClientCard(StartingCards_ImagePath, new int[]{2,3,4,5},new int[]{1,1,0,0}, null, null,StartingPoints.STARTING_ONE_GREEN_ONE_BLUE_ONE_RED,null,205),
    };


    /**
     * Gets card by ID.
     *
     * @param ID the unique Card id
     * @return the ClientCard object
     */
    public static ClientCard getCardBYid(int ID)
    {
        if(ID==0){return null;}
        ID = abs(ID);

        if(ID<=80){return Deck[ID-1];}
        else if(ID<200){return GoalDeck[ID-100];}
        return StartingDeck[ID-200];
    }


}
