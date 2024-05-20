package main.java.it.polimi.ingsw.ClientSide;

import main.java.it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import main.java.it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Game client.
 * @author maiva
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
            }
            catch (SocketException e){e.printStackTrace();}
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
        byte[] data = new byte[1024*10];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try {
            sendData(message.getBytes());
            socket.receive(packet);

            String Response_message = new String (packet.getData(), StandardCharsets.UTF_8);
            //System.out.println("Server >" + Response_message.trim());

            return Response_message.trim();

        } catch (IOException e) {
            ClientExceptionHandler.ServerUnreachable(new RuntimeException(e));
        }

        return ("Unable to reach server");
    }

    /**
     *
     * @param message
     * @param port
     * @return
     */
    public static String checkIfClosed(String message, int port) {
        byte[] data = message.getBytes();

        DatagramPacket packet = new DatagramPacket(data, data.length);

        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ipAddress, port);
        try {

            socket.send(sendPacket);
            socket.receive(packet);

            String Response_message = new String (packet.getData(), StandardCharsets.UTF_8);

            return Response_message.trim();

        } catch (IOException e) {
            return ("Unable to reach server");
        }
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
