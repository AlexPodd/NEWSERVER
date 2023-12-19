package TCPserer;

import AsyncServer.AsyncServer;
import Server.NEWUDPServer;
import Server.RoomHandler;

import java.io.IOException;

public class Otlad {
    public static void main(String[] args) {
        final NEWUDPServer newudpServer;
        final AsyncServer server;
        final RoomHandler roomHandler;

        try {
            server = new AsyncServer(8081);
            newudpServer = new NEWUDPServer();
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
