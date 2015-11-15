package de.h_da.VS.Praktikum.Graph;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import de.h_da.VS.Praktikum.SpeedControl;

public class Edge {
	private Node sourceNode;
	private Node destinationNode;
	private float allowedSpeedLimit;
	
	SpeedControl control;
	
	/**
	 * Create Speed Control
	 */
	private void createSpeedControl() {
			// calc middle
			float x1 = getSourceNode().getPosition().getX();
			float y1 = getSourceNode().getPosition().getY();
			float x2 = getDestinationNode().getPosition().getX();
			float y2 = getDestinationNode().getPosition().getY();

			float x = (x1 + x2) / 2;
			float y = (y1 + y2) / 2;
			control = new SpeedControl(x,y);
	}
	
	public void update(GameContainer gc) {
		control.update(gc);
		allowedSpeedLimit = control.getSpeedLimit();
	}

	public Edge(Node sourceNode, Node destinationNode, float allowedSpeedLimit) {
		super();
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.allowedSpeedLimit = allowedSpeedLimit;
		createSpeedControl();
	}

	/**
	 * Prints the edge on screen 
	 * @param gc
	 * @param g
	 */
	public void render(GameContainer gc, Graphics g) {
		g.draw(new Line(this.sourceNode.getPosition(), this.destinationNode.getPosition()));
		control.render(gc, g);
	}

	/**
	 * Calculated the cost of this edge
	 * @return
	 */
	public float getCost() {
		return getLength() / this.getAllowedSpeedLimit();
	}
	
	/**
	 * Return the length of this edge
	 * @return
	 */
	public float getLength() {
		return this.getSourceNode().getPosition().distance(this.getDestinationNode().getPosition());
	}
	
	/**
	 * Return the beginning node
	 * @return
	 */
	public Node getSourceNode() {
		return sourceNode;
	}

	/**
	 * Return the end node
	 * @return
	 */
	public Node getDestinationNode() {
		return destinationNode;
	}

	/**
	 * Return the allowed speedlimit
	 * @return
	 */
	public float getAllowedSpeedLimit() {
		if (this.allowedSpeedLimit < 0) {
			throw new RuntimeException("allowedSpeed can not be < 0");
		}
		return allowedSpeedLimit;
	}

	/**
	 * Sets the allowed speed limit
	 * @param allowedSpeedLimit
	 */
	public void setAllowedSpeedLimit(float allowedSpeedLimit) {
		if (allowedSpeedLimit < 0) {
			throw new RuntimeException("allowedSpeed can not be < 0");
		}
		this.allowedSpeedLimit = allowedSpeedLimit;
	}

	/**
	 * Return the id of this edge
	 * @return
	 */
	synchronized public  String getId() {
		
		return "" + this.sourceNode.getId() + ":" + this.destinationNode.getId();
	}

}
