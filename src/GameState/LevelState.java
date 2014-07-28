package GameState;

import Entity.*;
import TileMap.*;

import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GamePanel;

public abstract class LevelState extends GameState {
	
	protected TileMap tileMap;
	protected GameStateManager gsm;
	protected Background bg;
	protected Player player;
	protected HUD hud;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Item> items;
	
	protected LevelState(GameStateManager gsm)
	{
		this.gsm = gsm;
	}
	
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	
	public abstract void populateEnemies();
	public abstract void populateItems();
	
	protected void updateEnemies()
	{
		if(enemies == null)
			return;
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				enemies.remove(i);
				i--;
			}
		}
	}
	protected void updateItems()
	{
		if(items == null)
			return;
		for(int i = 0; i < items.size(); i++)
		{
			Item e = items.get(i);
			e.update();
			if(e.pickedUp)
			{
				items.remove(i);
				i--;
			}
		}
	}
	public void update()
	{
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		updateEnemies();
		updateItems();
	}
	public void draw(Graphics2D g)
	{
		bg.draw(g);
		tileMap.draw(g);
		player.draw(g);
		for(int i = 0; i < enemies.size(); i ++)
		{
			enemies.get(i).draw(g);
		}
		for(int i = 0; i < items.size(); i ++)
		{
			items.get(i).draw(g);
		}
		
		hud.draw(g);
	}

}
