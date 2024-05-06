package it.polimi.ingsw.ServerSide.Utility;

import it.polimi.ingsw.ServerSide.Cards.GoalCard;
import it.polimi.ingsw.ServerSide.Cards.PlayableCard;
import it.polimi.ingsw.ServerSide.Table.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public interface CardRandomizer {

    static List<PlayableCard> ShuffleDeck() {

        Random rnd = new Random();
        List<PlayableCard> StandardDeck = new ArrayList<>();

        for(int ID=1; ID<=40; ID++){ StandardDeck.add(Deck.getCardBYid(ID));}
        Collections.shuffle(StandardDeck, rnd);

        return StandardDeck;
    }

    static List<PlayableCard> ShuffleGoldDeck() {

        Random rnd = new Random();
        List<PlayableCard> GoldDeck = new ArrayList<>();

        for(int ID=41; ID<=80; ID++){ GoldDeck.add(Deck.getCardBYid(ID));}
        Collections.shuffle(GoldDeck, rnd);

        return GoldDeck;

    }

    static List<GoalCard> ShuffleGoalDeck() {

        Random rnd = new Random();
        List<GoalCard> GoalDeck = new ArrayList<>();

        for(int ID=100; ID<=115; ID++){ GoalDeck.add(Deck.getGoalCardByID(ID));}
        Collections.shuffle(GoalDeck, rnd);

        return GoalDeck;
    }

    static List<PlayableCard> ShuffleStartingDeck() {

        Random rnd = new Random();
        List<PlayableCard> StartingDeck = new ArrayList<>();

        for(int ID=200; ID<=205; ID++){ StartingDeck.add(Deck.getCardBYid(ID));}
        Collections.shuffle(StartingDeck, rnd);

        return StartingDeck;
    }


}

