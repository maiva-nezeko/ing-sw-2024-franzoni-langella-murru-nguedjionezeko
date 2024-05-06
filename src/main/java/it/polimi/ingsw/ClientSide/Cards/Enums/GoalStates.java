package it.polimi.ingsw.ClientSide.Cards.Enums;


/**
 * possible type of goal states
 * */
public enum GoalStates {

    /**
     * 2 points for every 2 object, of the indicated type, in the player's board*/
    TWO_FOR_TWO_FEATHERS, TWO_FOR_TWO_SALT, TWO_FOR_TWO_PAPER,

    /**
     * 3 points for every set of the different object in the player's board*/
    THREE_FOR_COMBO,

    /**
     * 2 points for every set of 3 resource object in the player's board */

    TWO_FOR_THREE_FUNGUS, TWO_FOR_THREE_WOLF, TWO_FOR_THREE_LEAF, TWO_FOR_THREE_BUTTERFLY,

    /**
     * 2 points for every group of the same resource cards in the staircase pattern  in the player's board*/
    TWO_FOR_RED_STAIRCASE, TWO_FOR_BLUE_STAIRCASE, TWO_FOR_GREEN_STAIRCASE, TWO_FOR_PURPLE_STAIRCASE,

    /**
     * 3 points for every group of resource cards with the exact color and L pattern as the goal card in the player's board */
    THREE_FOR_RED_L, THREE_FOR_BLUE_L, THREE_FOR_GREEN_L, THREE_FOR_PURPLE_L,

}
