package it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import it.polimi.ingsw.ClientSide.Cards.ClientCard;

import java.awt.*;
/**
 * the type GUI_card
 */
public class GUI_Card extends GUI_object{

    private ClientCard Card; private boolean isFlipped;

    /**
     * Gui card
     *
     * @param _xSize length of the image int
     * @param _ySize width of the image int
     * @param _xCoord horizontal coordinate of the image int
     * @param _yCoord vertical coordinate of the image int
     * @param card ClientCard
     */
    public GUI_Card(int _xSize, int _ySize, int _xCoord, int _yCoord, ClientCard card)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);
        this.Card = card; this.isFlipped = false;
    }
    /**
     * Get card
     *
     * @return the card
     */
    public ClientCard getCard(){return this.Card;}
    /**
     * Set the card ClientCard
     * @param card the card
     * @param isFlipped if the card is flipped or not
     */
    public void setCard(ClientCard card, boolean isFlipped) {
        this.Card = card; this.isFlipped = isFlipped;
    }

    /**
     * Get the reference of the image
     * @param isFlipped boolean
     * @return if the card is flipped, get the background of the card else return the front of the card with the references
     *
     */
    private Image getReferenceImage(boolean isFlipped) {

        if (isFlipped){return this.getCard().getBackgroundImage() ;}
        return this.getCard().getReferenceImage();

    }




    /**
     * Render the object
     * @param g the object that render the graphics of the game
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
     * See if the card is flipped boolean
     *
     * @return  the boolean
     */
    public boolean isFlipped() { return this.isFlipped;}
}
