package GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.GamePanel;
import main.Soundtrack;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private GamePanel gp;
	private int currentState;
	private boolean paused = false;
	private boolean initializing = false;
	
	public static final int STARTUP = -1; // not included in gamestate arraylist
	public static final int MENUSTATE = 0;
	public static final int HELPSTATE = 1;
	public static final int CHOOSECHARACTERSTATE = 2;
	public static final int DEADSTATE = 3;
	public static final int GAMEOVERSTATE = 4;
	public static final int PAUSESTATE = 5;
	public static final int LEVEL_1_1 = 6;
	public static final int LEVEL_1_2 = 7;
	public static final int LEVEL_1_3 = 8;
	
	public static final String[] characters = 
							{
								"VAUGHN",
								"BRAINZ"
							};
	public final int START_LIVES = 3;
	public double money;
	public int lives;
	public String character;
	public final String DEFAULT_CHARACTER = "VAUGHN";
	
	
	private int previousState = LEVEL_1_1;
	
	public GameStateManager(GamePanel panel)
	{
		gp = panel;
		currentState = STARTUP;
		character = DEFAULT_CHARACTER;
		gameStates = new ArrayList<GameState>();
		gameStates.add(new MenuState(this));
		gameStates.add(new HelpState(this));
		gameStates.add(new ChooseCharacterState(this));
		gameStates.add(new DeadState(this));
		gameStates.add(new GameOverState(this));
		gameStates.add(new PauseState(this));
		gameStates.add(new Level_1_1(this));
		setState(MENUSTATE);
		
		// gameplay variables
		
		lives = START_LIVES;
	}
	
	public void setState(int state)
	{
		if(currentState != STARTUP && currentState != MENUSTATE && Soundtrack.isPlaying())
		{
			Soundtrack.stop();
		}
		
		initializing = true;
		currentState = state;
		gameStates.get(currentState).init();
		initializing = false;
	}
	public GameState getState()
	{
		return gameStates.get(currentState);
	}
	
	public void resumePrevious()
	{
		initializing = true;
		currentState = previousState;
		initializing = false;
		
		Soundtrack.play();
		
		if(paused)
			paused = false;
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
		if(k == KeyEvent.VK_ESCAPE && (currentState != STARTUP &&
										currentState != MENUSTATE &&
										currentState != CHOOSECHARACTERSTATE &&
										currentState != HELPSTATE &&
										currentState != DEADSTATE &&
										currentState != GAMEOVERSTATE)) 
		{
			if(paused)
			{
				resumePrevious();
			}
			else
			{
				gameStates.get(currentState).keyReleased(-1);
				previousState = currentState;
				setState(PAUSESTATE);
				paused = true;
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

	public void decLives(int i) 
	{
		lives -= i;
	}
	
	public int getPrevious()
	{
		return previousState;
	}
	
	public void reset()
	{
		gp.init();
	}
}
