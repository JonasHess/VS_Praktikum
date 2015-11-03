package de.hda.VSPraktikum;

import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer extends Thread{

    int port;
    Monitor monitor;
	private ServerSocket listenSocket;
    
    public SocketServer(int port, Monitor monitor) {
		super();
		this.port = port;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		super.run();
		
		try {
			startListening();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void startListening() throws Exception{
        listenSocket = new ServerSocket(port);
        System.out.println("Multithreaded Server starts on Port " + port);
        while (true){
            Socket client = listenSocket.accept();
            System.out.println("New Connection with: " +     // Output connection
                    client.getRemoteSocketAddress());   // (Client) address
            new Car(client, monitor).start();
        }
    }
}
