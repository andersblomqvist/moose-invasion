package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.damage.MeleeHit;
import com.andblomqdasberg.mooseinvasion.entity.monster.EntityMonster;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

public class WeaponMelee extends AbstractWeapon {

	private int rangeX = 16;
	private int rangeY = 12;
	private int offsetX = rangeX / -2;
	private int offsetY = 4;
	private BoxCollider box = new BoxCollider(0, 0, rangeX, rangeY, "melee");
	private MeleeHit hit;
	
	/**
	 * 	Stats set in {@link WeaponList}
	 */
	public WeaponMelee(int id, int damage, int ammo, int fireRate, String name, boolean isMelee) {
		super(id, damage, ammo, fireRate, name, isMelee);
		hit = new MeleeHit(damage);
		box.e = hit;
		sprite = new Sprite(1, new int[] {id});
	}
	
	@Override
	public void shoot(float x, float y) {
		super.shoot(x, y);
		playSound();
		box.colliding = false;
		box.x = (int) x + offsetX;
		box.y = (int) y + offsetY;
		hit.damage = damage;
		GameManager.sInstance.spawnParticles(ParticleType.MELEE_SWING, 1, x, y);
		for(int i = 0; i < GameManager.sInstance.entities.size(); i++) {
			AbstractEntity e = GameManager.sInstance.entities.get(i);
			if(e instanceof EntityMonster)
				if(box.AABBCollision(e.collider) == CollisionType.DEFAULT)
					AudioPlayer.play("weapon-melee-hit.wav");
		}
	}
	
	@Override
	public void activate(float x, float y) {
		super.activate(x, y);
		AudioPlayer.play("weapon-draw-knife.wav");
	}
}
