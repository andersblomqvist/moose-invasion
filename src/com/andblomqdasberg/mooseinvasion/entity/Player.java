package com.andblomqdasberg.mooseinvasion.entity;

import java.awt.Color;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
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
	
	private ArrayList<AbstractWeapon> weapons;
	private AbstractWeapon currentWeapon;

	private int gold;

	public GUIText ammoText;
	public GUIText goldText;
	
	public Player(int x, int y) {
		super(SPRITE_ID, jack, x, y);
		weapons = new ArrayList<>();
		setRandomPlayerModel();
		
		weight = 10;
		friction = 0.9f;
		maxSpeed = 2.0f;
		weapons.add(WeaponList.PISTOL);
		currentWeapon = weapons.get(0);
		
		/*
		ammoText = new GUIText(currentWeapon.getBulletsInMag() + 
				"/" + currentWeapon.getMagazineCapacity(), MooseInvasion.WIDTH-140, MooseInvasion.HEIGHT-6);
		*/
		goldText =  new GUIText("$"+String.valueOf(gold), MooseInvasion.WIDTH - 13, MooseInvasion.HEIGHT-6);
		goldText.setColor(new Color(255, 205, 85));
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
	}

 	/*
	@Override
	public void render(Graphics g, int gameTick) {
		super.render(g, gameTick);
	}
	*/

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
	
	private void updateGoldText() {
		goldText.x = MooseInvasion.WIDTH - goldText.text.length() * 13;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		goldText.text = "$"+String.valueOf(gold);

	}
}
