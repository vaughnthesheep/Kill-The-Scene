package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import TileMap.TileMap;

public abstract class Item extends MapObject {
	
	protected BufferedImage[] sprites;
	public boolean pickedUp;
	protected int type;
	protected final int WIDTH = 20;
	protected final int HEIGHT = 20;
	protected final int CWIDTH = 20;
	protected final int CHEIGHT = 20;
	protected final double FALLSPEED = 0.2;
	protected final double MAXFALL = 4.0;
	protected final double MINX = -2;
	protected final double MAXX = 2;
	
	public Item(TileMap tm)
	{
		super(tm);
		Random rand = new Random();
		facingRight = true;
		dy = -2;
		dx = MINX + (MAXX - MINX) * rand.nextDouble();
	}
	
	protected void getNextPosition()
	{
		if(!falling)
			dx = 0;
		
		// falling
		if(falling)
		{
			dy += FALLSPEED;
			
			if(dy > MAXFALL)
			{
				dy = MAXFALL;
			}
		}
	}
	
	public abstract void update();
	public abstract void pickedUp(Player player);
	public void draw(Graphics2D g)
	{
		super.draw(g);
	}
}
