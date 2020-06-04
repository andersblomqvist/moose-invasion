package com.andblomqdasberg.mooseinvasion.entity;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

import java.awt.*;

/**
 * 	Basic projectile entity
 * 
 * 	@author Anders Blomqvist
 */
public class Projectile extends Entity
{
	private static int SPRITE_ID = 2;
	private static int numberOfSprites = 2;
	
	// Projectile damage
	public int damage;
	public boolean fmj = false;
	
	private float speedX = -6.5f;
	
	// Collision sizes
	public int width = 6;
	public int height = 2;
	public int offsetX = 4;
	public int offsetY = 9;
	
	// Which index this projectile have in GameManager list
	// Used for removal when collision with moose.
	public int index = -1;
	
	private int muzzle = 0;
	private int maxFrames = 15;
	
	public Projectile(float f, float y) {
		super(SPRITE_ID, numberOfSprites, f, y);
		velocity = new Vector2D(speedX, 0f);
	}

	/**
	 * 	Constructor called by player
	 */
	public Projectile(float x, float y, int damage, boolean fmj) {
		this(x, y);
		this.damage = damage;
		this.fmj = fmj;
	}
	
	/**
	 * 	Tries to remove the projectile. Called from {@link Moose} when
	 * 	collision is detected.
	 */
	public void tryRemove(int index) {
		if(!fmj) {
			GameManager.sInstance.removeProjectile(index);
		}
	}
	
	@Override
	public void render(Graphics g, int gameTick) {
		// Render muzzleflash
		if(muzzle < maxFrames) {
			g.drawImage(sprite.img[0],
					(int)x*MooseInvasion.SCALE,
					(int)y*MooseInvasion.SCALE,
					16*MooseInvasion.SCALE,
					16*MooseInvasion.SCALE,
					null
			);
			muzzle += 1;
		} else {
			g.drawImage(sprite.img[1],
					(int)x*MooseInvasion.SCALE,
					(int)y*MooseInvasion.SCALE,
					16*MooseInvasion.SCALE,
					16*MooseInvasion.SCALE,
					null
			);
		}
	}
}
