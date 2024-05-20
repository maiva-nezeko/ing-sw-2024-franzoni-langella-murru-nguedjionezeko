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
import main.java.it.polimi.ingsw.ServerSide.Utility.GameStates;
import main.java.it.polimi.ingsw.ServerSide.Utility.PersistenceManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * The type Table manager.
 */
public class TableManager {


    //PlayerGrids
    /**
     *
     */
    private static final int NumOf_Rows = ServerConstants.getNumOfRows();
    /**
     *
     */
    private static final int NumOf_Columns = NumOf_Rows / 2;


    /**
     * Place starting card.
     *
     * @param id       the id
     * @param game     the game
     * @param username the username
     */
    public static void PlaceStartingCard(int id, Game game, String username) {
        if (game == null) {
            return;
        }
        int player = game.getPlayerNumber(username);

        List<Player> Players = game.getPlayers();
        PlayableCard Card = Deck.getCardBYid(id);
        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        assert Card != null;
        Card.setFlipped(id < 0);

        Players.get(player).setScoreBoard( Card.addPoints(Players.get(player).getScoreBoard()) );
        Players.get(player).consumeCard(id);


        OccupiedSpaces[player][NumOf_Rows / 2][NumOf_Columns / 2] = id;
        game.getRelatedTable().setOccupiedSpaces(OccupiedSpaces);

        if (ServerConstants.getDebug()) {
            ServerConstants.printMessage("ID = " + id + "  ");
        }

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
    public static boolean playCardByIndex(int Row_index, int Columns_index, int id, String username) {
        Game game = MultipleGameManager.getGameInstance(username);
        if (game == null) {
            ServerConstants.printMessageLn("Can't find game");
            return false;
        }

        Player chosenPlayer = game.getPlayerByUsername(username);
        ServerConstants.printMessageLn("Request Play by: " + username);

        int playerIndex = game.getPlayerNumber(username);
        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        PlayableCard Card = Deck.getCardBYid(abs(id));
        if (Card == null) {
            throw new NullPointerException();
        }

        Card.setFlipped(id < 0);

        int[] Points = chosenPlayer.getScoreBoard();
        if (!Card.isPlayable(Points)) {
            if (ServerConstants.getDebug()) {
                ServerConstants.printMessageLn("Can't play this card here: "+ Arrays.toString(Points));
            }
            return false;
        }

        int[] CornerValue = {0, 0, 0, 0};
        int index = 0;

        for (int Number : Card.getCorners()) {
            CornerValue[index] = Math.min(Number, 2);
            index++;
        }


        int[] SurroundingCorners = getSurroundingCorners(OccupiedSpaces, Row_index, Columns_index, playerIndex);


        if (/*TlCheck*/( SurroundingCorners[0] > CornerValue[0]) /*TrCheck*/ || (SurroundingCorners[1] > CornerValue[1])
                /*BlCheck*/ || (SurroundingCorners[2] > CornerValue[2]) /*BrCheck*/ || (SurroundingCorners[3] > CornerValue[3])) {

            OccupiedSpaces[playerIndex][Row_index][Columns_index] = id;
            chosenPlayer.consumeCard(id);


            ServerConstants.printMessage("ID = " + id + "  ");
            ServerConstants.printMessage("CardCorners = " + Arrays.toString(Card.getCorners()) + "\t");
            ServerConstants.printMessage("SurroundingCorners = " + Arrays.toString(SurroundingCorners) + "\t");
            ServerConstants.printMessage(Arrays.toString(Points) + " -> ");


            chosenPlayer.setScoreBoard(Card.addPoints(Points));
            game.modifyPlayer(game.getPlayerNumber(chosenPlayer.getUsername()), chosenPlayer);
            game.getRelatedTable().setOccupiedSpaces(OccupiedSpaces);


            AdjustScore(Row_index, Columns_index, Card, username, game);

            if (game.isGameStarted()) {
                PersistenceManager.SaveGame(game);
            }

            return true;
        }

        ServerConstants.printMessageLn("Can't play this card here: "+ Arrays.toString(Points));
        return false;

    }

    /**
     *
     * @param occupiedSpaces
     * @param row_index
     * @param columns_index
     * @param playerIndex
     * @return
     */
    private static int[] getSurroundingCorners(int[][][] occupiedSpaces, int row_index, int columns_index, int playerIndex) {

        PlayableCard Tl_Card = null, Tr_Card = null, Bl_Card = null, Br_Card = null;
        int Tl_space = 0, Tr_Space = 0, Bl_Space = 0, Br_Space = 0;

        if (columns_index - 1 >= 0) {
            if (row_index - 1 >= 0) {
                Tl_space = occupiedSpaces[playerIndex][row_index - 1][columns_index - 1];
            }
            if (row_index + 1 < NumOf_Rows) {
                Bl_Space = occupiedSpaces[playerIndex][row_index + 1][columns_index - 1];
            }
        }
        if (columns_index + 1 < NumOf_Columns) {
            {
                if (row_index - 1 >= 0) {
                    Tr_Space = occupiedSpaces[playerIndex][row_index - 1][columns_index + 1];
                }
                if (row_index + 1 < NumOf_Rows) {
                    Br_Space = occupiedSpaces[playerIndex][row_index + 1][columns_index + 1];
                }
            }
        }

        if (Tl_space != 0) {
            Tl_Card = Deck.getCardBYid(Tl_space);
        }
        if (Bl_Space != 0) {
            Bl_Card = Deck.getCardBYid(Bl_Space);
        }

        if (Tr_Space != 0) {
            Tr_Card = Deck.getCardBYid(Tr_Space);
        }
        if (Br_Space != 0) {
            Br_Card = Deck.getCardBYid(Br_Space);
        }


        int[] SurroundingCorners = {-1, -1, -1, -1};
        if (Tl_Card != null) {
            Tl_Card.setFlipped(Tl_space < 0);
            SurroundingCorners[0] = Tl_Card.getCorners()[3];
        }
        if (Tr_Card != null) {
            Tr_Card.setFlipped(Tr_Space < 0);
            SurroundingCorners[1] = Tr_Card.getCorners()[2];
        }

        if (Bl_Card != null) {
            Bl_Card.setFlipped(Bl_Space < 0);
            SurroundingCorners[2] = Bl_Card.getCorners()[1];
        }
        if (Br_Card != null) {
            Br_Card.setFlipped(Br_Space < 0);
            SurroundingCorners[3] = Br_Card.getCorners()[0];
        }

        return SurroundingCorners;

    }

    /**
     *
     * @param Row_index
     * @param Columns_index
     * @param Card
     * @param username
     * @param game
     */
    private static void AdjustScore(int Row_index, int Columns_index, PlayableCard Card, String username, Game game) {
        if (game == null) {
            return;
        }

        Player chosenPlayer = game.getPlayerByUsername(username);
        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        int[] OldPoints = chosenPlayer.getScoreBoard();
        int playerIndex = game.getPlayerNumber(username);

        int NumOf_Rows = ServerConstants.getNumOfRows();
        int NumOf_Columns = NumOf_Rows / 2;

        //remove CoveredPoints
        int[] SurroundingCorners = getSurroundingCorners(OccupiedSpaces, Row_index, Columns_index, playerIndex);
        for (int Number : SurroundingCorners) {
            if (Number > 1) {
                OldPoints[Number - 1]--;
            }
        }
        if (ServerConstants.getDebug()) {
            ServerConstants.printMessage(" CoveredCorners: " + Arrays.toString(SurroundingCorners));
        }


        if (Card instanceof GoldCard Card_golden) {
            if (Card_golden.getPointCond().equals(PointCondition.TWO_FOR_CORNER)) {
                for (int Number : SurroundingCorners) {
                    if (Number != -1) {
                        OldPoints[0] += 2;
                    }
                }
            }
        }

        chosenPlayer.setScoreBoard(OldPoints);
        game.modifyPlayer(playerIndex, chosenPlayer);

        if (ServerConstants.getDebug()) {
            ServerConstants.printMessageLn(Arrays.toString(OldPoints));
        }

        if (game.getGameState().equals(GameStates.LAST_TURN) && playerIndex == game.getLastPlayer()) {
            int player_index = 0;

            for (Player player_iterator : game.getPlayers()) {
                AddGoalPoints(player_iterator, player_index, game);
                player_index++;
            }

            game.end();
            return;
        }

        if (OldPoints[0] >= 20 && game.getGameState().equals(GameStates.PLAYING)) {
            game.nextPhase();
            game.setLastPlayer(playerIndex);
            ServerConstants.printMessageLn("Last turn Started");
        }

        ServerConstants.printMessageLn("Placed: "+ Arrays.toString(OldPoints));
    }

    /**
     *
     * @param chosenPlayer
     * @param player_index
     * @param game
     */
    private static void AddGoalPoints(Player chosenPlayer, int player_index, Game game) {

        int[] OldScoreBoard = chosenPlayer.getScoreBoard();
        Table chosenTable = game.getRelatedTable();

        GoalCard[] Cards = new GoalCard[]{Deck.getGoalCardByID(chosenPlayer.getPrivateCardsID()[3]),
                chosenTable.getGoalCards()[0], chosenTable.getGoalCards()[1]};

        for (GoalCard GCard : Cards) {
            switch (GCard.getGoalState()) {

                case TWO_FOR_TWO_FEATHERS:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[5] / 2);
                    break;
                case TWO_FOR_TWO_SALT:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[6] / 2);
                    break;
                case TWO_FOR_TWO_PAPER:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[7] / 2);
                    break;

                case THREE_FOR_COMBO:
                    int temp =min(OldScoreBoard[5], OldScoreBoard[6]);
                    OldScoreBoard[0] += 3*min(temp, OldScoreBoard[7]);
                    break;

                case TWO_FOR_THREE_FUNGUS:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[1] / 3);
                    break;
                case TWO_FOR_THREE_WOLF:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[2] / 3);
                    break;
                case TWO_FOR_THREE_LEAF:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[3] / 3);
                    break;
                case TWO_FOR_THREE_BUTTERFLY:
                    OldScoreBoard[0] += 2 * (OldScoreBoard[4] / 3);
                    break;

                case TWO_FOR_RED_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(0, player_index, game);
                    break;
                case TWO_FOR_BLUE_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(1, player_index, game);
                    break;
                case TWO_FOR_GREEN_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(2, player_index, game);
                    break;
                case TWO_FOR_PURPLE_STAIRCASE:
                    OldScoreBoard[0] += 2 * Stair_points(3, player_index, game);
                    break;

                case THREE_FOR_RED_L:
                    OldScoreBoard[0] += 3 * L_points(0, player_index, game);
                    break;
                case THREE_FOR_BLUE_L:
                    OldScoreBoard[0] += 3 * L_points(1, player_index, game);
                    break;
                case THREE_FOR_GREEN_L:
                    OldScoreBoard[0] += 3 * L_points(2, player_index, game);
                    break;
                case THREE_FOR_PURPLE_L:
                    OldScoreBoard[0] += 3 * L_points(3, player_index, game);
                    break;

            }
        }

        chosenPlayer.setScoreBoard(OldScoreBoard);
        game.modifyPlayer(game.getPlayerNumber(chosenPlayer.getUsername()), chosenPlayer);

        if (ServerConstants.getDebug()) {
            ServerConstants.printMessageLn(chosenPlayer.getUsername() + " " + Arrays.toString(OldScoreBoard));
        }
    }


    /**
     *
     * @param id
     * @param color
     * @return
     */
    private static boolean checkCardColor(int id, int color) {
        id = abs(id);
        if (id == 0 || id > 80) {
            return false;
        }

        int Card_color = (id - 1) / 10;
        if (Card_color >= 4) {
            Card_color -= 4;
        }

        return color == Card_color;
    }

    /**
     * Stair points int.
     *
     * @param color    the color
     * @param player   the player
     * @return the int
     */
    private static int Stair_points(int color, int player, Game game) {
        int count = 0;
        Table table = game.getRelatedTable();
        int[][] OccupiedSpaces = table.getOccupiedSpaces()[player];


        for (int Row_index = 3; Row_index < NumOf_Rows; Row_index++) {
            for (int Column_index = 0; Column_index < NumOf_Columns - 3; Column_index++) {

                if (color == 0 || color == 1) {
                    if (checkCardColor(OccupiedSpaces[Row_index][Column_index], color) &&
                            checkCardColor(OccupiedSpaces[Row_index + 1][Column_index + 1], color) && checkCardColor(OccupiedSpaces[Row_index + 2][Column_index + 2], color)) {
                        count++;
                    }
                } else {
                    if (checkCardColor(OccupiedSpaces[Row_index][Column_index], color) &&
                            checkCardColor(OccupiedSpaces[Row_index - 1][Column_index - 1], color) && checkCardColor(OccupiedSpaces[Row_index - 2][Column_index - 2], color)) {
                        count++;
                    }
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
     * @return the int
     */
    private static int L_points(int color, int player, Game game) {
        int count = 0;
        Table table = game.getRelatedTable();

        int[][][] OccupiedSpaces = table.getOccupiedSpaces();

        //for all we start top left card
        for (int Row_index = 0; Row_index < NumOf_Rows - 3; Row_index++) {
            for (int Column_index = 0; Column_index < NumOf_Columns - 1; Column_index++) {

                switch (color) {
                    case 0 -> { //red L
                        if ( checkCardColor(OccupiedSpaces[player][Row_index][Column_index], 0)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 2][Column_index], 0)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 3][Column_index + 1],2)) { count++; }
                    }
                    case 1 -> { //Blue L
                        if ( checkCardColor(OccupiedSpaces[player][Row_index][Column_index+1], 0)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 1][Column_index], 1)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 3][Column_index],1)) { count++; }
                    }
                    case 2 -> { //green L
                        if ( checkCardColor(OccupiedSpaces[player][Row_index][Column_index + 1], 2)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 2][Column_index +1], 2)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 3][Column_index],3)) { count++; }
                    }
                    case 3 -> {//purple L
                        if ( checkCardColor(OccupiedSpaces[player][Row_index][Column_index], 1)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 1][Column_index + 1], 3)
                                && checkCardColor(OccupiedSpaces[player][Row_index + 3][Column_index + 1],3)) { count++; }
                    }
                }
            }
        }




        return count;

    }


}
