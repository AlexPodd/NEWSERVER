package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;

public class UDPServer {
    private DatagramSocket socket;
    public UDPServer(){
        socket = null;
        try {
            socket = new DatagramSocket(15914);
            byte[] receiveData = new byte[2048];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                Thread clientThread = new Thread(new ClientHandler(new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength()), receivePacket.getAddress(), receivePacket.getPort()));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
