package main.java.it.polimi.ingsw.ServerSide.Utility;

public enum GameStates {

PLAYER_JOINING, PLAYING, LAST_TURN, GAME_ENDED, RESTORED;

public static GameStates advanceState(GameStates old_state){
    return switch (old_state) {
        case PLAYER_JOINING -> PLAYING;
        case PLAYING -> LAST_TURN;
        default -> null;
    };

}
                                                                    }

