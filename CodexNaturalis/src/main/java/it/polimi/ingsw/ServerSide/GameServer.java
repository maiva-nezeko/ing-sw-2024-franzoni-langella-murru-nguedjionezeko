package main.java.it.polimi.ingsw.ServerSide;

import main.java.it.polimi.ingsw.ServerSide.MainClasses.Game;
import main.java.it.polimi.ingsw.ServerSide.MainClasses.MultipleGameManager;
import main.java.it.polimi.ingsw.ServerSide.Utility.ServerConstants;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The Game server as in the mean for the Server communication via Socket.
 */
public class GameServer extends Thread{

    private int port;
    private DatagramSocket socket;
    private int IntegerInString;
    private final Game game;
    private boolean running;
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
     * The maximum Time per each player's turn. Once previousTime is greater
     * or equal to timePerTurn, the player skips that turn.
     */
    static double timePerTurn = (100000000.0);
    /**
     * The Previous time tracking the time passed.
     */
    static long previousTime = System.nanoTime();


    public void run() {

        System.out.println("Starting Server on port: "+port);

        if(System.nanoTime() == previousTime+timePerTurn && game!=null){
            System.out.println("Turn timer Expired"); previousTime=System.nanoTime();
            game.changePlayerTurn();
        }

        while (this.running) {



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
            //System.out.println("Client ["+packet.getAddress() +" "+ packet.getPort()  + "] > " + Arrays.toString(message));

            String ack = "ack";
            String username = message[1];
            String response;

            Game game = MultipleGameManager.getGameInstance(username);
            assert game!= null;

            switch (message[0]) {

                case "SendUpdate":
                    String Update = Server_IO.SocketUpdate(username);
                    if(ServerConstants.getDebug()){System.out.println("Update:\n"+Update);}
                    sendData(Update.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case  "GameStartedStatus":
                    if(game.isGameStarted())
                    {sendData("true".getBytes(), packet.getAddress(), packet.getPort());}
                    else{sendData("false".getBytes(), packet.getAddress(), packet.getPort());}
                    break;

                case  "SendCurrentTurn":
                    if(game.getCurrentPlayerTurn() == game.getPlayerNumber(username))
                    {sendData("true".getBytes(), packet.getAddress(), packet.getPort());}
                    else{sendData("false".getBytes(), packet.getAddress(), packet.getPort());}
                    break;


                //modifiers
                case "Flip":
                    IntegerInString = Integer.parseInt(message[2]);
                    Server_IO.Flip(IntegerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "Draw":
                    IntegerInString = Integer.parseInt(message[2]);
                    Server_IO.DrawCard(IntegerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "ChooseGoalCard":
                    IntegerInString = Integer.parseInt(message[2]);
                    Server_IO.ChooseGoalCard(IntegerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "PlaceStartingCard":
                    IntegerInString = Integer.parseInt(message[2]);
                    System.out.println("Placed StartingCard");
                    Server_IO.PlaceStartingCard(IntegerInString, username);
                    sendData(ack.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "playCardByIndex":
                    System.out.println("Requested Play " + message[2] + " " + message[3] + " " + message[4]);
                    String returnValue = "false";

                    BigInteger flag = BigInteger.valueOf(0);
                    if(Server_IO.PlayCardByIndex(Integer.parseInt(message[2]), Integer.parseInt(message[3]),
                            Integer.parseInt(message[4]), username)){returnValue = "true";}

                    sendData(returnValue.getBytes(), packet.getAddress(), packet.getPort());
                    break;

                case "getNewPort":
                    System.out.println("NewSocketPort = " + game.getPort());
                    String newPort = "" + game.getPort();
                    sendData(newPort.getBytes(), packet.getAddress(), packet.getPort());
                    break;


                case "JoinPackage":

                    if(MultipleGameManager.getGameInstance(username)!=null){ response = "Connection Failed: username already present"; }
                    else{ MultipleGameManager.JoinGame(username); response = "Joining";}
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
