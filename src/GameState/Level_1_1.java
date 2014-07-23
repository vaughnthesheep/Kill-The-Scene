package GameState;

import Entity.*;
import Entity.Enemies.Brawler;
import Entity.Enemies.Slugger;
import TileMap.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.GamePanel;
import main.Soundtrack;



public class Level_1_1 extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private HUD hud;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;

	public Level_1_1(GameStateManager gsm)
	{
		this.gsm = gsm;
	}
	
	public void init()
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100,100);
		hud = new HUD(player);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		Soundtrack.setSong("Charlsewood.wav");
		Soundtrack.play();
	}
	
	private void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
		Slugger s;
		Point[] points = new Point[]{
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		
		for(int i = 0; i < points.length; i++)
		{
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		// brawler test
		
		//Brawler b = new Brawler(tileMap, "playersprites.gif");
		//b.setPosition(250,150);
		//enemies.add(b);
	}
	
	public void update()
	{
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		// update background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// update enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
			{
				explosions.remove(i);
				i--;
			}
		}
		
	}
	public void draw(Graphics2D g)
	{
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i ++)
		{
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		// draw HUD
		hud.draw(g);
	}
	
	
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_R) player.setPunching();
		if(k == KeyEvent.VK_E) player.setBlocking(true);
		if(k == KeyEvent.VK_F) player.setThrowing();
	}
	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setBlocking(false);
	}
}
