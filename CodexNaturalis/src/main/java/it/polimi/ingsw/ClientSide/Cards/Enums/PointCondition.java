package it.polimi.ingsw.ClientSide.Cards.Enums;


/**
 * possible type of point condition
 * */
public enum PointCondition {

    /**
     * player achieves points (1-3-5) when this card is played */
    ONE_POINT_FLAT, THREE_POINT_FLAT, FIVE_POINT_FLAT,

    /**
     * player achieves 1 points for every object of the same type as the one on the card.
     * It is considered the situation of player's board when this card is played (included the played card) */
    ONE_FOR_FEATHER, ONE_FOR_SALT, ONE_FOR_PAPER,

    /**
     * player achieves 2 points for every corner covered by this card*/
    TWO_FOR_CORNER
}
