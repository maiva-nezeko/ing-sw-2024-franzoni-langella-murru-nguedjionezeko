package it.polimi.ingsw.ServerSide;

import it.polimi.ingsw.ServerSide.MainClasses.Game;
import it.polimi.ingsw.ServerSide.UpdateClasses.ClientHandler;
import it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The Game server as in the mean for the Server communication via Socket.
 * @author Edoardo Carlo Murru
 */
public class GameServer implements Runnable{

    private final int port;
    private DatagramSocket socket;
    private final Game game;
    private boolean running;

    private int TimeoutNumber=-1;
    public  void shutDown(){ running=false; socket.close();}


    /**
     * Instantiates a new Game server.
     *
     * @param Port where the Socket communication happens.
     */
    public GameServer(int Port, Game game)
    {
        this.port = Port;
        this.running = true;
        this.game = game;

        try{
            this.socket = new DatagramSocket(Port);
        }catch (SocketException e){e.printStackTrace();}


    }

    /**
     * Unit of time definition 'second' in conversion from nanoseconds (System. nano time)
     */
    static final double second = (1000000000.0);
    /*
     * The maximum Time per each player's turn. Once previousTime is greater
     * or equal to timePerTurn, the player skips that turn.
     */
    static final double timePerTurn = 120*second;
    /**
     * The Previous time tracking the time passed.
     */
    static long previousTime;


    public void resetTimer(boolean fromGameServer){
        previousTime = System.nanoTime();

        if(fromGameServer){
            TimeoutNumber++;
        }
        else {
            TimeoutNumber=0;
            ServerConstants.printMessageLn("Timer reset");
        }

        if( ( this.game.getPlayerCount()>1 )
                && ( TimeoutNumber == this.game.getPlayerCount()-1 +10) ){
            System.out.println("Playercount: "+this.game.getPlayerCount()+" Timeout: "+TimeoutNumber);
            this.game.end();
        }
    }


    /**
     * Starts a new Socket Server port communication and notifies the Client according to GameStates, connection
     * attempts, update requests and more occurrences.
     */
    public void run() {

        ServerConstants.printMessageLn("Starting Server on port: "+port);
        previousTime = System.nanoTime();

        while (this.running) {

            if (this.game != null && this.game.isGameStarted()) {
                if (System.nanoTime() >= previousTime + timePerTurn) {
                    ServerConstants.printMessageLn("Turn timer Expired");
                    if (TimeoutNumber >= 0) {
                        game.changePlayerTurn();
                    }
                    resetTimer(true);

                }
            }

            //SocketCommands
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                if (this.running) {
                    socket.receive(packet);
                    ClientHandler packetHandler = new ClientHandler(packet, game, socket);
                    Thread clientThread = new Thread(packetHandler);
                    clientThread.start();

                }
            } catch (IOException e) {
                if (this.running) {
                    throw new RuntimeException(e);
                } else {
                    break;
                }
            }


        }

    }


}
