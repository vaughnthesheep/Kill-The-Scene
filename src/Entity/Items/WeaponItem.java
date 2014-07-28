package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import Entity.Animation;
import Entity.Item;
import Entity.Player;
import Entity.Weapons.WoodenBat;

public class WeaponItem extends Item{
	
	public static final int WOODEN_BAT = 0;
	public static final int METAL_BAT = 1;
	private String spritePath;
	private Player player;
	
	public WeaponItem(TileMap tm, Player player, int type) {
		super(tm);
		this.type = type;
		this.player = player;
		pickedUp = false;
		width = WIDTH;
		height = HEIGHT;
		cwidth = CWIDTH;
		cheight = CHEIGHT;
		
		switch (type)
		{
		case WOODEN_BAT:
			spritePath = "wooden-bat.gif";
		}
		
		sprites = new BufferedImage[1];
		try
		{
			sprites[0] = ImageIO.read(getClass().getResourceAsStream("/Sprites/Items/" + spritePath));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update()
	{
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(
			player.getx() + player.getWidth()/2 > x - width/2 &&
			player.getx() - player.getWidth()/2 < x + width/2 &&
			player.gety() + player.getHeight()/2 > y - height/2 &&
			player.gety() - player.getHeight()/2 < y + height/2)
		{
			pickedUp(player);
		}
	}

	public void pickedUp(Player player)
	{
		if(player.equipped || !player.isDucking())
			return;
		switch (type)
		{
		case 0:
			player.equipped = true;
			player.weapon = new WoodenBat(tileMap, true);
			pickedUp = true;
			break;
		case 1:
			// give player metal bat
			break;
		default:
			System.out.println("WeaponItem type error, make sure constructor was given correct type (int)");
		}
	}

	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
