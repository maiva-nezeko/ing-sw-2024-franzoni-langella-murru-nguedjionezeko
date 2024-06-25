package it.polimi.ingsw.ServerSide.Table;

import it.polimi.ingsw.ServerSide.Cards.*;
import it.polimi.ingsw.ServerSide.Cards.Enums.*;

import static java.lang.Math.abs;

/**
 * Every game has decks instantiated:
 * Deck - contains Resource Cards  and Gold Cards,
 * GoalDeck - contains Goal Cards,
 * StartingCard - contains starting cards.
 * Decks are divided according to their use and proprieties.
 * */

public class Deck {

    private static final PlayableCard[] Deck ={
            //red cards
            new ResourceCard(new int[]{2,1,2,0}, 1), new ResourceCard(new int[]{2,2,0,1},2),
            new ResourceCard(new int[]{1,0,2,2}, 3), new ResourceCard(new int[]{0,2,1,2},4),
            new ResourceCard(new int[]{0,6,4,2}, 5), new ResourceCard(new int[]{7,2,0,3},6),
            new ResourceCard(new int[]{2,5,8,0}, 7), new ResourceCard(new int[]{1,2,1,0},8),
            new ResourceCard(new int[]{2,0,1,1}, 9), new ResourceCard(new int[]{0,1,2,1},10),

            //blue cards
            new ResourceCard(new int[]{3,3,1,0},11), new ResourceCard(new int[]{0,1,3,3},12),
            new ResourceCard(new int[]{3,0,3,1},13), new ResourceCard(new int[]{1,3,0,3},14),
            new ResourceCard(new int[]{0,5,7,3},15), new ResourceCard(new int[]{4,3,0,8},16),
            new ResourceCard(new int[]{6,0,3,2},17), new ResourceCard(new int[]{0,1,3,1},18),
            new ResourceCard(new int[]{1,0,1,3},19), new ResourceCard(new int[]{1,3,1,0},20),

            //green cards
            new ResourceCard(new int[]{4,1,4,0}, 21), new ResourceCard(new int[]{4,4,0,1}, 22),
            new ResourceCard(new int[]{1,0,4,4}, 23), new ResourceCard(new int[]{0,1,4,4}, 24),
            new ResourceCard(new int[]{0,5,6,4}, 25), new ResourceCard(new int[]{2,4,0,7}, 26),
            new ResourceCard(new int[]{8,0,4,3}, 27), new ResourceCard(new int[]{1,1,4,0}, 28),
            new ResourceCard(new int[]{1,1,0,4}, 29), new ResourceCard(new int[]{0,4,1,1}, 30),

            //purple cards
            new ResourceCard(new int[]{5,5,1,0}, 31), new ResourceCard(new int[]{0,1,5,5}, 32),//checked
            new ResourceCard(new int[]{5,0,5,1}, 33), new ResourceCard(new int[]{1,5,0,5}, 34),//checked
            new ResourceCard(new int[]{0,6,3,5}, 35), new ResourceCard(new int[]{8,5,0,2}, 36),//checked
            new ResourceCard(new int[]{5,4,7,0}, 37), new ResourceCard(new int[]{5,0,1,1}, 38),//checked
            new ResourceCard(new int[]{1,1,0,5}, 39), new ResourceCard(new int[]{0,5,1,1}, 40),//checked

            // red gold cards
            new GoldCard(new int[]{0,1,1,6},41, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_BLUE),      new GoldCard(new int[]{1,7,0,1},42, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_GREEN), //check
            new GoldCard(new int[]{8,1,1,0},43, PointCondition.ONE_FOR_PAPER, PlayCondition.THREE_SAME_ONE_PURPLE),    new GoldCard(new int[]{1,1,0,1},44, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),//check
            new GoldCard(new int[]{1,1,1,0},45, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),    new GoldCard(new int[]{0,1,1,1},46, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),//check
            new GoldCard(new int[]{1,0,7,0},47, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{6,1,0,0},48, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),//check
            new GoldCard(new int[]{8,0,1,0},49, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,0,1,0},50, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),//check

            //blue gold cards
            new GoldCard(new int[]{7,1,1,0},51, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_PURPLE),       new GoldCard(new int[]{0,1,1,8},52, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_GREEN),  //check
            new GoldCard(new int[]{1,0,6,1},53, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_RED),       new GoldCard(new int[]{1,1,0,1},54, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),//check
            new GoldCard(new int[]{1,0,1,1},55, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),      new GoldCard(new int[]{0,1,1,1},56, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),//check
            new GoldCard(new int[]{1,0,8,0},57, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,7,0,0},58, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),//check
            new GoldCard(new int[]{0,1,0,6},59, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{0,1,0,1},60, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),//check

            //green gold cards
            new GoldCard(new int[]{6,1,1,0},61, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_PURPLE),    new GoldCard(new int[]{1,8,0,1},62, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_RED),
            new GoldCard(new int[]{1,0,7,1},63, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_BLUE),         new GoldCard(new int[]{0,1,1,1},64, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),
            new GoldCard(new int[]{1,1,1,0},65, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),     new GoldCard(new int[]{1,0,1,1},66, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),
            new GoldCard(new int[]{1,0,6,0},67, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{8,1,0,0},68, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),
            new GoldCard(new int[]{0,7,0,1},69, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,1,0,0},70, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),

            //purple gold cards
            new GoldCard(new int[]{1,6,0,1},71, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_GREEN),     new GoldCard(new int[]{1, 0,8,1},72, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_BLUE),
            new GoldCard(new int[]{0,1,1,7},73, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_RED),          new GoldCard(new int[]{1, 1,0,1},74, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),
            new GoldCard(new int[]{1,1,1,0},75, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),    new GoldCard(new int[]{1, 0,1,1},76, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),
            new GoldCard(new int[]{7,0,1,0},77, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1, 8,0,0},78, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),
            new GoldCard(new int[]{0,0,6,1},79, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1, 1,0,0},80, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME)};
    //checked


    private static final GoalCard[] GoalDeck = {
            new GoalCard( GoalStates.TWO_FOR_RED_STAIRCASE,100),           new GoalCard( GoalStates.TWO_FOR_GREEN_STAIRCASE, 101),
            new GoalCard( GoalStates.TWO_FOR_BLUE_STAIRCASE,102),          new GoalCard( GoalStates.TWO_FOR_BLUE_STAIRCASE, 103),
            new GoalCard( GoalStates.THREE_FOR_RED_L,104),                 new GoalCard( GoalStates.THREE_FOR_GREEN_L, 105),
            new GoalCard( GoalStates.THREE_FOR_BLUE_L,106),                new GoalCard( GoalStates.THREE_FOR_PURPLE_L, 107),
            new GoalCard( GoalStates.TWO_FOR_THREE_FUNGUS, 108),           new GoalCard( GoalStates.TWO_FOR_THREE_LEAF, 109),
            new GoalCard( GoalStates.TWO_FOR_THREE_WOLF, 110),             new GoalCard( GoalStates.TWO_FOR_THREE_BUTTERFLY,111),
            new GoalCard( GoalStates.THREE_FOR_COMBO, 112),                new GoalCard( GoalStates.TWO_FOR_TWO_PAPER, 113),
            new GoalCard( GoalStates.TWO_FOR_TWO_SALT, 114),               new GoalCard( GoalStates.TWO_FOR_TWO_FEATHERS, 115)
    };


    //Back, Front
    private static final StartingCard[] StartingDeck = {
            //fronts
            new StartingCard(new int[]{2,4,5,3},200,new int[]{1,4,5,1}, StartingPoints.STARTING_ONE_PURPLE),//check
            new StartingCard(new int[]{4,3,2,5},201,new int[]{3,1,1,2}, StartingPoints.STARTING_ONE_RED),//check
            new StartingCard(new int[]{5,3,2,4},202,new int[]{1,1,1,1}, StartingPoints.STARTING_ONE_RED_ONE_GREEN),//check
            new StartingCard(new int[]{4,5,3,2},203,new int[]{1,1,1,1}, StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE),
            new StartingCard(new int[]{5,2,4,3},204,new int[]{1,1,0,0}, StartingPoints.STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN),
            new StartingCard(new int[]{2,3,4,5},205,new int[]{1,1,0,0}, StartingPoints.STARTING_ONE_GREEN_ONE_BLUE_ONE_RED),
    };


    /**
     * Gets Card by unique Card id.
     * @param ID the id number
     * @return the Card
     */
    public static PlayableCard getCardBYid(int ID)
    {
        ID = abs(ID);

        if( !(ID>0 && ID<=80) && !(ID>=200 && ID<=205)) { return null; }

        if(ID<=80){return Deck[ID-1];}
        return StartingDeck[ID-200];
    }

    /**
     * Gets Goal Card by id.
     * @param ID the unique Card id
     * @return the Goal Card
     */
    public static GoalCard getGoalCardByID(int ID){
        if(ID<100 || ID>115) { return null; }
        return GoalDeck[ID-100];}

}
