package Server;

import java.net.InetAddress;

public class Client {
    private InetAddress IP;
    private int Port;
    private int Number;

    public void setNumber(int number) {
        Number = number;
    }

    public int getNumber() {
        return Number;
    }

    public Client(InetAddress IP, int Port){
        this.IP = IP;
        this.Port = Port;
    }

    public synchronized boolean equalClient(Client client){
        return client.IP.toString().equals(this.IP.toString()) && client.Port == this.Port;
    }
}
