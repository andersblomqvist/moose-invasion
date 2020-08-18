package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.entity.damage.Projectile;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	Simple weapon with pistol behaviour
 * 
 * 	@author Anders Blomqvist
 */
public class WeaponPistol extends AbstractWeapon {

	/**
	 * 	Stats are set in {@link WeaponList}
	 * 
	 * 	@param stats Data about the weapon
	 */
	public WeaponPistol(int id, int damage, int ammo, int fireRate, String name, boolean isMelee) {
		super(id, damage, ammo, fireRate, name, isMelee);
		
		sprite = new Sprite(1, new int[] {id});
	}
	
	@Override
	public void shoot(float x, float y) {
		super.shoot(x, y);
		GameManager.sInstance.spawnParticles(ParticleType.AMMO, 1, x, y);
        GameManager.sInstance.spawnEntity(new Projectile(x+1, y, damage, false, false));
        playSound();
	}
	
	@Override
	public void activate(float x, float y) {
		super.activate(x, y);
		AudioPlayer.play("weapon-ammo-pickup.wav");
	}
	
	@Override
	public void levelUp() {
		System.out.println("Early level up");
		
		level += 1;
		switch(level) {
    		case 2:
    			fireRate = 30;
    			break;
    		case 3:
    			fireRate = 20;
    			break;
    		case 4:
    			damage = 51;
    			break;
    		case 5:
    			fireRate = 15;
    			break;
		}
		
		super.levelUp();
	}
}
