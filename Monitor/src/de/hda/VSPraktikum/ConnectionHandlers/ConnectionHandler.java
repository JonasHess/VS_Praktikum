package de.hda.VSPraktikum.ConnectionHandlers;

import java.net.ServerSocket;
import java.net.Socket;

import de.hda.VSPraktikum.Monitor;


public abstract class ConnectionHandler extends Thread{

    int port;
    Monitor monitor;
    
    public ConnectionHandler(int port, Monitor monitor) {
		super();
		this.port = port;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		super.run();
		
		try {
			startListening(this.port, this.monitor);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	protected abstract void startListening(int port, Monitor monitor) throws Exception;
}
