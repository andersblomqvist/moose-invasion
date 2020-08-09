package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Partile which shows ontop of the player when a beer is consumed.	
 * 
 * 	@author Anders Blomqvist	
 */
public class BeerParticle extends AbstractParticle {
	
	private int spriteId = 4;
	private float speed = 1;
	
	public BeerParticle(float x, float y) {
		this.x = x;
		this.y = y;
		this.lifetime = 0;
		this.velocity = new Vector2D(0, speed);
		
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3},
				Assets.sInstance.particles[spriteId]);
		
		sprite.animationSpeed = 4;
	}
	
	@Override
	public void tick() {
		lifetime++;
		this.y -= velocity.y;
	}
}