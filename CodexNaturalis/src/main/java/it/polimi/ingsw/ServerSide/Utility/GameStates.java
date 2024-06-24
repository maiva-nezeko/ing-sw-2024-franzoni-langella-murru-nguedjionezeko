package main.java.it.polimi.ingsw.ServerSide.Utility;

/**
 * GameStates from Player's Point of View: Joining, Playing, Last Turn, Game Ended and Restored Game.
 */
public enum GameStates {

PLAYER_JOINING,
    PLAYING,
    LAST_TURN,
    GAME_ENDED,
    RESTORED;

public static GameStates advanceState(GameStates old_state){
    return switch (old_state) {
        case PLAYER_JOINING -> PLAYING;
        case PLAYING -> LAST_TURN;
        default -> null;
    };

}
                                                                    }

