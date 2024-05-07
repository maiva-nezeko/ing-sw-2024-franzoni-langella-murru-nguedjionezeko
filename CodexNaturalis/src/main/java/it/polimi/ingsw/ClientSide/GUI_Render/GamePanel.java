package main.java.it.polimi.ingsw.ClientSide.GUI_Render;


import main.java.it.polimi.ingsw.ClientSide.Controller.KeyboardInputs;
import main.java.it.polimi.ingsw.ClientSide.Controller.MouseInputs;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final int xWindowSize = ClientConstants.getxWindowSize();
    private final int yWindowSize = ClientConstants.getyWindowSize();

    public GamePanel() {

        this.setPanelSize();
        MouseInputs mouseInputs = new MouseInputs(this);
        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);

    }

    private void setPanelSize() {
        Dimension size = new Dimension(this.xWindowSize, this.yWindowSize);
        this.setPreferredSize(size);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        RenderPlayer.render(this, g, Client_Game.getCurrentScene());

    }
}