package main.java.it.polimi.ingsw.ClientSide.GUI_Render;


import main.java.it.polimi.ingsw.ClientSide.Cards.Deck;
import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects.*;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.awt.*;

/**
 * the type FULL_GUI
 */
public class FULL_GUI {

    //GUI_Objects
    static int xWindowSize = ClientConstants.getxWindowSize();
    static int yWindowSize = ClientConstants.getyWindowSize();

    private static final int CardXSize = ((xWindowSize/6) /2) - ((xWindowSize/6) /20);
    private static final int CardYSize = CardXSize *10/15;

    //DrawScene Constants
    private static final int BG_Hand1Card_XPos = xWindowSize/4 + CardXSize/2;
    private static final int BG_Hand1Card_YPos = yWindowSize- yWindowSize/6 + CardYSize/3;

    private static final int BG_CommonGoal1_XPos = xWindowSize/2 + CardXSize/15;
    private static final int BG_CommonGoal1_YPos = yWindowSize/2 - xWindowSize/6 + CardYSize/2;



    //HandScene Constants
    private static final int Hand_CardXSize = xWindowSize/9 - xWindowSize/45;
    private static final int Hand_CardYSize = Hand_CardXSize * 10/15;
    private static final int Hand_Hand1Card_XPos = (2*xWindowSize)/3 + (xWindowSize)/3 - 2*CardXSize;
    private static final int Hand_Hand1Card_YPos = xWindowSize/3 + Hand_CardYSize/3;

    private static final int Hand_CommonGoal1_XPos = xWindowSize-xWindowSize/6 + CardXSize/15;
    private static final int Hand_CommonGoal1_YPos = CardYSize/2;
    private static final int CardYDelta = CardYSize + CardYSize/10;

    private static final Image pointBoardImage = ImagesCollection.getPointBoardImage();
    private static final Image[] MenuImages = ImagesCollection.getMenu_GUI_Images();


    /**
     * Creation of the game in GUI
     */
    private static final GUI_object[] GUI = {
    //0-3
            new GUI_Image(xWindowSize/6,xWindowSize/3,xWindowSize/2,yWindowSize/2 - xWindowSize/6, null), //DrawScene_deck
            new GUI_Image(xWindowSize/6,xWindowSize/3,xWindowSize/2 - xWindowSize/6,yWindowSize/2 - xWindowSize/6, pointBoardImage), //DrawScene_pointBoard
            new GUI_Image(0,0, 0,0, null), //Bg_PlayerBoard
            new GUI_Image(xWindowSize/2,yWindowSize/6,xWindowSize/4,yWindowSize -yWindowSize/6, null),//DrawScene_PlayerHand
    //4-5
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos, BG_CommonGoal1_YPos + CardYDelta + CardYSize/2, null), //BG_GoldDeck_Space
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos + xWindowSize/12, BG_CommonGoal1_YPos + CardYDelta + CardYSize/2, null), //DrawScene_Deck_Space
    //6-11
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos, BG_CommonGoal1_YPos + 2* CardYDelta + CardYSize/2, null), //BG_Card1_Space
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos + xWindowSize/12, BG_CommonGoal1_YPos + 2* CardYDelta + CardYSize/2, null), //DrawScene_Card2_Space
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos, BG_CommonGoal1_YPos + 3* CardYDelta + CardYSize/2, null), //BG_Card3_Space
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos + xWindowSize/12, BG_CommonGoal1_YPos + 3* CardYDelta + CardYSize/2, null), //DrawScene_Card4_Space

            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos, BG_CommonGoal1_YPos, null), //DrawScene_CommonGoal1
            new GUI_Card(CardXSize, CardYSize, BG_CommonGoal1_XPos + xWindowSize/12, BG_CommonGoal1_YPos, null), //DrawScene_CommonGoal2
    //12-15
            new GUI_Card(CardXSize, CardYSize, BG_Hand1Card_XPos + (3*CardXSize/2), BG_Hand1Card_YPos, null), //DrawScene_HandCard1_Space
            new GUI_Card(CardXSize, CardYSize, BG_Hand1Card_XPos + 2*(3*CardXSize/2), BG_Hand1Card_YPos, null), //DrawScene_HandCard2_Space
            new GUI_Card(CardXSize, CardYSize, BG_Hand1Card_XPos + 3*(3*CardXSize/2), BG_Hand1Card_YPos, null), //DrawScene_HandCard3_Space

            new GUI_Card(CardXSize, CardYSize, BG_Hand1Card_XPos, BG_Hand1Card_YPos, null), //DrawScene_PrivateGoal1



    //16-19
            new GUI_Image(xWindowSize/6,xWindowSize/3,xWindowSize-xWindowSize/6,0, null), //PlayScene_deck
            new GUI_Image(xWindowSize/6,xWindowSize/3,xWindowSize - 2*xWindowSize/6,0, pointBoardImage), //PlayScene_pointBoard
            new GUI_Image(xWindowSize - xWindowSize/3,yWindowSize, 0,0, null), //PlayScene_PlayerBoard
            new GUI_Image(xWindowSize/3,yWindowSize - xWindowSize/3,(2*xWindowSize)/3,xWindowSize/3 , null),//PlayScene_PlayerHand

    //20-21
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos, Hand_CommonGoal1_YPos + CardYDelta + CardYSize/2, null), //PlayScene_GoldDeck_Space
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos + xWindowSize/12, Hand_CommonGoal1_YPos + CardYDelta + CardYSize/2, null), //PlayScene_Deck_Space
    //22-27
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos, Hand_CommonGoal1_YPos + 2* CardYDelta + CardYSize/2, null), //PlayScene_Card1_Space
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos + xWindowSize/12, Hand_CommonGoal1_YPos + 2* CardYDelta + CardYSize/2, null), //PlayScene_Card2_Space
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos, Hand_CommonGoal1_YPos + 3* CardYDelta + CardYSize/2, null), //PlayScene_Card3_Space
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos + xWindowSize/12, Hand_CommonGoal1_YPos + 3* CardYDelta + CardYSize/2, null), //PlayScene_Card4_Space

            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos, Hand_CommonGoal1_YPos, null), //PlayScene_CommonGoal1
            new GUI_Card(CardXSize, CardYSize, Hand_CommonGoal1_XPos + xWindowSize/12, Hand_CommonGoal1_YPos, null), //PlayScene_CommonGoal2
    //28-33
            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos, Hand_Hand1Card_YPos, null), //PlayScene_HandCard1_Space
            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos, Hand_Hand1Card_YPos + Hand_CardYSize + Hand_CardYSize/8, null), //PlayScene_HandCard2_Space
            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos, Hand_Hand1Card_YPos + 2*Hand_CardYSize+ 2*Hand_CardYSize/8, null), //PlayScene_HandCard3_Space

            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos - 3*Hand_CardXSize/2, Hand_Hand1Card_YPos, null), //PlayScene_PrivateGoal1
            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos - 3*Hand_CardXSize/2, Hand_Hand1Card_YPos + Hand_CardYSize + Hand_CardYSize/8, null), //PlayScene_PrivateGoal2
            new GUI_Card(Hand_CardXSize, Hand_CardYSize, Hand_Hand1Card_XPos - 3*Hand_CardXSize/2, Hand_Hand1Card_YPos + 2*(Hand_CardYSize + Hand_CardYSize/8), null), //PlayScene_StartingCard

    //34-37
            new GUI_Image(xWindowSize/4, yWindowSize/10, xWindowSize/2-xWindowSize/8, yWindowSize/4, MenuImages[0]), //NewMultiplayerGame
            new GUI_Image(xWindowSize/4, yWindowSize/10, xWindowSize/2-xWindowSize/8, 2*yWindowSize/4 - yWindowSize/10, MenuImages[1]), //NewLocalGame
            new GUI_Image(xWindowSize/10, yWindowSize/10, xWindowSize/2-xWindowSize/8, 3*yWindowSize/4 - yWindowSize/5, MenuImages[2]), //Options
            new GUI_Image(xWindowSize/10, yWindowSize/10, xWindowSize/2+xWindowSize/40 , 3*yWindowSize/4 - yWindowSize/5, MenuImages[3]), //Quit
    //38-40
            new GUI_Image(xWindowSize/10, yWindowSize/10, xWindowSize/2-xWindowSize/10, yWindowSize/4, MenuImages[4] ),//2PlayersButton_local
            new GUI_Image(xWindowSize/10, yWindowSize/10, xWindowSize/2-xWindowSize/10, 2*yWindowSize/4 - yWindowSize/10, MenuImages[5]),//3PlayersButton_local
            new GUI_Image(xWindowSize/10, yWindowSize/10, xWindowSize/2-xWindowSize/10, 3*yWindowSize/4 - yWindowSize/5, MenuImages[6]),//4PlayersButton_local
    };

    public static GUI_object[] getGUI() { return GUI; }

    public static void renderGUI(Graphics g, String scene)
    {
        int index=0;

        switch(scene)
        {
            case "Main_Menu": for(index=34; index<38; index++){GUI[index].renderObject(g);}
                break;
            case "NewLocalGame_Player_Selection": for(index=38; index<41; index++){GUI[index].renderObject(g);}
                break;


            case "Draw": for(index=0; index<16; index++){GUI[index].renderObject(g);}
                break;
            case "Play", "Choose_Goal": for(index=16; index<34; index++){GUI[index].renderObject(g);}
                break;
        }
    }

    public static void updateGUI()
    {
        int[] publicCards = Client_IO.requestPublicCardsID();

        for(int CardIndex = 4; CardIndex<12; CardIndex++ )
        {((GUI_Card) GUI[CardIndex]).setCard(Deck.getCardBYid(publicCards[CardIndex-4]), publicCards[CardIndex-4]<0);
            ((GUI_Card) GUI[CardIndex+16]).setCard(Deck.getCardBYid(publicCards[CardIndex-4]), publicCards[CardIndex-4]<0);}

        int[] PlayerHand = Client_IO.requestPlayerHand();

        for(int HandIndex = 12; HandIndex<=15; HandIndex++ )
        {((GUI_Card) GUI[HandIndex]).setCard(Deck.getCardBYid(PlayerHand[HandIndex-12]), PlayerHand[HandIndex-12]<0);
            ((GUI_Card) GUI[HandIndex+16]).setCard(Deck.getCardBYid(PlayerHand[HandIndex-12]), PlayerHand[HandIndex-12]<0);}

        ((GUI_Card) GUI[32]).setCard(Deck.getCardBYid(PlayerHand[5]), PlayerHand[5]<0);
        ((GUI_Card) GUI[33]).setCard(Deck.getCardBYid(PlayerHand[4]), PlayerHand[4]<0);


    }

}
