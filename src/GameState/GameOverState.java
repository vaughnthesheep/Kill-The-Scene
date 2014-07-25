package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.GamePanel;
import main.Soundtrack;

public class GameOverState extends GameState {
	
	private Font font;
	private long startTime;
	private final long WAIT_TIME = 5000;
	
	public GameOverState(GameStateManager gsm)
	{
		this.gsm = gsm;
	}
	
	@Override
	public void init() {
		font = new Font("Arial", Font.PLAIN, 10);
		startTime = System.nanoTime();
		Soundtrack.setSong("Game Over.wav"); // change to game over song
		Soundtrack.playOnce();
	}

	@Override
	public void update() {
		if((System.nanoTime() - startTime) / 1000000 >= WAIT_TIME)
		{
			gsm.reset();
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,GamePanel.WIDTH,GamePanel.HEIGHT);
		g.setColor(new Color(252,252,252));
		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		java.awt.geom.Rectangle2D rect;
		rect = fm.getStringBounds("GAME OVER", g);
		int textWidth  = (int)(rect.getWidth());
		int x = (GamePanel.WIDTH - textWidth) / 2;
		g.drawString("GAME OVER", x, GamePanel.HEIGHT/2);
		
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
