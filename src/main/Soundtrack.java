package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import sun.applet.Main;

public class Soundtrack {
	
	private static Clip song;
	
	
	public Soundtrack()
	{
		
	}
	
	public static void setSong(String s)
	{
		try
		{
			song = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getClass().getResourceAsStream("/Music/" + s));
			song.open(inputStream);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static synchronized void play()
	{
		song.loop(-1);
	}
	
	public static synchronized void stop()
	{
		song.stop();
	}

}
