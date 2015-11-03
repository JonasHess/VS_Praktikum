package de.h_da.VS.Praktikum.Transmitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

import com.sun.swing.internal.plaf.synth.resources.synth;

import de.h_da.VS.Praktikum.Cars.Car;

public abstract class Transmitter extends Thread {
	private static final String hosts = "localhost"; //Hieraus kann man eine Liste mit allen hosts machen
	private static final int port = 9999;
	
	private Car car;
	private String currentEdge = "";
	
        private Socket socket;
        //protected BufferedReader fromServer;   
        protected DataOutputStream toServer;
	private boolean connected = false;
	
	private boolean stop = false;

	/**
	 * Constructor
	 * 
	 * @param car
	 */
	public Transmitter(Car car) {
		super();
		this.car = car;
		connectToServer(hosts);
	}

	/**
	 * main method of this thread
	 */
	@Override
	public void run() {
		super.run();
		while (true) {
			if (stop) {
				return;
			}
			if(currentEdge != car.getCurrentEdge().getId()) {
				connected = disconnectFromServer();
				connected = connectToServer(hosts); //hier muss spÃ¤ter der entsprechende Server Ã¼bergeben werden
			}
			try {
				this.sendData(getCarString());
				Thread.sleep(500);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Connect to host
	 */
        private boolean connectToServer(String host) {
            	try {
                	socket = new Socket(host, port);
                	toServer = new DataOutputStream(socket.getOutputStream());
                	currentEdge = car.getCurrentEdge().getId();
                	return true;
            	}
            	catch(Exception e) {
                	e.printStackTrace();
                	return false;
            	}            
        }
        
	/**
	 * Disconnect from host
	 */	
	private boolean disconnectFromServer() {
        	try {
                	socket.close();
                	toServer.close();
                	return true;
            	}
    		catch(IOException e) {
                	return false;
            	}
        }
	
	/**
	 * Is called to stop this thread
	 */
	synchronized public void stopTransmitter() {
		disconnectFromServer();
		this.stop = true;
	}

	/**
	 * Return the encodes string of this car.
	 * 
	 * @return
	 */
	private String getCarString() {
		StringBuilder b = new StringBuilder();
		b.append(car.getId());
		b.append(";");
		b.append(car.getAverageSpeed());
		b.append(";");
		b.append(car.isInTrafficJam());
		b.append(";");
		b.append(car.getCurrentEdge().getId());
		b.append(";");
		return b.toString();
	}

	/**
	 * Sends the collected car information to the monitoring servers.
	 * 
	 * @throws Exception
	 */
	protected abstract void sendData(String payLoad) throws Exception;
	

}
