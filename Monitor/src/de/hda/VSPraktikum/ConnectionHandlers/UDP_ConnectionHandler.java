package de.hda.VSPraktikum.ConnectionHandlers;

import de.hda.VSPraktikum.Monitor;
import de.hda.VSPraktikum.Cars.UDP_Car;
import java.net.DatagramPacket;

import java.net.DatagramSocket;

public class UDP_ConnectionHandler extends ConnectionHandler{
    /**
     * Constructor
     * 
     * @param port
     * @param monitor
     */
    public UDP_ConnectionHandler(int port, Monitor monitor) {
            super(port, monitor);
    }

    @Override
    protected void startListening(int port, Monitor monitor) throws Exception {
        DatagramSocket listenSocket = null;
        DatagramPacket packet = null;
        int len = 128;
        try {
            listenSocket = new DatagramSocket(port);
            System.out.println("Multithreaded UDP-Server starts on Port " + port);
            while(true) {
                packet = new DatagramPacket(new byte[len], len);
                listenSocket.receive(packet);
                new UDP_Car(packet, monitor);
            }
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            if(listenSocket != null) {
                listenSocket.close();
            }
        }
    }
}
