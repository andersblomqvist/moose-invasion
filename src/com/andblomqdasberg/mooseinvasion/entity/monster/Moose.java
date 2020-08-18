package com.andblomqdasberg.mooseinvasion.entity.monster;

import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Default mob. 100 hp, random speed and pretty easy to kill
 * 
 * 	@author Anders Blomqvist
 */
public class Moose extends EntityMonster
{
	private int spriteId = 3;
	
	public Moose() {
		super(-10f, GameRandom.getRandomY(), new Vector2D());
		
		maxHealth = 100;
		health = maxHealth;
		width = 10;
        height = 8;
        offsetX = 3;
        offsetY = 7;
		
        anim = new int[] {0, 1, 2, 1};
        deathAnim = new int[] {3, 4, 5};
        whiteFrameIndex = 7;
        
        bloodParticles = 4;
        bloodAndMeatParticles = 1;
        
		sprite = new Sprite(spriteId, anim);
		collider = new BoxCollider(this, width, height, "moose");
		
		// Randomize animation	
		if(GameRandom.nextFloat() > 0.5)
			sprite.anim = new int[] {1, 0};
		
		// Set random x speed.
		velocity.x = GameRandom.randomBetween(2, 4) / 10f;
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
