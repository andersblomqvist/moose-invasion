package com.andblomqdasberg.mooseinvasion.entity.player;

import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Movement behaviour for the player
 * 
 * 	@author Anders Blomqvist
 */
public class PlayerMovement {

	private float moveSpeed = 4;
	private float accel = 1.8f;
	private float deccel = 0.8f;
	private float friction = 0.9f;
	
	private Vector2D wishDir = new Vector2D();
	
	/**
	 * 	Handles the player input and applies movement to vector
	 * 
	 * 	@param velocity	Reference to player velocity vector
	 * 	@param appliedFriction Rangin from 0-1 where 1 is normal friction and
	 * 						   0 is like ice. 
	 * 	@returns the new velocity vector
	 */
	public Vector2D playerMove(Vector2D velocity, float appliedFriction) {
		wishDir.x = getInputX();
		wishDir.y = getInputY();
		
		// When nothing is pressed we don't add new movement
		if(wishDir.equals(Vector2D.ZERO))
			return velocity;
		else {
			
			// Cap movement
			if(Math.abs(velocity.x) > Math.abs(moveSpeed))
				return velocity;
			
			// Accelerate
			else {
				velocity.x += wishDir.x * accel;
				velocity.y += wishDir.y * accel;
			}
		}
		
		// Apply friction to velocity before we return it
		velocity = applyFriction(velocity, appliedFriction);
		return velocity;
	}
	
	public Vector2D applyFriction(Vector2D velocity, float cutOff) {
		
		float speed = velocity.magnitude();
		float control = speed < deccel ? deccel : speed;
		float drop = control * friction * cutOff;
		float newSpeed = speed - drop;
		
		if(newSpeed < 0)
			newSpeed = 0;
		if(speed > 0)
			newSpeed /= speed;
		
		return velocity.mul(newSpeed);
	}
	
	private int getInputX() {
		if(InputHandler.right(false))
			return 1;
		else
			return -1;
	}
	
	private int getInputY() {
		if(InputHandler.down(false))
			return 1;
		else
			return -1;
	}
}
