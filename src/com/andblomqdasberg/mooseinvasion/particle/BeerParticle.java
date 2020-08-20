package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	Partile which shows ontop of the player when a beer is consumed.	
 * 
 * 	@author Anders Blomqvist	
 */
public class BeerParticle extends AbstractParticle {
	
	private int spriteId = 4;
	
	public BeerParticle(float x, float y) {
		super(x, y);
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3},
				Assets.sInstance.particles[spriteId]);
		
		sprite.animationSpeed = 10;
		lifeTime = GameRandom.randomBetween(2, 4) * 10;
		
		x = GameRandom.randomBetween((int)x - 8,(int)x + 16);
		velocity.x = GameRandom.randomBetween(-4, 4) / 10f;
		velocity.y = -0.4f;
	}
	
	@Override
	public void tick(int ticks) {
		if(ticks % 30 == 0)
    		velocity.x = GameRandom.randomBetween(-4, 4) / 10f;
		
		this.x += velocity.x;
		this.y += velocity.y;
	}
}