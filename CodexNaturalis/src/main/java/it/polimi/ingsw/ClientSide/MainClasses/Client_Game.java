package main.java.it.polimi.ingsw.ClientSide.MainClasses;

import main.java.it.polimi.ingsw.ClientSide.Client_IO;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GamePanel;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.GameWindow;
import main.java.it.polimi.ingsw.ClientSide.GUI_Render.RenderPlayer;
import main.java.it.polimi.ingsw.ClientSide.GameClient;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.nio.file.FileSystems;
import java.util.Scanner;

/**
 * The type Client game.
 */
public class Client_Game implements Runnable {

    private static final String MainDirPAth = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    private GamePanel gamePanel;
    private final int FPS_SET = 30;


    private static final String[] Scenes = {"Main_Menu", "Game_Menu", "Draw", "Play", "Options", "NewLocalGame_Player_Selection", "Choose_Goal"};
    private static String CurrentScene = "Main_Menu";

    /**
     * Change scene.
     *
     * @param scene the scene
     */
    public static void  ChangeScene(int scene){CurrentScene = Scenes[scene];}

    /**
     * Get current scene string.
     *
     * @return the string
     */
    public static String getCurrentScene(){return CurrentScene;}


    /**
     * Instantiates a new Client game.
     */
    public Client_Game() {

        if(ClientConstants.getGUI()){
            gamePanel = new GamePanel();
            GameWindow gameWindow = new GameWindow(gamePanel);
            gamePanel.requestFocus();
            System.out.println("Constructing Window ");
            startGameLoop();
        }

        else { startGameLoop(); JoinGame(); }



    }

    public static String SetUsername(String additionalInfo)
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

        Client_IO.setUsername(userName);
        return userName;
    }


    /**
     * Join game.
     */
    public static void JoinGame()
    {

        String userName = "";
         // Create a Scanner object
        Scanner scanner = new Scanner(System.in);

        if(ClientConstants.getGUI()){ RenderPlayer.fillEmpty_Grid(); }
        System.out.println(MainDirPAth);

        SetUsername("First you need to insert your Username: ");

        String JoinStatus = Client_IO.JoinGame().trim();
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

                    if(JoinStatus.toLowerCase().contains("username")){SetUsername("Username already present, please set a new one:");}

                }
                else{
                    System.out.println("Would you like to reconnect to a previous game? y/n");
                    response = scanner.nextLine();
                    if(response.contains("y")){
                        System.out.println("Insert the port number that was given to you when you connected to the previous game: ");
                        response = scanner.nextLine();

                        try{ int lastPort = Integer.parseInt(response); JoinStatus = Client_IO.Reconnect(userName, lastPort);}
                        catch (NumberFormatException e){ System.out.println("Please insert a recognizable number"); }
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


        GameClient socketClient = new GameClient(this, ClientConstants.getIp());
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

