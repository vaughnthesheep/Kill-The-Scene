package GameState;

import Entity.*;
import Entity.Enemies.Brawler;
import Entity.Weapons.WoodenBat;
import TileMap.*;
import java.awt.*;
import java.util.ArrayList;
import main.GamePanel;
import main.Soundtrack;



public class Level_1_1 extends LevelState {
	
	WoodenBat bat;
	public Level_1_1(GameStateManager gsm)
	{
		super(gsm);
		this.gsm = gsm;
	}
	
	public void init()
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		fallLimit = 350;
		
		bat = new WoodenBat(tileMap);
		player = new Player(tileMap, gsm, this, bat); // test
		player.setPosition(100,100);
		hud = new HUD(player);
		
		populateEnemies();
		
		Soundtrack.setSong("Charlsewood.wav");
		Soundtrack.play();
	}
	
	private void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
		Brawler b;
		Point[] points = new Point[]{
			new Point(250, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		
		for(int i = 0; i < points.length; i++)
		{
			b = new Brawler(tileMap, gsm, player, "testbrawler.gif", false);
			b.setPosition(points[i].x, points[i].y);
			enemies.add(b);
		}
		
	}
	
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
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
		
		hud.draw(g);
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
