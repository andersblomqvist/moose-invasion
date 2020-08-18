package com.andblomqdasberg.mooseinvasion.entity.damage;

import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Holder for an entity which causes damage to monsters. Carried over via
 * 	the BoxCollider.
 * 
 * 	@author Anders Blomqvist
 *
 */
public class EntityDamage extends AbstractEntity {
	
	public int damage;
	
	public EntityDamage(float x, float y, Vector2D velocity) {
		super(x, y, velocity);
	}
}
