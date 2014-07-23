package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.GamePanel;


public class PauseState extends GameState {
	
	private int currentChoice = 0;
	
	private String[] options = {
			"RESUME",
			"OPTIONS",
			"QUIT"
	};
	
	private Font font;
	private Color color;

	public PauseState(GameStateManager gsm)
	{
		this.gsm = gsm;
		color = new Color(168, 16, 0);
		font = new Font("Arial", Font.PLAIN, 10);
	}
	
	public void update()
	{

	}
	
	public void draw(Graphics2D g)
	{
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
			gsm.resumePrevious();
		}
		if(currentChoice == 1)
		{
		}
		if(currentChoice == 2)
		{
			System.exit(0);
		}
	}
	
	public void start()
	{

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

	@Override
	public void init() 
	{
		
	}
	
}
