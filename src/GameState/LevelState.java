package GameState;

import Entity.*;
import Entity.Enemies.Brawler;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.GamePanel;
import main.Soundtrack;

public abstract class LevelState extends GameState {
	
	protected TileMap tileMap;
	protected GameStateManager gsm;
	protected Background bg;
	protected Player player;
	protected HUD hud;
	protected ArrayList<Enemy> enemies;
	
	protected LevelState(GameStateManager gsm)
	{
		this.gsm = gsm;
	}
	
	public abstract ArrayList<Enemy> getEnemies();
	
	protected void updateEnemies()
	{
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				enemies.remove(i);
				i--;
			}
			else
			{
				e.checkAttack(player);
			}
		}
	}

}
