package monitor;

import java.net.ServerSocket;
import java.net.Socket;


public class Monitor extends Thread{

    int cost;
    int edgeId;
    public static void main(String[] args) throws Exception{
        int port = 9999;
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Multithreaded Server starts on Port "+port);
        while (true){
            Socket client = listenSocket.accept();
            System.out.println("Connection with: " +     // Output connection
                    client.getRemoteSocketAddress());   // (Client) address
            new Car(client).start();
        }
    }
}
