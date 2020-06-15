package com.andblomqdasberg.mooseinvasion.entity;

import java.awt.Color;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.weapon.AbstractWeapon;
import com.andblomqdasberg.mooseinvasion.weapon.WeaponList;

/**
 * 	Player entity
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Player extends Entity {
	
	private static int SPRITE_ID = 0;
	private static int[] jack = {0, 1};
	private static int[] igor = {2, 3};
	private static int[] pcmr = {4, 5};
	private static int[] scout = {6, 7};

	// Movement
	private float friction = 0.9f;
	private float weight = 10f;
	private float maxSpeed = 2.0f;
	private float accel = 1.8f;
	
	// Weapons
	private ArrayList<AbstractWeapon> weapons;
	private AbstractWeapon currentWeapon;

	private int gold;

	private GUIText ammoText;
	private GUIText goldText;
	private GUIImage ammoIcon;
	
	public Player(int x, int y) {
		super(SPRITE_ID, jack, x, y);
		weapons = new ArrayList<>();
		setRandomPlayerModel();
		
		weight = 10;
		friction = 0.9f;
		maxSpeed = 2.0f;
		weapons.add(WeaponList.PISTOL);
		weapons.add(WeaponList.CARBINE);
		weapons.add(WeaponList.UZI);
		currentWeapon = weapons.get(0);
		currentWeapon.activate(x, y);
		
		ammoText = new GUIText("", MooseInvasion.WIDTH, MooseInvasion.HEIGHT-6);
		ammoIcon = new GUIImage(
				MooseInvasion.WIDTH/1.03f, 
				MooseInvasion.HEIGHT-MooseInvasion.SPRITE_Y_SIZE*2, 
				Assets.sInstance.sprites[4][0]);
		goldText =  new GUIText("$"+String.valueOf(gold), MooseInvasion.WIDTH - 13, 16);
		goldText.style.color = new Color(255, 205, 85);
	}
	
	@Override
	public void tick() {
		applyFriction();
		currentWeapon.tick(x, y);
		checkInput();

		x += velocity.x;
		y += velocity.y;
		
		if(x < 0)
			x = 0;
		else if(x > MooseInvasion.WIDTH-16)
			x = MooseInvasion.WIDTH-16;
		
		if(y < 0)
			y = 0;
		else if(y > MooseInvasion.HEIGHT-16)
			y = MooseInvasion.HEIGHT-16;
		
		updateGoldText();
		updateAmmoText();
	}

	/**
	 * 	When no keys are pressed we reduce speed, just like friction
	 */
	private void applyFriction() {
		if(velocity.y < 0) {
			velocity.y += 1.0/weight * friction;
			if (velocity.y > 0)
				velocity.y = 0;
		} else {
			velocity.y -= 1.0/weight * friction;
			if (velocity.y < 0)
				velocity.y = 0;
		}
		
		if(velocity.x < 0) {
			velocity.x += 1.0/weight * friction;
			if (velocity.x > 0)
				velocity.x = 0;
		} else {
			velocity.x -= 1.0/weight * friction;
			if (velocity.x < 0)
				velocity.x = 0;
		}
	}
	
	/**
	 * 	Method for handling all input from the user, changes player velocity on directional keys
	 * 	and fires weapon on left mouse click
	 */
 	private void checkInput() {
 		
		if(InputHandler.up(false))
			if(velocity.y > -maxSpeed)
				velocity.y -= accel/weight;
		
		if(InputHandler.down(false))
			if(velocity.y < maxSpeed)
				velocity.y += accel/weight;

		if(InputHandler.right())
			if(velocity.x < maxSpeed)
				velocity.x += accel/weight;
		
		if(InputHandler.left())
			if(velocity.x > -maxSpeed)
				velocity.x -= accel/weight;
		
		// Cyckle weapon by pressing one single button instead
		// of specifc weapon number button
		if(InputHandler.cycleWeapon())
			cycleWeapon();
		
		if(InputHandler.num1())
			directSwitchToWeapon(0);
		
		if(InputHandler.num2())
			directSwitchToWeapon(1);
		
		if(InputHandler.num3())
			directSwitchToWeapon(2);
	}

 	/**
 	 * 	Cycles between the current weapons by only pressing one button.
 	 * 	Starts at 0 and goes forward til the end of weapon list where it 
 	 * 	loops back to 0 again.
 	 */
 	private void cycleWeapon() {
 		AudioPlayer.play("weapon-ammo-pickup.wav");
 		if(currentWeapon.id < weapons.size() - 1) {
 			// Not at the end yet, go forward.
 			currentWeapon.deactivate();
 			currentWeapon = weapons.get(currentWeapon.id + 1);
 			currentWeapon.activate(x, y);
 		}
 		else {
 			// At the end, go back to beginning
 			currentWeapon.deactivate();
 			currentWeapon = weapons.get(0);
 			currentWeapon.activate(x, y);
 		}
 	}
 	
 	/**
 	 * 	Directly switches to specific weapon
 	 * 
 	 * 	@param weaponID ID of weapon which is the slot in weapon list
 	 */
 	private void directSwitchToWeapon(int weaponID) {
 		// Check if have the weapon first and if we dont hold it already
 		if(weapons.get(weaponID) != null && currentWeapon.id != weaponID) {
 			// Do switch
 			AudioPlayer.play("weapon-ammo-pickup.wav");
 			currentWeapon.deactivate();
 			currentWeapon = weapons.get(weaponID);
 			currentWeapon.activate(x, y);
 		}
 	}
 	
	/**
	 * 	Randomizes a player model
	 */
	private void setRandomPlayerModel() {
		int animation = GameRandom.nextInt(4);
		switch(animation) {

			//temporary way of randomly getting a player character
			case 0:
				sprite.anim = jack;
				break;
			case 1:
				sprite.anim = igor;
				break;
			case 2:
				sprite.anim = pcmr;
				break;
			case 3:
				sprite.anim = scout;
				break;
			default:
				sprite.anim = jack;
				break;
		}
	}
	
	/**
	 * 	Update gold text and position
	 */
	private void updateGoldText() {
		goldText.text = "$123456789";
		goldText.x = MooseInvasion.SPRITE_X_SIZE * 19 - goldText.text.length()*(MooseInvasion.SPRITE_X_SIZE/2);
	}
	
	/**
	 * 	Update ammo text, position and color
	 */
	private void updateAmmoText() {
		ammoText.text = "" + currentWeapon.ammo;
		ammoText.x = MooseInvasion.SPRITE_X_SIZE * 19 - ammoText.text.length()*(MooseInvasion.SPRITE_X_SIZE/2);
		
		ammoIcon.x = MooseInvasion.SPRITE_X_SIZE * 19;
		ammoIcon.y = MooseInvasion.HEIGHT - MooseInvasion.SPRITE_Y_SIZE + 2;
		
		// Set text color to red when ammo is low
		if(currentWeapon.ammo < 30)
			ammoText.style.color = Color.RED;
		else
			ammoText.style.color = Color.WHITE;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		goldText.text = "$"+String.valueOf(gold);

	}
}
