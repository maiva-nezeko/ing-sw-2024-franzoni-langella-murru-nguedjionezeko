package it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import it.polimi.ingsw.ClientSide.Cards.ClientCard;

import java.awt.*;
/**
 * The GUI_card renders/paints physical cards associated with ClientCard in Deck.
 *
 * @see GUI_object;
 */
public class GUI_Card extends GUI_object{

    private ClientCard Card; private boolean isFlipped;

    /**
     * Gui card initialization with its parameters.
     *
     * @param _xSize length of the image int
     * @param _ySize width of the image int
     * @param _xCoord horizontal coordinate of the image int
     * @param _yCoord vertical coordinate of the image int
     * @param card the ClientCard type associated
     */
    public GUI_Card(int _xSize, int _ySize, int _xCoord, int _yCoord, ClientCard card)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);
        this.Card = card; this.isFlipped = false;
    }

    public ClientCard getCard(){return this.Card;}

    /**
     * Sets the card (in a Card Space).
     * @param card the card ID as an int
     * @param isFlipped the boolean indicating if the card is flipped
     */
    public void setCard(ClientCard card, boolean isFlipped) {
        this.Card = card; this.isFlipped = isFlipped;
    }

    /**
     * Gets the reference as an image.
     * @param isFlipped boolean
     * @return the image being referenced
     *
     */
    private Image getReferenceImage(boolean isFlipped) {

        if (isFlipped){return this.getCard().getBackgroundImage() ;}
        return this.getCard().getReferenceImage();

    }


    /**
     * Renders the object.
     * @param g the graphics of the game
     */
    public void renderObject(Graphics g){

        if(this.Card!=null) {
            Image referenceImg = this.getReferenceImage(this.isFlipped);
            if (referenceImg != null) {
                g.drawImage(referenceImg.getScaledInstance(this.xSize, this.ySize, Image.SCALE_DEFAULT), this.xCoord, this.yCoord, null);
            }
            else {g.drawRect(this.xCoord, this.yCoord, this.xSize, this.ySize);}
        }
        else {g.drawRect(this.xCoord, this.yCoord, this.xSize, this.ySize);}
    }

    /**
     * See if the card is flipped as a boolean.
     *
     * @return  the boolean
     */
    public boolean isFlipped() { return this.isFlipped;}
}
