package de.hda.VSPraktikum;

import java.util.LinkedList;
import java.util.List;

import de.hda.VSPraktikum.Cars.Car;

public class Monitor {
	private List<Car> connectedCars;
	private String id;
	
	public Monitor(String id) {
		super();
		this.id = id;
		this.connectedCars = new LinkedList<Car>();
	}
	
	public synchronized void addCar(Car car) {
		if (! connectedCars.contains(car)) {
			this.connectedCars.add(car);
		}
		// TODO throw exception when currentEdge of car is != Monitor id
	}
	
	public synchronized void removeCar(Car car) {
		this.connectedCars.remove(car);
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
	 
	
	public synchronized void printStatus() {
		boolean trafficJam = false;
		float averageSpeed = -1.0f;
		for (Car c : this.connectedCars) {
			averageSpeed += c.getAverageSpeed();
			trafficJam = trafficJam || c.isInTrafficJam();
		}
		if (this.connectedCars.size() > 0) {
			averageSpeed = averageSpeed / this.connectedCars.size();
		}
		
		System.out.println("Connected Cars: " + connectedCars.size() + " Stau: " + trafficJam + " AvgSpeed: " + averageSpeed);
	}
	
	
}
