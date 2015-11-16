package de.h_da.VS.Praktikum.Transmitter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import de.h_da.VS.Praktikum.Cars.Car;


public abstract class Transmitter extends Thread {
	
	protected Car car;
	protected String host;
	protected int port;
	protected boolean stop = false;	
	/**
	 * Constructor
	 * 
	 * @param car
	 */
	public Transmitter(Car car, String host, int port) {
		super();
		this.car = car;
		this.host = host;
		this.port = port;
		this.stop = false;
	}
	
	protected abstract void openConnection() throws Exception;

	protected abstract  void closeConnection() ;
	
	protected abstract boolean isConnected();
	
	/**
	 * Sends the collected car information to the monitoring servers.
	 * 
	 * @throws Exception
	 */
	protected abstract void sendData(String payLoad) throws Exception;
	
	/**
	 * Return the encodes string of this car.
	 * 
	 * @return
	 */
	protected String getCarString() {
		StringBuilder b = new StringBuilder();
		b.append(car.getId());
		b.append(";");
		b.append(car.getAverageSpeed());
		b.append(";");
		b.append(car.isInTrafficJam());
		b.append(";");
		b.append(car.getCurrentEdge().getId());
		b.append(";");
		b.append("\n");
		return b.toString();
	}

	/**
	 * Is called to stop this thread
	 */
	synchronized public void stopTransmitter() {
		this.stop = true;
	}
	
	/**
	 * main method of this thread
	 */
	
	@Override
	public void run() {
		super.run();
		while (true) {
			try {
			if (stop) {
				if (isConnected()) {
					closeConnection();
				}
				return;
			} 
			int attempt = 0;
			Exception exception = null;
			while (attempt < 3 && !isConnected()) {
				try {
					openConnection();
				}
				catch(Exception e) {
					exception = e;
				}
				attempt++;
			}
			if (isConnected()) {
				this.sendData(getCarString());
			} else {
				exception.printStackTrace();
				continue;
			}
			Thread.sleep(500);

			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}
