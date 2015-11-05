package de.h_da.VS.Praktikum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.Cars.Car;
import de.h_da.VS.Praktikum.Cars.SmartCar;
import de.h_da.VS.Praktikum.Exceptions.EndOfRoadException;
import de.h_da.VS.Praktikum.Graph.Edge;
import de.h_da.VS.Praktikum.Graph.Node;

public class TrafficSimulator {
	List<Car> carList;
	final int carCount = 400;
	final float maxSpeed = 10;
	final float minSpeed = 2f;
	final float speedMultiplier = 1f;

	private List<Node> nodesList;

	/**
	 * Constructor
	 */
	public TrafficSimulator() {
		this.carList = new ArrayList<Car>();

		nodesList = new ArrayList<Node>();
		this.createStreets();

		for (int i = 0; i < carCount; i++) {
			this.spawnNewCar();
		}
	}

	/**
	 * Initializes the graph
	 */
	private void createStreets() {

		// Junctions
		addNode('Q', 10, 10);
		addNode('A', 10, 50);
		addNode('X', 10, 90);
		addNode('B', 50, 90);
		addNode('Y', 90, 90);
		addNode('C', 90, 50);
		addNode('Z', 90, 10);
		addNode('D', 50, 10);
		addNode('G', 50, 35);
		addNode('H', 35, 50);
		addNode('E', 50, 65);
		addNode('F', 65, 50);

		// Streets
		connectNodes('Q', 'A');
		connectNodes('A', 'X');
		connectNodes('X', 'B');
		connectNodes('B', 'Y');
		connectNodes('Y', 'C');
		connectNodes('C', 'Z');
		connectNodes('Z', 'D');
		connectNodes('D', 'Q');
		connectNodes('D', 'C');
		connectNodes('C', 'B');
		connectNodes('B', 'A');
		connectNodes('A', 'D');
		connectNodes('H', 'A');
		connectNodes('F', 'C');
		connectNodes('B', 'E');
		connectNodes('D', 'G');
		connectNodes('H', 'E');
		connectNodes('E', 'F');
		connectNodes('F', 'G');
		connectNodes('G', 'H');
	}

	/**
	 * spawns a new car
	 */
	public void spawnNewCar() {
		Node start = this.getNode('A');
		
		Node end = this.getNode('F');
		float speed = getRandomFloat(minSpeed, maxSpeed) * speedMultiplier;
		Car c = new SmartCar(start, end, speed, this.nodesList);
		start.addCarToSpawnList(c);
	}

	/**
	 * Step in the game logic
	 */
	public void tick() {
		colisionDetection();
	
		for (int i = this.carList.size() - 1; i >= 0; i--) {
			Car auto = this.carList.get(i);
			try {
				auto.tick(this);
			} catch (EndOfRoadException e) {
				auto.delete();
				this.carList.remove(i);
				this.spawnNewCar();
			}
		}
		spawnWaitingCars();
	}

	/**
	 * Is used to add a new node to the graph
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	private void addNode(char id, float x, float y) {
		float xAchse = Main.windowsWidth * (1 - (x / 100));
		float yAchse = Main.windowsHeight * (1 - (y / 100));
		nodesList.add(new Node(id, new Vector2f(xAchse, yAchse)));
	}

	/**
	 * Connects two nodes and created a new edge
	 * 
	 * @param id1
	 * @param id2
	 */
	private void connectNodes(char id1, char id2) {
		Node n1 = getNode(id1);
		Node n2 = getNode(id2);
		n1.connectToNode(n2, maxSpeed, false);
	}

	/**
	 * Returns the node with matching id
	 * 
	 * @param id
	 * @return
	 */
	private Node getNode(char id) {
		for (Node n : this.nodesList) {
			if (n.getId() == id) {
				return n;
			}
		}
		return null;
	}

	/**
	 * draws all shades and forms to screen
	 * 
	 * @param gc
	 * @param g
	 */
	public void render(GameContainer gc, Graphics g) {
		g.clear();
		g.setBackground(Color.black);

		g.setColor(Color.white);
		for (Node n : this.nodesList) {
			n.render(gc, g);
		}

		for (Car auto : this.carList) {
			Shape s = auto.getShape();
			s.setCenterX(s.getCenterX() - Car.width / 2);
			s.setCenterY(s.getCenterY() - Car.height / 2);
			if (auto.isInTrafficJam()) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}

			g.fill(s);
			g.setColor(Color.black);
			g.draw(s);
		}
	}

	/**
	 * Spawns cars, that where not yet able to be spawned, as a different car
	 * was blocking the space on spawn point
	 */
	private void spawnWaitingCars() {
		for (Node n : this.nodesList) {
			List<Car> waitingCars = n.getCarsReadyToSpawnList();
			Shape shape = new Rectangle(n.getPosition().x, n.getPosition().y, Car.width, Car.height);
			for (int i = waitingCars.size() - 1; i >= 0; i--) {
				Car waitingCar = waitingCars.get(i);
				boolean colision = false;
				for (int j = 0; j < this.carList.size(); j++) {
					Car drivingCar = this.carList.get(j);
					if (shape.intersects(drivingCar.getShape())) {
						colision = true;
						break;
					}
				}
				if (!colision) {
					waitingCars.remove(i);
					waitingCar.startCar();
					this.carList.add(waitingCar);
				}
			}
		}
	}

	/**
	 * returns a radom float number
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static float getRandomFloat(float min, float max) {
		Random random = new Random();
		return random.nextFloat() * (max - min) + min;
	}

	/**
	 * return a radom int
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInteger(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	

	/**
	 * Checks for any colliding cars. Collision == TrafficJam
	 */
	public void colisionDetection() {

		for (Car auto : this.carList) {
			auto.setIsInTrafficJam(false);
		}

		for (Car auto1 : this.carList) {
			for (Car auto2 : this.carList) {
				if (auto1 == auto2) {
					continue;
				}
				boolean onSameEdge = auto1.getCurrentEdge() == auto2.getCurrentEdge();
				boolean onPreviousEdge = auto1.getPreviousEdge() == auto2.getCurrentEdge();

				if (!onSameEdge && !onPreviousEdge) {
					continue;
				}
				boolean isBehindAuto1 = auto1.getDistanceToDestination() < auto2.getDistanceToDestination();
				if (onPreviousEdge || (onSameEdge && isBehindAuto1)) {
					boolean colision = auto1.getShape().intersects(auto2.getShape());
					if (colision) {
						auto2.setIsInTrafficJam(true);
					}
				}
			}
		}
	}

}
