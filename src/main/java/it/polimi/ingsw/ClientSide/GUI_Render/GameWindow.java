package it.polimi.ingsw.ClientSide.GUI_Render;


import javax.swing.*;

/**
 * the type GameWindow
 */
public class GameWindow {

    /**
     * The builder of GameWindow,
     * @param gamePanel the panel of the game
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
