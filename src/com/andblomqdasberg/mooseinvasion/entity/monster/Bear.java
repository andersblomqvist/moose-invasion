package com.andblomqdasberg.mooseinvasion.entity.monster;

import java.awt.Graphics;
import java.awt.Image;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	The bear entity. A slow but fat entity which requires two sprites for
 * 	rendering. He's more than 16 px in width.
 * 
 * 	@author Anders Blomqvist
 */
public class Bear extends EntityMonster {
	private int spriteId = 6;
	
	private int[] animFront;
	private int[] deathAnimFront;
 	
	private Sprite spriteFront;
	
	public Bear() {
		super(-10, GameRandom.getRandomY(), new Vector2D(0.15f, 0f));
		
		maxHealth = 750;
		health = maxHealth;
		width = 22;
		height = 10;
		offsetX = 7;
		offsetY = 6;
		
		anim = new int[] {0, 2};
		deathAnim = new int[] {4, 6, 8};
		animFront = new int[] {1, 3};
		deathAnimFront = new int[] {5, 7, 9};
		whiteFrameIndex = 10;
		remainCorpse = true;
		bloodParticles = 8;
        bloodAndMeatParticles = 3;
		
		sprite = new Sprite(spriteId, anim);
		spriteFront = new Sprite(spriteId, animFront);
		collider = new BoxCollider(this, width, height, "bear");
		
		velocity.x = 0.2f;
	}
	
	@Override
	protected void onDamageTaken() {
		super.onDamageTaken();
		
		if(health <= 0) {
			AudioPlayer.play("entity-bear-death.wav");
			spriteFront.anim = deathAnimFront;
			collider.enabled = false;
		} else if(health < maxHealth / 2) {
			// Leave if we already been here
			if(velocity.x == 0.12f)
				return;
			GameManager.sInstance.spawnDecoration(DecorationType.BLOOD_POOL, x, y);
			AudioPlayer.play("entity-bear-hit.wav");
			velocity.x = 0.12f;
		}
	}
	
	/**
	 * 	x2 sprite rendering. Render first via super() and then the second
	 * 	manually.
	 */
	@Override
	public void render(Graphics g) {
		// Render first sprite
		super.render(g);
		
		// Render seconds sprite
		if(!dead) {
			if(hit) {
				Image img = spriteFront.img[whiteFrameIndex+1];
				g.drawImage(img,
		                (int)(x+16)*MooseInvasion.X_SCALE,
		                (int)y*MooseInvasion.Y_SCALE,
		                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
		                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
		                null);
			} else {
				int frame = ticks / 15 % spriteFront.anim.length;
		        Image img = spriteFront.img[spriteFront.anim[frame]];
		        g.drawImage(img,
		                (int)(x+16)*MooseInvasion.X_SCALE,
		                (int)y*MooseInvasion.Y_SCALE,
		                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
		                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
		                null
		        );
			}
		}
			
		// Render death animation.
		else {
			Image img;
			
			img = spriteFront.img[spriteFront.anim[deadFrame]];
			g.drawImage(img,
	                (int)(x+16)*MooseInvasion.X_SCALE,
	                (int)y*MooseInvasion.Y_SCALE,
	                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
	                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
	                null);
		}
	}
}
