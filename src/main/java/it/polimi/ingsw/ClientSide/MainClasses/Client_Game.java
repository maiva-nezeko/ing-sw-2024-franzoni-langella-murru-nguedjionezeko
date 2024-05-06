package it.polimi.ingsw.ClientSide.MainClasses;

import it.polimi.ingsw.ClientSide.Client_IO;
import it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import it.polimi.ingsw.ClientSide.GUI_Render.GameWindow;
import it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import it.polimi.ingsw.ClientSide.GameClient;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.nio.file.FileSystems;
import java.util.Scanner;

/**
 * The Client game. In short, the Game seen from the Clien't point of view.
 */
public class Client_Game implements Runnable {

    private static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    private GamePanel gamePanel;
    private final int FPS_SET = 30;


    private static final String[] Scenes = {"Main_Menu", "Game_Menu", "Draw", "Play", "Options", "NewLocalGame_Player_Selection", "Choose_Goal"};
    private static String CurrentScene = "Main_Menu";

    /**
     * Change scene into one of the ones listed in the Scenes string[].
     *
     * @param scene the option chosen to change into
     */
    public static void  ChangeScene(int scene){CurrentScene = Scenes[scene];}

    /**
     * Get current scene between the ones listed in the Scenes string[].
     *
     * @return the scene as a string
     */
    public static String getCurrentScene(){return CurrentScene;}


    /**
     * Instantiates a new Client game. Constructs a window and starts Game Loop
     * and/or Join Game.
     */
    public Client_Game() {

        if(ClientConstants.getGUI()){
            gamePanel = new GamePanel();
            GameWindow gameWindow = new GameWindow(gamePanel);
            gamePanel.requestFocus();
        }

        if(ClientConstants.getGUI()){
            System.out.println("Constructing Window "); startGameLoop();}
        else { startGameLoop(); JoinGame(); }



    }

    /**
     * Join game method is how a Client enters or creates a Game.
     * It shows the player messages that reflect the possible actions.
     */
    public static void JoinGame()
    {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter username");
        String userName = scanner.nextLine();

        if(ClientConstants.getGUI()){ RenderPlayer.fillEmpty_Grid(); }
        System.out.println(MainDirPAth);

        Client_IO.setUsername(userName);

        String JoinStatus = Client_IO.JoinGame(userName).trim();
        System.out.println("JoinStatus = "+ JoinStatus);

        while(!JoinStatus.contains("Joining") &&
                !JoinStatus.equals("Reconnecting to previous game") &&
                !JoinStatus.equals("Connection attempt was successful"))
        {

            if(JoinStatus.toLowerCase().contains("connection failed")){
                System.out.println("Would you like to create a new game? y/n");
                String response = scanner.nextLine();
                if(response.contains("y")){
                    System.out.println("Insert a playerCount between 1 and 4:");
                    response = scanner.nextLine();
                    JoinStatus = Client_IO.CreateGame(userName, Integer.parseInt(response));
                }
                else{
                    System.out.println("Would you like to reconnect to a previous game? y/n");
                    response = scanner.nextLine();
                    if(response.contains("y")){JoinStatus = Client_IO.Reconnect(userName);}
                }

            }



            else {
                System.out.println("Enter new username");
                userName = scanner.nextLine();

                if(ClientConstants.getGUI()){ RenderPlayer.fillEmpty_Grid(); }
                System.out.println(MainDirPAth);

                Client_IO.setUsername(userName);

                JoinStatus = Client_IO.JoinGame(userName).trim();

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
            Client_IO.requestUpdate();}

        }

        if(ClientConstants.getGUI())
        {
            Client_IO.requestUpdate();
            System.out.println("Changing scene");
            ChangeScene(6);
        }
        else
        {
            ChangeScene(6);
            Client_IO.requestUpdate();
            System.out.println("Changing scene");
        }


    }

    private void startGameLoop()
    {
        Thread gameThread = new Thread(this);
        gameThread.start();


        GameClient socketClient = new GameClient(this, "localhost");
        socketClient.start();

    }


    @Override
    public void run()
    {
        //duration of each frame in nanoseconds
        double timePerFrame = (1000000000.0) / FPS_SET;

        long previousTime = System.nanoTime();
        long lastCheck = System.currentTimeMillis();

        int frames=0;
        double deltaFrames = 0;

        while(true){

            long currentTime = System.nanoTime();

            //Take Care of logic and reset time
            deltaFrames += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if(deltaFrames>= 1){
                if(ClientConstants.getGUI()){ gamePanel.repaint(); }
                frames++; deltaFrames--;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000)
            {
                lastCheck = System.currentTimeMillis();
                frames = 0;
            }
        }

    }

}

