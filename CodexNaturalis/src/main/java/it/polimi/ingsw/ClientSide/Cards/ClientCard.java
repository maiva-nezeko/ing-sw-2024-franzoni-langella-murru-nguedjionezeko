package it.polimi.ingsw.ClientSide.Cards;


import it.polimi.ingsw.ClientSide.Cards.Enums.*;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Client card works alongside the TUI class to ensure Cards are rendered correctly.
 * @author Darelle Maiva Nguedjio Nezeko, Edoardo Carlo MUrru
 */
public class ClientCard {


    /**
     *
     */
    String ImagePath;

    int[] Corners;

    /**
     * The corners of the Flipped card.
     */
    int[] AltCorners;

    int id;

    int color;

    private final Image BackgroundImage; private final Image referenceImage;

    /**
     * The grid of played Cards rendered as symbols in TUI.
     */
    final String[][] Text = new String[10][9];

    /**
     * The resources found on the Cards associated to a symbol, listed in an array.
     */
    final String[] Emojis = {" ", "\033[0;107m"+"0"+ANSI_RESET, ANSI_RED+"&"+ANSI_RESET, ANSI_BLUE+"$"+ANSI_RESET, ANSI_GREEN+"%"+ANSI_RESET, ANSI_PURPLE+"#"+ANSI_RESET,
            ANSI_YELLOW+"~"+ANSI_RESET, ANSI_YELLOW+"*"+ANSI_RESET, ANSI_YELLOW+"@"+ANSI_RESET,
            "-", "|", ".", "+" };
    //empty, blank⬜, mushroom🍄‍, wolf🐺, leaf🍃, butterfly🦋, feather🕊️, salt🧂, paper📜, Dash, UPDash, point, plus
      //String Dash = Emojis[9]
      //String UP = Emojis[10]
      //String point = Emojis[11]
      //String plus = Emojis[12]


    /**
     * The Number associated with a resource - for calculating scorePoints, listed in an int array.
     */
    final String[] NumberEmojis = {"0", "1", "2", "3", "4", "5"};


    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String[] Colors = new String[]{ANSI_RED, ANSI_BLUE, ANSI_GREEN, ANSI_PURPLE, ANSI_YELLOW, ANSI_RESET };

    /**
     * Instantiates a new Client card.
     *
     * @param ImagePath      the image path
     * @param Corners        the Card corners
     * @param altCorners     the flipped corners
     * @param pointCond      the Point condition
     * @param playCond       the Play condition
     * @param startingPoints the starting points
     * @param goalState      the goal state
     * @param id             the ID
     */
    public ClientCard(String ImagePath, int[] Corners, int[] altCorners,
                      PointCondition pointCond, PlayCondition playCond,  StartingPoints startingPoints, GoalStates goalState, int id) {


        BufferedImage rI = null;


        try {
            rI = ImageIO.read(new File(ImagePath));
        } catch (java.io.IOException e) {
            System.out.println("error");
        }

        int SubImg_yCoord;

        if (id <= 80) {
            SubImg_yCoord = ((id - 1) % 10) * 378;
        } else if (id < 200) {
            SubImg_yCoord = (id - 100) * 378;
        } else {
            SubImg_yCoord = (id - 200) * 378;
        }


        assert rI != null;
        this.BackgroundImage = rI.getSubimage(0, SubImg_yCoord, 544, 378);
        this.referenceImage = rI.getSubimage(544, SubImg_yCoord, 544, 378);


        this.ImagePath = ImagePath;
        this.id = id;
        this.Corners = Corners;

        this.color = (id-1)/10;
        if(this.color>=4){ this.color-=4;} //0 Red, 1 Blue, 2 Green, 3 Purple, 4 goal, 5 starting
        if(id>=100){this.color = 4;}
        if(id>=200){this.color = 5;}

        if(altCorners!=null){this.AltCorners = altCorners;}
        else{this.AltCorners = new int[]{1,1,1,1}; }

        setText(goalState, pointCond, playCond, startingPoints);

    }

    /**
     * Gets the reference image.
     *
     * @return the image
     */
    public Image getReferenceImage(){return referenceImage;}

    /**
     * Gets the background image.
     *
     * @return the image
     */
    public Image getBackgroundImage(){return BackgroundImage;}

    /**
     * Gets the Card id.
     *
     * @return the id as an int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get text as in the TUI PlayBoard made of characters and symbols.
     *
     * @param isFlipped the boolean indicating if a Card is flipped
     * @return the PlayBoard as a string matrix
     */
    public String[][] getText(boolean isFlipped) {
        if(isFlipped){ return new String[][] {Text[5], Text[6], Text[7], Text[8], Text[9]}; }
        return new String[][] {Text[0], Text[1], Text[2], Text[3], Text[4]};  }


    /**
     * Gets color of a Card.
     *
     * @return the color
     * @deprecated PlayableCard already has a method to get its color;
     *             PlayableCard and ClientCard are linked!
     */
    public int getColor() { return this.color; }

    /**
     * Sets a Card's Borders in place in the TUI Player's view of their Personal Cards and the Common Cards.
     *
     * @param goalState the GoalCard condition, if present
     * @param pointCond the condition to get Points, if present
     * @param playCond the condition to play the Card, if present
     * @param startingPoints the Starting Card details, if applicable
     */
    private void setText(GoalStates goalState, PointCondition pointCond, PlayCondition playCond, StartingPoints startingPoints)
    {
        Text[0] = new String[]{"+",Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],"+"};
        Text[2] = new String[]{Emojis[10], " ", " ", " ", ".", " ", " ", " ",Emojis[10]};
        Text[4] = new String[]{"+","-","-","-","-","-","-","-","+"};

        Text[5] = new String[]{"+",Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],"+"};
        Text[9] = new String[]{"+",Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],"+"};

        if(goalState != null){ setGoal(goalState); }
        else{ setPlayableText(pointCond, playCond, startingPoints); }

        fixText();

    }

    /**
     * The Card borders are painted of the correct Card color, after being rendered.
     */
    private void fixText() {

        for(int RowIndex =0; RowIndex<Text.length; RowIndex++)
        {
            for(int charIndex =0; charIndex<Text[0].length; charIndex++)
            {
                switch (Text[RowIndex][charIndex])
                {
                    case "+","|","-","." -> Text[RowIndex][charIndex] = Colors[this.color]+Text[RowIndex][charIndex]+ANSI_RESET;
                }
            }
        }

    }

    /**
     * Sets Cards Resources in Corners for Playable Cards. Different implementation for every case.
     *
     * @param pointCond the condition to get Points, if present
     * @param playCond the condition to play the Card, if present
     * @param startingPoints the Starting Card details, if applicable
     */
    private void setPlayableText(PointCondition pointCond, PlayCondition playCond, StartingPoints startingPoints) {

        if(pointCond==null) {
            Text[1] = new String[]{Emojis[10], Emojis[Corners[0]], " ", " ", " ", " ", " ", Emojis[Corners[1]], Emojis[10]};
        }
        else { setPointCondition(pointCond) ;}

        if(playCond == null)
        {
            Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", " ", " ", " ", " ", Emojis[Corners[3]], Emojis[10]  };
        }
        else { setPlayCondition(playCond); }

        Text[6] = new String[]{ Emojis[10],Emojis[AltCorners[0]], " ", " ", " ", " ", " ", Emojis[AltCorners[1]], Emojis[10] };

        if(startingPoints == null){ Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[color+2], " ", " ", " ", Emojis[10] }; }
        else{ setStartingPoints(startingPoints );}

        Text[8] = new String[]{ Emojis[10],Emojis[AltCorners[1]], " ", " ", " ", " ", " ", Emojis[AltCorners[2]], Emojis[10] };

    }

    /**
     * Rendering Corners of Cards that give points after a condition is verified, with possible
     * number of Points given and linked condition combination listed.
     * All possibilities have a different switch case implementation, so to cover all PointConditions.
     *
     * @param pointCond the condition to get Points
     */
    private void setPointCondition(PointCondition pointCond) {

        switch (pointCond)
        {
            case ONE_POINT_FLAT ->      Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", " ", NumberEmojis[1], " ", " ", Emojis[Corners[1]], Emojis[10]  };
            case THREE_POINT_FLAT ->    Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", " ", NumberEmojis[3], " ", " ", Emojis[Corners[1]], Emojis[10]  };
            case FIVE_POINT_FLAT ->     Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", " ", NumberEmojis[5], " ", " ", Emojis[Corners[1]], Emojis[10]  };
            case ONE_FOR_FEATHER ->     Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", NumberEmojis[1], " ", Emojis[6], " ", Emojis[Corners[1]], Emojis[10] };
            case ONE_FOR_SALT ->        Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", NumberEmojis[1], " ", Emojis[7], " ", Emojis[Corners[1]], Emojis[10] };
            case ONE_FOR_PAPER ->       Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", NumberEmojis[1], " ", Emojis[8], " ", Emojis[Corners[1]], Emojis[10]  };
            case TWO_FOR_CORNER ->      Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", NumberEmojis[2], " ", Emojis[1], " ", Emojis[Corners[1]], Emojis[10]  };

        }

    }

    /**
     * Rendering Corners of Cards that need a number of resources in PlayBoard to be present,
     * before being played.
     * All possibilities have a different switch case implementation, so to cover all PlayConditions.
     *
     * @param playCond the condition to play the Card
     */
    private void setPlayCondition(PlayCondition playCond) {

        switch (playCond){
            case TWO_SAME_ONE_RED ->    Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[2], Emojis[color+2], Emojis[2], " ", Emojis[Corners[3]], Emojis[10]  };
            case TWO_SAME_ONE_BLUE ->   Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[2], Emojis[color+2], Emojis[3], " ", Emojis[Corners[3]], Emojis[10]  };
            case TWO_SAME_ONE_GREEN ->  Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[2], Emojis[color+2], Emojis[4], " ", Emojis[Corners[3]], Emojis[10] };
            case TWO_SAME_ONE_PURPLE -> Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[2], Emojis[color+2], Emojis[5], " ", Emojis[Corners[3]], Emojis[10] };

            case THREE_SAME -> Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[3], " ", Emojis[color+2], " ", Emojis[Corners[3]], Emojis[10] };

            case THREE_SAME_ONE_RED ->      Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[3], Emojis[color+2], Emojis[2], " ", Emojis[Corners[3]], Emojis[10] };
            case THREE_SAME_ONE_BLUE ->     Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[3], Emojis[color+2], Emojis[3], " ", Emojis[Corners[3]], Emojis[10] };
            case THREE_SAME_ONE_GREEN ->    Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[3], Emojis[color+2], Emojis[4], " ", Emojis[Corners[3]], Emojis[10] };
            case THREE_SAME_ONE_PURPLE ->   Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[3], Emojis[color+2], Emojis[5], " ", Emojis[Corners[3]], Emojis[10] };

            case FIVE_SAME -> Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", NumberEmojis[5], " ", Emojis[color+2], " ", Emojis[Corners[3]], Emojis[10] };

        }
    }

    /**
     * Rendering Starting Cards Corners.
     * All different six Cards are implemented with a different switch case.
     *
     * @param startingPoints the specific Starting Card to paint
     */
    private void setStartingPoints(StartingPoints startingPoints) {

        switch (startingPoints){
            case STARTING_ONE_PURPLE -> Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[5], " ", " ", " ", Emojis[10] };
            case STARTING_ONE_RED ->    Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[2], " ", " ", " ", Emojis[10] };

            case STARTING_ONE_RED_ONE_GREEN ->      Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[2], ".", Emojis[4], " ", " ", Emojis[10] };
            case STARTING_ONE_BLUE_ONE_PURPLE ->    Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[3], ".", Emojis[5], " ", " ", Emojis[10] };

            case STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN ->  Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[3], Emojis[5], Emojis[4], " ", " ", Emojis[10] };
            case STARTING_ONE_GREEN_ONE_BLUE_ONE_RED ->     Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[4], Emojis[3], Emojis[2], " ", " ", Emojis[10] };
        }

    }


    /**
     * Rendering Goal Cards Corners.
     * All the different sixteen Cards are implemented with a different switch case.
     *
     * @param goalState the GoalCard condition
     */
    private void setGoal(GoalStates goalState) {

        Text[1] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};
        Text[3] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};

        switch (goalState) {
            case TWO_FOR_TWO_FEATHERS ->        Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[2], Emojis[6], " ", Emojis[10] };
            case TWO_FOR_TWO_SALT ->            Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[2], Emojis[7], " ", Emojis[10] };
            case TWO_FOR_TWO_PAPER ->           Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[2], Emojis[8], " ", Emojis[10] };

            case THREE_FOR_COMBO ->             Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };

            case TWO_FOR_THREE_FUNGUS ->        Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[3], Emojis[2], " ", Emojis[10] };
            case TWO_FOR_THREE_WOLF ->          Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[3], Emojis[3], " ", Emojis[10] };
            case TWO_FOR_THREE_LEAF ->          Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[3], Emojis[4], " ", Emojis[10] };
            case TWO_FOR_THREE_BUTTERFLY ->     Text[2] = new String[]{ Emojis[10]," ", NumberEmojis[2], " ", "x", NumberEmojis[3], Emojis[5], " ", Emojis[10] };

            case TWO_FOR_RED_STAIRCASE ->       Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case TWO_FOR_BLUE_STAIRCASE ->      Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case TWO_FOR_GREEN_STAIRCASE ->     Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case TWO_FOR_PURPLE_STAIRCASE ->    Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };

            case THREE_FOR_RED_L ->             Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case THREE_FOR_BLUE_L ->            Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case THREE_FOR_GREEN_L ->           Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
            case THREE_FOR_PURPLE_L ->          Text[2] = new String[]{ Emojis[10]," ", " ", " ", " ", " ", " ", " ", Emojis[10] };
        }


        Text[6] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};
        Text[7] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};
        Text[8] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};


    }

}
