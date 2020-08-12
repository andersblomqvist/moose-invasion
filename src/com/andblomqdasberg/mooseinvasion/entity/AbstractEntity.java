package com.andblomqdasberg.mooseinvasion.entity;

import java.awt.Graphics;
import java.awt.Image;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Abstract class for an entity. An entity is a game object which needs to be
 * 	updated and rendered. It can also collide with other entities or other
 * 	colliders.
 * 
 * 	Entities can also be compared by their y-position for sorting entities for
 * 	z-depth rendering.
 * 	
 * 	A new entity type needs to create a sprite object and a BoxCollider. It also
 * 	needs to override the getX() and getY() methods.
 * 
 * 	@author Anders Blomqvist
 */
public abstract class AbstractEntity implements Comparable<AbstractEntity> {
	
	// If the entity is outside the window by +/- this value we delete it.
	private static int BOUNDS_MARGIN = 20;
	public boolean alive = true; 			// When alive is false we delete
	
	public float x, y;
	public Vector2D velocity;
	public BoxCollider collider;
	
	// Are we visible for the renderer?
	public boolean visible = true;
	
	protected Sprite sprite;
	
	public int ticks;
	
	/**
	 * 	Create a new entity with specified position and velocity
	 */
	public AbstractEntity(float x, float y, Vector2D velocity) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
	}
	
	/**
	 * 	Check out of bounds and update velocity
	 */
	public void tick(int ticks) {
		this.ticks = ticks;
        if (this.x < -BOUNDS_MARGIN ||
            this.x > MooseInvasion.WIDTH + BOUNDS_MARGIN ||
            this.y < -BOUNDS_MARGIN ||
            this.y > MooseInvasion.HEIGHT + BOUNDS_MARGIN ) {
        	// Set alive to false when out of bounds with a bit of margin
            alive = false;
        }
        
        x += velocity.x;
        y += velocity.y;
	}
	
	/**
	 * 	Render entity if visible
	 */
	public void render(Graphics g) {
		if(!visible)
			return;
		
		int frame = ticks / 15 % sprite.anim.length;
        Image img = sprite.img[sprite.anim[frame]];
        g.drawImage(img,
                (int)x*MooseInvasion.X_SCALE,
                (int)y*MooseInvasion.Y_SCALE,
                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
                null
        );
        
        // collider.render(g);
	}
	
	public void onCollisionEnter(BoxCollider b, CollisionType direction) {}
	
	public void onCollisionExit(BoxCollider b, CollisionType direction) {}
	
	public void onTriggerEnter(BoxCollider b) {}
	
	public void onTriggerExit(BoxCollider b) {}
	
	/**
	 * 	Remove this entity from GameManager entity list.
	 */
	public void destroy() {
		GameManager.sInstance.entities.remove(this);
	}
	
	/**
	 * 	Compare y position for z-rendering.
	 */
	@Override
	public int compareTo(AbstractEntity e) {
		if(this.y > e.y)
			return 1;
		else if(this.y == e.y)
			return 0;
		else
			return -1;
	}
	
	/**
	 * 	Override for each entity due to different sprites.
	 * 
	 * 	@returns the origin x position of the collider. The sprite may not be 
	 * 	start at the top left which means there is an offset. 
	 */
	public float getX() {
		return 0;
	}
	
	/**
	 * 	Override for each entity due to different sprites.
	 * 
	 * 	@returns the origin y position of the collider. The sprite may not be 
	 * 	start at the top left which means there is an offset. 
	 */
	public float getY() {
		return 0;
	}
}