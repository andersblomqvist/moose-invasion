package com.andblomqdasberg.mooseinvasion.entity;

import com.andblomqdasberg.mooseinvasion.entity.monster.Moose;

/**
 * 	List of all the entities in the game
 * 
 * 	@author Anders Blomqvist
 */
public class EntityList {
	
	/**
	 * 	Get a new instance of an entity from a string.	
	 * 
	 * 	@param entity What type of entity
	 * 	@returns A new instance of an entity
	 */
	public static AbstractEntity getEntity(String entity) {
		switch(entity) {
			case "moose":
				return new Moose();
			default:
				return null;
		}
	}
}
