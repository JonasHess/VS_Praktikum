package de.hda.VSPraktikum;

import java.util.LinkedList;
import java.util.List;

import de.hda.VSPraktikum.Cars.Car;
import static java.lang.Float.NaN;
import java.util.Iterator;

public class Monitor {
	private List<Car> connectedCars;
	private String id;
	
	public Monitor(String id) {
		super();
		this.id = id;
		this.connectedCars = new LinkedList<Car>();
	}
	public synchronized void addCar(Car car) {
            int index = connectedCars.indexOf(car);  //Notiz: equals ist in Car überladen     
            if (index == -1) {
                    this.connectedCars.add(car);
            }
            else {
                    this.connectedCars.remove(index);
                    this.addCar(car);
            }
            // TODO throw exception when currentEdge of car is != Monitor id
	}
	public synchronized boolean containsCar(Car car) {
            return connectedCars.contains(car);
        }
	public synchronized void removeCar(Car car) {
		this.connectedCars.remove(car);
	}
	public synchronized void removeCar(int index) {
            this.connectedCars.remove(index);
        }
        public synchronized Car getSameCar(Car car) {
            int index = connectedCars.indexOf(car);
            if(index != -1) {
                return connectedCars.get(index);
            }
            return null;
        }
        	
	public synchronized void printStatus() {  
            boolean trafficJam = false;
            float averageSpeed = 0.0f;
            long averageUpdateInterval = 0;
            int numOfValidAvgSpeed = 0;
            int numOfValidAvgUpdate = 0;
            
            //Kick old cars out
            for(Iterator<Car> c = connectedCars.iterator(); c.hasNext(); ) {
                Car car = c.next();
                if(System.currentTimeMillis()-car.getLastTimeStamp() > 300) {
                    c.remove();
                }                    
            }

            for (Car c : this.connectedCars) {   
                    float cAvgSpeed = c.getAverageSpeed();
                    long cAvgUpdate = c.getUpdateInterval();
                    if(cAvgSpeed != NaN && cAvgSpeed != -1.0f) {
                        averageSpeed += c.getAverageSpeed();
                        numOfValidAvgSpeed++;
                    }
                    if(cAvgUpdate != -1) {
                        averageUpdateInterval += c.getUpdateInterval();
                        numOfValidAvgUpdate++;
                    }
                    trafficJam = trafficJam || c.isInTrafficJam();
            }
            if(numOfValidAvgSpeed > 0) {
                averageSpeed = averageSpeed / numOfValidAvgSpeed;
            }
            else {
                averageSpeed = -1.0f;
            }
            if(numOfValidAvgUpdate > 0) {
                averageUpdateInterval = averageUpdateInterval / numOfValidAvgUpdate;
            }
            else {
                averageUpdateInterval = -1;
            }
            System.out.println("Connected Cars: " + connectedCars.size() + " Stau: " + trafficJam + " AvgSpeed: " + averageSpeed + " AvgUpdate: " + averageUpdateInterval);
	}
	
//	 public static float getCosts() {       
//	        synchronized (cars) {             
//	            //Maximale Geschwindigkeit für eine bestimmte Edge. Muss noch vom Client übergeben werden
//	            float edgeAllowedSpeedLimit = 5;  //dummy Wert
//	            
//	            //Länge der Edge. Muss noch vom Client übergeben werden
//	            float edgeDistance = 5; //dummyWert
//	            
//	            
//	            float sum = 0;
//	            for(Car c: cars) {
//	                sum += c.averageSpeed;
//	            }  
//	            float avgSpeed;
//	            if(sum >= 0 && cars.size() > 0) {
//	                avgSpeed = sum/cars.size();
//	            }
//	            else {
//	                avgSpeed = edgeAllowedSpeedLimit;
//	            }
//	            float costs = 0;
//	            costs = edgeDistance / avgSpeed;
//	            /*
//	            costs entspricht jetzt der durchschnittlichen Zeit, die man benötigt, um die Edge zu durchlaufen
//	            */
//	            return costs;
//	        }        
//	    }	
}
