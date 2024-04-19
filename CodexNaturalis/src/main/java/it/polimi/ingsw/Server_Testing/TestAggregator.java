package main.java.it.polimi.ingsw.Server_Testing;

public interface TestAggregator {

    static void RunTest()
    {
        boolean failedFlag=false;

        if(CardsTester.RunCardTest()){failedFlag=true;}
        if(PersistenceTest.runPersistenceTest()){failedFlag = true;}
        if(GameTester.runGameTest()){failedFlag = true;}


        if(failedFlag){System.out.println("Test Failed");}
    }
}

