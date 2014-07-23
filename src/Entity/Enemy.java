package Entity;

import TileMap.TileMap;

public abstract class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dying;
	protected boolean dead;
	protected int damage;

	public Enemy(TileMap tm)
	{
		super(tm);
		dead = false;
		dying = false;
	}
	
	public boolean isDead(){ return dead; }
	public boolean isDying(){ return dying; }
	
	public int getDamage(){ return damage; }
	
	public abstract void hit(int damage, int force, boolean fromRight);
	public abstract void checkAttack(Player player);
	
	public void update()
	{
		
	}
	
}
