package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;

/**
 * 	Carbine weapon, main assault rifle
 * 
 * 	@author Anders Blomqvist
 */
public class WeaponCarbine extends AbstractWeapon {

	/**
	 * 	Stats are set in {@link WeaponList}
	 * 
	 * 	@param stats Data about the weapon
	 */
	public WeaponCarbine(int id, int damage, int ammo, int fireRate, String name, boolean isMelee) {
		super(id, damage, ammo, fireRate, name, isMelee);
	}
	
	@Override
	public void shoot(float x, float y) {
		super.shoot(x, y);
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
    			fireRate = 6;
    			break;
    		case 3:
    			fireRate = 5;
    			break;
    		case 4:
    			damage = 51;
    			break;
    		case 5:
    			penetrationLight = true;
    			break;
		}
		
		super.levelUp();
	}
}