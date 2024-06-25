package it.polimi.ingsw.ClientSide;

import it.polimi.ingsw.ClientSide.MainClasses.Client_Game;
import it.polimi.ingsw.ClientSide.Utility.ClientConstants;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Constants and methods used for Socket communication with Server, from a Client's perspective: the IpAddress,
 * the socket, sending and receiving data or messages, etc.
 * @author Edoardo Carlo Murru, Darelle Maiva Nguedjio Nezeko
 */
public class GameClient extends Thread{
    private static InetAddress ipAddress;
    private static DatagramSocket socket;

    /**
     * Instantiates a new Game client by getting a new Socket along with the ip address.
     *
     * @param ignoredGame   the game
     * @param ipAddress_str the ip address string
     */
    public GameClient(Client_Game ignoredGame, String ipAddress_str)
    {

        if(ClientConstants.getSocket()){
            try{
                socket = new DatagramSocket();
                ipAddress = InetAddress.getByName(ipAddress_str);
            }
            catch (SocketException | UnknownHostException e){e.printStackTrace();}
        }

    }

    /**
     * Listens for a message response.
     *
     * @param message   the message
     * @return the response string
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
     * Controls if a given port is closed or can be used for communication by attempting to send and receive a
     * trial message.
     *
     * @param message   the control message
     * @param port      the desired port
     * @return the response string
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
     * Sends Socket data.
     *
     * @param data      the data
     * @throws RuntimeException for socket communication, in case of Runtime errors
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
