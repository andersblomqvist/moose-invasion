package com.andblomqdasberg.mooseinvasion.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 	Handle audio
 * 
 * 	@author David Åsberg
 * 	@author Anders Blomqvist
 */
public class AudioPlayer {

	// Master volume
	public static float globalVolume = -5f;
	
	// Hash map for storing sounds which is currently playing incase we want to 
	// modify their volume or pause/play.
	public static HashMap<String, AudioPlayer> ACTIVE_SOUNDS = new HashMap<String, AudioPlayer>();
	
	/**
	 * 	Set master volume
	 * 
	 * 	@param v Percentage volume (0 - 100 %)
	 */
	public static void setGlobalVolume(int v) {
		float dv = (100f - v) / 100f;
		globalVolume = -65f*dv - 5f;
		System.out.println("Got user volume: " + v + "%");
		System.out.println("Global volume: " + globalVolume);
	}
	
	/**
	 * 	Static method for playing sound. Call this with the sound filename
	 * 	to play sounds.
	 * 	
	 * 	@param soundName Name of the sound file (including file ending).
	 */
	public static void play(String soundName) {
		AudioPlayer a = null;
		try { a = new AudioPlayer(soundName); }
        catch (Exception e) { e.printStackTrace(); }
        a.play();
        
        // Add music and ambient to active sounds so we can pause them
        if(soundName.contains("ambient") || soundName.contains("music"))
        	ACTIVE_SOUNDS.put(soundName, a);
	}
	
	/**
	 * 	Stops playing specified sound.
	 * 
	 * 	@param soundName Name of the sound file
	 */
	public static void stop(String soundName) {
		ACTIVE_SOUNDS.get(soundName).clip.stop();
		ACTIVE_SOUNDS.remove(soundName);
	}

    private Clip clip;
    private AudioInputStream audioInputStream;
    private String filePath;
    private FloatControl gainControl;
    
    public AudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.filePath = "assets\\audio\\" + filePath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(globalVolume);
    }
    
    public void play() {
        clip.start();
        
        // Remove thread when done
        clip.addLineListener( new LineListener() {
        	public void update(LineEvent evt) {
        		if (evt.getType() == LineEvent.Type.STOP)
        			evt.getLine().close();
        	}
    	});
    }
}
