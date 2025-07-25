package it.polimi.ingsw.ClientSide.MainClasses;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.Controller.Shortcuts;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.GUI_Render.GameWindow;
import it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import it.polimi.ingsw.ClientSide.GameClient;
import it.polimi.ingsw.ClientSide.TUI_Render.TUI;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import javax.swing.*;
import java.nio.file.FileSystems;
import java.util.Scanner;

/**
 * Manages scenes changes and GUI choices from the Client's perspective as a whole, allowing
 * the Player key actions such as Joining a Game and Setting a username in GUI.
 * @author Edoardo Carlo Murru, Darelle Maiva Nguedjio Nezeko
 */
public class Client_Game implements Runnable {

    private static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    private static GamePanel gamePanel;

    public static GamePanel getGamePanel() {
        return gamePanel;
    }


    private static GameStates CurrentScene = GameStates.MAIN_MENU;

    /**
     * Changes CurrentScene constant to displayed scene.
     *
     * @param scene the string referencing the new scene
     */
    public static void  ChangeScene(GameStates scene){CurrentScene = scene;}

    /**
     * Gets the current scene as a string from GameStates enumeration.
     *
     * @return the scene
     */
    public static GameStates getCurrentScene(){return CurrentScene;}


    /**
     * Instantiates a new Client game, with a new GamePanel (a new window), sends the message that a window is being
     * constructed and calls for Game loop to start, after filling all empty Card slots for the Client.
     */
    public Client_Game() {

        SetTech();

        if(ClientConstants.getGUI()){
            gamePanel = new GamePanel();
            new GameWindow(gamePanel);

            gamePanel.requestFocus();
            System.out.println("Constructing Window ");

            RenderPlayer.fillEmpty_Grid();

            startGameLoop();
        }

        else {
            startGameLoop();
            JoinGame();
        }



    }

    /**
     * Sets username through Keyboard inputs when Joining: checks that the typed username doesn't contain special characters,
     * then notifies that the username has been successfully set.
     *
     * @param additionalInfo the info to send to a Client that is joining a Game
     *                       The info sent could be a message that indicates if a username
     *                       is already present, for example.
     * @see Client_Game#JoinGame()
     */
    public static void SetUsername(String additionalInfo)
    {
        Scanner scanner = new Scanner(System.in);
        String userName = "";

        boolean correctUsername = false;
        while (!correctUsername) {

            System.out.println(additionalInfo);
            System.out.println("Enter username");
             userName = scanner.nextLine();

            if(userName.matches("^[a-zA-Z0-9]+$")){ correctUsername = true; }
            else{ System.out.println("Please use only letters or numbers"); }

        }

        System.out.println("Username set");
        Client_IO.setUsername(userName);
    }

    /**
     * Sets the communication technology a Player wants to use when Joining between Socket and RMI.
     * It is noted that a Game can be played even if the Clients don't all use the same technology.
     * An additional choice for the Game rendering is between the TUI and the GUI.
     * @see Client_Game#JoinGame()
     */
    public static void SetTech()
    {
        Scanner scanner = new Scanner(System.in);
        boolean socketSet = false;
        boolean GUISet = false;

        while(!socketSet) {
            System.out.println("Press [1] to use SocketServer or [2] to use RMI");
            String response = scanner.nextLine();

            if (response.contains("1")){ ClientConstants.SetSocket(true); socketSet=true;}
            else if (response.contains("2")) { ClientConstants.SetSocket(false); socketSet=true; }
        }
        while(!GUISet) {
            System.out.println("Press [1] to use GUI or [2] to use TUI");
            String response = scanner.nextLine();

            if (response.contains("1")){ ClientConstants.setGUI(true); GUISet=true;}
            else if (response.contains("2")) { ClientConstants.setGUI(false); GUISet=true; }
        }
    }

    /**
     * Takes care of all necessary operation to Join a Game, including error messages and failed connection attempts.
     * A Client can choose to create a Game, if no previous Games are saved in the server,
     * to Join an existing game or to Reconnect to a Game they were disconnected to (using the same
     * username they joined with).
     * JoinGame also gets a new Port for communication, requests updates and starts a turn timer before changing
     * scene to 'CHOOSE_GOAL' in a successful Join attempt. Every step is notified to the Player.
     */
    public static void JoinGame()
    {

        String userName = "";
         // Create a Scanner object
        Scanner scanner = new Scanner(System.in);
        String response = "";
        System.out.println(MainDirPAth);
        boolean reconnected = false;

        SetUsername("First you need to insert your Username: ");

        String JoinStatus = Client_IO.JoinGame().trim();
        System.out.println("JoinStatus = "+ JoinStatus);

        while(!JoinStatus.contains("Joining") &&
                !JoinStatus.equals("Reconnecting to previous game") &&
                !JoinStatus.equals("Connection attempt was successful"))
        {

            if(JoinStatus.toLowerCase().contains("connection failed")){
                if(JoinStatus.toLowerCase().contains("username"))
                {
                    System.out.println("Would you like to reconnect to a previous game? y/n");
                    response = scanner.nextLine();
                    if(response.contains("y")){
                        System.out.println("Insert the port number that was given to you when you connected to the previous game: ");
                        response = scanner.nextLine();

                        try{
                            int lastPort = Integer.parseInt(response); JoinStatus = Client_IO.Reconnect(lastPort);
                            reconnected = !JoinStatus.toLowerCase().contains("failed");
                        }
                        catch (NumberFormatException e){ System.out.println("Please insert a recognizable number"); }
                    }
                    else
                    {
                        SetUsername("Change your username please");
                    }
                }
                else {
                    System.out.println("Would you like to create a new game? y/n");
                    response = scanner.nextLine();
                    if (response.contains("y")) {
                        System.out.println("Insert a playerCount between 2 and 4:");

                        int number = 0;
                        response = scanner.nextLine();

                        try{
                            number = Integer.parseInt(response);
                        }catch (NumberFormatException e)
                        {
                            number = 0;
                        }

                        while(number<2 || number>4)
                        {
                            System.out.println("Insert a playerCount between 2 and 4:");

                            response = scanner.nextLine();
                            try{
                                number = Integer.parseInt(response);
                            }catch (NumberFormatException e)
                            {
                                number = 0;
                            }

                        }

                        JoinStatus = Client_IO.CreateGame(number);

                        if (JoinStatus.toLowerCase().contains("username")) {
                            SetUsername("Username already present, please set a new one:");
                        }
                    }
                }


            }
            else {

                if(ClientConstants.getGUI()){ RenderPlayer.fillEmpty_Grid(); }
                System.out.println(MainDirPAth);

                Client_IO.setUsername(userName);

                JoinStatus = Client_IO.JoinGame().trim();

                System.out.println(JoinStatus);
            }
        }

        Client_IO.getNewPort();

        double timePerTurn = (1000000000.0);
        long previousTime = System.nanoTime();

        while(!ClientConstants.isGameStarted())
        {
            if(System.nanoTime() == previousTime+timePerTurn)
            { previousTime = System.nanoTime(); System.out.print("Waiting for game to start: ");
                Client_IO.requestUpdate();
            }

        }

        if(ClientConstants.getGUI())
        {
            Client_IO.requestUpdate();
            ChangeScene(GameStates.CHOOSE_GOAL);
            if(reconnected){ detectScene(); }
        }
        else
        {
            ChangeScene(GameStates.CHOOSE_GOAL);
            if(reconnected){ detectScene(); }

            Client_IO.requestUpdate();
            TUI.renderTUI();
        }


    }

    private static void detectScene() {
        int[] Hand = Client_IO.requestPlayerHand();

        if(Hand.length == 6)
        {
            if(Hand[5] != 0 ){ ChangeScene(GameStates.CHOOSE_GOAL);  }
            else if(Hand[4] != 0){ ChangeScene(GameStates.PLACE_STARTING); }
            else if(Hand[0] == 0 || Hand[1] == 0 || Hand[2] == 0){ ChangeScene(GameStates.DRAW); }
            else{ ChangeScene(GameStates.PLAY); }
        }

    }


    /**
     *  Implementation of the method to start a Game loop.
     *
     *  @see Client_Game#Client_Game()  where it's followed by 'JoinGame' in the TUI case,
     *                                  while we wait for further mouse inputs in the GUI case.
     */
    private void startGameLoop()
    {
        GameClient socketClient = new GameClient(this, ClientConstants.getIp());
        socketClient.start();

        Thread gameThread = new Thread(this);
        gameThread.start();


    }


    @Override
    public void run()
    {
        //duration of each frame in nanoseconds
        int FPS_SET = 15;
        double timePerFrame = (1000000000.0) / FPS_SET;

        long previousTime = System.nanoTime();
        long lastCheck = System.currentTimeMillis();

        double deltaFrames = 0;

        while (true) {

            long currentTime = System.nanoTime();

            //Take Care of logic and reset time
            deltaFrames += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if (deltaFrames >= 1) {
                if (ClientConstants.getGUI()) {
                    gamePanel.repaint();
                }
                deltaFrames--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
            }
        }

    }

    /**
     * End of Game messages according to the assigned GameStates for the Players and
     * suggesting follow-up actions (create a new game).
     *
     * @param gameState the GameState for each Player, between 'YOU_WIN' and 'YOU_LOSE'
     */
    public static void endGame(GameStates gameState) {
        ChangeScene(gameState);
        Scanner scanner = new Scanner(System.in);

        if(gameState.equals(GameStates.YOU_WIN))
        {
            if(ClientConstants.getGUI()){
               int input = JOptionPane.showConfirmDialog(gamePanel, null, "You win last mach! Congratulations!, would you like to get back to main menu?", JOptionPane.YES_NO_OPTION);
               if(input == 0){ Shortcuts.BackToMenu("Have fun!", gamePanel);   return; }
            }
            else {
                System.out.println("You won last game, would you like to play some more? y/n");
                String response = scanner.nextLine();
                if(response.contains("y")){ ChangeScene(GameStates.MAIN_MENU); JoinGame(); return;}
            }
        }
        else
        {
            if(ClientConstants.getGUI()){
                int input =  JOptionPane.showConfirmDialog(gamePanel, null, "You lost last match, unfortunately, would you like to get back to main menu?", JOptionPane.YES_NO_OPTION);
                if(input == 0){ Shortcuts.BackToMenu("Have fun!", gamePanel);   return; }
            }
            else {
                System.out.println("You lost last game, would you like to play some more? y/n");
                String response = scanner.nextLine();
                if(response.contains("y")){ ChangeScene(GameStates.MAIN_MENU); JoinGame();   return; }
            }
        }

        System.exit(1);

    }

}

