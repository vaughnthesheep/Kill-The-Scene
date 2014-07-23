package Entity;

import TileMap.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.util.ArrayList;

public class Player extends MapObject {

	// player variables
	
	private int health;
	private int maxHealth;
	private int strength;
	private int maxStrength;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private long maxFlinchTime;
	
	// how fast player can be moved left and right in the air
	private double airMoveSpeed;
	
	// fireball
	private boolean throwing;
	private int projectileCost;
	private int projectileDamage;
	private ArrayList<Projectile> projectiles;
	
	// scratch
	private boolean punching;
	private int punchDamage;
	private int punchRange;
	
	// block
	private boolean blocking;
	private int blockAmount;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	
	// num frames per action (check player sprites)
	private final int[] numFrames = {
		2, 4, 1, 3, 1, 3, 3, 1, 2
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int DUCKING = 4;
	private static final int THROWING = 5;
	private static final int PUNCHING = 6;
	private static final int BLOCKING = 7;
	private static final int DYING = 8;
	
	private static final int CHEIGHT_DEFAULT = 40;
	private static final int CHEIGHT_DUCKING = 20;
	
	
	public Player(TileMap tm)
	{
		super(tm);
		
		width = 30;
		height = 50;
		cwidth = 20;
		cheight = CHEIGHT_DEFAULT;
		
		moveSpeed = 0.4;
		airMoveSpeed = 0.2;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.20;
		maxFallSpeed = 4.0;
		jumpStart = -5.5;
		stopJumpSpeed = 0.3;
		maxFlinchTime = 1000;
		
		facingRight = true;
		
		health = maxHealth = 25;
		strength = maxStrength = 25;
		
		projectileCost = 5;
		projectileDamage = 5;
		projectiles = new ArrayList<Projectile>();
		
		punchDamage = 5;
		punchRange = 40;
		
		blockAmount = 5;
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/spritesheet.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 9; i ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != THROWING && i != PUNCHING && i != BLOCKING && i != DYING)
					{// sprites for scratch are 60 px wide, not 30
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}
					else
					{
						bi[j] = spritesheet.getSubimage(j*width*2, i*height, width*2, height);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
	}
	
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public int getStrength() {return strength;}
	public int getMaxStrength() {return maxStrength;}
	
	public void setThrowing()
	{
		if(currentAction == PUNCHING || currentAction == BLOCKING)
			return;
		throwing = true;
	}
	
	public void setPunching()
	{
		if(currentAction == THROWING || currentAction == BLOCKING)
			return;
		punching = true;
	}
	public void setBlocking(boolean block)
	{
		if(currentAction == PUNCHING || currentAction == THROWING)
			return;
		blocking = block;
	}
	
	
	private void getNextPosition()
	{
		// movement
		if(left)
		{
			if(falling)
				dx -= airMoveSpeed;
			else
				dx -= moveSpeed;
			if(dx < -maxSpeed)
			{
				dx = -maxSpeed;
			}
		}
		else if(right)
		{
			if(falling)
				dx += airMoveSpeed;
			else
				dx += moveSpeed;
			if(dx > maxSpeed)
			{
				dx = maxSpeed;
			}
		}
		else
		{
			if(dx > 0 && !falling)
			{
				dx -= stopSpeed;
				if(dx < 0)
				{
					dx = 0;
				}
			}
			else if(dx < 0 && !falling)
			{
				dx += stopSpeed;
				if(dx > 0)
				{
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking or ducking (unless in air)
		if((currentAction == PUNCHING || currentAction == THROWING || currentAction == DUCKING || currentAction == BLOCKING) && !(jumping || falling))
		{
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling)
		{
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling)
		{
			dy += fallSpeed;
			
			if(dy > 0)
			{
				jumping = false;
			}
			if(dy < 0 && !jumping)
			{
				dy += stopJumpSpeed;
			}
			if(dy > maxFallSpeed)
			{
				dy = maxFallSpeed;
			}
		}
	}
	
	public void update()
	{
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == PUNCHING)
		{
			if(animation.hasPlayedOnce())
			{
				punching = false;
			}
		}
		if(currentAction == THROWING)
		{
			if(animation.hasPlayedOnce())
			{
				throwing = false;
			}
		}
		
		// projectile attack
		
		if(throwing && currentAction != THROWING)
		{
			if(strength >= projectileCost)
			{
				strength -= projectileCost;
				Projectile p = new Projectile(tileMap, facingRight);
				p.setPosition(x, y);
				projectiles.add(p);
			}
		}
		
		// update fireballs
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).update();
			if(projectiles.get(i).shouldRemove())
			{
				projectiles.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > maxFlinchTime)
			{
				flinching = false;
			}
		}
		
		// set animation
		if(punching)
		{
			if(currentAction != PUNCHING)
			{
				currentAction = PUNCHING;
				animation.setFrames(sprites.get(PUNCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(throwing)
		{
			if(currentAction != THROWING)
			{
				currentAction = THROWING;
				animation.setFrames(sprites.get(THROWING));
				animation.setDelay(100);
				width = 60;
			}
		}
		else if(blocking)
		{
			if(currentAction != BLOCKING)
			{
				currentAction = BLOCKING;
				animation.setFrames(sprites.get(BLOCKING));
				animation.setDelay(-1);
				width = 60;
			}
		}
		else if(dy > 0)
		{
			if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(down)
		{
			if(currentAction != DUCKING)
			{
				currentAction = DUCKING;
				animation.setFrames(sprites.get(DUCKING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else
		{
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != PUNCHING && currentAction != THROWING)
		{
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	public void checkAttack(ArrayList<Enemy> enemies)
	{
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			
			// check scratch
			if(punching)
			{
				if(facingRight)
				{
					if(
						e.getx() > x &&
						e.getx() < x + punchRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(punchDamage);
					}
				}
				else
				{
					if(
						e.getx() < x &&
						e.getx() > x - punchRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(punchDamage);
					}
				}
			}
			
			// projectiles
			for(int j = 0; j < projectiles.size(); j++)
			{
				if(projectiles.get(j).intersects(e))
				{
					e.hit(projectileDamage);
					projectiles.get(j).setHit();
				}
			}
			
		}
		
	}
	
	public void hit(int damage)
	{
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void draw(Graphics2D g)
	{
		// IMPORTANT FIRST METHOD
		setMapPosition();
		
		// Draw projectiles
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).draw(g);
		}
		
		// Blinks (does not draw half the time) if flinching
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0)
			{
				return;
			}
		}
		
		super.draw(g);
	}
	
}
