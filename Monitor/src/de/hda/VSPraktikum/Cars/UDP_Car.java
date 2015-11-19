package de.hda.VSPraktikum.Cars;

import de.hda.VSPraktikum.Monitor;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDP_Car extends Car{
    private DatagramPacket packet;
    private Monitor monitor;
    public UDP_Car(DatagramPacket packet, Monitor monitor) {
        super(monitor);  
        this.packet = packet;
        this.monitor = monitor;

    }
    @Override
    public void run() {
        try {
            parseLine(new String(packet.getData()));
            Car oldCar = monitor.getSameCar(this);
            this.setNewTimeStamp();
            if(oldCar != null) {
                this.updateInterval = this.timeStamp-oldCar.timeStamp;
                //System.out.println(updateInterval);
            }
            else
                this.updateInterval = -1;
            monitor.addCar(this);
        } catch (Exception e) {
            System.out.println(e);
        }        
    }    

}
