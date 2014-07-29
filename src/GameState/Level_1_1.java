package GameState;

import Entity.*;
import Entity.Enemies.Brawler;
import Entity.Items.WeaponItem;
import TileMap.*;
import java.awt.*;
import java.util.ArrayList;
import main.Soundtrack;



public class Level_1_1 extends LevelState {
	
	public Level_1_1(GameStateManager gsm)
	{
		super(gsm);
		this.gsm = gsm;
	}
	
	public void init()
	{
		tileMap = new TileMap(30);
		fallLimit = 320;
		tileMap.loadTiles("/Tilesets/charlsewood.gif");
		tileMap.loadMap("/Maps/cwoodtest.map");
		tileMap.setPosition(0, 0);
		bg = new Background("/Backgrounds/cwoodbg.gif", 0.1);
		player = new Player(tileMap, gsm, this);
		player.setPosition(100,100);
		hud = new HUD(player);
		
		populateEnemies();
		populateItems();
		
		Soundtrack.setSong("Charlsewood.wav");
		Soundtrack.play();
	}
	
	public void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
		Brawler b;
		Point[] points = new Point[]{
			new Point(250, 100),
			new Point(860, 100),
			new Point(1525, 100),
			new Point(1680, 100),
			new Point(1800, 100)
		};
		
		for(int i = 0; i < points.length; i++)
		{
			b = new Brawler(tileMap, gsm, player, "testbrawler.gif", false);
			b.setPosition(points[i].x, points[i].y);
			enemies.add(b);
		}
	}
	
	public void populateItems()
	{
		items = new ArrayList<Item>();
		WeaponItem w = new WeaponItem(tileMap, player, WeaponItem.WOODEN_BAT);
		w.setPosition(200,100);
		items.add(w);
	}
	
	public void update()
	{
		super.update();
	}
	
	public void draw(Graphics2D g)
	{
		super.draw(g);
	}
	
	public void keyPressed(int k)
	{
		player.keyPressed(k);
	}
	public void keyReleased(int k)
	{
		player.keyReleased(k);
	}
}
