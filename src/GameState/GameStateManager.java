package GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;
	private boolean paused = false;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public GameStateManager()
	{
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		
		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
	}
	
	public void setState(int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update()
	{
		if(!paused)
			gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ESCAPE) 
			paused = !paused;
		
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}
}
