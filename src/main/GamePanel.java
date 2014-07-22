package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.JPanel;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// pause screen
	private Font pauseFont = new Font("Arial", Font.PLAIN, 10);
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() 
	{
		super();
		
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	public void init()
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}
	
	public void run()
	{
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running)
		{
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed/1000000;
			if(wait < 0)
			{
				wait = 0;
			}
			
			try
			{
				Thread.sleep(wait);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void update()
	{
		gsm.update();
	}
	
	private void draw()
	{
		gsm.draw(g);
		
		if(gsm.isPaused())
		{
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,WIDTH,HEIGHT);
			g.setColor(new Color(252,252,252));
			
			g.setFont(pauseFont);
			FontMetrics fm = g.getFontMetrics(pauseFont);
			java.awt.geom.Rectangle2D rect;
			rect = fm.getStringBounds("PAUSE", g);
			int textWidth  = (int)(rect.getWidth());
			int x = (WIDTH - textWidth) / 2;
			g.drawString("PAUSE", x, HEIGHT/2);
		}
	}
	
	private void drawToScreen()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key)
	{
		
	}
	public void keyPressed(KeyEvent key)
	{
		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key)
	{
		gsm.keyReleased(key.getKeyCode());
	}
}
