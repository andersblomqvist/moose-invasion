package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;

/**
 * 	Carbine weapon, main assault rifle
 * 
 * 	@author Anders Blomqvist
 */
public class WeaponUZI extends AbstractWeapon {

	/**
	 * 	Stats are set in {@link WeaponList}
	 * 
	 * 	@param stats Data about the weapon
	 */
	public WeaponUZI(int id, int damage, int ammo, int fireRate, String name) {
		super(id, damage, ammo, fireRate, name);
	}
	
	@Override
	public void shoot(float x, float y) {
		GameManager.sInstance.spawnParticles(ParticleType.AMMO, 1, x, y);
        GameManager.sInstance.spawnEntity(
        		new Projectile(x+1, y, damage, penetrationLight, false));
        playSound();
	}
	
	@Override
	public void levelUp() {
		level += 1;
		switch(level) {
    		case 2:
    			damage = 17;
    			break;
    		case 3:
    			damage = 20;
    			break;
    		case 4:
    			damage = 25;
    			break;
    		case 5:
    			penetrationLight = true;
    			break;
		}
	}
}