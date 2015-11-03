package de.h_da.VS.Praktikum.Transmitter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import de.h_da.VS.Praktikum.Cars.Car;


public class TCP_Transmitter extends Transmitter{

	
	private Socket socket;
	private DataOutputStream toServer;
	
    public TCP_Transmitter(Car car, String host, int port) {
		super(car, host, port);
	}
    
    
    

	@Override
	protected void openConnection(String host, int port) throws Exception {
		this.socket = new Socket(host, port);
		this.toServer = new DataOutputStream(socket.getOutputStream());
	}
    
	@Override
	protected void closeConnection() {
		if (isConnected()) {
			try {
				socket.close();
				this.toServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	protected boolean isConnected() {
		return this.socket != null && this.toServer != null && this.socket.isConnected();
	}
	
	@Override
	protected void sendData(String payLoad) throws Exception {
		toServer.writeBytes(payLoad);
	}

}
