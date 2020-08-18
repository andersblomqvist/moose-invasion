package com.andblomqdasberg.mooseinvasion.entity.damage;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.entity.monster.EntityMonster;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

import java.awt.*;

/**
 * 	Basic projectile entity
 * 
 * 	@author Anders Blomqvist
 */
public class Projectile extends EntityDamage
{
	private static float speedX = -7f;
	
	private int spriteId = 2;
	
	// Projectile specs
	public boolean penetrationLight = false;	// Only penetrade 1 mob
	public boolean penetrationFull = false;		// No stop
	
	// Which index this projectile have in GameManager list
	// Used for removal when collision with moose.
	public int index = -1;
	
	private int muzzle = 0;
	private int maxFrames = 15;
	
	public Projectile(float x, float y) {
		super(x, y, new Vector2D(speedX, 0));
		
		width = 6;
		height = 2;
		offsetX = 3;
		offsetY = 8;
		
		sprite = new Sprite(spriteId, new int[] {});
		collider = new BoxCollider(this, width, height, "projectile");
	}

	/**
	 * 	Constructor called by player
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param damage
	 * 	@param light Toggle 1 time penetration
	 * 	@param full Toggle unlimited penetration
	 */
	public Projectile(float x, float y, int damage, boolean light, boolean full) {
		this(x, y);
		this.damage = damage;
		this.penetrationLight = light;
		this.penetrationFull = full;
	}
	
	@Override
	public void onTriggerEnter(BoxCollider b) {
		if(b.e instanceof EntityMonster) {
			// No removal if we got full penetration
			if(penetrationFull)
				return;
			
			// Skip removal once if we have light penetration
			// The -= 10f is needed to prevent the moose taking double damage from
			// the projectile. It should just take damage once and keep going.
			if(penetrationLight) {
				penetrationLight = false;
				this.x -= 10f;
				return;
			}
			
			GameManager.sInstance.entities.remove(this);
		}
	}
	
	@Override
	public void render(Graphics g) {		
		// Render muzzleflash
		if(muzzle < maxFrames) {
			g.drawImage(sprite.img[0],
					(int)x*MooseInvasion.X_SCALE,
					(int)y*MooseInvasion.Y_SCALE,
					16*MooseInvasion.X_SCALE,
					16*MooseInvasion.Y_SCALE,
					null
			);
			muzzle += 1;
		} else {
			g.drawImage(sprite.img[1],
					(int)x*MooseInvasion.X_SCALE,
					(int)y*MooseInvasion.Y_SCALE,
					16*MooseInvasion.X_SCALE,
					16*MooseInvasion.Y_SCALE,
					null
			);
		}
		
		// collider.render(g);
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