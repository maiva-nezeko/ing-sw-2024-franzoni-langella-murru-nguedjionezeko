package main.java.it.polimi.ingsw.ServerSide.Cards.Enums;

/**
 * The enumeration of all the different conditions in which playing a given Gold Card
 * generates points for the Player.
 */
public enum PointCondition {
    /*
     * One point without any specific point condition.
     */
    ONE_POINT_FLAT,
    /*
     * Three points without any specific point condition.
     */
    THREE_POINT_FLAT,
    /*
     * Five points without any specific point condition.
     */
    FIVE_POINT_FLAT,
    /*
     * One point for every visible "feather" in the Player's board condition.
     */
    ONE_FOR_FEATHER,
    /*
     * One point for every visible "salt" in the Player's board condition.
     */
    ONE_FOR_SALT,
    /*
     * One point for every visible "paper" in the Player's board condition.
     */
    ONE_FOR_PAPER,
    /*
     * Two points for every corner the Card will cover condition.
     */
    TWO_FOR_CORNER
}
