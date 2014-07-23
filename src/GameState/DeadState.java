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
	private BufferedImage sprite;
	private BufferedImage spritesheet;
	
	public DeadState(GameStateManager gsm)
	{
		this.gsm = gsm;
		font = new Font("Arial", Font.PLAIN, 10);
		try
		{
			spritesheet =  ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/spritesheet.gif"));
			sprite = spritesheet.getSubimage(0, 0, 30, 50);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void init() {
		gsm.decLives(1);
		if(gsm.lives == 0)
			gsm.setState(gsm.GAMEOVERSTATE);
		startTime = System.nanoTime();
	}

	@Override
	public void update() {
		
		if((System.nanoTime() - startTime) / 1000000 >= WAIT_TIME)
		{
			gsm.setState(gsm.getPrevious());
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);
		g.drawImage(sprite,GamePanel.WIDTH/2-25, GamePanel.HEIGHT/2-25, null);
		g.setColor(new Color(252,252,252));
		g.setFont(font);
		g.drawString(" x " + gsm.lives, GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
