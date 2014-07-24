package Entity;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import TileMap.TileMap;

public class Projectile extends MapObject{
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	private double angleRatio;
	private Player player;

	public Projectile(TileMap tm, Player player)
	{
		super(tm);
		this.player = player;
		hit = false;
		
		moveSpeed = 3.8;
		angleRatio = 0.71;
		
		width = 25;
		height = 25;
		cwidth = 20;
		cheight = 20;
		
		// load sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Projectiles/cd.gif"));
			sprites = new BufferedImage[8];
			for(int i = 0; i < sprites.length; i++)
			{
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++)
			{
				hitSprites[i] = spritesheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		setVector(player);
		
	}
	
	private void setVector(Player player)
	{
		// set direction
		if(player.up && player.left)
		{
			dx = -moveSpeed*angleRatio;
			dy = -moveSpeed*angleRatio;
		}
		else if(player.up && player.right)
		{
			dx = moveSpeed*angleRatio;
			dy = -moveSpeed*angleRatio;
		}
		else if(player.up)
		{
			dx = 0;
			dy = -moveSpeed;
		}
		else if(player.down && player.left && player.falling)
		{
			dx = -moveSpeed*angleRatio;
			dy = moveSpeed*angleRatio;
		}
		else if(player.down && player.right && player.falling)
		{
			dx = moveSpeed*angleRatio;
			dy = moveSpeed*angleRatio;
		}
		else if(player.down && player.falling)
		{
			dx = 0;
			dy = moveSpeed;
		}
		else if(player.left)
		{
			dx = -moveSpeed;
			dy = 0;
		}
		else if(player.right)
		{
			dx = moveSpeed;
			dy = 0;
		}
		else if(player.facingRight)
		{
			dx = moveSpeed;
			dy = 0;
		}
		else
		{
			dx = -moveSpeed;
			dy = 0;
		}
	}
	
	public void setHit()
	{
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
		dy = 0;
	}
	
	public boolean isHit()
	{
		return hit;
	}
	
	public boolean shouldRemove(){ return remove; }
	
	public void update()
	{
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if((dx == 0 && !hit) || (dy == 0 && !hit))
		{
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce())
		{
			remove = true;
		}
	}
	
	public void draw(Graphics2D g)
	{
		setMapPosition();
		
		super.draw(g);
	}
	
}
