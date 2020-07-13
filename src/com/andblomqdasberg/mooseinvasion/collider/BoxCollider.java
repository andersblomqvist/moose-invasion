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
	
	public boolean colliding = false;
	
	public BoxCollider(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * 	AABB Collision detection which returns which side we collided with
	 * 
	 * 	@param p Player reference
	 * 
	 * 	@return collition type enum for what side
	 */
	public CollisionType AABBCollision(Player p) {
		if(this.x <= p.getColX() + p.width && this.x + width >= p.getColX() &&
			this.y <= p.getColY() + p.height && this.y + height >= p.getColY()) {
			
			colliding = true;
			
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
			if(colliding) {
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
