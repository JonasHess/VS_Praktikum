package de.hda.VSPraktikum.Cars;

import de.hda.VSPraktikum.Monitor;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDP_Car extends Car{
    
    public UDP_Car(DatagramPacket packet, Monitor monitor) {
        super(monitor);  
        try {
            parseLine(new String(packet.getData()));
            Car oldCar = monitor.getSameCar(this);
            this.setNewTimeStamp();
            if(oldCar != null)
                this.updateInterval = this.timeStamp-oldCar.timeStamp;
            else
                this.updateInterval = -1;
            monitor.addCar(this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void run() {
        
    }    

}
