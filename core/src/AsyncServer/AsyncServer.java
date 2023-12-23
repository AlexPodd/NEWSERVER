package AsyncServer;

import Server.RoomHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AsyncServer {
    private static final Logger logger = Logger.getLogger("AsyncTCP");
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private LinkedList<SocketChannel> Clients;
    public ConcurrentLinkedQueue ActiveRoomName;
    private RoomHandler roomHandler;
    public AsyncServer(final int PORT) throws IOException {
        FileHandler fileHandler = new FileHandler("assets/Log.log");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        Clients = new LinkedList<>();
        ActiveRoomName = new ConcurrentLinkedQueue();

        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void setRoomHandler(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }

    public void Start(){
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel clientSocketChannel = serverSocketChannel.accept();
                        clientSocketChannel.configureBlocking(false);
                        clientSocketChannel.register(selector, SelectionKey.OP_READ);
                        Clients.add(clientSocketChannel);
                        logger.log(Level.INFO, "Accepted connection from client: " + clientSocketChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        try {
                            int bytesRead = clientSocketChannel.read(buffer);
                        }
                        catch (Exception e) {
                            Clients.remove(clientSocketChannel);
                            logger.info("Connection closed by client: " + clientSocketChannel.getRemoteAddress());
                            logger.info(Clients.toString());
                                key.cancel();
                                clientSocketChannel.close();
                                continue;
                        }
                        if (clientSocketChannel.isConnectionPending()) {
                            clientSocketChannel.finishConnect();
                        }



                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        String inputLine = new String(data);
                        logger.info("Client message: "+inputLine);
                        String outputLine = ReplyMessage(inputLine, String.valueOf(clientSocketChannel.getRemoteAddress()));
                        logger.info("Server message: "+outputLine);
                        ByteBuffer responseBuffer = ByteBuffer.wrap((outputLine).getBytes());
                        clientSocketChannel.write(responseBuffer);
                        if (!responseBuffer.hasRemaining()) {
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            }
        }
        catch (Exception e){
            logger.warning("Exception "+e);
        }
    }
    public String ReplyMessage(String inputLine, String Addr){
        String[] Command = inputLine.split(" ");
        switch (Command[0]){
            case "CreateRoom":
                if(ActiveRoomName.contains(Command[1])){
                    return "NameIsBusy\r\n";
                }
                else {
                    ActiveRoomName.add(Command[1]);
                    roomHandler.AddTcpClient(Command[1], Addr);
                    return "E SN3#70#2130#-1#-1 SN3#455#2120#-1#-1 SN3#427#2590#-1#-1 SN3#283#3023#-1#-1 SN3#967#2851#-1#-1 SN3#968#2726#-1#-1\r\n";
                }
            case "ConnectToRoom":
                if(!ActiveRoomName.contains(Command[1])){
                    return "NameIsNotFound\r\n";
                }
                roomHandler.AddTcpClient(Command[1], Addr);
                return "E SN3#70#2130#-1#-1 SN3#455#2120#-1#-1 SN3#427#2590#-1#-1 SN3#283#3023#-1#-1 SN3#967#2851#-1#-1 SN3#968#2726#-1#-1\r\n";
        }
        return "ReceivedMessage\r\n";
    }
    public synchronized void sendMessageToClient(final String TCPID1, final String TCPID2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (int i = 0; i < Clients.size(); i++) {
                        if(Clients.get(i).getRemoteAddress().toString().equals(TCPID1) || Clients.get(i).getRemoteAddress().toString().equals(TCPID2)){
                            ByteBuffer buffer = ByteBuffer.wrap("StartNewGame\r\n".getBytes());
                            Clients.get(i).write(buffer);
                            logger.info("SendToClient StartNewGame "+Clients.get(i).getRemoteAddress());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
        }).start();
    }

}
