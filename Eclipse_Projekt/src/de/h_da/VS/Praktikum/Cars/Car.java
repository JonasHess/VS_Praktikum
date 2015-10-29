package de.h_da.VS.Praktikum.Cars;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import com.sun.swing.internal.plaf.synth.resources.synth;

import de.h_da.VS.Praktikum.TrafficSimulator;
import de.h_da.VS.Praktikum.Exceptions.EndOfRoadException;
import de.h_da.VS.Praktikum.Graph.Edge;
import de.h_da.VS.Praktikum.Graph.Node;

public class Car {

	private static int nextId = 1;
	
	
	private int id;
	protected Node destination;
	private Vector2f lastKnownPosition;
	protected Edge currentEdge;
	private Edge previousEdge = null;
	private float maxSpeed;
	
	private boolean isInTrafficJam = true;
	private List<Float> averageSpeedList;
	private List<Boolean> trafficJamHistoryList;
	private float lastDistance = Float.MAX_VALUE;
	final private float trafficJamSpeed = 0.0f;


	/**
	 * Constructor
	 * 
	 * @param currentEdge
	 * @param spawnPoint
	 * @param destination
	 * @param maxSpeed
	 */
	public Car(Edge currentEdge, Vector2f spawnPoint, Node destination, float maxSpeed) {
		
		this.id = Car.nextId++;
		this.currentEdge = currentEdge;
		this.lastKnownPosition = spawnPoint;
		this.destination = destination;

		this.maxSpeed = maxSpeed;
		this.trafficJamHistoryList = new LinkedList<Boolean>();
		for (int i = 0; i < 70; i++) {
			trafficJamHistoryList.add(false);
		}

		this.averageSpeedList = new LinkedList<Float>();
		for (int i = 0; i < 70; i++) {
			averageSpeedList.add(0f);
		}

		
	}
	
	synchronized public int getId() {
		return this.id;
	}

	/**
	 * Is called when car arrives at destination
	 */
	synchronized public void delete() {
		// ignore, but do not delete!
	}
	
	/**
	 *  Returns the edge, the car was driving on, before the current edge
	 *  Is used for collision detection
	 * @return
	 */
	synchronized public Edge getPreviousEdge() {
		return this.previousEdge;
	}

	/**
	 * Calculated the distance till the end of of current Edge
	 * @return
	 */
	synchronized public float getDistanceToDestination() {
		return this.lastKnownPosition.distance(this.currentEdge.getDestinationNode().getPosition());
	}

	/**
	 * Calculated the current Speed.
	 * Returns 0.0 when car is in traffic jam.
	 * @return
	 */
	private float getCurrentSpeed() {
		float result;
		if (isInTrafficJam) {
			result = this.trafficJamSpeed;
		} else {
			if (maxSpeed > this.currentEdge.getAllowedSpeedLimit()) {
				result = this.currentEdge.getAllowedSpeedLimit();
			} else {
				result = this.maxSpeed;
			}
		}
		this.addNewAverageSpeed(result);
		return result;
	}

	/**
	 * Return the edge the car is currently driving on
	 * @return
	 */
	synchronized public Edge getCurrentEdge() {
		return this.currentEdge;
	}

	/**
	 * Calculates the average speed
	 * @return
	 */
	synchronized public float getAverageSpeed() {
		double result = 0d;
		for (float f : this.averageSpeedList) {
			result = result + f;
		}
		result = result / this.averageSpeedList.size();
		return (float) result;
	}

	/**
	 * Adds a measurement, needed to calculate the average speed. 
	 * @param currentSpeed
	 */
	private void addNewAverageSpeed(float currentSpeed) {
		this.averageSpeedList.remove(0);
		this.averageSpeedList.add(currentSpeed);
	}

	/**
	 * Is called when a collision with a different car was detected 
	 * @param trafficJam
	 */
	public void setIsInTrafficJam(boolean trafficJam) {
		this.isInTrafficJam = trafficJam;
		this.trafficJamHistoryList.remove(0);
		this.trafficJamHistoryList.add(trafficJam);
	}

	/**
	 * Returns TRUE if car was stuck in a traffic jam in the past 70 ticks. 
	 * @return
	 */
	synchronized public boolean isInTrafficJam() {
		for (Boolean b : this.trafficJamHistoryList) {
			if (b) {
				return b;
			}
		}
		return false;
	}

	

	
	/**
	 * Has the car arrived at the end of its current edge?
	 * @param currentPosition
	 * @return
	 */
	public boolean arrivedAtEndOfEdge(Vector2f currentPosition) {
		float distance = currentPosition.distance(currentEdge.getDestinationNode().getPosition());
		if (distance < 0) {
			distance = distance * -1f;
		}
		if ((distance * 0.99999) >= lastDistance) {  // Vermeidet Rundungsfehler
			return true;
		} else {
			this.lastDistance = distance;
			return false;
		}
	}

	/**
	 * Calculates the direction of the next turn. 
	 * @return
	 */
	protected Edge findNextDestination() {
		return currentEdge.getDestinationNode().getRandomEdge();
	}

	
	public void setNextDestination(Edge edge) {
		this.lastDistance = Float.MAX_VALUE;
		this.previousEdge = currentEdge;
		this.currentEdge = edge;
	}

	private Vector2f getCurrentPosition() throws EndOfRoadException{
		if (this.arrivedAtEndOfEdge(lastKnownPosition)) {
			if (this.currentEdge.getDestinationNode().equals(this.destination)) {
				throw new EndOfRoadException();
			}
			Edge nextDestination = this.findNextDestination();
			if (nextDestination == null ) {
				throw new EndOfRoadException();
			}
			this.setNextDestination(nextDestination);
		}
		Vector2f direction = this.currentEdge.getDestinationNode().getPosition().sub(this.lastKnownPosition);
		direction.normalise();
		this.direction = direction.copy();
		direction.scale(this.getCurrentSpeed());
		return lastKnownPosition.copy().add(direction);
	}

	public Vector2f direction = null;

	public Vector2f getLastPosition() {
		return this.lastKnownPosition;
	}

	public void tick(TrafficSimulator navi) throws EndOfRoadException {
		this.lastKnownPosition = this.getCurrentPosition();

	}

	public Shape getShape() {
		Shape shape = new Rectangle(0, 0, 70, 40);
		shape.setLocation(lastKnownPosition);
		return shape;
	}
}
