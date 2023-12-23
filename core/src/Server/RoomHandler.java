package Server;

import AsyncServer.AsyncServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RoomHandler {
    private static final Logger logger = Logger.getLogger("RoomLog");
    private NEWUDPServer UDPserver;
    private AsyncServer TCPServer;
    private LinkedList<Room> ActiveRooms;
    public RoomHandler(NEWUDPServer UDPserver, AsyncServer TCPServer) {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("assets/RoomLog.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        this.UDPserver = UDPserver;
        this.ActiveRooms = new LinkedList<>();
        this.TCPServer =TCPServer;
    }
    public void AddTcpClient(String RoomName, String TCPID){
        for (Room room: ActiveRooms) {
            if(room.RoomName.equals(RoomName)){
                if (room.TCPID1!=null&&!room.TCPID2.equals("1")){
                    return;
                }
                logger.info("Add in room "+TCPID);
                room.AddTCP(TCPID);
                return;
            }
        }
        ActiveRooms.add(new Room(RoomName, TCPID));
        logger.info("Create new room "+ActiveRooms.getLast());
    }
    public void AddUdpClient(String TCPID, InetAddress IP, int Port){
        logger.info("First udp add "+ActiveRooms.getLast());
        for(Room room: ActiveRooms){
            if(room.TCPID1.equals(TCPID)){
                logger.info("add first clietn "+TCPID+" "+room.TCPID1);
                room.client1 = new Client(IP, Port);
                room.client1.setNumber(1);
            }
                if(room.TCPID2.equals(TCPID)) {
                    logger.info("add 2 clietn "+TCPID+" "+room.TCPID2);
                    room.client2 = new Client(IP, Port);
                    room.client2.setNumber(2);
                }
            if(room.client1 != null&& room.client2!=null && !room.GameIsGoing){
                logger.info("StartTheGameMessage ");
                UDPserver.AddNewGame(room.client1, room.client2);
                TCPServer.sendMessageToClient(room.TCPID1, room.TCPID2);
                room.GameIsGoing = true;
            }
        }
    }

 private class Room{
        private boolean GameIsGoing;
        private String RoomName;
     private Client client1;
     private Client client2;
     private String TCPID1;
     private String TCPID2;
     public Room(String RoomName, String TCPID1){
         this.RoomName = RoomName;
         this.TCPID1 = TCPID1;
         this.TCPID2 = "1";
         this.GameIsGoing = false;
     }

     public void setGameIsGoing(boolean gameIsGoing) {
         GameIsGoing = gameIsGoing;
     }

     public void AddTCP(String TCPID2){
         this.TCPID2 =TCPID2;
     }
     public String getRoomName() {
         return RoomName;
     }
 }

}
