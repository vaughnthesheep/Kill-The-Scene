package GameState;

public abstract class GameState {
	
	// MOVE THIS SECTION TO LEVELSTATE SUPERCLASS ONCE CREATED,
	// USELESS FOR NON-LEVEL STATES
	protected int fallLimit;
	public int getFallLimit()
	{
		return fallLimit;
	}

	protected GameStateManager gsm;
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
