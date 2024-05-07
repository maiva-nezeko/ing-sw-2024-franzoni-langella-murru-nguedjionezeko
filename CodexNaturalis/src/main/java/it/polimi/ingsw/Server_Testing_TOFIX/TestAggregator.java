package main.java.it.polimi.ingsw.Server_Testing_TOFIX;

public interface TestAggregator {

    static void RunTest()
    {
        boolean failedFlag=false;

        if(PersistenceTest.runPersistenceTest()){failedFlag = true;}
        if(GameTester.runGameTest()){failedFlag = true;}


        if(failedFlag){System.out.println("Test Failed");}
    }
}

