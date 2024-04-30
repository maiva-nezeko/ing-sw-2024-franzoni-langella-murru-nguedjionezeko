package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Game client.
 */
public class GameClient extends Thread{
    private static InetAddress ipAddress;
    private static DatagramSocket socket;
    private Client_Game game;

    private String message;

    /**
     * Instantiates a new Game client.
     *
     * @param game          the game
     * @param ipAddress_str the ip address str
     */
    public GameClient(Client_Game game, String ipAddress_str)
    {
        this.game = game;

        if(ClientConstants.getSocket()){
            try{
                socket = new DatagramSocket();
                ipAddress = InetAddress.getByName(ipAddress_str);
            }catch (SocketException e){e.printStackTrace();}
            catch (UnknownHostException e){e.printStackTrace();}}

    }

    /**
     * Listen for response string.
     *
     * @param message the message
     * @return the string
     */
    public static String listenForResponse(String message)
    {
        byte[] data = new byte[2048];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        boolean unmodified = true;
        int tries = 0;
        double timePerUpdate = (10000000000.0); long previousTime = System.nanoTime();
        sendData(message.getBytes());

        while (unmodified)
        {
            if(System.nanoTime() == previousTime+timePerUpdate){
                previousTime = System.nanoTime();
                tries ++; sendData(message.getBytes());
                if(tries==5){System.out.println("Unable to reach server"); System.exit(1);}}

            try {
                socket.receive(packet);

                String Response_message = new String (packet.getData(), StandardCharsets.UTF_8);
                //System.out.println("Server >" + Response_message.trim());

                unmodified = false;
                return Response_message.trim();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return packet.toString();


    }

    /**
     * Send data.
     *
     * @param data the data
     */
    public static void sendData(byte[] data)
    {
        if(ClientConstants.getSocket()){
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, ClientConstants.getPort());
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }}
    }

}
