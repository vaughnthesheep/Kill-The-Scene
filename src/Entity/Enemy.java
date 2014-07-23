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
	
	public abstract void hit(int damage, int force, boolean fromRight);
	
	public void update()
	{
		
	}
	
}
