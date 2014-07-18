package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import sun.applet.Main;

public class Soundtrack {
	
	private String song;
	private static Clip clip;
	
	
	public Soundtrack(String song)
	{
		try
		{
			this.song = song;
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getClass().getResourceAsStream("/Music/" + song));
			clip.open(inputStream);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static synchronized void play()
	{
		clip.loop(-1);
	}
	
	public static synchronized void stop()
	{
		clip.stop();
	}

}
