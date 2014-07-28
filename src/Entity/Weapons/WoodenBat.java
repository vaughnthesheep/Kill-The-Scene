package Entity.Weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import Entity.Animation;
import Entity.Player;

public class WoodenBat extends Weapon{
	
	private final String SPRITESHEET = "wooden_bat.gif";
	
	public WoodenBat(TileMap tm)
	{
		super(tm);
		tileMap = tm;
		width = 100;
		height = 50;
		numFrames = new int[]{2, 4, 1, 3, 1, 3, 3, 3, 3, 3, 1};
		
		swingDamage = 8;
		swingForce = 5;
		swingRange = 50;
		stabDamage = 2;
		stabForce = 2;
		stabRange = 50;
		
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Weapons/" + SPRITESHEET));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i ++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
				}
				sprites.add(bi);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(Player.IDLE_DELAY);
	}
	
	public void draw(Graphics2D g)
	{
		setMapPosition();
		super.draw(g);
	}
}
