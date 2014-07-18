package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MenuPlayer {
	
	private ArrayList<BufferedImage[]> sprites;
	public boolean walking;
	private int previous;
	private int currentAction;
	private Animation animation;
	private int x;
	private int y;
	
	// num frames per action (check player sprites)
	private final int[] numFrames = {
		2, 4, 1, 3, 1, 3, 3, 1, 2
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int DUCKING = 4;
	private static final int THROWING = 5;
	private static final int PUNCHING = 6;
	private static final int BLOCKING = 7;
	private static final int DYING = 8;
	
	public MenuPlayer(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		try
		{
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/spritesheet.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i ++) //!!!!!!!!! 7 = number of different animations, change for other players.
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != THROWING && i != PUNCHING && i != BLOCKING && i != DYING)
					{// sprites for scratch are 60 px wide, not 30
						bi[j] = spritesheet.getSubimage(j*30, i*50, 30, 50);
					}
					else
					{
						bi[j] = spritesheet.getSubimage(j*60, i*50, 60, 50);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		animation = new Animation();
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(100);
		walking = true;
		currentAction = WALKING;
	}
	
	public void update()
	{
		if(walking)
		{
			x += 1;
		}
		else
		{
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
			}
		}
		animation.update();
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(animation.getImage(),
				(int)(x),
				(int)(y),
				null);
	}
	
	public int getx()
	{
		return x;
	}
}
