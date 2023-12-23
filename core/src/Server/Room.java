package Server;

import Entity.Enemy;
import Entity.Player;
import Entity.Skeleton;
import LvlManager.LvlManager;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Room {
    public LinkedList<GameState> PrevGameState;

    public LinkedList<PlayerInput> UnprocessedPlayerInput;

    private GameState Game;

    private static int[][] map, MapCorrector, DoorMap;

    private LinkedList<Enemy> enemies;

    private float TickTime;

    private Checker Pl1Checker;
    private Checker Pl2Checker;

    private LvlManager lvlManager;

    private long LastUpdate;

    private Client client1;
    private Client client2;
    private NEWUDPServer server;
    public Room(Client client1, Client client2, NEWUDPServer server){
        this.server = server;
        this.client1 = client1;
        this.client2 = client2;
        LastUpdate = System.currentTimeMillis();
        lvlManager = new LvlManager();
        map = lvlManager.getMap();
        MapCorrector = lvlManager.getMapCorrector();
        DoorMap = lvlManager.getOpenDoor();

        Pl1Checker = new Checker();
        Pl2Checker = new Checker();

        TickTime = 0;
        Skeleton skeleton1 = new Skeleton(70,2130, 2000, 0, 3, 16,16 ,1);
        Skeleton skeleton = new Skeleton(455,2120, 2000, 0, 3, 16,16 ,1);
        Skeleton skeleton2 = new Skeleton(427,2590, 2000, 0, 3, 16,16 ,1);
        Skeleton skeleton3 = new Skeleton(283,3023, 2000, 0, 3, 16,16 ,1);
        Skeleton skeleton4 = new Skeleton(967,2851, 2000, 0, 3, 16,16 ,1);
        Skeleton skeleton5 = new Skeleton(968,2726, 2000, 0, 3, 16,16 ,1);
        enemies = new LinkedList<>();
        enemies.add(skeleton);
        enemies.add(skeleton1);
        enemies.add(skeleton2);
        enemies.add(skeleton3);
        enemies.add(skeleton4);
        enemies.add(skeleton5);
        Game = new GameState(new Timestamp(System.currentTimeMillis()), new Player(0,0,300,1,6,16,32,1,DoorMap), new Player(0,0,300,1,6,16,32,1, DoorMap),enemies, map, MapCorrector);
        PrevGameState = new LinkedList<>();

        UnprocessedPlayerInput = new LinkedList<>();

    }

    public long getLastUpdate() {
        return LastUpdate;
    }

    public void UpdateGame() {
        if (UnprocessedPlayerInput.isEmpty()) {
            return;
        }
            LastUpdate = System.currentTimeMillis();
            TickTime += 50;
            InputProcessing();
            Game.UpdateGame(TickTime, lvlManager);
                String Message = client1.getNumber() + " " +Game.getGameState()+" "+ Pl1Checker.getLastZapros()+" ";
                Message+= Game.GetEnemyMessage();
                Message = Message + " " + calculateChecksum(Message);
                server.SendMessage(Message, client1.getIP(), client1.getPort());
            Message = client2.getNumber() + " " +Game.getGameState()+" "+ Pl2Checker.getLastZapros()+" ";
           Message+= Game.GetEnemyMessage();
            Message = Message + " " + calculateChecksum(Message);
            server.SendMessage(Message, client2.getIP(), client2.getPort());
        PrevGameState.add(Game.createCopy(new Timestamp(System.currentTimeMillis()), Game));
        Timestamp oneSecondAgo = new Timestamp(System.currentTimeMillis()-1000);
        if(PrevGameState.getFirst().getTimestamp().before(oneSecondAgo)){
            while (!PrevGameState.getFirst().getProcessedPlayerInput().isEmpty()){
                if (PrevGameState.getFirst().getProcessedPlayerInput().getFirst().getNumber() == 1){
                Pl1Checker.remove(PrevGameState.getFirst().getProcessedPlayerInput().getFirst().getNumZap());
                PrevGameState.getFirst().getProcessedPlayerInput().removeFirst();
                }
                else {
                    Pl2Checker.remove(PrevGameState.getFirst().getProcessedPlayerInput().getFirst().getNumZap());
                    PrevGameState.getFirst().getProcessedPlayerInput().removeFirst();
                }
            }

         PrevGameState.removeFirst();
        }
           }


    private void InputProcessing(){
        while (!UnprocessedPlayerInput.isEmpty()){
            if (UnprocessedPlayerInput.getFirst().getNumber() == 1){
                if(Pl1Checker.containsKey(UnprocessedPlayerInput.getFirst().getNumZap())){
                    UnpInpRemFirst();
                    continue;
                }
                else {
                    Pl1Checker.put(UnprocessedPlayerInput.getFirst().getNumZap(), true);
                }
            }
            if (UnprocessedPlayerInput.getFirst().getNumber() == 2){
                if(Pl2Checker.containsKey(UnprocessedPlayerInput.getFirst().getNumZap())){
                    UnpInpRemFirst();
                    continue;
                }
                else {
                    Pl2Checker.put(UnprocessedPlayerInput.getFirst().getNumZap(), true);
                }
            }
            Game.UpdatePlayerPos(Objects.requireNonNull(UnpInpPoll()));
        }
    }
    private synchronized void UnpInpRemFirst(){
        UnprocessedPlayerInput.removeFirst();
    }
    private synchronized PlayerInput UnpInpPoll(){
        return UnprocessedPlayerInput.poll();
    }
    public synchronized void AddUnpIn(String message, int number){
        UnprocessedPlayerInput.add(new PlayerInput(message, number));
        Collections.sort(UnprocessedPlayerInput);
    }

    public Client getClient1() {
        return client1;
    }

    public Client getClient2() {
        return client2;
    }
    public String calculateChecksum(String message) {
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

