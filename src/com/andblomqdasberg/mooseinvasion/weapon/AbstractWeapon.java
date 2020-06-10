package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;

/**
 * 	Abstract class for representing base functionalities
 * 	for a weapon.
 * 	
 * 	@author Anders Blomqvist
 */
public abstract class AbstractWeapon {
	
	public int id;
	
	public int ammo;
	public int damage;
	public int fireRate;
	
	public AudioPlayer audio;
	
	private int ticksSinceLastShot;
	
	/**
	 * 	Init weapon values
	 * 
	 * 	@param stats Data holder with all base info for a weapon.
	 * 	All weapons defined in {@link WeaponList}
	 */
	public AbstractWeapon(int id, int damage, int ammo, int fireRate) {
		this.id = id;
		this.damage = damage;
		this.ammo = ammo;
		this.fireRate = fireRate;
	}
	
	/**
	 * 	Update fire rate check
	 * 
	 * 	@param x player position
	 * 	@param y player position
	 */
	public void tick(float x, float y) {
		if(InputHandler.shoot()) {
			if(ticksSinceLastShot > fireRate) {
				ticksSinceLastShot = 0;
				shoot(x, y);
			}
		}
		
		ticksSinceLastShot++;
	}
	
	/**
	 * 	Override shoot method for each weapon.
	 * 
	 * 	@param x player position
	 * 	@param y player position
	 */
	public void shoot(float x, float y) {}
	
	/**
	 * 	Plays the sound connected with weapon ID
	 */
	public void playSound() {
		// Random between 1 - 3
		int r = GameRandom.nextInt(3) + 1;
		String version = "-" + r + ".wav";
		switch(id) {
			case 0:
				play("weapon-pistol" + version);
				break;
			case 1:
				play("weapon-carbine.wav");
				break;
			case 2:
				play("weapon-uzi");
				break;
		}
	}
	
	private void play(String sound) {
		try { audio = new AudioPlayer(sound); }
        catch (Exception e) {e.printStackTrace(); }
		audio.play();
	}
}