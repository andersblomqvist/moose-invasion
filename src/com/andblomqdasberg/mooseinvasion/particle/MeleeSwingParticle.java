package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	The white stuff shown when a melee weapon is used
 * 
 * 	@author Anders Blomqvist
 */
public class MeleeSwingParticle extends AbstractParticle {
	
	private int spriteId = 5;
	
	public MeleeSwingParticle(float x, float y) {
		this.x = x;
		this.y = y;
		sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2},
				Assets.sInstance.particles[spriteId]);
		velocity = new Vector2D();
		sprite.animationSpeed = 4;
		lifeTime = 16f;
	}
}
