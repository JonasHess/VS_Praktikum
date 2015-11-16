package de.h_da.VS.Praktikum.Cars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.Graph.Edge;
import de.h_da.VS.Praktikum.Graph.Node;
import de.h_da.VS.Praktikum.Transmitter.TCP_Transmitter;
import de.h_da.VS.Praktikum.Transmitter.Transmitter;
import de.h_da.VS.Praktikum.Transmitter.UDP_Transmitter;

public class SmartCar extends Car {

	private Transmitter transmitter;
	private List<Node> nodesList;

	/**
	 * Constructor
	 * 
	 * @param currentEdge
	 * @param spawnPoint
	 * @param destination
	 * @param maxSpeed
	 */
	public SmartCar(Node startNode, Node destination, float maxSpeed, List<Node> nodesList) {
		super(startNode, destination, maxSpeed, nodesList);		
	}

	public void startCar() {
		this.currentEdge = this.findNextDestination(startNode, destination);
		this.onRoadChange(currentEdge);
	}
	
	private Transmitter getNewTransmitter() {
		//return new TCP_Transmitter(this, "localhost", 8888);
		return new UDP_Transmitter(this, "localhost", 8888);
	}
	
	private void stopTransmitter() {
		if (this.transmitter != null) {
			this.transmitter.stopTransmitter();
			this.transmitter = null;
		}
	}
	
	/**
	 * Is called when car arrives at destination
	 */
	@Override
	public synchronized void delete() {
		this.stopTransmitter();
		super.delete();
	}

	private Node minVertex(Map<Node, Float> dist, Map<Node, Boolean> visited) {
		float x = Float.MAX_VALUE;
		Node bestNode = null;
		for (Map.Entry<Node, Float> d : dist.entrySet()) {
			if (!visited.get(d.getKey()) && d.getValue() < x) {
				bestNode = d.getKey();
			}
		}
		return bestNode;
	}

	private Map<Node, List<Node>> dijkstra(List<Node> graph, Node myNode) {
		final Map<Node, Float> dist = new HashMap<Node, Float>();
		final Map<Node, List<Node>> pred = new HashMap<Node, List<Node>>();
		final Map<Node, Boolean> visited = new HashMap<Node, Boolean>();

		for (Node n : graph) {
			if (n.getId() == myNode.getId()) {
				dist.put(n, 0.0f);
				visited.put(n, false);
				pred.put(n, new ArrayList<Node>());
			} else {
				dist.put(n, Float.MAX_VALUE);
				visited.put(n, false);
				pred.put(n, new ArrayList<Node>());
			}
		}

		for (int i = 0; i < graph.size(); i++) {
			final Node next = minVertex(dist, visited);
			visited.put(next, true);
			
			if (next == null) {
				return null;
			}
			final List<Node> neighbors = next.getNeighboursNodes();
			for (Node v : neighbors) {
				final float d = dist.get(next) + next.getEdgeConnectedToNode(v).getCost();
				if (dist.get(v) > d) {
					dist.put(v, d);
					pred.get(v).add(next);
				}
			}
		}

		return pred;
	}
	
	private Node findSameNeighbor(Node A, Node B) {
		List<Node> first = A.getNeighboursNodes();
		
		for (Node n : first) {
			for (Node p : n.getNeighboursNodes()) {
				if (p.getId() == B.getId()) {
					return n;
				}
			}
		}
		
		return null;
	}
	
	private Node nextNode(Map<Node, List<Node>> pred, Node myNode, Node goal) {
		final List<Node> path = new ArrayList<Node>();
		Node x = goal;
		while(x.getId() != myNode.getId()) {
			path.add(x);
			x = pred.get(x).get(pred.get(x).size() - 1);
		}
		
		return path.get(path.size() - 1);
	}

	/**
	 * Calculates the direction of the next turn.
	 * 
	 * @return
	 */
	@Override
	protected Edge findNextDestination(Node currentNode, Node destinationNode) {

		if (currentNode.getNeighboursNodes().size() == 1) {
			return currentNode.getEdges().get(0);
		}
		Map<Node, List<Node>> result = dijkstra(graph, currentNode);
		if (result == null) { // Kein Weg gefunden, da speed = 0
			return currentNode.getRandomEdge();
		}
		return currentNode.getEdgeConnectedToNode(nextNode(result, currentNode, destinationNode));
	}

	@Override
	protected void onRoadChange(Edge newRoad) {
		stopTransmitter();
		this.transmitter = getNewTransmitter();
		this.transmitter.start();
	}

}
