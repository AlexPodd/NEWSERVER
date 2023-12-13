package Server;

import Entity.Enemy;
import Entity.Player;
import LvlManager.LvlManager;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger;

public class UDPServer {
    private DatagramSocket socket;
    public LinkedList<GameState> PrevGameState;

    public LinkedList<Client> Clients;

    public static LinkedList<PlayerInput> UnprocessedPlayerInput;

    private static GameState Game;

    private TiledMap map;

    private LinkedList<Enemy> enemies;

    private long LastUpdate;

    private float TickTime;

    private static Checker Pl1Checker;
    private static Checker Pl2Checker;
    public UDPServer(){
        LvlManager lvlManager = new LvlManager();
        map = lvlManager.map;
        Pl1Checker = new Checker();
        Pl2Checker = new Checker();
        TickTime = 0;
        enemies = new LinkedList<>();
        Game = new GameState(new Timestamp(System.currentTimeMillis()), new Player(0,0,300,1,6,16,32,1), new Player(0,0,300,1,6,16,32,1),enemies, map);
        PrevGameState = new LinkedList<>();
        Clients = new LinkedList<>();
        LastUpdate = System.currentTimeMillis();
        UnprocessedPlayerInput = new LinkedList<>();
        socket = null;
        try {
            socket = new DatagramSocket(15914);
            byte[] receiveData = new byte[2048];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                Thread clientThread = new Thread(new ClientHandler(new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength()), receivePacket.getAddress(), receivePacket.getPort()));
                clientThread.start();
                UpdateServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    public synchronized void AddClient(Client client){
        for (Client client1: Clients){
            if(client1.equalClient(client)){
                return;
            }
        }
        Clients.add(client);
        client.setNumber(Clients.size());
    }
    public synchronized int FindClient(InetAddress Ip, int Port){
        Client client1 = new Client(Ip, Port);
        for (Client client: Clients){
            if(client1.equalClient(client)){
                return client.getNumber();
            }
        }
        return 0;
    }

    private void UpdateServer(){
        if (UnprocessedPlayerInput.isEmpty()){
            return;
        }
        if(System.currentTimeMillis()-LastUpdate>49){
            TickTime+=50;
            LastUpdate = System.currentTimeMillis();

           InputProcessing();
            Game.UpdateGame(TickTime);
            PrevGameState.add(Game.createCopy(new Timestamp(System.currentTimeMillis()),Game));

            int LastProcessedZap = 0;
            for (Client client: Clients){
                if (client.getNumber() == 1) {
                    LastProcessedZap = Pl1Checker.getLastZapros();
                }
                if (client.getNumber() == 2) {
                    LastProcessedZap = Pl1Checker.getLastZapros();
                }

                String Message = client.getNumber() + " " +Game.getGameState()+" "+ LastProcessedZap;

                Message = Message + " " + calculateChecksum(Message);
                byte[] sendData = Message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getIP(), client.getPort());
                try {
                    socket.send(sendPacket);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void InputProcessing(){
        Collections.sort(UnprocessedPlayerInput);
        while (!UnprocessedPlayerInput.isEmpty()){
            if (UnprocessedPlayerInput.getFirst().getNumber() == 1){
                if(Pl1Checker.containsKey(UnprocessedPlayerInput.getFirst().getNumZap())){
                    continue;
                }
                else {
                    Pl1Checker.put(UnprocessedPlayerInput.getFirst().getNumZap(), true);
                }
            }
            if (UnprocessedPlayerInput.getFirst().getNumber() == 2){
                if(Pl2Checker.containsKey(UnprocessedPlayerInput.getFirst().getNumZap())){
                    continue;
                }
                else {
                    Pl2Checker.put(UnprocessedPlayerInput.getFirst().getNumZap(), true);
                }
            }
            Game.UpdatePlayerPos(Objects.requireNonNull(UnprocessedPlayerInput.poll()));
        }
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
            if(!verifyChecksum(message, split[split.length-1])){
                return;
            };
            Client client = new Client(IP, Port);
            AddClient(client);
            if (new PlayerInput(message, FindClient(IP, Port)) == null){
                System.out.println(message);
                return;
            }
            UnprocessedPlayerInput.add(new PlayerInput(message, FindClient(IP, Port)));
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
}
