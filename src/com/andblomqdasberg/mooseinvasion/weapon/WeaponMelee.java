package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;

public class WeaponMelee extends AbstractWeapon {

	/**
	 * 	Stats set in {@link WeaponList}
	 */
	public WeaponMelee(int id, int damage, int ammo, int fireRate, String name, boolean isMelee) {
		super(id, damage, ammo, fireRate, name, isMelee);
	}
	
	@Override
	public void shoot(float x, float y) {
		super.shoot(x, y);
		System.out.println("Melee weapon.");
		GameManager.sInstance.spawnEntity(
				new Projectile(x+1, y, damage, false));
		AudioPlayer.play("weapon-melee-swoosh.wav");
	}
}
