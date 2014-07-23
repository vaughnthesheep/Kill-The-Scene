package Entity;

import TileMap.TileMap;

public abstract class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dying;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long maxFlinchTime;
	protected long flinchTimer;

	public Enemy(TileMap tm)
	{
		super(tm);
		dead = false;
		dying = false;
		flinching = false;
	}
	
	public boolean isDead(){ return dead; }
	public boolean isDying(){ return dying; }
	public boolean isFlinching(){ return flinching; }
	
	public int getDamage(){ return damage; }
	
	public void hit(int damage, boolean fromRight)
	{
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update()
	{
		
	}
	
}
