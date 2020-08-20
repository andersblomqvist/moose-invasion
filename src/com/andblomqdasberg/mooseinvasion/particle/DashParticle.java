package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	The white dots when performing a dash	
 * 
 * 	@author Anders Blomqvist
 */
public class DashParticle extends AbstractParticle {

	private int spriteId = 6;
	
	public DashParticle(float x, float y) {
		super(x, y);
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3, 4},
				Assets.sInstance.particles[spriteId]);
	
		lifeTime = 50;
		sprite.animationSpeed = 10;
	}
}
