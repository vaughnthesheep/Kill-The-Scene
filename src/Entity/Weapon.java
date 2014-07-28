package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import TileMap.TileMap;

public abstract class Weapon extends MapObject{

	public int swingDamage;
	public int swingForce;
	public int swingRange;
	public int stabDamage;
	public int stabForce;
	public int stabRange;
	public ArrayList<BufferedImage[]> sprites;
	protected int[] numFrames;
	protected static final int IDLE = 0;
	protected static final int WALKING = 1;
	protected static final int JUMPING = 2;
	protected static final int FALLING = 3;
	protected static final int DUCKING = 4;
	protected static final int THROWING = 5;
	protected static final int PUNCHING = 6;
	protected static final int SWINGING = 7;
	protected static final int STABBING = 8;
	protected static final int KICKING = 9;
	protected static final int BLOCKING = 10;
	
	public Weapon(TileMap tm)
	{
		super(tm);
	}
	
	public void draw(Graphics2D g)
	{
		super.draw(g);
	}
	
	public void setCurrentAction(int i)
	{
		currentAction = i;
	}
}
