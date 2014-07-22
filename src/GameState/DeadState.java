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
	private final long WAIT_TIME = 3000; // milliseconds
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

	@Override
	public void init() {
		
		gsm.lives -= 1;
		gsm.setState(gsm.GAMEOVERSTATE);
		startTime = System.nanoTime();
	}

	@Override
	public void update() {
		
		if((System.nanoTime() - startTime) / 1000000 >= WAIT_TIME)
		{
			gsm.setState(gsm.previousState);
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		g.drawImage(sprite,GamePanel.WIDTH-50, GamePanel.HEIGHT-25, null);
		g.setColor(new Color(252,252,252));
		g.setFont(font);
		g.drawString(" x " + gsm.lives, GamePanel.WIDTH, GamePanel.HEIGHT);
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
