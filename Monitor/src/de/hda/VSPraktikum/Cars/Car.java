package de.hda.VSPraktikum.Cars;


import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import de.hda.VSPraktikum.Monitor;

public abstract class Car extends Thread {
    
    protected Monitor monitor;
	
    private int id;
    private boolean isInTrafficJam;
    private float averageSpeed;
    private String currentEdge; 
    
    protected long timeStamp = 0;
    protected long updateInterval;
    
    public Car(Monitor monitor) {
        this.monitor = monitor;
    }
    
    protected void parseLine(String line) throws Exception {
        String[] fragments = line.split(";");
        
        this.id = Integer.parseInt(fragments[0]);
        this.averageSpeed = Float.parseFloat(fragments[1]);
        this.isInTrafficJam = Boolean.parseBoolean(fragments[2]);
        this.currentEdge = fragments[3];
        
        
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	if (obj == null)
    		return false;
    	if (getClass() != obj.getClass())
    		return false;
        Car otherCar = (Car)obj;
    	if (id != otherCar.getCarId()) {
    		return false;
    	}
        if(!currentEdge.equals(otherCar.getCurrentEdge())) {
            return false;
        }
        return true;
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
    public long getLastTimeStamp() {
        return timeStamp;
    }
    public long getUpdateInterval() {
        return updateInterval;
    }
    public void setNewTimeStamp() {
        long newTimeStamp = System.currentTimeMillis();
        if(this.timeStamp != 0)
            this.updateInterval = newTimeStamp - this.timeStamp;
        else
            this.updateInterval = -1;
        //System.out.println("New:" + newTimeStamp + " Old:" + timeStamp + " Update:" + updateInterval);
        this.timeStamp = newTimeStamp;
        
    }
}
