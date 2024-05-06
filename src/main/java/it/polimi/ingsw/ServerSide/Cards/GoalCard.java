package it.polimi.ingsw.ServerSide.Cards;

import it.polimi.ingsw.ServerSide.Cards.Enums.GoalStates;

/**
 * The abstract Card type Goal Card class.
 */
public class GoalCard extends Card{

    private final GoalStates GoalState;

    /**
     * Instantiates a new Goal card.
     *
     * @param GoalState the specific goal state associated with the card
     * @param ID        the unique card id
     */
    public GoalCard(GoalStates GoalState, int ID)
    {
        super(ID); this.GoalState = GoalState;
    }

    /**
     * Get goal state from Goal States enum.
     *
     * @return the goal condition to fulfill
     */
    public GoalStates getGoalState(){return this.GoalState;}


}
