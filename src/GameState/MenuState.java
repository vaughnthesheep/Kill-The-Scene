package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.Soundtrack;
import Entity.MenuPlayer;


public class MenuState extends GameState {
	
	private int currentChoice = 0;
	
	private String[] options = {
			"START",
			"HELP",
			"QUIT"
	};
	
	private Font font;
	private Color color;
	private InputStream in;
	private boolean intro;
	private BufferedImage title;
	private ArrayList<BufferedImage> titlePieces;
	
	private MenuPlayer menuplayer;

	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		intro = true;
		color = new Color(168, 16, 0);
		
		try
		{
			in = getClass().getResourceAsStream("/Fonts/Open Sans 600italic.ttf");
			font = new Font("Arial", Font.PLAIN, 10);
			title = ImageIO.read(getClass().getResourceAsStream("/Menu/title.gif"));
			
			titlePieces = new ArrayList<BufferedImage>();
			
			for(int i = 0; i < main.GamePanel.WIDTH/10; i++)
			{
				titlePieces.add(title.getSubimage(i*10, 0, 10, title.getHeight()));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		menuplayer = new MenuPlayer(-30, 100);
		
	}
	
	
	public void init()
	{
		
	}
	
	public void update()
	{
		if(menuplayer.getx() >= (GamePanel.WIDTH / 2 - 17))
		{
			menuplayer.walking = false;
			menuplayer.x = GamePanel.WIDTH / 2 - 17;
			start();
		}
		menuplayer.update();
	}
	
	public void draw(Graphics2D g)
	{
		// draw background
		g.setColor(Color.BLACK);
		g.fillRect(0,0,GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(color);
		g.fillRect(0, 90, GamePanel.WIDTH, 70);
		menuplayer.draw(g);
		
		if(!intro)
		{
			g.drawImage(title, 0, 0, null);
			// draw menu options
			g.setFont(font);
			FontMetrics fm   = g.getFontMetrics(font);
			java.awt.geom.Rectangle2D rect;
			for(int i = 0; i < options.length; i++)
			{
				if(i == currentChoice)
				{
					g.setColor(Color.WHITE);
				}
				else
				{
					g.setColor(color);
				}
				rect = fm.getStringBounds(options[i], g);
				int textWidth  = (int)(rect.getWidth());
				int x = (GamePanel.WIDTH - textWidth) / 2;
				g.drawString(options[i], x, 190 + i*15);
			}
		}
	}
	
	private void select()
	{
		if(currentChoice == 0)
		{
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1)
		{
			//help
		}
		if(currentChoice == 2)
		{
			System.exit(0);
		}
	}
	
	public void start()
	{
		if(intro)
		{
			Soundtrack.setSong("Intro.wav");
			Soundtrack.play();
			intro = false;
		}
	}
	
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ENTER)
		{
			if(!intro)
			{
				select();
			}
			else
			{
				start();
				menuplayer.x = GamePanel.WIDTH / 2 - 15;
			}
		}
		if(k == KeyEvent.VK_UP)
		{
			if(intro)
			{
				
			}
			else
			{
				currentChoice--;
				if(currentChoice == -1)
				{
					currentChoice = options.length -1;
				}
			}
			
		}
		if(k == KeyEvent.VK_DOWN)
		{
			if(intro)
			{
				
			}
			else
			{
				currentChoice++;
				if(currentChoice == options.length)
				{
					currentChoice = 0;
				}
			}
		}
	}
	
	public void keyReleased(int k)
	{
		
	}
	
}
