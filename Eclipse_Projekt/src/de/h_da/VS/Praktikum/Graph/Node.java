package de.h_da.VS.Praktikum.Graph;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.TrafficSimulator;
import de.h_da.VS.Praktikum.Cars.Car;

public class Node {
	
	private Vector2f position;
	private List<Edge> edges;
	private char id;
	
	
	private List<Car> carsReadyToSpawnList;

	/**
	 * Constructor
	 * @param id
	 * @param position
	 */
	public Node(char id, Vector2f position) {
		super();
		this.id = id;
		this.position = position;
		this.edges = new ArrayList<Edge>();
		this.carsReadyToSpawnList = new ArrayList<Car>();
	}
	
	/**
	 * Return a list with all outgoing edges.
	 * @return
	 */
	public List<Edge> getEdges() {
		return this.edges;
	}
	
	public boolean isConnectedToNode(Node n) {
		return getNeighboursNodes().contains(n);
	}
	public Edge getEdgeConnectedToNode(Node dest) {
		List<Edge> edges = this.getEdges();
		
		for (Edge e: edges) {
			if (e.getDestinationNode().getId() == dest.getId()) {
				return e;
			}
		}
		return null;
		
	}
	
	public List<Node> getNeighboursNodes() {
		List<Node> result = new ArrayList<Node>(this.edges.size());
		
		for (int i = 0; i < this.edges.size(); i++) {
			result.add(this.edges.get(i).getDestinationNode());
		}
		return result;
	}
	
	
	/**
	 * Return the id of this node
	 * @return
	 */
	public char getId() {
		return this.id;
	}
	
	/**
	 * Creates a new edge by connecting 2 nodes.
	 * @param node
	 * @param allowedSpeedLimit
	 * @param connectBackwards
	 */
	public void connectToNode(Node node, float allowedSpeedLimit, boolean connectBackwards) {
		this.edges.add(new Edge(this, node, allowedSpeedLimit));
		if (connectBackwards) {
			node.connectToNode(this, allowedSpeedLimit, false);
		}
	}
	
	/**
	 * Return a list with cars that are ready to spawn at this node
	 * @return
	 */
	public List<Car> getCarsReadyToSpawnList() {
		return this.carsReadyToSpawnList;
	}
	
	/**
	 * Spawns a car at his node, as soon as there is no other car blocking the node
	 * @param car
	 */
	public void addCarToSpawnList(Car car) {
		this.carsReadyToSpawnList.add(car);
	}
	
	/**
	 * Returns the position of this node on screen
	 * @return
	 */
	public Vector2f getPosition() {
		return this.position.copy();
	}
	
	/**
	 * Draws the node to the screen. Represented by a small circle
	 * @param gc
	 * @param g
	 */
	public void render(GameContainer gc, Graphics g)
	{
		g.draw(new Circle(this.position.x, this.position.y, 15f));
		for (Edge e : this.edges) {
			e.render(gc, g);
		}
		g.drawString("" + this.getId(), this.getPosition().x + 10, this.getPosition().y + 10);
	}
	
	/**
	 * Return a random connected outgoing edge
	 * @return
	 */
	public Edge getRandomEdge() {
		if (this.edges.size() == 0) {
			return null;
		}
		return this.edges.get(TrafficSimulator.getRandomInteger(0, this.edges.size() - 1));
	}
	
	
	
}

