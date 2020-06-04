package com.andblomqdasberg.mooseinvasion;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 	Handle audio
 * 
 * 	@author David Åsberg
 */
public class AudioPlayer{

    private Clip clip;
    private AudioInputStream audioInputStream;
    private String filePath;
    private FloatControl gainControl;

    // Main volume control
    private float globalVolume = -30f;
    
    public AudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.filePath = "assets\\audio\\" + filePath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(getVolume() + globalVolume);
    }
    
    public void play() {
        clip.start();
        
        // Remove thread when done
        clip.addLineListener( new LineListener() {
        	public void update(LineEvent evt) {
        		if (evt.getType() == LineEvent.Type.STOP) {
        			evt.getLine().close();
        		}
        	}
    	});
    }

    public void pause() {
        clip.stop();
    }

    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(0);
        this.play();
    }

    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.stop();
        clip.close();
    }

    public void setVolume(float change) {
        gainControl.setValue(change);
    }

    public float getVolume() {
        return gainControl.getValue();
    }

    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
