package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Particle for when player drank a beer and throws away the glas. Same
 * 	behavior as a ammo shell but with different sound
 * 	
 * 	@author Anders Blomqvist
 */
public class BeerGlasParticle extends AbstractParticle {

	private int spriteId = 3;
	private float gravity = 0.12f;
	private float ground;
	boolean soundPlayed = false;
	
	public BeerGlasParticle(float x, float y) {
		this.x = x;
		this.y = y;
		ground = this.y + 10;
		
		this.velocity = new Vector2D(
				GameRandom.nextFloat()+0.5f, -(GameRandom.nextInt(1) + 2));
		
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3, 4, 5, 6, 7},
				Assets.sInstance.particles[spriteId]);
		
		sprite.animationSpeed = 7;
	}
	
	/**
	 * 	Parabola flying
	 */
	@Override
	public void tick() {
		
		if(this.y < ground)
			velocity.y += gravity;
		else {
			playSound();
			velocity.x = 0;
			velocity.y = 0;
		}
		
		this.x += velocity.x;
		this.y += velocity.y;
	}

	/**
	 * 	Play shell drop sound
	 */
	private void playSound() {
		if(!soundPlayed){
			AudioPlayer.play("misc-glass-break.wav");
			soundPlayed = true;
		}
	}
}
