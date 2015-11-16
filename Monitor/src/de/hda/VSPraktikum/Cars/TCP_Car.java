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
    public void run() {
        try {
            this.initializeConnection();
            String line;
            while(this.isConnected()) {
            	line =  this.readNextLine();
                if(line == null || line.length() == 0 || line.equals("bye")) {
                    break;
                }
                // System.out.println("Received: "+ line);
                try {
                	parseLine(line);
                        this.setNewTimeStamp();
                        if(!monitor.containsCar(this))
                            this.monitor.addCar(this);
                
                } catch (Exception e) {
                	System.out.println(e);
                	continue;
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
        	this.monitor.removeCar(this);
        }
    }        

    protected void initializeConnection() throws Exception {
            fromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); // Datastream
    }

    protected boolean isConnected() {
            return this.socket != null && this.socket.isConnected();
    }

    protected String readNextLine() throws Exception {
            return fromClient.readLine();
    }

}
