package com.andblomqdasberg.mooseinvasion.collider;

import java.awt.Color;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.entity.Player;

/**
 * 	Box collider
 * 
 * 	@author Anders Blomqvist
 */
public class BoxCollider {

	public int x, y, width, height;
	public boolean trigger;
	public String tag;
	
	public boolean colliding = false;
	
	/**
	 * 	Default constructor
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
	 * 	Trigger, need to specify a unique tag aswell
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param width
	 * 	@param height
	 * 	@param trigger
	 * 	@param tag Identity of this trigger
	 */
	public BoxCollider(int x, int y, int width, int height, boolean trigger, String tag) {
		this(x, y, width, height);
		this.trigger = trigger;
		this.tag = tag;
	}

	/**
	 * 	AABB Collision detection which returns which side we collided with
	 * 
	 * 	@param p Player reference
	 * 
	 * 	@return collition type enum for what side
	 */
	public CollisionType AABBCollision(Player p) {
		
		// Do AABB detect
		if(this.x <= p.getColX() + p.width && this.x + width >= p.getColX() &&
			this.y <= p.getColY() + p.height && this.y + height >= p.getColY()) {
			
			// We are now colliding
			colliding = true;
			
			// Leave if we are a trigger
			if(trigger)
				return CollisionType.TRIGGER;
			
			// Check at which side the collision happen
			if(p.getColX() <= this.x - p.width + 2)
				return CollisionType.WEST;
			
			if(p.getColY() <= this.y - p.height + 2)
				return CollisionType.NORTH;
			
			if(p.getColY() + p.height >= this.y + height + p.height - 2)
				return CollisionType.SOUTH;
			
			if(p.getColX() + p.width >= this.x + width + p.width - 2)
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
	 * 	Debug render
	 */
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(
				x*MooseInvasion.X_SCALE, 
				y*MooseInvasion.Y_SCALE, 
				width*MooseInvasion.X_SCALE, 
				height*MooseInvasion.Y_SCALE);
	}
}
