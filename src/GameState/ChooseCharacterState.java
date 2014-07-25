package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import Entity.MenuPlayer;
import main.GamePanel;

public class ChooseCharacterState extends GameState {
	
	private GameStateManager gsm;
	private Font font;
	private String[] characters;
	private MenuPlayer menuplayer;
	private int currentChoice;
	private String currentName;
	private boolean shifting;
	private boolean switched; // check if menu player has been switched during shift
	private int dx;
	FontMetrics fm;
	Rectangle2D rect;
	
	public ChooseCharacterState(GameStateManager gsm)
	{
		this.gsm = gsm;
		currentChoice = 0;
		shifting = false;
	}
	
	public void init()
	{
		font = new Font("Arial", Font.PLAIN, 10);
		menuplayer = new MenuPlayer(GamePanel.WIDTH / 2 - 17, 100, gsm.DEFAULT_CHARACTER, false);
		menuplayer.walking = false;
		characters = GameStateManager.characters;
		currentName = characters[currentChoice];
	}

	@Override
	public void update()
	{
		menuplayer.update();
		if(shifting)
		{
			menuplayer.x += dx;
			if(menuplayer.x > GamePanel.WIDTH+15 && !switched)
			{
				menuplayer = new MenuPlayer(-15, 100, characters[currentChoice], false);
				switched = true;
				currentName = characters[currentChoice];
			}
			else if(menuplayer.x < -15 && !switched)
			{
				menuplayer = new MenuPlayer(GamePanel.WIDTH+15, 100, characters[currentChoice], false);
				switched = true;
				currentName = characters[currentChoice];
			}
			else if(switched && dx > 0 && menuplayer.x > (GamePanel.WIDTH / 2 - 17))
			{
				menuplayer.x = (GamePanel.WIDTH / 2 - 17);
				dx = 0;
				shifting = false;
			}
			else if(switched && dx < 0 && menuplayer.x < (GamePanel.WIDTH / 2 - 17))
			{
				menuplayer.x = (GamePanel.WIDTH / 2 - 17);
				dx = 0;
				shifting = false;
			}
		}
	}

	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);
		g.setColor(new Color(168, 16, 0));
		g.fillRect(0, 90, GamePanel.WIDTH, 70);
		fm = g.getFontMetrics(font);
		rect = fm.getStringBounds("CHOOSE YOUR CHARACTER", g);
		int textWidth  = (int)(rect.getWidth());
		int x = (GamePanel.WIDTH - textWidth) / 2;
		g.drawString("CHOOSE YOUR CHARACTER", x, 50);
		rect = fm.getStringBounds(currentName, g);
		textWidth  = (int)(rect.getWidth());
		x = (GamePanel.WIDTH - textWidth) / 2;
		g.setColor(Color.WHITE);
		g.drawString(currentName, x, 205);
		
		menuplayer.draw(g);
	}
	

	@Override
	public void keyPressed(int k)
	{
		if(shifting)
		{
			return;
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			currentChoice++;
			if(currentChoice == characters.length)
			{
				currentChoice = 0;
			}
			dx = -10;
			shifting = true;
			switched = false;
		}
		if(k == KeyEvent.VK_LEFT)
		{
			currentChoice--;
			if(currentChoice == -1)
			{
				currentChoice = characters.length -1;
			}
			dx = 10;
			shifting = true;
			switched = false;
		}
		if(k == KeyEvent.VK_ENTER)
		{
			gsm.character = characters[currentChoice];
			gsm.setState(GameStateManager.LEVEL_1_1);
		}
	}
	
	
	public void keyReleased(int k)
	{
		// nothing
	}
	
	
	
}
