package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class DeadState extends GameState {
	
	private Font font;
	private long startTime;
	private final long WAIT_TIME = 2000; // milliseconds
	private BufferedImage head;
	
	public DeadState(GameStateManager gsm)
	{
		this.gsm = gsm;
		font = new Font("Arial", Font.PLAIN, 10);
	}
	
	
	public void init()
	{
		gsm.decLives(1);
		try
		{
			head =  ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/" + gsm.character + "/head.gif"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(gsm.lives == 0)
			gsm.setState(GameStateManager.GAMEOVERSTATE);
		startTime = System.nanoTime();
	}
	
	public void update()
	{
		if((System.nanoTime() - startTime) / 1000000 >= WAIT_TIME)
		{
			gsm.setState(gsm.getPrevious());
		}
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);
		g.drawImage(head,GamePanel.WIDTH/2-25, GamePanel.HEIGHT/2 - 20, null);
		g.setColor(new Color(252,252,252));
		g.setFont(font);
		g.drawString(" x " + gsm.lives, GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
	}
	
	public void keyPressed(int k)
	{
		return;
	}
	public void keyReleased(int k)
	{
		return;
	}

}
