package com.andblomqdasberg.mooseinvasion.weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	Abstract class for representing base functionalities
 * 	for a weapon.
 * 	
 * 	@author Anders Blomqvist
 */
public abstract class AbstractWeapon {
	
	// Toggle the ability to shoot on and off. Set to off when we are in
	// the city and on in the stages.
	public static boolean ALLOW_SHOOTING = false;

	// Damage boost when beer is consumed. Applies on all weapons.
	public static boolean DAMAGE_INCREASE = false;
	
	public int id;
	public int ammo;
	public int damage;
	public int fireRate;
	public int level;
	public boolean penetrationLight;
	public boolean penetrationFull;
	public boolean melee;
	public String name;
	
	private int ticksSinceLastShot = 0;
	
	private GUIText weaponText;
	private int ticksSinceTextActivated = 0;
	
	private int baseDamage;
	private int damageIncrease;
	
	protected Sprite sprite;
	private int offsetX = 0;
	
	/**
	 * 	Init weapon values
	 * 
	 * 	@param stats Data holder with all base info for a weapon.
	 * 	All weapons defined in {@link WeaponList}
	 */
	public AbstractWeapon(int id, int damage, int ammo, int fireRate, String name, boolean isMelee) {
		this.id = id;
		this.damage = damage;
		this.ammo = ammo;
		this.fireRate = fireRate;
		this.name = name;
		melee = isMelee;
		
		// Updated via weapon upgrades from shop
		this.level = 1;
		this.penetrationLight = false;
		this.penetrationFull = false;
		
		baseDamage = damage;
		damageIncrease = damage * 2;
	}
	
	/**
	 * 	Called when the player swapps to this weapon
	 */
	public void activate(float x, float y) {
		weaponText = new GUIText(this.name, x-this.name.length()/2, y, 
				"player-gui");
		weaponText.style.setStyle(MooseInvasion.RENDER_WIDTH*0.02f, 
				Color.GREEN);
		ticksSinceTextActivated = 0;
	}
	
	/**
	 * 	Called when the player switches away from this weapon
	 */
	public void deactivate() {
		weaponText.destroy();
	}
	
	/**
	 * 	Update fire rate check
	 * 
	 * 	@param x player position
	 * 	@param y player position
	 */
	public void tick(float x, float y) {
		if(ticksSinceLastShot > fireRate) {
			if(InputHandler.shoot(false) && ALLOW_SHOOTING) {
				ticksSinceLastShot = 0;
				if(ammo > 0) {
					shoot(x, y);
					offsetX = 1;	// Add recoil
					ammo--;
				} else if(melee)
					shoot(x, y);
			}
		}
		
		// Reset recoil
		if(ticksSinceLastShot > fireRate / 2) 
			offsetX = 0;
		
		if(weaponText != null) {
			weaponText.y -= 1f;
			
			if(ticksSinceTextActivated > 50)
				weaponText.destroy();
		}
		
		ticksSinceLastShot++;
		ticksSinceTextActivated++;
	}
	
	/**
	 * 	Render weapon image	
	 */
	public void render(Graphics g, int ticks, float x, float y) {
		int frame = ticks / 15 % sprite.anim.length;
        Image img = sprite.img[sprite.anim[frame]];
        if(ticks / 15 % 2 == 0)
        	y -= 1;
        	
        g.drawImage(img,
                (int)(x+offsetX)*MooseInvasion.X_SCALE,
                (int)y*MooseInvasion.Y_SCALE,
                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
                null
        );
	}
	
	/**
	 * 	Override shoot method for each weapon but call this method aswell so
	 * 	weapons can get a damage increase when player drinks beer.
	 * 
	 * 	@param x player position
	 * 	@param y player position
	 */
	public void shoot(float x, float y) {
		if(DAMAGE_INCREASE)
			damage = damageIncrease;
		else
			damage = baseDamage;
	}
	
	/**
	 * 	Override weapon level up.
	 * 
	 * 	@NOTE This method has to be called at the end of the override so we get updated
	 * 	damage values.
	 */
	public void levelUp() {
		baseDamage = damage;
		damageIncrease = damage * 2;
	}
	
	/**
	 * 	Plays the sound connected with weapon ID
	 */
	public void playSound() {
		// Random between 1 - 3
		int r = GameRandom.nextInt(3) + 1;
		String version = "-" + r + ".wav";
		switch(id) {
			case 0:
				AudioPlayer.play("weapon-pistol" + version);
				break;
			case 1:
				AudioPlayer.play("weapon-carbine.wav");
				break;
			case 2:
				AudioPlayer.play("weapon-uzi-1.wav");
				break;
			case 3:
				AudioPlayer.play("weapon-melee-swoosh.wav");
				break;
		}
	}
}