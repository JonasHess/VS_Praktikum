package de.h_da.VS.Praktikum.Transmitter;

import de.h_da.VS.Praktikum.Cars.Car;

public class TCP_Transmitter extends Transmitter{

	/**
	 * Constructor
	 * @param car
	 */
	public TCP_Transmitter(Car car) {
		super(car);
	}

	/**
	 * Sends the collected car information to the monitoring servers.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void sendData(String payLoad) throws Exception {
		//TODO sent payload
	}

}
