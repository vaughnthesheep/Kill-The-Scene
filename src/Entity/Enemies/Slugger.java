package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Slugger extends Enemy{
	
	private BufferedImage[] sprites;

	public Slugger(TileMap tm)
	{
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		maxFlinchTime = 400;
		
		width = 30;
		height = 50;
		cwidth = 20;
		cheight = 40;
		
		health = maxHealth = 2;
		damage = 1;
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));
		
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++)
			{
				sprites[i] = spritesheet.getSubimage(width*i,0,width,height);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	private void getNextPosition()
	{
		// movement
		if(left)
		{
			dx -= moveSpeed;
			if(dx < -maxSpeed)
			{
				dx = -maxSpeed;
			}
		}
		else if(right)
		{
			dx += moveSpeed;
			if(dx > maxSpeed)
			{
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling)
		{
			dy += fallSpeed;
		}
	}
	
	public void update()
	{
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > maxFlinchTime)
			{
				flinching = false;
			}
		}
		
		// movement
		// if hits wall
		if(right && dx == 0)
		{
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0)
		{
			left = false;
			right = true;
			facingRight = true;
		}
		
		animation.update();
		
	}
	
	public void draw(Graphics2D g)
	{
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
	}
	
}
