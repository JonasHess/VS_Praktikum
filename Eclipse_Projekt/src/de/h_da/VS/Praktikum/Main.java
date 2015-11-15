package de.h_da.VS.Praktikum;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.sun.javafx.tk.Toolkit;

public class Main extends BasicGame
{
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	final public static int windowsHeight = (int) ((int) gd.getDisplayMode().getHeight()  * 0.50);
	final public static int windowsWidth = (int) ((int) gd.getDisplayMode().getWidth() * 0.50);
	
	private TrafficSimulator trafficSimulator;
	
	
	public Main(String gamename)
	{
		super(gamename);
	}
	

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("Traffic Simulator"));
			appgc.setDisplayMode(windowsWidth, windowsHeight, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Initializes the 2d engine
	 */
	@Override
	public void init(GameContainer gc) throws SlickException {
		this.trafficSimulator = new TrafficSimulator();
	}

	/**
	 * Draws all shapes to screen
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		this.trafficSimulator.render(gc, g);
		try {
			Thread.sleep(0); // render just all 1000ms
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Calls a tick in the game logic
	 */
	int time = 0;
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		time += delta;
		if (time >= 100) { // 1000 == 1sec hier zeitraffer einbauen
			this.trafficSimulator.tick(gc);
			time = 0;
		}
	}


}
