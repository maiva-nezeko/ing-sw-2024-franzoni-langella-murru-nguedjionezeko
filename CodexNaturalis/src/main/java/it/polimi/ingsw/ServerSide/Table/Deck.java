package it.polimi.ingsw.Table;


import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Cards.Enums.GoalStates;
import it.polimi.ingsw.Cards.Enums.PlayCondition;
import it.polimi.ingsw.Cards.Enums.PointCondition;
import it.polimi.ingsw.Cards.Enums.StartingPoints;

public class Deck {

    //ColorCards( Background, Cards1-10)


    //0empty, 1white 2fungus 3wolf, 4leaf, 5butterfly, 6feather, 7salt, 8paper
    //colors 0red, 1blue, 2green, 3purple, 4objective, 5-11Starting


    private static final ResourceCard[] Deck = {
            //RED CARDS
            new ResourceCard(new int[]{2,1,2,0}, 0), new ResourceCard(new int[]{2,2,0,1}, 1), //check
            new ResourceCard(new int[]{1,0,2,2}, 2), new ResourceCard(new int[]{0,2,1,2}, 3), //check
            new ResourceCard(new int[]{0,6,4,2}, 4), new ResourceCard(new int[]{7,2,0,3}, 5), //check
            new ResourceCard(new int[]{2,5,8,0}, 6), new ResourceCard(new int[]{1,2,1,0}, 7), //check
            new ResourceCard(new int[]{2,0,1,1}, 8), new ResourceCard(new int[]{0,1,2,1}, 9), //check

            //BLUE
            new ResourceCard(new int[]{3,3,1,0}, 10), new ResourceCard(new int[]{0,1,3,3}, 11), //check
            new ResourceCard(new int[]{3,0,3,1}, 12), new ResourceCard(new int[]{1,3,0,3}, 13),//check
            new ResourceCard(new int[]{0,5,7,3}, 14), new ResourceCard(new int[]{4,3,0,8}, 15),//check
            new ResourceCard(new int[]{6,0,3,2}, 16), new ResourceCard(new int[]{0,1,3,1}, 17),//check
            new ResourceCard(new int[]{1,0,1,3}, 18), new ResourceCard(new int[]{1,3,1,0}, 19),//check

            //GREEN
            new ResourceCard(new int[]{4,1,4,0}, 20), new ResourceCard(new int[]{4,4,0,1}, 21),
            new ResourceCard(new int[]{1,0,4,4}, 22), new ResourceCard(new int[]{0,1,4,4}, 23),
            new ResourceCard(new int[]{0,5,6,4}, 24), new ResourceCard(new int[]{2,4,0,7}, 25),
            new ResourceCard(new int[]{8,0,4,3}, 26), new ResourceCard(new int[]{1,1,4,0}, 27),
            new ResourceCard(new int[]{1,1,0,4}, 28), new ResourceCard(new int[]{0,4,1,1}, 29),

            //PURPLE
            new ResourceCard(new int[]{5,5,1,0}, 30), new ResourceCard(new int[]{0,1,5,5}, 31),//checked
            new ResourceCard(new int[]{5,0,5,1}, 32), new ResourceCard(new int[]{1,5,0,5}, 33),//checked
            new ResourceCard(new int[]{0,6,3,5}, 34), new ResourceCard(new int[]{8,5,0,2}, 35),//checked
            new ResourceCard(new int[]{5,4,7,0}, 36), new ResourceCard(new int[]{5,0,1,1}, 37),//checked
            new ResourceCard(new int[]{1,1,0,5}, 38), new ResourceCard(new int[]{0,5,1,1}, 39),//checked

            // RED GOLDEN
            new GoldCard(new int[]{0,1,1,6},40, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_BLUE),      new GoldCard(new int[]{1,7,0,1},41, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_GREEN), //check
            new GoldCard(new int[]{8,1,1,0},42, PointCondition.ONE_FOR_PAPER, PlayCondition.THREE_SAME_ONE_PURPLE),    new GoldCard(new int[]{1,1,0,1},43, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),//check
            new GoldCard(new int[]{1,1,1,0},44, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),    new GoldCard(new int[]{0,1,1,1},45, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),//check
            new GoldCard(new int[]{1,0,7,0},46, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{6,1,0,0},47, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),//check
            new GoldCard(new int[]{8,0,1,0},48, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,0,1,0},49, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),//check

            //BLUE GOLDEN
            new GoldCard(new int[]{7,1,1,0},50, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_PURPLE),       new GoldCard(new int[]{0,1,1,8},51, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_GREEN),  //check
            new GoldCard(new int[]{1,0,6,1},52, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_RED),       new GoldCard(new int[]{1,1,0,1},53, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),//check
            new GoldCard(new int[]{1,0,1,1},54, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),      new GoldCard(new int[]{0,1,1,1},55, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),//check
            new GoldCard(new int[]{1,0,8,0},56, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,7,0,0},57, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),//check
            new GoldCard(new int[]{0,1,0,6},58, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{0,1,0,1},59, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),//check

            //GREEN GOLDEN
            new GoldCard(new int[]{6,1,1,0},60, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_PURPLE),    new GoldCard(new int[]{1,8,0,1},61, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_RED),
            new GoldCard(new int[]{1,0,7,1},62, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_BLUE),         new GoldCard(new int[]{0,1,1,1},63, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_PURPLE),
            new GoldCard(new int[]{1,1,1,0},64, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),     new GoldCard(new int[]{1,0,1,1},65, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),
            new GoldCard(new int[]{1,0,6,0},66, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{8,1,0,0},67, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),
            new GoldCard(new int[]{0,7,0,1},68, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1,1,0,0},69, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME),

            //PURPLE GOLDEN
            new GoldCard(new int[]{1,6,0,1},70, PointCondition.ONE_FOR_FEATHER, PlayCondition.TWO_SAME_ONE_GREEN),     new GoldCard(new int[]{1, 0,8,1},71, PointCondition.ONE_FOR_PAPER, PlayCondition.TWO_SAME_ONE_BLUE),
            new GoldCard(new int[]{0,1,1,7},72, PointCondition.ONE_FOR_SALT, PlayCondition.TWO_SAME_ONE_RED),          new GoldCard(new int[]{1, 1,0,1},73, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_BLUE),
            new GoldCard(new int[]{1,1,1,0},74, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_GREEN),    new GoldCard(new int[]{1, 0,1,1},75, PointCondition.TWO_FOR_CORNER, PlayCondition.THREE_SAME_ONE_RED),
            new GoldCard(new int[]{7,0,1,0},76, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1, 8,0,0},77, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),
            new GoldCard(new int[]{0,0,6,1},78, PointCondition.THREE_POINT_FLAT, PlayCondition.THREE_SAME),            new GoldCard(new int[]{1, 1,0,0},79, PointCondition.FIVE_POINT_FLAT, PlayCondition.FIVE_SAME)};
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


    public static ResourceCard getCardBYid(int ID)
    {
        if(ID<80){return Deck[ID];}
        return StartingDeck[ID-200];
    }

    public static GoalCard getGoalCardByID(int ID){ return GoalDeck[ID-100];}

}
