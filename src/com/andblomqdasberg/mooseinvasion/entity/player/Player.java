package com.andblomqdasberg.mooseinvasion.entity.player;

import java.awt.Color;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;
import com.andblomqdasberg.mooseinvasion.weapon.AbstractWeapon;
import com.andblomqdasberg.mooseinvasion.weapon.WeaponList;

/**
 * 	Player entity
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Player extends AbstractEntity {

	// Player collision box
	public int height = 11;
	public int width = 8;
	public int offsetX = 5;
	public int offsetY = 4;
	
	// Sprites
	private int spriteId = 0;
	private int[] jack = {0, 1};
	private int[] igor = {2, 3};
	private int[] pcmr = {4, 5};
	private int[] scout = {6, 7};

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
	private ArrayList<AbstractWeapon> weapons = new ArrayList<AbstractWeapon>();
	private AbstractWeapon currentWeapon;
	public boolean allowShooting;

	// Misc
	public int money = 9999;
	public int beers = 0;
	private int ticksSinceLastBeer = 0;
	private int beerDuration = 600;
	private boolean beer = false;
	public boolean inCity = true;

	private GUIText ammoText;
	private GUIText moneyText;
	private GUIText beerText;
	
	public Player(int x, int y) {
		super(x, y, Vector2D.ZERO);
		
		sprite = new Sprite(spriteId, getRandomPlayerModel());
		collider = new BoxCollider(this, width, height, "player");
		
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
				MooseInvasion.WIDTH - 20, 
				MooseInvasion.HEIGHT - 8*5, 
				"player-gui");
	}
	
	@Override
	public void tick(int ticks) {
		this.ticks = ticks;
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
		
		if(beer) {
			if(ticks % 5 == 0)
				GameManager.sInstance.spawnParticles(ParticleType.BEER, 1, x, y);
			
			if(ticksSinceLastBeer == 60)
				GameManager.sInstance.spawnParticles(ParticleType.BEER_GLASS, 1, x, y);
			
			if(ticksSinceLastBeer > beerDuration) {
				ticksSinceLastBeer = 0;
				AbstractWeapon.DAMAGE_INCREASE = false;
				friction = 0.9f;
				beer = false;
				System.out.println(" > Feel sober again");
			}
			
			ticksSinceLastBeer++;
		}
		
		updateGUIText();
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
		
		if(InputHandler.consume()) {
			if(inCity)
				AudioPlayer.play("misc-error-2.wav");
			else
				consumeBeer();
		}
			
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
	private int[] getRandomPlayerModel() {
		int animation = GameRandom.nextInt(4);
		switch(animation) {
			// Temporary way of randomly getting a player character
			case 0:
				return jack;
			case 1:
				return igor;
			case 2:
				return pcmr;
			case 3:
				return scout;
			default:
				return jack;
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
	 * 	Consumes a beer which doubles the damage but also makes the character
	 * 	harder to control.
	 */
	private void consumeBeer() {
		if(beers == 0) {
			AudioPlayer.play("misc-error-2.wav");
			return;
		}
			
		if(beer)
			return;
		
		AudioPlayer.play("misc-drink-beer.wav");
		
		// Increase damage and decrease friction for some time (set by beerDuration).
		AbstractWeapon.DAMAGE_INCREASE = true;
		friction = 0.3f;
		beer = true;
		ticksSinceLastBeer = 0;
		beers -= 1;
		System.out.println(" > You are drunk from the beer!");
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
	
	/**
	 *	@returns the collder x position
	 */
	@Override
	public float getX() {
		return x + offsetX;
	}
	
	/**
	 * 	@returns the collider y position
	 */
	@Override
	public float getY() {
		return y + offsetY;
	}
}
