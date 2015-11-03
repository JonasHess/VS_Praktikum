package de.hda.VSPraktikum;


import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Car extends Thread {
    
	private Monitor monitor;
	
    private int id;
    private boolean isInTrafficJam;
    private float averageSpeed;
    private String currentEdge; 
    
    private final Socket client;
    private BufferedReader fromClient;
    
    public Car(Socket client, Monitor monitor) {
        this.client = client;
        this.monitor = monitor;
    }
    
    @Override
    public void run() {
        try {
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); // Datastream FROM Client
            String line;
            while(this.client.isConnected()) {
            	line = fromClient.readLine(); // Read Request
                if(line == null || line.length() == 0 || line.equals("bye")) {
                    break;
                }
               // System.out.println("Received: "+ line);
              
                String[] fragments = line.split(";");
                
                try {
	                id = Integer.parseInt(fragments[0]);
	                averageSpeed = Float.parseFloat(fragments[1]);
	                isInTrafficJam = Boolean.parseBoolean(fragments[2]);
	                currentEdge = fragments[3];
	                this.monitor.addCar(this);
	                
                } catch (Exception e) {
                	System.out.println(e);
                	this.monitor.removeCar(this);
                	continue;
                }
            }
            this.monitor.removeCar(this);
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
            this.monitor.removeCar(this);
        }
    }
    
   
    
    @Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("id: " + id);
		b.append( "averageSpeed: " + averageSpeed);
		b.append( "isInTrafficJam: " + isInTrafficJam);
		b.append( "currentEdge: " + currentEdge);
		return b.toString();
	}

	public int getCarId() {
		return id;
	}

	public boolean isInTrafficJam() {
		return isInTrafficJam;
	}

	public float getAverageSpeed() {
		return averageSpeed;
	}

	public String getCurrentEdge() {
		return currentEdge;
	}

	public Monitor getMonitor() {
		return monitor;
	}
    
    
}
