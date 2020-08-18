package com.andblomqdasberg.mooseinvasion.entity.monster;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Wolf mob. A fast but low hp mob.
 * 
 * 	@author Anders Blomqvist
 */
public class Wolf extends EntityMonster {

	private int spriteId = 7;
	
	public Wolf() {
		super(-10f, GameRandom.getRandomY(), new Vector2D(0.8f, 0f));
		
		maxHealth = 51;
		health = maxHealth;
		
		width = 16;
        height = 10;
        offsetX = 1;
        offsetY = 6;
		
        anim = new int[] {0, 1};
        deathAnim = new int[] {2, 3, 4};
        whiteFrameIndex = 5;
        
        bloodParticles = 4;
        bloodAndMeatParticles = 1;
        
		sprite = new Sprite(spriteId, anim);
		collider = new BoxCollider(this, width, height, "wolf");
	}
	
	@Override
    public void tick(int ticks) {
    	super.tick(ticks);
    	
    	if(ticks % 60 == 0)
    		velocity.y = GameRandom.randomBetween(-3, 3) / 10f;
    	
    	// Game window bounds
		if(y < 0)
			velocity.y *= -1;
		else if(y > MooseInvasion.HEIGHT-16)
			velocity.y *= -1;
    }

	@Override
	public void onEnabled() {
		AudioPlayer.play("entity-wolf-spawn.wav");
	}
	
	@Override
	public float getX() {
		return x + offsetX;
	}
	
	@Override
	public float getY() {
		return y + offsetY;
	}
}
