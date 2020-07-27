package com.andblomqdasberg.mooseinvasion.entity;

import com.andblomqdasberg.mooseinvasion.entity.player.Player;

/**
 * 	List of all the entities in the game
 * 
 * 	@author Anders Blomqvist
 */
public class EntityList {

	public static Player PLAYER;
	
	// Mobs
	public static Moose MOOSE;
	
	/**
	 * 	Get a new instance of an entity from a string.	
	 * 
	 * 	@param entity What type of entity
	 * 	@returns A new instance of an entity
	 */
	public static Entity getEntity(String entity) {
		switch(entity) {
			case "moose":
				return new Moose();
			default:
				return null;
		}
	}
}
