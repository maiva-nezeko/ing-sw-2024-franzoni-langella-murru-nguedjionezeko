package main.java.it.polimi.ingsw.ClientSide.Cards;


import main.java.it.polimi.ingsw.ClientSide.Cards.Enums.*;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ClientCard {

    String ImagePath;
    int[] Corners; int[] AltCorners;
    int id; int color;

    private final Image BackgroundImage; private final Image referenceImage;

    String[][] Text = new String[10][9];

    String[] Emojis = {" ", "\033[0;107m"+"0", "\033[41m"+"m", "w", "l", "b", "f", "s", "p", "-", "|", "." };
        //empty, blank⬜, mushroom🍄‍, wolf🐺, leaf🍃, butterfly🦋, feather🕊️, salt🧂, paper📜, Dash, UPDash, point
    String[] NumberEmojis = {"0", "1", "2", "3", "4", "5"};

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







        this.ImagePath = ImagePath; this.id = id;
        this.Corners = Corners;

        this.color = (id-1)/10;
        if(this.color>=4){ this.color-=4;} //0 Red, 1 Blue, 2 Green, 3 Purple, 4 goal, 5 starting
        if(id>=100){this.color = 4;}
        if(id>=200){this.color = 5;}

        if(altCorners!=null){this.AltCorners = altCorners;}
        else{this.AltCorners = new int[]{1,1,1,1}; }

        Text[0] = new String[]{Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9]};
        Text[2] = new String[]{Emojis[10], " ", " ", " ", ".", " ", " ", " ",Emojis[10]};
        Text[4] = new String[]{"-","-","-","-","-","-","-","-","-"};

        Text[5] = new String[]{Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9]};
        Text[9] = new String[]{Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9],Emojis[9]};



        if(goalState==null) {
            if(pointCond==null){
                Text[1] = new String[]{ Emojis[10],Emojis[Corners[0]], " ", " ", " ", " ", " ", Emojis[Corners[1]], Emojis[10] };
            }
            else{ switch (pointCond)
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

            if (playCond == null) {

                Text[3] = new String[]{ Emojis[10],Emojis[Corners[2]], " ", " ", " ", " ", " ", Emojis[Corners[3]], Emojis[10]  };

            }
            else { switch (playCond){
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

            Text[6] = new String[]{ Emojis[10],Emojis[AltCorners[0]], " ", " ", " ", " ", " ", Emojis[AltCorners[1]], Emojis[10] };
            if(startingPoints == null){ Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[color+2], " ", " ", " ", Emojis[10] }; }
            else{
                switch (startingPoints){
                    case STARTING_ONE_PURPLE -> Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[5], " ", " ", " ", Emojis[10] };
                    case STARTING_ONE_RED ->    Text[7] = new String[]{ Emojis[10]," ", " ", " ", Emojis[2], " ", " ", " ", Emojis[10] };

                    case STARTING_ONE_RED_ONE_GREEN ->      Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[2], ".", Emojis[4], " ", " ", Emojis[10] };
                    case STARTING_ONE_BLUE_ONE_PURPLE ->    Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[3], ".", Emojis[5], " ", " ", Emojis[10] };

                    case STARTING_ONE_BLUE_ONE_PURPLE_ONE_GREEN ->  Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[3], Emojis[5], Emojis[4], " ", " ", Emojis[10] };
                    case STARTING_ONE_GREEN_ONE_BLUE_ONE_RED ->     Text[7] = new String[]{ Emojis[10]," ", " ", Emojis[4], Emojis[3], Emojis[2], " ", " ", Emojis[10] };
                }
            }
            Text[8] = new String[]{ Emojis[10],Emojis[AltCorners[1]], " ", " ", " ", " ", " ", Emojis[AltCorners[2]], Emojis[10] };
        }
        else
        {
            Text[1] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};;
            Text[3] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};;

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


            Text[6] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};;
            Text[7] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};;
            Text[8] = new String[]{Emojis[10], " ", " ", " ", " ", " ", " ", " ",Emojis[10]};;

        }

    }

    public Image getReferenceImage(){return referenceImage;}
    public Image getBackgroundImage(){return BackgroundImage;}

    public int getId() {
        return this.id;
    }
    public String getImagePath(){
        return this.ImagePath;
    }

    public String[][] getText(boolean isFlipped) {
        if(isFlipped){ return new String[][] {Text[5], Text[6], Text[7], Text[8], Text[9]}; }
        return new String[][] {Text[0], Text[1], Text[2], Text[3], Text[4]};  }


    public int getColor() { return this.color; }

}
