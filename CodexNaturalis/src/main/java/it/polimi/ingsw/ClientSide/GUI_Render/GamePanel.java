package main.java.it.polimi.ingsw.ClientSide.GUI_Render;


import main.java.it.polimi.ingsw.ClientSide.Controller.KeyboardInputs;
import main.java.it.polimi.ingsw.ClientSide.Controller.MouseInputs;
import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.swing.*;
import java.awt.*;

/**
 * The GamePanel class built with Swing JPanel.
 */
public class GamePanel extends JPanel {
    private final int xWindowSize = ClientConstants.getxWindowSize();
    private final int yWindowSize = ClientConstants.getyWindowSize();

    /**
     * GamePanel Builder implementation:
     * sets the Panel size, links Keyboard inputs, links Mouse inputs.
     */
    public GamePanel() {

        this.setPanelSize();
        MouseInputs mouseInputs = new MouseInputs(this);
        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);

    }

    /**
     * Sets Panel size.
     */
    private void setPanelSize() {
        Dimension size = new Dimension(this.xWindowSize, this.yWindowSize);
        this.setPreferredSize(size);
    }


    /**
     * Renders all objects presents in the panel of the game.
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        RenderPlayer.render(this, g);

    }
}