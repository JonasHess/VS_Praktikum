package monitor;

import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Car extends Thread {
    private static final ArrayList<Car> cars = new ArrayList<>();
    
    private int id;
    private boolean isInTrafficJam;
    private float averageSpeed;
    private String currentEdge; 
    
    private final Socket client;
    
    private BufferedReader fromClient;
    //private DataOutputStream toClient;
    public Car(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {        
        registerCar(this);
        boolean connected = true;        
        
        try {
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); // Datastream FROM Client
            //toClient = new DataOutputStream (client.getOutputStream()); // TO Client
            String line;
            while(connected) {
                line = fromClient.readLine(); // Read Request
                System.out.println("Received: "+ line);
                if(line.equals("bye")) {
                    connected = false;
                }
                else {
                    String[] fragments = line.split(";");
                    id = Integer.parseInt(fragments[0]);
                    averageSpeed = Float.parseFloat(fragments[1]);
                    isInTrafficJam = Boolean.parseBoolean(fragments[2]);
                    currentEdge = fragments[3];
                    
                    //debug
                    System.out.println("id: " + id);
                    System.out.println("averageSpeed: " + averageSpeed);
                    System.out.println("isInTrafficJam: " + isInTrafficJam);
                    System.out.println("currentEdge: " + currentEdge);
                }
            }            
        }
        catch(IOException | NumberFormatException e) {
            System.out.println(e);
        }
        dischargeCar(this);
    }
    
    private static void registerCar(Car c) {
        synchronized(cars) {
            cars.add(c);
        }
    }
    private static void dischargeCar(Car c) {
        synchronized(cars) {
            cars.remove(c);
        }
    }
    
    public static float getCosts() {       
        synchronized (cars) {             
            //Maximale Geschwindigkeit für eine bestimmte Edge. Muss noch vom Client übergeben werden
            float edgeAllowedSpeedLimit = 5;  //dummy Wert
            
            //Länge der Edge. Muss noch vom Client übergeben werden
            float edgeDistance = 5; //dummyWert
            
            
            float sum = 0;
            for(Car c: cars) {
                sum += c.averageSpeed;
            }  
            float avgSpeed;
            if(sum >= 0 && cars.size() > 0) {
                avgSpeed = sum/cars.size();
            }
            else {
                avgSpeed = edgeAllowedSpeedLimit;
            }
            float costs = 0;
            costs = edgeDistance / avgSpeed;
            /*
            costs entspricht jetzt der durchschnittlichen Zeit, die man benötigt, um die Edge zu durchlaufen
            */
            return costs;
        }        
    }
    public static int getNumberOfCars() {
        synchronized (cars) {
            return cars.size();
        }
    }
}
