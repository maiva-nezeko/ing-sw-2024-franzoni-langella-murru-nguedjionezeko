package main.java.it.polimi.ingsw.Server_Testing;

import main.java.it.polimi.ingsw.ServerSide.Cards.GoalCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import main.java.it.polimi.ingsw.ServerSide.Table.Deck;
import main.java.it.polimi.ingsw.ServerSide.Table.Table;
import main.java.it.polimi.ingsw.ServerSide.UpdateClasses.TableManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface CardsTester {

    static boolean RunCardTest()
    {
        boolean failedFlag = false;

        if(CardRandomizer_NoDuplicates()){failedFlag=true;}
        if(IsPlayable_test()){failedFlag=true;}
        if(PlayCard_test()){failedFlag=true; }

        if(EndGame_Test()){failedFlag=true;}


        return failedFlag;
    }


    private static boolean CardRandomizer_NoDuplicates() {

        Game game = MultipleGameManager.addGame(1, new ArrayList<>());
        MultipleGameManager.JoinGame("CardRandomizer_NoDuplicates", game);

        String testEvaluation="\nCardRandomizer_NoDuplicates-> ";
        PlayableCard newCard; boolean failedFlag = false;
        Table relatedTable = game.getRelatedTable();

        List<Integer> GoldDeck = new ArrayList<>();
        do{
            newCard = relatedTable.drawResourceCard(true);
            if(newCard!=null){ GoldDeck.add(newCard.getID()); }
        } while (newCard != null);
        if(GoldDeck.size()==40){ testEvaluation+=("GoldDeck passed \t"); }
        else{ testEvaluation+=("GoldDeck failed \t"); failedFlag = true;}


        List<Integer> ResourceDeck = new ArrayList<>();
        do{
            newCard = relatedTable.drawResourceCard(false);
            if(newCard!=null){ ResourceDeck.add(newCard.getID()); }
        } while (newCard != null);
        if(ResourceDeck.size()==40){ testEvaluation+=("ResourceDeck passed \t"); }
        else{ testEvaluation+=("ResourceDeck failed \t"); failedFlag = true;}

        List<Integer> StartingDeck = new ArrayList<>();
        do{
            newCard = relatedTable.drawStartingCard();
            if(newCard!=null){ StartingDeck.add(newCard.getID()); }
        } while (newCard != null);
        if(StartingDeck.size()==6){ testEvaluation+=("StartingDeck passed \t"); }
        else{ testEvaluation+=("StartingDeck failed \t"); failedFlag = true;}

        List<Integer> GoalDeck = new ArrayList<>();
        GoalCard newGoalCard;
        do{
            newGoalCard = relatedTable.drawGoalCard();
            if(newGoalCard!=null){ GoalDeck.add(newGoalCard.getID()); }
        } while (newGoalCard != null);
        if(GoalDeck.size()==16){ testEvaluation+=("GoalDeck passed \t"); }
        else{ testEvaluation+=("GoalDeck failed \t\n"); failedFlag = true;}


        if(failedFlag){
            testEvaluation += "GoldDeck: " + GoldDeck + "\n";
            testEvaluation += "ResourceDeck: " + ResourceDeck + "\n";
            testEvaluation += "StartingDeck: " + StartingDeck + "\n";
            testEvaluation += "GoalDeck: " + GoalDeck + "\n"; }

        System.out.println(testEvaluation);
        game.end();

        return failedFlag;

    }

    private static boolean IsPlayable_test() {
        //TestPlays = CardToPlay, ScoreBoard
        int[][] TestPlays = {{2},{0, 0,0,0,0, 0,0,0}, {71},{0, 15,0,3,4, 0,0,0}, {72},{0, 0,0,0,2, 0,0,0}, {73},{0, 0,0,0,3, 0,0,0},
                {74},{0, 0,0,0,0, 0,0,0}, {79},{0, 15,0,3,5, 0,0,0}, {72},{0, 0,0,0,2, 0,0,0}, {-73},{0, 0,0,0,18, 0,0,0}  };
        boolean[] Results  = {true, true, false, false, false, true, false, true };
        //requires( Results.length == TestPlays.length/2 ;


        boolean failedFlag = false; boolean testFlag = false;
        StringBuilder testEvaluation= new StringBuilder("IsPlayableTest-> ");

        for (int TestIndex=0; TestIndex<TestPlays.length; TestIndex+=2) {

            testFlag = false;

            PlayableCard Card =  Deck.getCardBYid(TestPlays[TestIndex][0]);
            Card.setFlipped(TestPlays[TestIndex][0]<0);

            testFlag = ( Card.isPlayable(TestPlays[TestIndex+1]) != Results[TestIndex/2] );

            if(testFlag){ failedFlag = true; testEvaluation.append("Failed Test: ").append(TestIndex / 2).append(" ");  }


        }

        if(!failedFlag){System.out.println("IsPlayableTest -> Passed");}
        if(failedFlag){System.out.println(testEvaluation);}

        return failedFlag;
    }

    private static boolean PlayCard_test() {

        Game game = MultipleGameManager.addGame(1, new ArrayList<>());
        MultipleGameManager.JoinGame("PlayCard_test", game);

        int[][] TestPlays = {{201, 24, /*TR*/1, 6, /*BL*/ 2}, {-201, 24, /*BR*/3, 6, /*TR*/ 1, 19, /*BL*/ 2}, {203, 27,/*Tl*/0, 8,/*BL*/2}};
        int[][] Results = {{-1}, {1, 2, 3, 2, 0, 0, 1, 0}, {-1}};
        boolean failedFlag = false; boolean cantPlayFlag;

        int StartingRow = ServerConstants.getNumOfRows()/2;
        int StartingColumn = ServerConstants.getNumOfRows()/4;


        StringBuilder testEvaluation= new StringBuilder("TableManager, PlayCard-> ");

        int[][] Corners = {{StartingRow-1, StartingColumn-1}, {StartingRow-1, StartingColumn+1}, {StartingRow+1, StartingColumn-1}, {StartingRow+1, StartingColumn+1}};

        for (int playIndex=0; playIndex<TestPlays.length; playIndex++) {

            if(ServerConstants.getDebug()){System.out.println("Test: "+playIndex);}
            cantPlayFlag = false;
            int[] play = TestPlays[playIndex];
            //play
            for (int index = 0; index < play.length-1; index++) {



                if (index == 0) {  game.getPlayers().get(0).setCard(4, play[index]);
                    TableManager.PlaceStartingCard(play[index], 0, game.getPlayers().get(0).getUsername()); }

                else {  game.getPlayers().get(0).setCard(0, play[index]);
                    cantPlayFlag = !TableManager.playCardByIndex(Corners[play[index+1]][0], Corners[play[index+1]][1], play[index], game.getPlayers().get(0).getUsername() ); index++;}

                if(cantPlayFlag){ if(ServerConstants.getDebug()){System.out.println("CantPlayFlagUpdated");}  break;}
            }

            //check
            if(Results[playIndex][0] == -1 && !cantPlayFlag){ failedFlag=true; testEvaluation.append("Failed on test: ").append(playIndex).append(" played card even if not supposed to\n"); }
            else if(Results[playIndex][0] != -1 && cantPlayFlag){ failedFlag=true; testEvaluation.append("Failed on test: ").append(playIndex).append(" not played card even if possible\n"); }
            else if(Results[playIndex][0] != -1)
            {  if(!Arrays.equals(Results[playIndex] , game.getPlayers().get(0).getScoreBoard())){
                testEvaluation.append("Failed on test: ").append(playIndex).append(" wrong Points\n"); failedFlag=true;  }}

            //reset
            game.getRelatedTable().emptyGrid(0); game.getPlayers().get(0).setScoreBoard(new int[8]);
        }

        if(!failedFlag){System.out.println("PlayTest -> Passed");}
        if(failedFlag){System.out.println(testEvaluation);}

        game.end();
        return failedFlag;
    }

    private static boolean EndGame_Test()
    {
        boolean failedEmptyDecks = false; boolean failed20Points = false;

        Game game1 = MultipleGameManager.addGame(1, new ArrayList<>());
        MultipleGameManager.JoinGame("EmptyDeck", game1);
        Table table1 = game1.getRelatedTable();

        for(int cardsDrawn = 0; cardsDrawn<80; cardsDrawn++){
            table1.DrawCard(3, "EmptyDeck" );
            game1.getPlayers().get(0).setCard(1, 0);
        }

        failedEmptyDecks = MultipleGameManager.getGameInstance("EmptyDeck")!=null;

        if(failedEmptyDecks){ System.out.println("EndGame_Test -> EmptyDecks failed "); }
        if(!failedEmptyDecks){ System.out.println("EndGame_Test -> EmptyDecks passed "); }



        Game game2 = MultipleGameManager.addGame(1, new ArrayList<>());
        MultipleGameManager.JoinGame("20Points", game2);
        Table table2 = game2.getRelatedTable();

        table2.DealCards();

        game2.getPlayers().get(0).setScoreBoard(new int[]{ 19, 0,0,0,0, 0,0,0 });

        game2.getPlayers().get(0).setCard(4, 201);
        game2.getPlayers().get(0).setCard(1, 10);

        TableManager.PlaceStartingCard(201, 0, game2.getPlayers().get(0).getUsername());
        TableManager.playCardByIndex( ServerConstants.getNumOfRows()/2 -1, ServerConstants.getNumOfRows()/4 -1, 10, game2.getPlayers().get(0).getUsername() );

        failed20Points = MultipleGameManager.getGameInstance("20Points")!=null;

        if(failed20Points){ System.out.println("EndGame_Test -> 20Points failed "); }
        if(!failed20Points){ System.out.println("EndGame_Test -> 20Points passed "); }



        return failed20Points || failedEmptyDecks;





    }
}
