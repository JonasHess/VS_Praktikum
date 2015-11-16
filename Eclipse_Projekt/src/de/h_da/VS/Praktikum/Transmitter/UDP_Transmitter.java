package de.h_da.VS.Praktikum.Transmitter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import de.h_da.VS.Praktikum.Cars.Car;

public class UDP_Transmitter extends Transmitter{

	private DatagramSocket socket;
	private InetAddress address;
	private DatagramPacket packet;
	
	public UDP_Transmitter(Car car, String host, int port) {
		super(car, host, port);
	}

	@Override
	protected void openConnection() throws Exception {
		socket = new DatagramSocket();	
		address = InetAddress.getByName(host);
	}

	@Override
	protected void closeConnection() {
		if (isConnected()) {

			socket.close();
		}		
	}

	@Override
	protected boolean isConnected() {
		return this.socket != null;
	}

	@Override
	protected void sendData(String payLoad) throws Exception {
		byte[] data = payLoad.getBytes();
		packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);	
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
