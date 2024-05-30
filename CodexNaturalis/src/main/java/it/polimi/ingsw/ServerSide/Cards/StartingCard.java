package it.polimi.ingsw.ServerSide.Cards;

import it.polimi.ingsw.ServerSide.Cards.Enums.StartingPoints;

/**
 * A PlayableCard type Starting card, the first card each Player puts on the board.
 */
public class StartingCard extends PlayableCard {
    private final int[] AlternateCorners;
    private final StartingPoints SP;

    /**
     * Instantiates a new Starting card.
     *
     * @param Corners            the corners when the card is not flipped
     * @param id                 the unique id
     * @param AlternativeCorners the corners when the card is flipped
     * @param SP                 the starting resources placed in the center associated to the starting
     *                           card only if it's flipped; they're listed in the enum StartingPoints
     */
    public StartingCard(int[] Corners, int id, int[] AlternativeCorners, StartingPoints SP)
    {
        super(Corners, id);
        this.AlternateCorners = AlternativeCorners;
        this.SP = SP;
    }

    /*
     * Starting Cards have a resource on all the corners if they're not Flipped,
     * and a unique set of resources - listed in AlternateCorners - when they are.
     */
    public int[] getCorners()
    {
        if(this.isFlipped){return this.AlternateCorners;}
        return this.Corners;
    }

    /* Starting Cards are all different and unique so the points for the resources they entail
     *  are given and not Calculated when they are flipped; on the contrary when they aren't we
     * can look at the corners */
    public int[] addPoints(int[] OldPoints)
    {
        for(int Value: getCorners()){ if(Value>1){OldPoints[Value-1]++;}}

        if(this.isFlipped){
            switch (this.SP) {
                case STARTING_ONE_PURPLE: OldPoints[4]++; break;
                case STARTING_ONE_RED : OldPoints[1]++; break;
                case STARTING_ONE_RED_ONE_GREEN : OldPoints[1]++; OldPoints[3]++; break;
                case STARTING_ONE_BLUE_ONE_PURPLE : OldPoints[2]++; OldPoints[4]++; break;
                case STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN : OldPoints[2]++; OldPoints[3]++; OldPoints[4]++; break;
                case STARTING_ONE_GREEN_ONE_BLUE_ONE_RED: OldPoints[1]++; OldPoints[2]++; OldPoints[3]++; break;
            }}

        return OldPoints;
    }
}
