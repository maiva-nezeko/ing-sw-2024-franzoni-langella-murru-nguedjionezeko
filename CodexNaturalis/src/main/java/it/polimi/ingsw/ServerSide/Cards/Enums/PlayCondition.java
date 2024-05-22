package main.java.it.polimi.ingsw.ServerSide.Cards.Enums;

/**
 * The enumeration of all possible Play conditions of a Playable Card, grouped
 * by requirements so to avoid listing alle 40 Gold Cards.

 * On top of the corners rule, some Cards cannot be played unless specific
 * resources are present (and visible) on the Player's board.
 */
public enum PlayCondition {

    /*
     * TWO same-as-card-color resources and
     *  one red (fungus) or
     *  one blue (wolf) or
     *  one green (leaf) or
     *  one purple (butterfly) play condition.
     */
    TWO_SAME_ONE_RED,TWO_SAME_ONE_BLUE, TWO_SAME_ONE_GREEN, TWO_SAME_ONE_PURPLE,
    /*
     * THREE same-as-card-color (fungi/wolves/leaves/butterflies) resources play condition.
     */
    THREE_SAME,
    /*
     * THREE same-as-card-color resources and
     *  one red (fungus) or
     *  one blue (wolf) or
     *  one green (leaf) or
     *  one purple (butterfly) play condition.
     */
    THREE_SAME_ONE_RED, THREE_SAME_ONE_BLUE, THREE_SAME_ONE_GREEN, THREE_SAME_ONE_PURPLE,
    /*
     * FIVE same-as-card-color (fungi/wolves/leaves/butterflies) resources play condition.
     */
    FIVE_SAME
// as an unwritten rule of the game, looking at the cards, in cas of n_SAME_ONE_color
// the "n" and the "same" cards can't be the same type of resource
// e.g.: there's no such thing as a 'THREE_RED_ONE_RED' condition, because that would be
    // listed as a FOUR_SAME card!

}
