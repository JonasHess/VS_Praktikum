package de.h_da.VS.Praktikum.Transmitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.sun.swing.internal.plaf.synth.resources.synth;

import de.h_da.VS.Praktikum.Cars.Car;

public abstract class Transmitter extends Thread {

	private Car car;
	private boolean stop = false;

	/**
	 * Constructor
	 * 
	 * @param car
	 */
	public Transmitter(Car car) {
		super();
		this.car = car;
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
			try {

				this.sendData(getCarString());
				Thread.sleep(500);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Is called to stop this thread
	 */
	synchronized public void stopTransmitter() {
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
