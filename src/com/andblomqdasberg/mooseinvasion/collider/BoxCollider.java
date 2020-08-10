package com.andblomqdasberg.mooseinvasion.collider;

import java.awt.Color;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.player.Player;

/**
 * 	Box collider.
 * 
 * 	Note. All entities should be triggers because they are not supposed to
 * 	prevent any kinds of movement. Only static colliders should be solids.
 * 	
 * 	@author Anders Blomqvist
 */
public class BoxCollider {

	// Static x and y position
	public int x, y;
	
	// Dimensions
	public int width, height;
	
	// Trigger options
	public boolean trigger = false;
	public String tag;
	
	// Entity refernce
	public AbstractEntity e;
	
	// Track if this collider is colliding
	public boolean colliding = false;
	
	/**
	 * 	Static collider
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param width
	 * 	@param height
	 */
	public BoxCollider(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 	Static Trigger
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param width
	 * 	@param height
	 * 	@param trigger
	 * 	@param tag Identity of this trigger
	 */
	public BoxCollider(int x, int y, int width, int height, String tag) {
		this(x, y, width, height);
		this.tag = tag;
		trigger = true;
	}
	
	/**
	 * 	Moving collider
	 * 
	 * 	@param e Reference to moving entity
	 * 	@param width
	 * 	@param height
	 */
	public BoxCollider(AbstractEntity e, int width, int height) {
		this.e = e;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 	Moving trigger
	 * 
	 * 	@param e Reference to moving entity
	 * 	@param width
	 * 	@param height
	 * 	@param tag Name of trigger
	 */
	public BoxCollider(AbstractEntity e, int width, int height, String tag) {
		this(e, width, height);
		this.tag = tag;
		trigger = true;
	}

	/**
	 * 	Player collision check with static collider. Here we care about in which
	 * 	direction the collision happen to prevent movement.
	 * 
	 * 	@param p Player reference
	 * 	@return collition type enum for what side
	 */
	public CollisionType AABBCollision(Player p) {
		
		// Do AABB detect
		if(this.x <= p.getX() + p.width && this.x + width >= p.getX() &&
			this.y <= p.getY() + p.height && this.y + height >= p.getY()) {
			
			// We are now colliding
			colliding = true;
			
			// Leave if we are a trigger
			if(trigger)
				return CollisionType.TRIGGER;
			
			// Check at which side the collision happen
			if(p.getX() <= this.x - p.width + 2)
				return CollisionType.WEST;
			
			if(p.getY() <= this.y - p.height + 2)
				return CollisionType.NORTH;
			
			if(p.getY() + p.height >= this.y + height + p.height - 2)
				return CollisionType.SOUTH;
			
			if(p.getX() + p.width >= this.x + width + p.width - 2)
				return CollisionType.EAST;
			
			return CollisionType.NONE;
				
		} else {
			// If this collider have been in a collision this will be true
			// which means a player have left
			if(colliding) {
				if(trigger)
					p.onTriggerExit(tag);
				else
					p.onCollisionExit();
				colliding = false;
			}
			return CollisionType.NONE;
		}
			
	}
	
	/**
	 * 	Check collision with another moving collider.
	 * 
	 * 	@param reference to collider
	 * 	@return collition enum for what side
	 */
	public CollisionType AABBCollision(BoxCollider b) {
		if(e.getX() <= b.e.getX() + b.width && e.getX() + width >= b.e.getX() &&
		   e.getY() <= b.e.getY() + b.height && e.getY() + height >= b.e.getY()) {
			
			// If we are already colliding - leave
			if(colliding && b.colliding)
				return CollisionType.DEFAULT;
			
			// We are now colliding
			colliding = true;
			b.colliding = true;
			
			// Send correct respond depending on what type of colliders
			if(b.trigger)
				e.onTriggerEnter(b);
			else
				e.onCollisionEnter(CollisionType.DEFAULT);
			if(trigger)
				b.e.onTriggerEnter(this);
			else
				b.e.onCollisionEnter(CollisionType.DEFAULT);
			
			return CollisionType.DEFAULT;
		} else {
			// Handle when colliders leave each other.
			if(colliding && b.colliding) {
				if(trigger)
					if(b.trigger) {
						b.e.onTriggerExit(this);
						e.onTriggerExit(b);
					} else {
						b.e.onTriggerExit(this);
						e.onCollisionExit(b);
					}
				else {
					if(b.trigger) {
						e.onTriggerExit(b);
						b.e.onCollisionExit(this);
					} else {
						b.e.onCollisionExit(this);
						e.onCollisionExit(b);
					}
				}
				colliding = false;
				b.colliding = false;
			}
		}
		
		return CollisionType.NONE;
	}
	
	/**
	 * 	Debug render
	 */
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(
				(int)e.getX()*MooseInvasion.X_SCALE, 
				(int)e.getY()*MooseInvasion.Y_SCALE, 
				width*MooseInvasion.X_SCALE, 
				height*MooseInvasion.Y_SCALE);
	}
}
