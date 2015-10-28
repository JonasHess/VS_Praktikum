package de.h_da.VS.Praktikum.Cars;

import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.Graph.Edge;
import de.h_da.VS.Praktikum.Graph.Node;
import de.h_da.VS.Praktikum.Transmitter.TCP_Transmitter;
import de.h_da.VS.Praktikum.Transmitter.Transmitter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SmartCar extends Car{


	private Transmitter transmitter;
	
	/**
	 * Construcor
	 * @param currentEdge
	 * @param spawnPoint
	 * @param destination
	 * @param maxSpeed
	 */
	public SmartCar(Edge currentEdge, Vector2f spawnPoint, Node destination, float maxSpeed) {
		super(currentEdge, spawnPoint, destination, maxSpeed);
		
		this.transmitter = new TCP_Transmitter(this);
		transmitter.start();
		
	}

	/**
	 * Is called when car arrives at destination
	 */
	@Override
	public synchronized void delete() {
		this.transmitter.stopTransmitter();
		super.delete();
	}
	
	/**
	 * Calculates the direction of the next turn. 
	 * @return
	 */
	@Override
	protected Edge findNextDestination() {
		
		Node finalDestination = destination; 
		Node curretPosition = currentEdge.getDestinationNode();
		
		
		// TODO  calculate shortest path to destination;

		
		List<Edge> outgoingEdges = curretPosition.getEdges();
		float cost1 = outgoingEdges.get(0).getCost();
		Node nextNode1 = outgoingEdges.get(0).getDestinationNode(); 

		
		
		throw new NotImplementedException();
		
		// ACHTUNG:  SmartCars werden noch nicht gepawnt!
		
	}



	
}
