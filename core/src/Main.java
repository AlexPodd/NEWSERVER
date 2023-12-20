import AsyncServer.AsyncServer;
import Server.NEWUDPServer;
import Server.RoomHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int UDPport = 15914;
        int TCPport = 8081;
        final NEWUDPServer newudpServer;
        final AsyncServer server;
        final RoomHandler roomHandler;

        try {
            server = new AsyncServer(TCPport);
            newudpServer = new NEWUDPServer(UDPport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        roomHandler = new RoomHandler(newudpServer, server);
        server.setRoomHandler(roomHandler);
        newudpServer.setRoomHandler(roomHandler);
        Thread udpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                newudpServer.Start();
            }
        });

        Thread tcpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                server.Start();
            }
        });

        udpThread.start();
        tcpThread.start();
    }

}
