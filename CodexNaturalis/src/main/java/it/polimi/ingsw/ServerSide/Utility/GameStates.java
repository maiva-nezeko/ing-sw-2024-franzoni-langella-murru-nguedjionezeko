package it.polimi.ingsw.ServerSide.Utility;

public enum GameStates {

PLAYER_ONE_TURN, PLAYER_TWO_TURN, PLAYER_THREE_TURN, PLAYER_FOUR_TURN;

public static GameStates nextTurn(GameStates old_state){
    return switch (old_state) {
        case PLAYER_ONE_TURN -> PLAYER_TWO_TURN;
        case PLAYER_TWO_TURN -> PLAYER_THREE_TURN;
        case PLAYER_THREE_TURN -> PLAYER_FOUR_TURN;
        case PLAYER_FOUR_TURN -> PLAYER_ONE_TURN;
    };

}
                                                                    }

