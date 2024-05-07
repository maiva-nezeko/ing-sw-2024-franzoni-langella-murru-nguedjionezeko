package main.java.it.polimi.ingsw.ClientSide.GUI_Render.GUI_Objects;

import main.java.it.polimi.ingsw.ClientSide.Cards.ClientCard;

import java.awt.*;

public class GUI_Card extends GUI_object{

    private ClientCard Card; private boolean isFlipped;

    public GUI_Card(int _xSize, int _ySize, int _xCoord, int _yCoord, ClientCard card)
    {
        super(_xSize, _ySize, _xCoord, _yCoord);
        this.Card = card; this.isFlipped = false;
    }

    public ClientCard getCard(){return this.Card;}
    public void setCard(ClientCard card, boolean isFlipped) {
        this.Card = card; this.isFlipped = isFlipped;
    }

    private Image getReferenceImage(boolean isFlipped) {

        if (isFlipped){return this.getCard().getBackgroundImage() ;}
        return this.getCard().getReferenceImage();

    }




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

    public boolean isFlipped() { return this.isFlipped;}
}
