package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.Sprite;

public class BloodParticle extends AbstractParticle {	
	
	private int spriteId = 0;
	
	public BloodParticle(float x, float y) {
		super(x, y);
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3, 4, 5},
				Assets.sInstance.particles[spriteId]);
	}
}
