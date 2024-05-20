package main.java.it.polimi.ingsw.ServerSide.Table;


import main.java.it.polimi.ingsw.ServerSide.Cards.GoalCard;
import main.java.it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.Utility.CardRandomizer;
import main.java.it.polimi.ingsw.ServerSide.Utility.GameStates;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

/**
 * The board of the game, each game has its own board filled with decks and cards.
 */

public class Table {

    private final Game relatedGame;


    /**
     * The Randomized list of Playable Cards.
     */
    private final List<PlayableCard> ShuffledDeck = CardRandomizer.ShuffleDeck();
    /**
     * The Randomized list of Gold Cards.
     */
    private final List<PlayableCard> ShuffledGoldDeck = CardRandomizer.ShuffleGoldDeck();
    /**
     * The Randomized list of Goal Cards.
     */
    private final List<GoalCard> ShuffledGoalDeck = CardRandomizer.ShuffleGoalDeck();
    /**
     * The Randomized list of Starting Cards.
     */
    private final List<PlayableCard> ShuffledStartingDeck = CardRandomizer.ShuffleStartingDeck();

    /**
     * Allow to draw a GoldCard from GoldShuffledDeck if the boolean is true and a ResourceCard from ShuffleDeck otherwise.
     * @param isGold     states if a card is gold or not
     * @return PlayableCard drawn casually from Deck
     */
    public PlayableCard drawResourceCard(boolean isGold)
    {
        PlayableCard returnCard = null;

        if (isGold){   if(!ShuffledGoldDeck.isEmpty()){ returnCard = ShuffledGoldDeck.remove(0);}}
        else  { if(!ShuffledDeck.isEmpty()){ returnCard = ShuffledDeck.remove(0);}}

        return returnCard;
    }

    /**
     * Allow to draw a card from ShuffledStartingDeck that contains startingCard.
     * @return PlayableCard drawn casually from StartingDeck
     */

    public PlayableCard drawStartingCard()
    { PlayableCard returnCard = null;  if(!ShuffledStartingDeck.isEmpty()){ returnCard= ShuffledStartingDeck.remove(0);} return returnCard;}

    /**
     * Allow to draw a card from ShuffledGoalDeck that contains GoalCard.
     * @return PlayableCard drawn casually from GoalDeck
     */

    public GoalCard drawGoalCard()
    {GoalCard returnCard = null; if(!ShuffledGoalDeck.isEmpty()){ returnCard = ShuffledGoalDeck.remove(0);} return returnCard;}


    private int[][][] OccupiedSpaces;
    public int[][][] getOccupiedSpaces(){return OccupiedSpaces;}
    public void setOccupiedSpaces(int[][][] newOccupiedSpaces){ this.OccupiedSpaces=newOccupiedSpaces; }

    private int[] PublicSpacesID;
    public int[] getPublicSpacesID(){ return this.PublicSpacesID; }
    public void setPublicSpacesID(int[] newPublicSpaces){
        if(newPublicSpaces.length!=8){throw new ArrayIndexOutOfBoundsException();}
        this.PublicSpacesID = newPublicSpaces; }

    /**
     * Instantiates a new Table.
     * @param PlayerCount   the number of Players.
     * @param relatedGame   the Game associated to the Table.
     * */
    public Table(int PlayerCount, Game relatedGame)
    {
        int numOf_Rows = ServerConstants.getNumOfRows();
        int numOf_Columns = numOf_Rows / 2;

        this.OccupiedSpaces = new int[PlayerCount][numOf_Rows][numOf_Columns];
        this.PublicSpacesID = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        this.relatedGame = relatedGame;
    }

    public GoalCard[] getGoalCards(){
        return new GoalCard[] { Deck.getGoalCardByID(PublicSpacesID[6]), Deck.getGoalCardByID(PublicSpacesID[7])}; }


    /**
     * Fills the empty spaces in the table intended to the four drawable cards from resource and gold deck
     * whenever a player pick one of them.
     */
    public void AutoFillSpaces(){

        if(PublicSpacesID[0]==0){
            PlayableCard Card = drawResourceCard(true);
            if(Card != null){ PublicSpacesID[0] = -Card.getID(); }
        }

        if(PublicSpacesID[1]==0){
            PlayableCard Card = drawResourceCard(false);
            if(Card != null){ PublicSpacesID[1] = -Card.getID(); }
        }

        if(PublicSpacesID[2]==0){  if(PublicSpacesID[0]!=0){MoveCard(0,2);  AutoFillSpaces();} else if(PublicSpacesID[1]!=0){MoveCard(1,2);  AutoFillSpaces();}    }
        if(PublicSpacesID[3]==0){  if(PublicSpacesID[1]!=0){MoveCard(1,3);  AutoFillSpaces();} else if(PublicSpacesID[0]!=0){MoveCard(0,3);  AutoFillSpaces();}    }
        if(PublicSpacesID[4]==0){  if(PublicSpacesID[0]!=0){MoveCard(0,4);  AutoFillSpaces();} else if(PublicSpacesID[1]!=0){MoveCard(1,4);  AutoFillSpaces();}    }
        if(PublicSpacesID[5]==0){  if(PublicSpacesID[1]!=0){MoveCard(1,5);  AutoFillSpaces();} else if(PublicSpacesID[0]!=0){MoveCard(0,5);  AutoFillSpaces();}    }
    }

    /**
     * You can move the card from a position to another of the array PublicSpace[] that represent the hand of the player
     * @param from  starting position of the card
     * @param to    destination of the card
     */
    public void MoveCard(int from, int to)
    {
        if(PublicSpacesID[from] != 0) { PublicSpacesID[to] = -PublicSpacesID[from];  PublicSpacesID[from] = 0;    }
    }

    /**
     * Allows a player to draw a card, if his hand it's empty, from the table in his hand.
     * @param position the position in your hand of the drawn card
     * @param username it's unique
     */
    public void DrawCard(int position, String username)
    {
        Player chosenPlayer = relatedGame.getPlayerByUsername(username);
        if(chosenPlayer==null){return;}

        int EmptyHandSlot = chosenPlayer.getEmptySlot();
        if(EmptyHandSlot != -1)
        {
            chosenPlayer.setCard(EmptyHandSlot, abs(PublicSpacesID[position]));
            PublicSpacesID[position]=0;
            AutoFillSpaces();

            if(PublicSpacesID[0]==0 && PublicSpacesID[1]==0 && relatedGame.getGameState().equals(GameStates.PLAYING))
            { relatedGame.nextPhase(); relatedGame.setLastPlayer(relatedGame.getPlayerNumber(username));}

            if(ServerConstants.getDebug()){ ServerConstants.printMessageLn("Player: "+username+" draw: "+PublicSpacesID[position] + " in position: " +position); }

        }

    }

    /**
     * It takes care of the distribution of the card in every player's hand
     * 0,1,2 -> are resource cards ;
     * 4 -> is the starting cards ;
     * 3,5 -> are the goal cards
     */
    public void DealCards()
    {
        this.PublicSpacesID[6] = drawGoalCard().getID();
        this.PublicSpacesID[7] = drawGoalCard().getID();

        List<Player> Players = this.relatedGame.getPlayers();

        for(Player player: Players)
        {
            player.setCard(0, drawResourceCard(true).getID());
            player.setCard(1, drawResourceCard(false).getID());
            player.setCard(2, drawResourceCard(false).getID());
            player.setCard(4, drawStartingCard().getID());
            player.setCard(3, drawGoalCard().getID());
            player.setCard(5, drawGoalCard().getID());

            this.relatedGame.modifyPlayer(this.relatedGame.getPlayerNumber(player.getUsername()), player);

            if(ServerConstants.getDebug()){ ServerConstants.printMessageLn(this.relatedGame.getPlayerByUsername(player.getUsername()).getUsername()
                    +" got hand: "+ Arrays.toString(this.relatedGame.getPlayerByUsername(player.getUsername()).getPrivateCardsID()));}
        }

    }

    /**
     * Reset grid of the player at his initial value "0".
     *
     * @param player unique username
     * */
    public void emptyGrid(int player)
    {for(int Rowindex=0; Rowindex<ServerConstants.getNumOfRows(); Rowindex++){
        for(int ColumnIndex=0; ColumnIndex<ServerConstants.getNumOfRows()/2; ColumnIndex++)
        {this.OccupiedSpaces[player][Rowindex][ColumnIndex] = 0;}}}

    /**
     * Draws a random Card.
     *
     * @param username the username of Player requesting draw.
     */
    public void drawRandom(String username) {
        for (int index=0; index<6; index++){
            if(PublicSpacesID[index]!=0){ DrawCard(index, username); break; }
        }
    }
}

