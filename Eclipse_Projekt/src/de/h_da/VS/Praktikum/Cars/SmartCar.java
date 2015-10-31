package de.h_da.VS.Praktikum.Cars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.Graph.Edge;
import de.h_da.VS.Praktikum.Graph.Node;
import de.h_da.VS.Praktikum.Transmitter.TCP_Transmitter;
import de.h_da.VS.Praktikum.Transmitter.Transmitter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.provider.certpath.Vertex;

public class SmartCar extends Car {

	private Transmitter transmitter;

	/**
	 * Construcor
	 * 
	 * @param currentEdge
	 * @param spawnPoint
	 * @param destination
	 * @param maxSpeed
	 */
	public SmartCar(Edge currentEdge, Vector2f spawnPoint, Node destination, float maxSpeed, List<Node> graph) {
		super(currentEdge, spawnPoint, destination, maxSpeed, graph);

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

	public Node minVertex(Map<Node, Float> dist, Map<Node, Boolean> visited) {
		float x = Float.MAX_VALUE;
		Node bestNode = null;
		for (Map.Entry<Node, Float> d : dist.entrySet()) {
			if (!visited.get(d.getKey()) && d.getValue() < x) {
				bestNode = d.getKey();
			}
		}

		return bestNode;
	}

	public Node dijkstra(List<Node> graph, Node myNode) {
		final Map<Node, Float> dist = new HashMap<>();
		final List<Node> pred = new ArrayList<Node>();
		final Map<Node, Boolean> visited = new HashMap<>();

		for (Node n : graph) {
			if (n.getId() == myNode.getId()) {
				dist.put(n, 0.0f);
				visited.put(n, false);
			} else {
				dist.put(n, Float.MAX_VALUE);
				visited.put(n, false);
			}
		}

		for (Node node : graph) {
			final Node next = minVertex(dist, visited);
			visited.put(next, true);

			final List<Node> neighbors = next.getNeighboursNodes();
			for (Node v : neighbors) {
				final float d = dist.get(next) + next.getEdges(v).getCost();
				if (dist.get(v) > d) {
					dist.put(v, d);
					pred.add(next);
				}
			}
		}

		return pred.get(0); // for edge myNode.getEdges(pred.get(0));
	}

	/**
	 * Calculates the direction of the next turn.
	 * 
	 * @return
	 */
	@Override
	protected Edge findNextDestination() {

		Node finalDestination = destination;
		Node currentPosition = currentEdge.getDestinationNode();

	}

}
