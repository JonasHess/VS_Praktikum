package de.h_da.VS.Praktikum.Transmitter;

import de.h_da.VS.Praktikum.Cars.Car;

public class UDP_Transmitter extends Transmitter{

	public UDP_Transmitter(Car car, String host, int port) {
		super(car, host, port);
	}

	@Override
	protected void openConnection(String host, int port) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void sendData(String payLoad) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
