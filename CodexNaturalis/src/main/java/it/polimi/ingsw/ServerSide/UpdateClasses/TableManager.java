package main.java.it.polimi.ingsw.ServerSide.UpdateClasses;


import main.java.it.polimi.ingsw.ServerSide.Cards.Enums.PointCondition;
import main.java.it.polimi.ingsw.ServerSide.Cards.GoalCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.GoldCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import main.java.it.polimi.ingsw.ServerSide.Table.Deck;
import main.java.it.polimi.ingsw.ServerSide.Table.Player;
import main.java.it.polimi.ingsw.ServerSide.Table.Table;
import main.java.it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

/**
 * The type Table manager.
 */
public class TableManager {


    //PlayerGrids
    private static final int NumOf_Rows = ServerConstants.getNumOfRows();
    private static final int NumOf_Columns = NumOf_Rows/2;


    /**
     * Place starting card.
     *
     * @param id       the id
     * @param game     the game
     * @param username the username
     */
    public static void PlaceStartingCard(int id, Game game, String username)
    {
        if(game==null){return;}
        int player = game.getPlayerNumber(username);

        List<Player> Players = game.getPlayers();
        PlayableCard Card = Deck.getCardBYid(id);
        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        Card.setFlipped(id<0);

        Players.get(player).setScoreBoard(Card.addPoints(Players.get(player).getScoreBoard()));
        Players.get(player).consumeCard(id);


        OccupiedSpaces[player][NumOf_Rows/2][NumOf_Columns/2] = id;
        game.getRelatedTable().setOccupiedSpaces(OccupiedSpaces);

        if(ServerConstants.getDebug()){System.out.print("ID = "+id + "  ");}

    }

    /**
     * Play card by index boolean.
     *
     * @param Row_index     the row index
     * @param Columns_index the columns index
     * @param id            the id
     * @param username      the username
     * @return the boolean
     */
    public static boolean playCardByIndex(int Row_index, int Columns_index, int id, String username)
    {
        Game game = MultipleGameManager.getGameInstance(username);
        if(game==null){
            if(ServerConstants.getDebug()){System.out.println("Can't find game");}
            return false;}

        Player chosenPlayer = game.getPlayerByUsername(username);
        int playerIndex = game.getPlayerNumber(username);

        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        PlayableCard Card = Deck.getCardBYid(abs(id));
        if(Card==null){throw new NullPointerException();}

        Card.setFlipped(id<0);

        int[] Points = chosenPlayer.getScoreBoard();
        if(!Card.isPlayable(Points)){
            if(ServerConstants.getDebug()){System.out.println("Can't play this card here");}
            return false;
        }

        int[] CornerValue = {0,0,0,0}; int index=0;

        for(int Number : Card.getCorners())
        {
            CornerValue[index] = Math.min(Number, 2);
            index++;
        }




        PlayableCard Tl_Card=null, Tr_Card=null, Bl_Card=null, Br_Card=null;

        if(Columns_index-1>=0) {{ if(Row_index-1>=0){ if(OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]!=0){
            Tl_Card = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]);}}
            if(Row_index+1<NumOf_Rows){ if(OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]!=0){
                Bl_Card = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]);}} }}

        if(Columns_index+1<NumOf_Columns) {   if(Row_index-1>=0){ if(OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]!=0){
            Tr_Card = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]);}}
            if(Row_index+1<NumOf_Rows){  if(OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]!=0){
                Br_Card = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]);}}}

        int[] SurroundingCorners = {-1,-1,-1,-1};
        if(Tl_Card!=null){ Tl_Card.setFlipped(OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]<0); SurroundingCorners[0] = Math.min(Tl_Card.getCorners()[3], 2);}
        if(Tr_Card!=null){ Tr_Card.setFlipped(OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]<0); SurroundingCorners[1] = Math.min(Tr_Card.getCorners()[2], 2);}
        if(Bl_Card!=null){ Bl_Card.setFlipped(OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]<0); SurroundingCorners[2] = Math.min(Bl_Card.getCorners()[1], 2);}
        if(Br_Card!=null){ Br_Card.setFlipped(OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]<0); SurroundingCorners[3] = Math.min(Br_Card.getCorners()[0], 2);}



        if(/*TlCheck*/(SurroundingCorners[0] > CornerValue[0]) /*TrCheck*/ || (SurroundingCorners[1] > CornerValue[1])
                /*BlCheck*/ || (SurroundingCorners[2] > CornerValue[2]) /*BrCheck*/ || (SurroundingCorners[3] > CornerValue[3]))
        {

            OccupiedSpaces[playerIndex][Row_index][Columns_index] = id;
            chosenPlayer.consumeCard(id);

            if(ServerConstants.getDebug()){
                System.out.print("ID = " +id+ "  ");
                System.out.print("CardCorners = " + Arrays.toString(CornerValue)+"\t");
                System.out.print("SurroundingCorners = "+ Arrays.toString(SurroundingCorners)+"\t");
                System.out.print(Arrays.toString(Points) + " -> ");}

            chosenPlayer.setScoreBoard(Card.addPoints(Points));
            game.modifyPlayer(game.getPlayerNumber(chosenPlayer.getUsername()), chosenPlayer);
            game.getRelatedTable().setOccupiedSpaces(OccupiedSpaces);


            AdjustScore(Row_index, Columns_index, Card, username);

            if(game.isGameStarted()){ PersistenceManager.SaveGame(game); }

            return true;
        }

        return false;

    }

    private static void AdjustScore(int Row_index, int Columns_index, PlayableCard Card, String username)
    {
        Game game = MultipleGameManager.getGameInstance(username);
        if(game==null){return;}

        Player chosenPlayer = game.getPlayerByUsername(username);

        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        int[] OldPoints = chosenPlayer.getScoreBoard();
        int playerIndex = game.getPlayerNumber(username);
        int[] CoveredCorners = {-1,-1,-1,-1};

        PlayableCard tempCard = null;
        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        //remove CoveredPoints
        if(Columns_index-1>=0) {{ if(Row_index-1>=0 && OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]!=0){ tempCard = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]);
            tempCard.setFlipped(OccupiedSpaces[playerIndex][Row_index-1][Columns_index-1]<0); CoveredCorners[0] = tempCard.getCorners()[3];} //UpLeft
            if(Row_index+1<NumOf_Rows && OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]!=0){ tempCard = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]);
                tempCard.setFlipped(OccupiedSpaces[playerIndex][Row_index+1][Columns_index-1]<0); CoveredCorners[1] =tempCard.getCorners()[1];}} } //DownLeft

        if(Columns_index+1<NumOf_Columns) { if(Row_index-1>=0  && OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]!=0){ tempCard = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]);
            tempCard.setFlipped(OccupiedSpaces[playerIndex][Row_index-1][Columns_index+1]<0); CoveredCorners[2]   = tempCard.getCorners()[2];} //UpRight
            if(Row_index+1<NumOf_Rows && OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]!=0){ tempCard = Deck.getCardBYid(OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]);
                tempCard.setFlipped(OccupiedSpaces[playerIndex][Row_index+1][Columns_index+1]<0); CoveredCorners[3] =  tempCard.getCorners()[0];}} //DownRight

        for(int Number : CoveredCorners){ if(Number>1){OldPoints[Number-1]--;} }
        if(ServerConstants.getDebug()){ System.out.print(" CoveredCorners: " + Arrays.toString(CoveredCorners)); }


        if(Card instanceof GoldCard Card_golden)
        {
            if(Card_golden.getPointCond().equals(PointCondition.TWO_FOR_CORNER))
            {   for(int Number : CoveredCorners){ if(Number!=-1){OldPoints[Number-1]+=2;} }    }
        }

        chosenPlayer.setScoreBoard(OldPoints);
        game.modifyPlayer(playerIndex, chosenPlayer);

        if(ServerConstants.getDebug()){ System.out.println(Arrays.toString(OldPoints)); }

        if(OldPoints[0]>=20){
            int player_index = 0;
            for(Player player_iterator: game.getPlayers()){AddGoalPoints(player_iterator, player_index, username); player_index++;}
            game.nextPhase();
            System.out.println("Last turn Started");
        }

    }

    private static void AddGoalPoints(Player chosenPlayer, int player_index, String username) {

        int[] OldScoreBoard = chosenPlayer.getScoreBoard();

        Game game = MultipleGameManager.getGameInstance(username);
        if(game==null){return;}

        Table chosenTable = game.getRelatedTable();

        GoalCard[] Cards = new GoalCard[]{    Deck.getGoalCardByID(chosenPlayer.getPrivateCardsID()[3]),
                chosenTable.getGoalCards()[0], chosenTable.getGoalCards()[1]};

        for(GoalCard GCard: Cards) {
            switch (GCard.getGoalState()) {

                case TWO_FOR_TWO_FEATHERS: OldScoreBoard[0] += 2 * (OldScoreBoard[5] / 2); break;
                case TWO_FOR_TWO_SALT: OldScoreBoard[0] += 2 * (OldScoreBoard[6] / 2); break;
                case TWO_FOR_TWO_PAPER: OldScoreBoard[0] += 2 * (OldScoreBoard[7] / 2); break;

                case THREE_FOR_COMBO:

                    int[] temp = OldScoreBoard;

                    while (temp[5] > 0 && temp[6] > 0 && temp[7] > 0) {
                        OldScoreBoard[0] += 3;
                        temp[5]--;
                        temp[6]--;
                        temp[7]--;
                    }
                    break;

                case TWO_FOR_THREE_FUNGUS: OldScoreBoard[0] += 2 * (OldScoreBoard[1] / 3);  break;
                case TWO_FOR_THREE_WOLF: OldScoreBoard[0] += 2 * (OldScoreBoard[2] / 3);   break;
                case TWO_FOR_THREE_LEAF: OldScoreBoard[0] += 2 * (OldScoreBoard[3] / 3);   break;
                case TWO_FOR_THREE_BUTTERFLY:  OldScoreBoard[0] += 2 * (OldScoreBoard[4] / 3);   break;

                case TWO_FOR_RED_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(1, player_index, username);
                    break;
                case TWO_FOR_BLUE_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(2, player_index, username);
                    break;
                case TWO_FOR_GREEN_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(3, player_index, username);
                    break;
                case TWO_FOR_PURPLE_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(4, player_index, username);
                    break;

                case THREE_FOR_RED_L:
                    OldScoreBoard[0] += 3 * L_points(1, player_index, username);
                    break;
                case THREE_FOR_BLUE_L:
                    OldScoreBoard[0] += 3 * L_points(2, player_index, username);
                    break;
                case THREE_FOR_GREEN_L:
                    OldScoreBoard[0] += 3 * L_points(3, player_index, username);
                    break;
                case THREE_FOR_PURPLE_L:
                    OldScoreBoard[0] += 3 * L_points(4, player_index, username);
                    break;

            }
        }

        chosenPlayer.setScoreBoard(OldScoreBoard);
        game.modifyPlayer(game.getPlayerNumber(chosenPlayer.getUsername()), chosenPlayer);

        if(ServerConstants.getDebug()){ System.out.println(chosenPlayer.getUsername() +" "+ Arrays.toString(OldScoreBoard)); }
    }

    /**
     * Stair points int.
     *
     * @param color    the color
     * @param player   the player
     * @param username the username
     * @return the int
     */
    public static int Stair_points(int color, int player, String username)
    {
        int count=0;
        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        Game game = MultipleGameManager.getGameInstance(username);
        if(game == null){return 0;}

        Table table = game.getRelatedTable();

        int[][][] OccupiedSpaces = table.getOccupiedSpaces();

        if(color==1 || color==2){for (int Row_index=3; Row_index<NumOf_Rows; Row_index++){
            for (int Column_index=0; Column_index<NumOf_Columns-3; Column_index++){
                if( (abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == color
                        && (abs(OccupiedSpaces[player][Row_index-1][Column_index+1])-1)/20 + 1 == color
                            && (abs(OccupiedSpaces[player][Row_index-2][Column_index+2])-1)/20 + 1 == color  ){   count++; }
            }
        }}

        if(color==3 || color==4){
            for (int Row_index=0; Row_index<NumOf_Rows-3; Row_index++){
                for (int Column_index=0; Column_index<NumOf_Columns-3; Column_index++){
                    if( (abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == color  &&
                            (abs(OccupiedSpaces[player][Row_index+1][Column_index+1])-1)/20 + 1 == color  &&
                                    (abs(OccupiedSpaces[player][Row_index+2][Column_index+2])-1)/20 + 1 == color ) { count++; }
                }
            }
        }

        return count;

    }

    /**
     * L points int.
     *
     * @param color    the color
     * @param player   the player
     * @param username the username
     * @return the int
     */
    public static int L_points(int color, int player, String username)
    {
        int count=0;
        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows/2;

        Game game = MultipleGameManager.getGameInstance(username);
        if(game == null){return 0;}

        Table table = game.getRelatedTable();

        int[][][] OccupiedSpaces = table.getOccupiedSpaces();

        //red
        if(color==0){for (int Row_index=0; Row_index<NumOf_Rows-4; Row_index++) {
            for (int Column_index = 0; Column_index < NumOf_Columns - 1; Column_index++) {
                if ( (abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == 1
                        && (abs(OccupiedSpaces[player][Row_index + 2][Column_index])-1)/20 + 1 == 3
                            && (abs(OccupiedSpaces[player][Row_index + 3][Column_index + 1])-1)/20 + 1 == 3){  count++; }}}}

        //green
        if(color==2) {
            for (int Row_index = 0; Row_index < NumOf_Rows - 4; Row_index++) {
                for (int Column_index = 1; Column_index < NumOf_Columns; Column_index++) {
                    if ( (abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == 3
                            && (abs(OccupiedSpaces[player][Row_index + 2][Column_index])-1)/20 + 1 == 3
                                && (abs(OccupiedSpaces[player][Row_index + 3][Column_index - 1])-1)/20 + 1 == 4){  count++; }}}}

        //blue
        if(color==1){for (int Row_index=0; Row_index<NumOf_Rows-4; Row_index++) {
            for (int Column_index = 1; Column_index < NumOf_Columns; Column_index++) {
                if ( (abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == 1
                        && (abs(OccupiedSpaces[player][Row_index + 1][Column_index - 1])-1)/20 + 1 == 2
                            && (abs(OccupiedSpaces[player][Row_index + 3][Column_index - 1])-1)/20 + 1 == 2){ count++; }}}}

        //purple
        if(color==3){for (int Row_index=0; Row_index<NumOf_Rows-4; Row_index++) {
            for (int Column_index = 0; Column_index < NumOf_Columns - 1; Column_index++) {
                if ((abs(OccupiedSpaces[player][Row_index][Column_index])-1)/20 + 1 == 2
                        && (abs(OccupiedSpaces[player][Row_index + 1][Column_index + 1])-1)/20 + 1 == 4
                            && (abs(OccupiedSpaces[player][Row_index + 3][Column_index + 1])-1)/20 + 1 == 4){ count++; }}}}

        return count;

    }


}
