package com.andblomqdasberg.mooseinvasion.entity.monster;

import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Holder for easily identifying if an entity is a monster.
 * 
 * 	Used to skip collision detection between two monsters.
 * 
 * 	@author Anders Blomqvist
 */
public class EntityMonster extends AbstractEntity {

	public EntityMonster(float x, float y, Vector2D velocity) {
		super(x, y, velocity);
	}
}
