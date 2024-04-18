package it.polimi.ingsw.ServerSide.Cards;

import it.polimi.ingsw.ServerSide.Cards.Enums.GoalStates;
public class GoalCard extends Card{

    private final GoalStates GoalState;
    public GoalCard(GoalStates GoalState, int ID)
    {
        super(ID); this.GoalState = GoalState;
    }

    public GoalStates getGoalState(){return this.GoalState;}


}
