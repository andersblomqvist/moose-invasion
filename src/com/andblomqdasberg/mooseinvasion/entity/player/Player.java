package com.andblomqdasberg.mooseinvasion.entity.player;

import java.awt.Color;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.entity.Entity;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.weapon.AbstractWeapon;
import com.andblomqdasberg.mooseinvasion.weapon.WeaponList;

/**
 * 	Player entity
 * 
 * 	@author Anders Blomqvist
 * 	@author David �sberg
 */
public class Player extends Entity {

	// Player collision box
	public int height = 11;
	public int width = 8;
	public int offsetX = 5;
	public int offsetY = 4;
	
	// Sprites
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
	
	// Collision handlers
	private boolean moveNorth = true;
	private boolean moveSouth = true;
	private boolean moveEast = true;
	private boolean moveWest = true;
	
	// Weapons
	private ArrayList<AbstractWeapon> weapons;
	private AbstractWeapon currentWeapon;
	public boolean allowShooting;

	// Misc
	public int money;
	public int beers;

	private GUIText ammoText;
	private GUIText moneyText;
	private GUIText beerText;
	
	public Player(int x, int y) {
		super(SPRITE_ID, jack, x, y);
		
		weapons = new ArrayList<>();
		setRandomPlayerModel();
		
		// Add pistol at the beginning
		weapons.add(WeaponList.PISTOL);
		currentWeapon = weapons.get(0);
		currentWeapon.activate(x, y);
		
		ammoText = new GUIText("", 0, 
				MooseInvasion.HEIGHT-MooseInvasion.SPRITE_Y_SIZE/2, "player-gui");
		
		// Ammo image icon
		new GUIImage(
				MooseInvasion.WIDTH-MooseInvasion.SPRITE_X_SIZE, 
				MooseInvasion.HEIGHT-MooseInvasion.SPRITE_Y_SIZE,
				Assets.sInstance.sprites[4][0], "player-gui");
		
		// Health image icon
		new GUIImage(
				MooseInvasion.WIDTH-MooseInvasion.SPRITE_X_SIZE, 
				MooseInvasion.HEIGHT-MooseInvasion.SPRITE_Y_SIZE*2,
				Assets.sInstance.sprites[4][1], "player-gui");
		
		// Beer image icon
		new GUIImage(
				MooseInvasion.WIDTH-MooseInvasion.SPRITE_X_SIZE, 
				MooseInvasion.HEIGHT-MooseInvasion.SPRITE_Y_SIZE*3,
				Assets.sInstance.sprites[4][5], "player-gui");
		
		moneyText = new GUIText(
				"$"+String.valueOf(money), 
				MooseInvasion.WIDTH - 13, 16, "player-gui");
		moneyText.style.color = new Color(255, 205, 85);
		
		beerText = new GUIText("" + beers, 
				(MooseInvasion.WIDTH - 16)*MooseInvasion.X_SCALE, 
				(MooseInvasion.HEIGHT - 3*16)*MooseInvasion.Y_SCALE, 
				"player-gui");
		
		// TODO
		// TODO
		// TODO
		money = 9999;
		// TODO
		// TODO
		// TODO
	}
	
	@Override
	public void tick() {
		applyFriction();
		currentWeapon.tick(x, y);
		checkInput();

		x += velocity.x;
		y += velocity.y;
		
		// Game window bounds
		if(x < 0)
			x = 0;
		else if(x > MooseInvasion.WIDTH-16)
			x = MooseInvasion.WIDTH-16;
		if(y < 0)
			y = 0;
		else if(y > MooseInvasion.HEIGHT-16)
			y = MooseInvasion.HEIGHT-16;
		
		updateGUIText();
	}
	
	/**
	 * 	When no keys are pressed we reduce speed, just like friction
	 */
	private void applyFriction() {
		beerText.x = MooseInvasion.WIDTH - 20 - (beerText.text.length() * 7);
		beerText.y = MooseInvasion.HEIGHT - 8*5;
		
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
 		
		if(InputHandler.up(false) && moveNorth)
			if(velocity.y > -maxSpeed)
				velocity.y -= accel/weight;
		
		if(InputHandler.down(false) && moveSouth)
			if(velocity.y < maxSpeed)
				velocity.y += accel/weight;

		if(InputHandler.right(false) && moveEast)
			if(velocity.x < maxSpeed)
				velocity.x += accel/weight;
		
		if(InputHandler.left(false) && moveWest)
			if(velocity.x > -maxSpeed)
				velocity.x -= accel/weight;
		
		// Cycle weapon by pressing one single button instead
		// of specifc weapon number button
		if(InputHandler.cycleWeapon())
			cycleWeapon();
		
		if(InputHandler.num1())
			directSwitchToWeapon(0);
		
		if(InputHandler.num2() && weapons.size() > 1)
			directSwitchToWeapon(1);
		
		if(InputHandler.num3() && weapons.size() > 2)
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

			// Temporary way of randomly getting a player character
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
	 * 	Updates all the GUI text
	 */
	public void updateGUIText() {
		
		ammoText.text = "" + currentWeapon.ammo;
		ammoText.x = MooseInvasion.WIDTH - 20 - (ammoText.text.length() * 7);
		
		// Set text color to red when ammo is low
		if(currentWeapon.ammo < 30)
			ammoText.style.color = Color.RED;
		else
			ammoText.style.color = Color.WHITE;
		
		moneyText.text = "$"+String.valueOf(money);
		moneyText.x = MooseInvasion.WIDTH - 8 - (moneyText.text.length() * 7);
		
		beerText.text = "" + beers;
		beerText.x = MooseInvasion.WIDTH - 20 - (beerText.text.length() * 7);
	}

	/**
	 * 	Adds money to player
	 * 
	 * 	@param multiplier How much will be added
	 */
	public void addScore(int multiplier) {
		this.money += multiplier;
		moneyText.text = "$"+String.valueOf(money);
	}

	/**
	 * 	When player collides with a BoxCollider
	 * 
	 * 	@param type Where the collision happen
	 */
	public void onCollisionEnter(CollisionType type) {
		switch(type) {
    		case NORTH:
    			moveSouth = false;
    			velocity.y = 0;
    		break;
    		case SOUTH:
    			moveNorth = false;
    			velocity.y = 0;
    		break;
    		case EAST:
    			moveWest = false;
    			velocity.x = 0;
    		break;
    		case WEST:
    			moveEast = false;
    			velocity.x = 0;
    		break;
    		default:
    			System.out.println("No supported collision type");
		}
	}
	
	/**
	 * 	We left a collider we previously had contact with.
	 * 	Here we want to reset the movement.
	 */
	public void onCollisionExit() {
		moveNorth = true;
		moveSouth = true;
		moveEast = true;
		moveWest = true;
	}
	

	/**
	 * 	Called when players enters a trigger collider
	 * 
	 * 	@param tag name of the trigger
	 */
	public void onTriggerEnter(String tag) {
		GameManager.sInstance.city.shopTrigger(tag, true);
	}
	
	/**
	 * 	Called when player leaves a trigger
	 * 
	 * 	@param tag name of the trigger
	 */
	public void onTriggerExit(String tag) {
		GameManager.sInstance.city.shopTrigger(tag, false);
	}
	
	/**
	 *	@returns the collder x position
	 */
	public float getColX() {
		return x + offsetX;
	}
	
	/**
	 * 	@returns the collider y position
	 */
	public float getColY() {
		return y + offsetY;
	}

	/**
	 * 	Add weapon to current weapon list
	 * 	@param weapon Name of the weapon
	 */
	public void buyWeapon(String weapon) {
		weapons.add(WeaponList.getWeaponByName(weapon));
	}

	/**
	 * 	Adds ammo to specifed weapon
	 * 
	 * 	@param weapon
	 * 	@param ammo 
	 */
	public void buyAmmo(String weapon, String ammo) {
		AbstractWeapon w = WeaponList.getWeaponByName(weapon);
		w.ammo += Integer.parseInt(ammo);
		System.out.println("added ammo");
	}

	/**
	 * 	Upgrades a weapon one level. Upgrades stats are set in each specific
	 * 	weapon class with an override method.
	 * 
	 * 	@param weapon Name of the weapon
	 */
	public void buyUpgrade(String weapon) {
		AbstractWeapon w = WeaponList.getWeaponByName(weapon);
		w.levelUp();
	}
}