package Entity;

import Entity.Weapons.Weapon;
import GameState.GameStateManager;
import GameState.LevelState;
import TileMap.*;
import javax.imageio.ImageIO;
import main.Soundtrack;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage; 
import java.util.ArrayList;

public class Player extends MapObject {
	
	GameStateManager gsm;
	LevelState level;

	// player variables
	private int health;
	private int maxHealth;
	private int strength;
	private int maxStrength;
	private boolean dying;
	private long dyingTimer;
	private boolean flinching;
	private long flinchTimer;
	private long maxFlinchTime;
	
	// how fast player can be moved left and right in the air
	private double airMoveSpeed;
	
	// projectile
	private boolean throwing;
	private int projectileCost;
	private int projectileDamage;
	private int projectileForce;
	private ArrayList<Projectile> projectiles;
	private boolean throwEnabled;
	
	// Melee
	private boolean punching;
	private int punchDamage;
	private int punchRange;
	private int punchForce;
	private boolean punchEnabled;
	
	// Weapon
	private boolean swinging;
	private boolean swingEnabled;
	private boolean stabbing;
	private boolean stabEnabled;
	private Weapon weapon;
	private boolean equipped;
	
	private boolean kicking;
	private int kickDamage;
	private int kickRange;
	private int kickForce;
	
	// block
	private boolean blocking;
	private int blockAmount;
	// fix for air blocking
	private boolean tryingToBlock;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	
	// num frames per action (check player sprites)
	private final int[] numFrames = {
		2, 4, 1, 3, 1, 3, 3, 3, 3, 3, 1, 1, 1
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int DUCKING = 4;
	private static final int THROWING = 5;
	private static final int PUNCHING = 6;
	private static final int SWINGING = 7;
	private static final int STABBING = 8;
	private static final int KICKING = 9;
	private static final int BLOCKING = 10;
	private static final int DYING1 = 11;
	private static final int DYING2 = 12;
	
	public static final int IDLE_DELAY = 400;
	public static final int WALKING_DELAY = 100;
	public static final int JUMPING_DELAY = -1;
	public static final int FALLING_DELAY = 100;
	public static final int DUCKING_DELAY = -1;
	public static final int THROWING_DELAY = 80;
	public static final int PUNCHING_DELAY = 50;
	public static final int SWINGING_DELAY = 80;
	public static final int STABBING_DELAY = 80;
	public static final int KICKING_DELAY = 120;
	public static final int BLOCKING_DELAY = -1;
	public static final int DYING1_DELAY = -1;
	public static final int DYING2_DELAY = -1;
	
	private final long DYING_TIME = 1600;
	
	
	public Player(TileMap tm, GameStateManager gsm, LevelState level)
	{
		super(tm);
		this.gsm = gsm;
		this.level = level;
		weapon = null;
		equipped = false;
		
		width = 30;
		height = 50;
		cwidth = 20;
		cheight = 40;
		
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
		projectileForce = 3;
		projectiles = new ArrayList<Projectile>();
		throwEnabled = true;
		
		punchDamage = 5;
		punchRange = 30;
		punchForce = 2;
		punchEnabled = true;
		
		kickDamage = 5;
		kickRange = 50;
		kickForce = 4;
		
		blockAmount = 5;
		tryingToBlock = false;
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/" + gsm.character + "/spritesheet.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != THROWING && i != PUNCHING && i != KICKING && i != SWINGING && i != STABBING && i != BLOCKING && i != DYING1 && i != DYING2)
					{// sprites for scratch are 60 px wide, not 30
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}
					else if(i == DYING2)
					{
						bi[j] = spritesheet.getSubimage(j*width*2 + width*2, i*height - height, width*2, height);
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
	
	public Player(TileMap tm, GameStateManager gsm, LevelState level, Weapon weapon)
	{
		super(tm);
		this.gsm = gsm;
		this.level = level;
		this.weapon = weapon;
		equipped = true;
		
		width = 30;
		height = 50;
		cwidth = 20;
		cheight = 40;
		
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
		projectileForce = 3;
		projectiles = new ArrayList<Projectile>();
		throwEnabled = true;
		
		punchDamage = 5;
		punchRange = 30;
		punchForce = 2;
		punchEnabled = true; // used for swinging as well
		
		kickDamage = 5;
		kickRange = 50;
		kickForce = 4;
		
		blockAmount = 5;
		tryingToBlock = false;
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/" + gsm.character + "/spritesheet.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != THROWING && i != PUNCHING && i != KICKING && i != SWINGING && i != STABBING && i != BLOCKING && i != DYING1 && i != DYING2)
					{// sprites for scratch are 60 px wide, not 30
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}
					else if(i == DYING2)
					{
						bi[j] = spritesheet.getSubimage(j*width*2 + width*2, i*height - height, width*2, height);
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
		animation.setDelay(IDLE_DELAY);
		if(equipped)
		{
			weapon.animation.setFrames(weapon.sprites.get(IDLE));
			weapon.animation.setDelay(IDLE_DELAY);
		}
		
	}
	
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public int getStrength() {return strength;}
	public int getMaxStrength() {return maxStrength;}
	
	
	public void setThrowing()
	{
		if(currentAction == PUNCHING || currentAction == BLOCKING || currentAction == KICKING || currentAction == SWINGING || currentAction == STABBING || !throwEnabled || strength < projectileCost)
			return;
		throwing = true;
		throwEnabled = false;
	}
	
	public void setPunching()
	{
		if(currentAction == THROWING || currentAction == KICKING || currentAction == BLOCKING || currentAction == SWINGING || currentAction == STABBING || !punchEnabled)
			return;
		if(currentAction != PUNCHING)
		{
			punching = true;
			checkAttack(level.getEnemies());
			punchEnabled = false;
		}
		else
		{
			punching = false;
			kicking = true;
			checkAttack(level.getEnemies());
			punchEnabled = false;
		}
	}
	public void setSwinging()
	{
		if(currentAction == THROWING || currentAction == KICKING || currentAction == BLOCKING || currentAction == PUNCHING || !punchEnabled)
			return;
		if(currentAction != SWINGING && currentAction != STABBING && currentAction != KICKING)
		{
			swinging = true;
			checkAttack(level.getEnemies());
			punchEnabled = false;
		}
		else if(currentAction == SWINGING)
		{
			swinging = false;
			stabbing = true;
			checkAttack(level.getEnemies());
			punchEnabled = false;
		}
		else
		{
			stabbing = false;
			kicking = true;
			checkAttack(level.getEnemies());
			punchEnabled = false;
		}
	}
	public void setBlocking(boolean block)
	{
		tryingToBlock = block;
		if(currentAction == PUNCHING ||
			currentAction == THROWING ||
			currentAction == KICKING ||
			currentAction == JUMPING ||
			currentAction == FALLING ||
			currentAction == SWINGING ||
			currentAction == STABBING)
		{
			return;
		}
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
		if((currentAction == PUNCHING ||
				currentAction == THROWING ||
				currentAction == KICKING ||
				currentAction == SWINGING ||
				currentAction == STABBING ||
				currentAction == DUCKING ||
				currentAction == BLOCKING ||
				currentAction == DYING1 ||
				currentAction == DYING2) && 
				!(jumping || falling))
		{
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling)
		{
			dy = jumpStart;
			falling = true;
			blocking = false;
		}
		
		// falling
		if(falling)
		{
			dy += fallSpeed;
			blocking = false;
			
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
		if(equipped)
		{
			weapon.setPosition(xtemp, ytemp);
		}
		
		// check if fell off cliff
		if(ytemp > gsm.getState().getFallLimit() && !dying)
		{
			dying();
			dx = 0;
			dy = 0;
			left = false;
			right = false;
		}
		
		if(!jumping && !falling && tryingToBlock)
		{
			blocking = true;
		}
		
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
		if(currentAction == SWINGING)
		{
			if(animation.hasPlayedOnce())
			{
				swinging = false;
			}
		}
		if(currentAction == STABBING)
		{
			if(animation.hasPlayedOnce())
			{
				stabbing = false;
			}
		}
		if(currentAction == KICKING)
		{
			if(animation.hasPlayedOnce())
			{
				kicking = false;
			}
		}
		
		// projectile attack
		
		if(throwing && currentAction != THROWING && strength >= projectileCost)
		{
			strength -= projectileCost;
			Projectile p = new Projectile(tileMap, up, down, left, right, facingRight, falling);
			int start = (int)x;
			if(!facingRight)
				start -= 10;
			else
				start += 10;
			p.setPosition(start, y - 10);
			projectiles.add(p);
		}
		
		// update projectiles
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
		if(dying)
		{
			if(currentAction != DYING1 && currentAction != DYING2)
			{
				currentAction = DYING1;
				animation.setFrames(sprites.get(DYING1));
				animation.setDelay(DYING1_DELAY);
				width = 60;
			}
			else if(currentAction == DYING1)
			{
				if((System.nanoTime() - dyingTimer) / 1000000 > DYING_TIME)
				{
					currentAction = DYING2;
					animation.setFrames(sprites.get(DYING2));
					animation.setDelay(DYING2_DELAY);
				}
			}
			// set to DeadState
			if((System.nanoTime() - dyingTimer) / 1000000 > 3500)
			{
				gsm.setState(GameStateManager.DEADSTATE);
			}
		}
		else if(punching)
		{
			if(currentAction != PUNCHING)
			{
				currentAction = PUNCHING;
				animation.setFrames(sprites.get(PUNCHING));
				animation.setDelay(PUNCHING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(PUNCHING));
					weapon.animation.setDelay(PUNCHING_DELAY);
				}
			}
		}
		else if(throwing)
		{
			if(currentAction != THROWING)
			{
				currentAction = THROWING;
				animation.setFrames(sprites.get(THROWING));
				animation.setDelay(THROWING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(THROWING));
					weapon.animation.setDelay(THROWING_DELAY);
				}
			}
		}
		else if(swinging)
		{
			if(currentAction != SWINGING)
			{
				currentAction = SWINGING;
				animation.setFrames(sprites.get(SWINGING));
				animation.setDelay(SWINGING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(SWINGING));
					weapon.animation.setDelay(SWINGING_DELAY);
				}
			}
		}
		else if(stabbing)
		{
			if(currentAction != STABBING)
			{
				currentAction = STABBING;
				animation.setFrames(sprites.get(STABBING));
				animation.setDelay(STABBING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(STABBING));
					weapon.animation.setDelay(STABBING_DELAY);
				}
			}
		}
		else if(kicking)
		{
			if(currentAction != KICKING)
			{
				currentAction = KICKING;
				animation.setFrames(sprites.get(KICKING));
				animation.setDelay(KICKING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(KICKING));
					weapon.animation.setDelay(KICKING_DELAY);
				}
			}
		}
		else if(blocking)
		{
			if(currentAction != BLOCKING)
			{
				currentAction = BLOCKING;
				animation.setFrames(sprites.get(BLOCKING));
				animation.setDelay(BLOCKING_DELAY);
				width = 60;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(BLOCKING));
					weapon.animation.setDelay(BLOCKING_DELAY);
				}
			}
		}
		else if(dy > 0)
		{
			if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(FALLING_DELAY);
				width = 30;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(BLOCKING));
					weapon.animation.setDelay(BLOCKING_DELAY);
				}
			}
		}
		else if(dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(JUMPING_DELAY);
				width = 30;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(JUMPING));
					weapon.animation.setDelay(JUMPING_DELAY);
				}
			}
		}
		else if(left || right)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(WALKING_DELAY);
				width = 30;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(WALKING));
					weapon.animation.setDelay(WALKING_DELAY);
				}
			}
		}
		else if(down)
		{
			if(currentAction != DUCKING)
			{
				currentAction = DUCKING;
				animation.setFrames(sprites.get(DUCKING));
				animation.setDelay(DUCKING_DELAY);
				width = 30;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(DUCKING));
					weapon.animation.setDelay(DUCKING_DELAY);
				}
			}
		}
		else
		{
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(IDLE_DELAY);
				width = 30;
				if(equipped)
				{
					weapon.animation.setFrames(weapon.sprites.get(IDLE));
					weapon.animation.setDelay(IDLE_DELAY);
				}
			}
		}
		
		animation.update();
		if(equipped)
		{
			weapon.animation.update();
		}
		
		// set direction
		if(currentAction != PUNCHING && currentAction != THROWING && currentAction != KICKING && currentAction != SWINGING && currentAction != STABBING)
		{
			if(right)
				facingRight = true;
			if(left)
				facingRight = false;
			
			if(equipped)
			{
				weapon.facingRight = facingRight;
			}
		}
		
		ArrayList<Enemy> enemies = level.getEnemies();
		
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			for(int j = 0; j < projectiles.size(); j++)
			{
				if(!projectiles.get(j).isHit() && projectiles.get(j).intersects(e) && !e.isDying() && !e.isDead())
				{
					e.hit(projectileDamage, projectileForce, !projectiles.get(j).facingRight);
					projectiles.get(j).setHit();
				}
			}
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
						e.hit(punchDamage, punchForce, false);
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
						e.hit(punchDamage, punchForce, true);
					}
				}
			}
			else if(swinging)
			{
				if(facingRight)
				{
					if(
						e.getx() > x &&
						e.getx() < x + weapon.swingRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(weapon.swingDamage, weapon.swingForce, false);
					}
				}
				else
				{
					if(
						e.getx() < x &&
						e.getx() > x - weapon.swingRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(weapon.swingDamage, weapon.swingForce, true);
					}
				}
			}
			else if(stabbing)
			{
				if(facingRight)
				{
					if(
						e.getx() > x &&
						e.getx() < x + weapon.stabRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(weapon.stabDamage, weapon.stabForce, false);
					}
				}
				else
				{
					if(
						e.getx() < x &&
						e.getx() > x - weapon.stabRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(weapon.stabDamage, weapon.stabForce, true);
					}
				}
			}
			else if(kicking)
			{
				if(facingRight)
				{
					if(
						e.getx() > x &&
						e.getx() < x + kickRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(kickDamage, kickForce, false);
					}
				}
				else
				{
					if(
						e.getx() < x &&
						e.getx() > x - kickRange &&
						e.gety() > y - height/2 &&
						e.gety() < y + height/2
					){
						e.hit(kickDamage, kickForce, true);
					}
				}
			}
		}
	}
	
	public void hit(int damage, int force, boolean fromRight)
	{
		if(dying) return;
		if(flinching) return;
		if(blocking && fromRight == facingRight)
		{
			int reduced = damage - blockAmount;
			if(reduced <= 0)
				return;
			health -= reduced;
			if(health < 0) health = 0;
			if(health == 0)
			{
				dying();
			}
			else
			{
				flinching = true;
				flinchTimer = System.nanoTime();
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
		else
		{
			health -= damage;
			if(health < 0) health = 0;
			if(health == 0)
			{
				dying();
			}
			else
			{
				flinching = true;
				flinchTimer = System.nanoTime();
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
	}
	
	public void dying()
	{
		dying = true;
		weapon = null;
		dyingTimer = System.nanoTime();
		Soundtrack.stop();
		Soundtrack.setSong("Dead.wav");
		Soundtrack.playOnce();
	}
	
	public boolean isDying()
	{
		return dying;
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
		if(equipped)
		{
			weapon.draw(g);
		}
	}
	
	public void keyPressed(int k)
	{
		if(!dying)
		{
			if(k == KeyEvent.VK_LEFT) setLeft(true);
			if(k == KeyEvent.VK_RIGHT) setRight(true);
			if(k == KeyEvent.VK_UP) setUp(true);
			if(k == KeyEvent.VK_DOWN) setDown(true);
			if(k == KeyEvent.VK_W) setJumping(true);
			if(k == KeyEvent.VK_E) setBlocking(true);
			if(k == KeyEvent.VK_R)
			{
				if(!equipped)
					setPunching();
				else
					setSwinging();
			}
			if(k == KeyEvent.VK_F) setThrowing();
		}
	}
	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_LEFT) setLeft(false);
		if(k == KeyEvent.VK_RIGHT) setRight(false);
		if(k == KeyEvent.VK_UP) setUp(false);
		if(k == KeyEvent.VK_DOWN) setDown(false);
		if(k == KeyEvent.VK_W) setJumping(false);
		if(k == KeyEvent.VK_E)
		{
			setBlocking(false);
			tryingToBlock = false;
		}
		if(k == KeyEvent.VK_R) punchEnabled = true;
		if(k == KeyEvent.VK_F) throwEnabled = true;
		
		if(k == -1)
		{
			setLeft(false);
			setRight(false);
			setUp(false);
			setDown(false);
			setJumping(false);
			setBlocking(false);
		}
	}
}
