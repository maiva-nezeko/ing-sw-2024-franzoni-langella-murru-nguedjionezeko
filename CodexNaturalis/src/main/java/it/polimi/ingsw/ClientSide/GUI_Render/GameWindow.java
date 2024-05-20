package main.java.it.polimi.ingsw.ClientSide.GUI_Render;


import javax.swing.*;

/**
 * The Window settings of the Game.
 * @author
 */
public class GameWindow {

    /**
     * Instantiates a new Game window.
     *
     * @param gamePanel the game panel
     */
    public GameWindow(GamePanel gamePanel) {
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.pack();
        jframe.setResizable(false);

        jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jframe.setVisible(true);
    }
}
