package de.hda.VSPraktikum.ConnectionHandlers;

import java.net.ServerSocket;
import java.net.Socket;

import de.hda.VSPraktikum.Monitor;
import de.hda.VSPraktikum.Cars.TCP_Car;

public class TCP_ConnectionHandler extends ConnectionHandler {

	/**
	 * Construcot
	 * 
	 * @param port
	 * @param monitor
	 */
	public TCP_ConnectionHandler(int port, Monitor monitor) {
		super(port, monitor);
	}

	@Override
	protected void startListening(int port, Monitor monitor) throws Exception {
		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(port);
			System.out.println("Multithreaded Server starts on Port " + port);
			while (true) {
				Socket client = listenSocket.accept();
				//System.out.println("\n New Connection with: " + client.getRemoteSocketAddress() + "\n");
				new TCP_Car(client, monitor).start();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (listenSocket != null) {
				listenSocket.close();
			}
		}
	}
}
