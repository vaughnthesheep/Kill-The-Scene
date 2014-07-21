package main;

import java.awt.Font;
import java.io.InputStream;

public class CustomFont {
	
	private InputStream in;
	private Font font;
	
	public CustomFont()
	{
		
	}
	
	public Font create(String resource, int size)
	{
		try
		{
			in = getClass().getResourceAsStream("/Fonts/Open Sans 600italic.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(0, size);
			font = new Font("Arial", Font.PLAIN, 10);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return font;
	}
}
