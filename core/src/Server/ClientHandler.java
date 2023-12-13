package Server;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

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