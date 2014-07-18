package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;



import main.GamePanel;
import main.Soundtrack;

import TileMap.Background;
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
	private Soundtrack soundtrack;

	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		intro = true;
		color = new Color(168, 16, 0);
		soundtrack = new Soundtrack("intro.wav");
		
		try
		{
			in = getClass().getResourceAsStream("/Fonts/Open Sans 600italic.ttf");
			font = new Font("Arial", Font.PLAIN, 10);
			title = ImageIO.read(getClass().getResourceAsStream("/Menu/title.gif"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		soundtrack.play();
	}
	
	public void init()
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void draw(Graphics2D g)
	{
		// draw background
		g.setColor(Color.BLACK);
		g.drawRect(0,0,GamePanel.WIDTH, GamePanel.HEIGHT);
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
	
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ENTER)
		{
			select();
		}
		if(k == KeyEvent.VK_UP)
		{
			currentChoice--;
			if(currentChoice == -1)
			{
				currentChoice = options.length -1;
			}
		}
		if(k == KeyEvent.VK_DOWN)
		{
			currentChoice++;
			if(currentChoice == options.length)
			{
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k)
	{
		
	}
}
