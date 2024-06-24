package main.java.it.polimi.ingsw.ClientSide.Cards.Enums;

/**
 * possible type of play condition
 * */
public enum PlayCondition {

    /**
     * player must have 2 same resource objects and 1 different resource object (of the type described in the card) on his board to play this card*/
    TWO_SAME_ONE_RED,TWO_SAME_ONE_BLUE, TWO_SAME_ONE_GREEN, TWO_SAME_ONE_PURPLE,
    /**
     * player must have 3 same resource object and (of the type described in the card) on his board to play this card*/
    THREE_SAME,

    /**
     * player must have 3 same resource object and 1 different (of the type described in the card) on his board to play this card*/
    THREE_SAME_ONE_RED, THREE_SAME_ONE_BLUE, THREE_SAME_ONE_GREEN, THREE_SAME_ONE_PURPLE,

    /**
     * player must have 5 same resource object (of the type described in the card) on his board to play this card*/
    FIVE_SAME

}
