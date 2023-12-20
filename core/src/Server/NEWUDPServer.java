package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class NEWUDPServer {
    private static final Logger logger = Logger.getLogger("UDPServer");
    private DatagramSocket socket;
    private LinkedList<Room> Games;
    private int Port;
    private RoomHandler roomHandler;
    public NEWUDPServer(int Port) throws IOException {
        FileHandler fileHandler = new FileHandler("assets/LogUDP.log");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        this.Port = Port;
        Games = new LinkedList<>();
        socket = null;
    }
    public void setRoomHandler(RoomHandler roomHandler){
        this.roomHandler = roomHandler;
    }
    public void Start(){
       Receiver();
    }
    public synchronized void UpdateGames(){
        for (Room room: Games){
            if (System.currentTimeMillis()-room.getLastUpdate()>49) {
                room.UpdateGame();
            }
        }
    }
    public synchronized void AddNewGame(Client client1, Client client2){
        Games.add(new Room(client1,client2,this));
        logger.info("Start new Game with "+client1.getIP()+":"+client1.getPort()+client2.getIP()+":"+client2.getPort());
    }
    public synchronized void AddUdpClient(String TCPID, InetAddress UdpIP, int UdpPort){
        roomHandler.AddUdpClient(TCPID, UdpIP, UdpPort);
        logger.info("AddUPD "+TCPID+" "+UdpIP+" "+UdpPort);
    }
    public synchronized void SendMessage(String Message, InetAddress IP, int Port){
        byte[] sendData = Message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IP, Port);
        try {
                    socket.send(sendPacket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
    }
    public class ClientHandler implements Runnable {


        private final String message;
        private final InetAddress IP;
        private final int Port;
        public ClientHandler(String message, InetAddress IP, int Port) {
            this.message = message;
            this.IP = IP;
            this.Port = Port;
        }

        @Override
        public void run() {
            String[] split = message.split(" ");
            if(split[0].equals("Con")){
                logger.info("Connection "+split[1]+" "+IP+" "+Port);
                AddUdpClient(split[1], IP, Port);
                return;
            }

            if(!verifyChecksum(message, split[split.length-1])){
                return;
            };
            for (int i = 0; i < Games.size(); i++) {
                if(Games.get(i).getClient1().equalClient(new Client(IP,Port))){
                    Games.get(i).AddUnpIn(message,1);
              }
                if(Games.get(i).getClient2().equalClient(new Client(IP,Port))){
                    Games.get(i).AddUnpIn(message,2);
                }
            }
        }
        public synchronized boolean verifyChecksum(String message, String checksum) {
            String calculatedChecksum = calculateChecksum(message);
            return calculatedChecksum.equals(checksum);
        }
        public synchronized String calculateChecksum(String message) {
            try {
                String[] Split = message.split(" ");
                String data = "";
                for (int i = 0; i < Split.length-1; i++) {
                    data+=Split[i]+" ";
                }
                data = data.substring(0, data.length() - 1);
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] messageBytes = data.getBytes();
                byte[] checksumBytes = digest.digest(messageBytes);
                StringBuilder checksumBuilder = new StringBuilder();
                for (byte b : checksumBytes) {
                    checksumBuilder.append(String.format("%02x", b));
                }
                return checksumBuilder.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public void Receiver() {
        Thread udpServerReceiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(Port);
                    byte[] receiveData = new byte[2048];

                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        Thread clientThread = new Thread(new ClientHandler(new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength()), receivePacket.getAddress(), receivePacket.getPort()));
                        clientThread.start();
                        UpdateGames();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                }
            }
        });
        udpServerReceiverThread.start();
    }
}
