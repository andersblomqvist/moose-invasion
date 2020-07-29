package com.andblomqdasberg.mooseinvasion.entity;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

import java.awt.*;

/**
 * 	Template class for an entity. This object needs rendering
 * 	and update.
 *
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Entity implements Comparable<Entity> {
	
    public Vector2D velocity;
    public float x, y;
    
    // Store if this entity needs updates (gc)
    public boolean alive = true;
    
    // When an entity is out of the game but still needs some rendering and
    // or updates.
    public boolean dead = false;
    
    // Handle rendering
    protected Sprite sprite;

    // If the entity is outside the game frame with this much
    // we consider it to be dead.
    private int boundsMargin = 20;
    
    /**
     * 	Creates an entity with default velocity (0, 0)
     */
    public Entity(int spriteId, int[] anim, float x, float y) {
        sprite = new Sprite(spriteId, anim);
        this.x = x;
        this.y = y;
        velocity = new Vector2D();
    }

    /**
     * 	Default constructor with no animation
     */
    public Entity(int spriteId, int numberOfSprites2, float x, float y) {
		this(spriteId, null, x, y);
	}

	/**
     * 	Entity update
     */
    public void tick(int ticks) {
    	// Set alive to false when out of bounds with a bit of margin
        if (
    		this.x < -boundsMargin ||
            this.x > MooseInvasion.WIDTH + boundsMargin ||
            this.y < -boundsMargin ||
            this.y > MooseInvasion.HEIGHT + boundsMargin
		) {
            alive = false;
        }
        
        x += velocity.x;
        y += velocity.y;
    }

    /**
     * 	Entity render
     */
    public void render(Graphics g, int gameTick) {
        int frame = gameTick / 15 % sprite.anim.length;
        Image img = sprite.img[sprite.anim[frame]];
        g.drawImage(img,
                (int)x*MooseInvasion.X_SCALE,
                (int)y*MooseInvasion.Y_SCALE,
                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
                null
        );
    }

    /**
     * 	Remove ourselves from the entity list 
     */
    public void destroy() {
    	GameManager.sInstance.entities.remove(this);
    }
    
    /**
     * 	Enables entities to be compared depending on their y-position
     * 	so we create an illusion of depth. Also if the entity is dead
     * 	we render it below other alive entities, regardless of y-pos.
     */
	@Override
	public int compareTo(Entity e1) {
		if(e1.dead)
			return 1;
		else if(this.y > e1.y)
			return 1;
		else if(this.y == e1.y)
			return 0;
		else
			return -1;
	}
}