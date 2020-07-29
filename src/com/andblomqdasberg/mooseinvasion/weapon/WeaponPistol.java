package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;

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
	public WeaponPistol(int id, int damage, int ammo, int fireRate, String name) {
		super(id, damage, ammo, fireRate, name);
	}
	
	@Override
	public void shoot(float x, float y) {
		super.shoot(x, y);
		GameManager.sInstance.spawnParticles(ParticleType.AMMO, 1, x, y);
        GameManager.sInstance.spawnEntity(new Projectile(x+1, y, damage, false, false));
        playSound();
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
