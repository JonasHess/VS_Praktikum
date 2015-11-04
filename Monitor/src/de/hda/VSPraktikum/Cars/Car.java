package de.hda.VSPraktikum.Cars;


import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import de.hda.VSPraktikum.Monitor;

public abstract class Car extends Thread {
    
	private Monitor monitor;
	
    private int id;
    private boolean isInTrafficJam;
    private float averageSpeed;
    private String currentEdge; 
    
  
    
    public Car(Monitor monitor) {
        this.monitor = monitor;
    }
    
    protected abstract void initializeConnection () throws Exception;
    
    protected abstract boolean isConnected();
    
    protected abstract String readNextLine() throws Exception;
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
    
   private void parseLine (String line) throws Exception {
	   String[] fragments = line.split(";");
	   
	   this.id = Integer.parseInt(fragments[0]);
	   this.averageSpeed = Float.parseFloat(fragments[1]);
	   this.isInTrafficJam = Boolean.parseBoolean(fragments[2]);
	   this.currentEdge = fragments[3];
       
       
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
