package de.hda.VSPraktikum.Cars;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import de.hda.VSPraktikum.Monitor;

public class TCP_Car extends Car {

	private final Socket socket;
	private BufferedReader fromClient;

	public TCP_Car(Socket client, Monitor monitor) {
		super(monitor);
		this.socket = client;
	}
	
	@Override
	protected void initializeConnection() throws Exception {
		fromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); // Datastream
	}
	
	@Override
	protected boolean isConnected() {
		return this.socket != null && this.socket.isConnected();
	}
	
	@Override
	protected String readNextLine() throws Exception {
		return fromClient.readLine();
	}

}
