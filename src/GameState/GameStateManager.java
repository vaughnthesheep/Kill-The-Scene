package GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.Soundtrack;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;
	private boolean paused = false;
	private boolean initializing = false;
	public static final int STARTUP = -1; // not included in gamestate arraylist
	public static final int MENUSTATE = 0;
	public static final int DEADSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int LEVEL_1_1 = 3;
	public static final int LEVEL_1_2 = 4;
	public static final int LEVEL_1_3 = 5;
	
	
	public GameStateManager()
	{
		currentState = STARTUP;
		gameStates = new ArrayList<GameState>();
		gameStates.add(new MenuState(this));
		gameStates.add(new DeadState(this));
		gameStates.add(new GameOverState(this));
		gameStates.add(new Level_1_1(this));
		setState(MENUSTATE);
	}
	
	public void setState(int state)
	{
		if(currentState != STARTUP)
		{
			Soundtrack.stop();
		}
		initializing = true;
		currentState = state;
		gameStates.get(currentState).init();
		initializing = false;
	}
	
	public void update()
	{
		if(!paused && !initializing)
			gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		if(!initializing)
			gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_ESCAPE && currentState != MENUSTATE)
		{
			paused = !paused;
			if(paused == true)
			{
				Soundtrack.stop();
			}
			else
			{
				Soundtrack.play();
			}
		}
		
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}

	public boolean isPaused() 
	{
		return paused;
	}
}
