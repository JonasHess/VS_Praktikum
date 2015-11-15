package de.h_da.VS.Praktikum;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import mdes.slick.sui.Button;
import mdes.slick.sui.Label;

public class SpeedControl extends Label {
	private float speed;
	private float maxSpeed;
	private float minSpeed;
	Button show;
	
	public float getSpeedLimit() {
		return speed;
	}
	
	 public enum State {
		 UP, DOWN
	 };
	 
	 boolean hasChangedValue = false;
	 State lastState  = State.UP;
	 State state = State.UP;
	
	public SpeedControl(float posX,float posY) {
		show = new Button();
		show.setBounds(posX, posY, 80, 16);
		show.setText("Speed: " + speed);
		
		maxSpeed = TrafficSimulator.maxSpeed;
		minSpeed = TrafficSimulator.minSpeed;
		speed = minSpeed;
	}
	
	public boolean pressedButton(int mousePosX, int mousePosY) {
		if (mousePosX > show.getX() && mousePosX < show.getX() + show.getWidth() &&
		mousePosY > show.getY() && mousePosY < show.getY() +  show.getHeight()) {
			return true;
		}
		return false;
	}

	public void update(GameContainer gc) {
		show.setText("Speed: " + speed);
		Input input = gc.getInput();
		if (input.isMouseButtonDown(0) && pressedButton(input.getMouseX(), input.getMouseY())) {
			state = State.DOWN;
		} else {
			state = State.UP;
		}	
		
		// button is pressed
		if (state == State.DOWN && lastState != state ) {
			if (speed < maxSpeed) {
				speed++;
			} else {
				speed = minSpeed;
			}
		}
		
		lastState = state;
	}
	
	public void render(GameContainer gc, Graphics g) {
        show.render(gc, g);
	}
}

