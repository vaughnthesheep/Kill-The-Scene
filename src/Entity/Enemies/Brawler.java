package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import GameState.GameStateManager;
import TileMap.*;
import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage; 
import java.util.ArrayList;

public class Brawler extends Enemy {
	
	GameStateManager gsm;
	Player player;

	// player variables
	private int health;
	private int maxHealth;
	private long dyingTimer;	// used twice, once for each frame
	private long flinchTimer;
	private long maxFlinchTime;
	private double airMoveSpeed;
	
	
	// scratch
	private boolean punching;
	private int punchDamage;
	private int punchRange;
	private int punchForce;
	private long punchReload;
	private long lastPunch;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	
	// num frames per action (check player sprites)
	private final int[] numFrames = {
		2, 4, 1, 3, 3, 1, 1
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int PUNCHING = 4;
	private static final int DYING1 = 5;
	private static final int DYING2 = 6;
	
	private final long DYING_TIME = 400;
	
	
	public Brawler(TileMap tm, GameStateManager gsm, Player player, String spriteType, boolean facingRight)
	{
		super(tm);
		this.gsm = gsm;
		this.facingRight = facingRight;
		this.player = player;
		
		width = 30;
		height = 50;
		cwidth = 20;
		cheight = 40;
		
		moveSpeed = 0.2;
		airMoveSpeed = 0.2;
		maxSpeed = 1;
		stopSpeed = 0.4;
		fallSpeed = 0.20;
		maxFallSpeed = 4.0;
		jumpStart = -5.5;
		stopJumpSpeed = 0.3;
		maxFlinchTime = 1000;
		
		health = maxHealth = 10;
		
		punchDamage = 5;
		punchRange = 30;
		punchForce = 3;
		punchReload = 1000;
		
		
		
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/"+spriteType));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != PUNCHING && i != DYING1 && i!= DYING2)
					{// sprites for scratch are 60 px wide, not 30
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}
					else if(i == PUNCHING)
					{
						bi[j] = spritesheet.getSubimage(j*width*2, i*height, width*2, height);
					}
					else if(i == DYING1)
					{
						bi[j] = spritesheet.getSubimage(j*width*2, i*height, width*2, height);
					}
					else if(i == DYING2)
					{
						bi[j] = spritesheet.getSubimage(j*width*2 + width*2, i*height - height, width*2, height);
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
	
	
	public void setPunching()
	{
		if((System.nanoTime() - lastPunch) / 1000000 < punchReload || dying)
			return;
		punching = true;
		lastPunch = System.nanoTime();
	}
	
	
	private void getNextPosition()
	{
		think();
		
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
		if((currentAction == PUNCHING || currentAction == DYING1 || currentAction == DYING2) && !(jumping || falling))
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
		
		// check if fell off cliff
		if(ytemp > gsm.getState().getFallLimit() && !dying)
		{
			dying();
			dx = 0;
			dy = 0;
			left = false;
			right = false;
		}
		
		// check attack has stopped
		if(currentAction == PUNCHING)
		{
			if(animation.hasPlayedOnce())
			{
				punching = false;
			}
		}
		
		// set animation
		if(dying)
		{
			if(currentAction != DYING1 && currentAction != DYING2)
			{
				currentAction = DYING1;
				animation.setFrames(sprites.get(DYING1));
				animation.setDelay(-1);
				width = 60;
			}
			else if(currentAction == DYING1)
			{
				if((System.nanoTime() - dyingTimer) / 1000000 > DYING_TIME)
				{
					currentAction = DYING2;
					animation.setFrames(sprites.get(DYING2));
					animation.setDelay(-1);
					dyingTimer = System.nanoTime();
				}
			}
			else if(currentAction == DYING2)
			{
				if((System.nanoTime() - dyingTimer) / 1000000 > DYING_TIME)
				{
					dead = true;
				}
			}
		}
		else if(punching)
		{
			if(currentAction != PUNCHING)
			{
				currentAction = PUNCHING;
				animation.setFrames(sprites.get(PUNCHING));
				animation.setDelay(50);
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
		if(currentAction != PUNCHING)
		{
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	public void checkAttack(Player player)
	{
		if(dying)
			return;
		// check scratch
		if(punching)
		{
			if(facingRight)
			{
				if(
					player.getx() > x &&
					player.getx() < x + punchRange &&
					player.gety() > y - height/2 &&
					player.gety() < y + height/2
				){
					player.hit(punchDamage, punchForce, false);
				}
			}
			else
			{
				if(
					player.getx() < x &&
					player.getx() > x - punchRange &&
					player.gety() > y - height/2 &&
					player.gety() < y + height/2
				){
					player.hit(punchDamage, punchForce, true);
				}
			}
		}
			
	}
	
	public void hit(int damage, int force, boolean fromRight)
	{
		if(dying) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0)
		{
			dying();
		}
		dy = -force;
		if(fromRight)
		{
			dx = -force;
		}
		else
		{
			dx = force;
		}
	}
	
	public void dying()
	{
		dying = true;
		dyingTimer = System.nanoTime();
	}
	
	public boolean isDying()
	{
		return dying;
	}
	
	public void draw(Graphics2D g)
	{
		// IMPORTANT FIRST METHOD
		setMapPosition();
		super.draw(g);
	}
	
	public void think()
	{
		if(currentAction == PUNCHING || dying || dead)
		{
			return;
		}
		else
		{
		
			int distance = (int)x - (int)player.getx();
			int abs = Math.abs(distance);
			if(abs < GamePanel.WIDTH/2)// if in sight
			{	
				if(abs > 30)
				{
					if(distance < 0)
					{
						right = true;
						left = false;
					}
					else
					{
						left = true;
						right = false;
					}
				}
				else
				{
					if(distance < 0 && !facingRight)
					{
						right = true;
						left = false;
					}
					else if(distance > 0 && facingRight)
					{
						left = true;
						right = false;
					}
					else
					{
						left = false;
						right = false;
						setPunching();
					}
				}
			}
		}
	}
	
}
