package com.andblomqdasberg.mooseinvasion.entity.player;

import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Movement behaviour for the player
 * 
 * 	@author Anders Blomqvist
 */
public class PlayerMovement {

	private float moveSpeed = 1.6f;
	private float accel = 0.25f;
	private float deccel = 0.5f;
	private float friction = 0.1f;
	public boolean north = false;
	public boolean south = false;
	public boolean east = false;
	public boolean west = false;
	
	public Vector2D wishDir = new Vector2D();
	
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
		
		if(!wishDir.equals(Vector2D.ZERO)) {
			// Only apply new movement if we're slow enough
			if(velocity.magnitude() < moveSpeed) {
				velocity.x += wishDir.x * accel;
				velocity.y += wishDir.y * accel;
			}
		}
		
		// Apply friction to velocity before we return it
		velocity = applyFriction(velocity, appliedFriction);
		
		if(north && velocity.y < 0)
			velocity.y = 0;
		if(south && velocity.y > 0)
			velocity.y = 0;
		if(west && velocity.x < 0)
			velocity.x = 0;
		if(east && velocity.x > 0)
			velocity.x = 0;
		
		return velocity;
	}
	
	public Vector2D applyFriction(Vector2D velocity, float cutOff) {
		float speed = velocity.magnitude();
		float control = speed < deccel ? deccel : speed;
		float drop = control * friction * cutOff;
		float newSpeed = speed - drop;
		
		if(newSpeed < 0.1f)
			newSpeed = 0;
		if(speed > 0)
			newSpeed /= speed;
		
		return velocity.mul(newSpeed);
	}
	
	/**
	 * 	Controls the ability for player to move in a specific direction
	 * 
	 * 	@param direction In which direction
	 * 	@param state Toggle block on or off
	 */
	public void blockMovement(CollisionType direction, boolean state) {
		
		// State is false when we left a collider so movement should be free
		if(!state) {
			north = state;
			south = state;
			west = state;
			east = state;
		}
			
		switch(direction) {
    		case NORTH:
    			north = state;
    			break;
    		case EAST:
    			east = state;
    			break;
    		case WEST:
    			west = state;
    			break;
    		case SOUTH:
    			south = state;
    			break;
			default:
				break;
		}
	}
	
	/**
	 * 	Will return:
	 * 		0 if no button is pressed.
	 * 		1 if right is pressed.
	 * 	   -1 if left is pressed.
	 * 
	 * 	Also checks if a movement direction is blocked. If blocked return 0,
	 *	otherwise return intended value.
	 */
	private int getInputX() {
		if(InputHandler.right(false))
			return east == true ? 0 : 1;
		else
			if(InputHandler.left(false))
				return west == true ? 0 : -1;
			else
				return 0;
	}
	
	/**
	 * 	Will return: 
	 * 		0 if no button is pressed.
	 * 		1 if down is pressed.
	 * 	   -1 if up is pressed.
	 * 
	 *	Also checks if a movement direction is blocked. If blocked return 0,
	 *	otherwise return intended value. 
	 */
	private int getInputY() {
		if(InputHandler.down(false))
			return south == true ? 0 : 1;
		else
    		if(InputHandler.up(false))
    			return north == true ? 0 : -1;
    		else
    			return 0;
	}
}
