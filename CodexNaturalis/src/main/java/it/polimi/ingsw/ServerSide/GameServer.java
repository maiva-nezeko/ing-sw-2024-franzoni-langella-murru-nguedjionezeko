package main.java.it.polimi.ingsw.ServerSide;

import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Objects;

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
                && ( TimeoutNumber == this.game.getPlayerCount()-1 ) ){
            System.out.println("Playercount: "+this.game.getPlayerCount()+" Timeout: "+TimeoutNumber);
            this.game.end();}
    }


    /**
     * Starts a new Socket Server port communication and notifies the Client according to GameStates, connection
     * attempts, update requests and more occurrences.
     */
    public void run() {

        int integerInString;

        ServerConstants.printMessageLn("Starting Server on port: "+port);
        previousTime = System.nanoTime();

        while (this.running) {

            if(this.game!=null && this.game.isGameStarted()) {
                if (System.nanoTime() >= previousTime + timePerTurn) {
                    ServerConstants.printMessageLn("Turn timer Expired");
                    if(TimeoutNumber>=0){
                        game.changePlayerTurn();
                    }
                    resetTimer(true);

                }
            }

            //SocketCommands
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                if(this.running){ socket.receive(packet);}
            } catch (IOException e) {
                if(this.running) { throw new RuntimeException(e);}
                else{break;}
            }
            String[] message = new String(packet.getData()).trim().split(",");
            ServerConstants.printMessageLn(Arrays.toString(message));
            ServerConstants.printMessage("Client ["+packet.getAddress() +" "+ packet.getPort()  + "] > " + Arrays.toString(message));

            String ack = "ack";
            String username = message[1];
            String response;

            switch (message[0]) {

                case "SendUpdate":
                    if(this.game == null){ break; }
                    String Update = Server_IO.SocketUpdate(username);
                    if(ServerConstants.getDebug()){ServerConstants.printMessageLn("Update:\n"+Update);}
                    sendData(Update.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case  "GameStartedStatus":
                    if(this.game == null){ break; }
                    if(game.isGameStarted())
                    {sendData("true".getBytes(), packet.getAddress(), packet.getPort());}
                    else{sendData("false".getBytes(), packet.getAddress(), packet.getPort());}
                    break;

                case  "SendCurrentTurn":
                    if(this.game == null){ break; }
                    response = game.getPlayers().get(game.getCurrentPlayerTurn()).getUsername();
                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                //modifiers
                case "Flip":
                    integerInString = Integer.parseInt(message[2]);
                    Server_IO.Flip(integerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "Draw":
                    if(this.game == null){ break; }

                    integerInString = Integer.parseInt(message[2]);
                    Server_IO.DrawCard(integerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    game.changePlayerTurn();
                    break;

                case "ChooseGoalCard":
                    integerInString = Integer.parseInt(message[2]);
                    Server_IO.ChooseGoalCard(integerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "PlaceStartingCard":
                    integerInString = Integer.parseInt(message[2]);
                    ServerConstants.printMessageLn("Placed StartingCard");
                    Server_IO.PlaceStartingCard(integerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "playCardByIndex":
                    ServerConstants.printMessageLn("Requested Play " + message[2] + " " + message[3] + " " + message[4]);
                    String returnValue = "false";

                    //BigInteger flag = BigInteger.valueOf(0);
                    if(Server_IO.PlayCardByIndex(Integer.parseInt(message[2]), Integer.parseInt(message[3]),
                            Integer.parseInt(message[4]), username)){returnValue = "true";}

                    sendData(returnValue.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "getNewPort":
                    String newPort = "" + Objects.requireNonNull(MultipleGameManager.getGameInstance(username)).getPort();

                    ServerConstants.printMessageLn("NewSocketPort = " + newPort);
                    sendData(newPort.getBytes(), packet.getAddress(), packet.getPort());
                    break;


                case "JoinPackage":
                    response = "Connection failed: no game found";
                    if(MultipleGameManager.getGameInstance(username)!=null){ response = "Connection Failed: username already present"; }
                    else{ if(MultipleGameManager.JoinGame(username)){ response = "Joining";}}
                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "AttemptingReconnection":

                    if(MultipleGameManager.Reconnect(username, Integer.parseInt(message[2]))!=null){ response = "Joining";}
                    else{ response = "Username Not Present";}

                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "CreateGame":
                    if(MultipleGameManager.CreateGame(username, Integer.parseInt(message[2]))){ response = "Joining new Game"; }
                    else{ response = "Creation attempt failed: Server Error or wrong PlayerCount"; }
                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "getUsernames":
                    response = Server_IO.getUsernames(MultipleGameManager.getGameInstance(username));
                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "getCurrentPlayerGrid":
                    if(this.game == null){ break; }

                    response = Server_IO.getGameBoard(game, game.getCurrentPlayerTurn());
                    sendData(response.getBytes(), packet.getAddress(), packet.getPort());
                    break;
                case "isClosed":
                    if(MultipleGameManager.getGameInstance(username)==null){ sendData("yes".getBytes(), packet.getAddress(), packet.getPort()); }
                    else{sendData("no".getBytes(), packet.getAddress(), packet.getPort());}
                    break;
            }


        }

    }

    /**
     * Sends the gathered data.
     *
     * @param data      the data information we want to send as a byte[] type
     * @param ipAddress the ip address of the receiver
     * @param port      the port used for communication
     */
    public void sendData(byte[] data, InetAddress ipAddress, int port)
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
