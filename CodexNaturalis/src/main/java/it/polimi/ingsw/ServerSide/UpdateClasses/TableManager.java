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
 * Manages all operations regarding a player placing a Card in his own Table o PlayBoards.
 * @author Darelle Maiva Nguedjio Nezeko, Edoardo Carlo Murru
 */
public class TableManager {


    //PlayerGrids
    /**
     * Number of rows in Player's Grid, from ServerConstants.
     *
     * @see ServerConstants#getNumOfRows()
     */
    private static final int NumOf_Rows = ServerConstants.getNumOfRows();
    /**
     * Number of Columns in Player's Grid.
     */
    private static final int NumOf_Columns = NumOf_Rows / 2;


    /**
     * Place starting card: if game is null, we simply exit the method; then, a player is initialized by calling for
     * 'getPlayerNumber' in Game, the method gets the list of Players and initializes a Card from Deck, along with
     * the Player's Grid. For a Player, first we get the ScoreBoard,then new points are added, finally we set the ScoreBoard
     * and consume the Starting Card. In the end, it gets the Table and sets which spaces are now occupied, and the Card
     * id is printed with a message.
     *
     * @param id       the unique card id
     * @param game     the game
     * @param username the username of the Player
     *
     * @see Game#getPlayerNumber(String)
     * @see Game#getPlayers()
     * @see Deck#getCardBYid(int)
     * @see Game#getRelatedTable().getOccupiedSpaces
     *
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
     * Places a card by using its index number: first it checks that the game exists, if not the Client is notified and the method returns false;
     * then it gets the Player, his index number and the game's related Table at the Player index, so to get the Cards
     * he already placed down. Eventually the Card can be set as flipped. It gets the Player's scores along with the resource
     * count array and if a Card isn't playable based on the Client is yet again notified and the method returns false; finally it gets the Card
     * corners and displays surrounding possible slots for future placements. All of this is also notified to the Player,
     * then the new points given by the Card are calculated (if any), the Card is set in place and the resources count array is
     * updated - along with the Score. The Game is then Saved.
     *
     * @param Row_index     the row index
     * @param Columns_index the columns index
     * @param id            the unique card id
     * @param username      the Player's username
     *
     * @return the boolean for if a Card has been placed
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
     * After a Card is set in place, renders surrounding possible slots for Cards to be placed. It scans first for the
     * slots before the index, both in bottom and top row, then for the slots after the specified row index, yet again
     * both bottom and top row. If a slot is already full, it renders the Card in it, checking first if it's set as flipped
     * (or not) and for the Card corners.
     *
     * @param occupiedSpaces the PlayBoard
     * @param row_index      the horizontal row index
     * @param columns_index  the vertical column index
     * @param playerIndex    the Player index number
     *
     * @return the array Surrounding Corners
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
     * After a Card has been placed, adjusts the Player's score and resources count in OldPoints array: first it gets
     * the array with the Corners that surround the Card, along with printing the associated message, then it checks if
     * the selected Card is the type to assign points based on the covered corners - in which case the points are calculated -
     * and updates the scores. Finally, it checks for what phase the game is: once 20 points are reached by any player,
     * the GameState changes from 'PLAYING' to 'LAST_TURN' and afterward Players play one last turn at the end of which
     * the common Goal points are added. If neither of this option is applicable, is simply notifies the Player that a Card
     * has been placed.
     *
     * @param Row_index     the row index
     * @param Columns_index the column index
     * @param Card          the Playable Card object
     * @param username      the Player's username
     * @param game          the Game
     *
     * @see Player#setScoreBoard(int[])
     */
    private static void AdjustScore(int Row_index, int Columns_index, PlayableCard Card, String username, Game game) {
        if (game == null) {
            return;
        }

        Player chosenPlayer = game.getPlayerByUsername(username);
        int[][][] OccupiedSpaces = game.getRelatedTable().getOccupiedSpaces();

        int[] OldPoints = chosenPlayer.getScoreBoard();
        int playerIndex = game.getPlayerNumber(username);

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
     * Adds Goal points, calculating them case by case: for Goal Cards that require a specific resource count, we can simply
     * use the Score array; on the contrary for the Goal Cards that imply some sort of peculiar placement, specific method are
     * implemented.
     *
     * @param chosenPlayer  the Player
     * @param player_index  the Player number/index
     * @param game          the Game
     *
     * @see TableManager#Stair_points(int, int, Game)
     * @see TableManager#L_points(int, int, Game)
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
     * Controls or checks if a Card is of a given color. If the Card id is below 0 or bigger than 80, the Card simply
     * doesn't exist; in any other case the Color, according to how the Card are indexed, the Color is found by diving
     * the id by 10 and eventually subtracting 4 (as there are four colors, and Cards of the same color are divided by
     * 40 numbers in between).
     * The return value is determined by whether the color attributed and the calculated color 'Card_color' are the same
     * number!
     *
     * @param id        the unique Card id
     * @param color     the Card color to compare to
     * @return the boolean response
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
     * Calculates Staircase type Goal Card points, according to the specified Card color, in particular checking for Cards
     * in all the PlayBoard, once one of the target color is found, the surrounding Cards, either in top right or bottom left,
     * are immediately checked. Each time there's a successful tris of Cards that checks all conditions, a count is increased.
     *
     * @param color    the Card color
     * @param player   the player number
     * @param game     the Game
     * @return the Points count
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
     * Calculates L type Goal Card points, according to the specified Card color, each different case implemented individually.
     *
     * @param color    the Card color
     * @param player   the player number
     * @param game     the Game
     * @return the Points count
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
