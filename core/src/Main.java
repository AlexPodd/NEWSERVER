import AsyncServer.AsyncServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int Port = 8081;
        final AsyncServer server;
        try {
            server = new AsyncServer(Port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread TCPThread = new Thread(new Runnable() {
            @Override
            public void run() {
                    server.Start();
            }
        });
        TCPThread.start();
    }

}
