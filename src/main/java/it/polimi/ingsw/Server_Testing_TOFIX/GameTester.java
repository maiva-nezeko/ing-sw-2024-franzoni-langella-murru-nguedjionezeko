package it.polimi.ingsw.Server_Testing_TOFIX;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;

import java.util.ArrayList;

public interface GameTester {

    static boolean runGameTest(){
        boolean failedFlag = false;

        if(uniqueNicknameTest()){failedFlag = true;}
        if(autoStartTest()){failedFlag = true;}
        if(reconnectionTest()){failedFlag = true;}

        return failedFlag;
    }

    private static boolean uniqueNicknameTest()
    {
        boolean failedFlag = false;

        Game game = MultipleGameManager.addGame(2, new ArrayList<>());
        if(!MultipleGameManager.JoinGame("uniqueNicknameTest", game)){failedFlag = true; System.out.println("Can't join game"); }
        if(MultipleGameManager.JoinGame("uniqueNicknameTest")){failedFlag = true; System.out.println("joinedWrongGame");}

        game.end();

        if(failedFlag){ System.out.println("uniqueNicknameTest -> failed"); }
        if(!failedFlag){ System.out.println("uniqueNicknameTest -> passed"); }

        return failedFlag;

    }

    private static boolean autoStartTest()
    {
        boolean failedFlag = false;

        Game game = MultipleGameManager.addGame(2, new ArrayList<>());
        MultipleGameManager.JoinGame("autoStartTest0", game);
        MultipleGameManager.JoinGame("autoStartTest1", game);

        if(!game.isGameStarted()){failedFlag = true;}

        if(failedFlag){ System.out.println("autoStartTest -> failed"); }
        if(!failedFlag){ System.out.println("autoStartTest -> passed"); }

        game.end();
        return failedFlag;

    }

    private static boolean reconnectionTest()
    {
        boolean failedFlag = false;

        Game game = MultipleGameManager.addGame(2, new ArrayList<>());
        MultipleGameManager.JoinGame("reconnectionTest0", game);
        MultipleGameManager.JoinGame("reconnectionTest1", game);

        Game reconnectedGame = MultipleGameManager.Reconnect("reconnectionTest1");

        failedFlag = !reconnectedGame.equals(game);

        if(failedFlag){ System.out.println("reconnectionTest -> failed"); }
        if(!failedFlag){ System.out.println("reconnectionTest -> passed"); }

        game.end();
        return failedFlag;

    }

}
