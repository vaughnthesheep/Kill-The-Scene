package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.GamePanel;
import main.Soundtrack;

public class GameOverState extends GameState {
	
	private Font font;
	
	public GameOverState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		font = new Font("Arial", Font.PLAIN, 10);
	}

	@Override
	public void init() {
		Soundtrack.setSong("Charlsewood.wav"); // change to game over song
		Soundtrack.playOnce();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
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

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
}
