package it.polimi.ingsw.ClientSide.Cards.Enums;


/**
 * The enumeration of all 16 Goal cards present in the game - ClientSide.
 */
public enum GoalStates {
    //goals
    /*
     * Two points for every two "feathers","salts" or "papers" on the Player's board.
     */
    TWO_FOR_TWO_FEATHERS, TWO_FOR_TWO_SALT, TWO_FOR_TWO_PAPER,
    /*
     * Three points for the combo: one feather one salt one paper on the Player's board.
     */
    THREE_FOR_COMBO,
    /*
     * Two points for every three "fungi","wolves", "leaves" or "butterflies" on the Player's board.
     */
    TWO_FOR_THREE_FUNGUS, TWO_FOR_THREE_WOLF, TWO_FOR_THREE_LEAF, TWO_FOR_THREE_BUTTERFLY,
    /*
     * Two points for every three red cards, blue cards, green cards or purple cards placed diagonally.
     */
    TWO_FOR_RED_STAIRCASE, TWO_FOR_BLUE_STAIRCASE, TWO_FOR_GREEN_STAIRCASE, TWO_FOR_PURPLE_STAIRCASE,
    /*
     * Three points for every L placement red, blue, green or purple goal card.
     */
    THREE_FOR_RED_L, THREE_FOR_BLUE_L, THREE_FOR_GREEN_L, THREE_FOR_PURPLE_L,
}
