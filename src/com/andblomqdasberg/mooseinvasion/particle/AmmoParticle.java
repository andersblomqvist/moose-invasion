package com.andblomqdasberg.mooseinvasion.particle;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

public class AmmoParticle extends AbstractParticle {
	
	private int spriteId = 2; 
	
	private float gravity = 0.12f;
	private float ground;
	
	boolean soundPlayed = false;

	public AmmoParticle(float x, float y) {
		this.x = x;
		this.y = y;
		ground = this.y + GameRandom.nextInt(20) + 10;
		
		// Random vector up to the right

		this.velocity = new Vector2D(GameRandom.nextFloat()+0.5f, -(GameRandom.nextInt(2) + 1f));
		
		this.sprite = new Sprite(
				spriteId, 
				new int[] {0, 1, 2, 3, 4, 5, 6, 7},
				Assets.sInstance.particles[spriteId]);
		
		sprite.animationSpeed = 5;
	}
	
	/**
	 * 	Parabola flying
	 */
	@Override
	public void tick() {
		if(this.y < ground) {
			velocity.y += gravity;
		}
		else {
			this.velocity.y *= -0.7;
			this.velocity.x *= 0.6;
			playShellDrop();
		}
		
		this.x += velocity.x;
		this.y += velocity.y;
	}

	/**
	 * 	Play shell drop sound
	 */
	private void playShellDrop() {
		if(!soundPlayed){
			AudioPlayer.play("weapon-shell-drop.wav");
			soundPlayed = true;
		}
	}
}
