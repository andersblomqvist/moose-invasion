package com.andblomqdasberg.mooseinvasion.entity;

import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	When a melee weapon hits we create this entity and connects it to the
 * 	BoxCollider so monsters can access its damage.
 * 
 * 	@author Anders
 */
public class MeleeHit extends AbstractEntity {

	public int damage;
	
	public MeleeHit(float x, float y, Vector2D velocity) {
		super(x, y, velocity);
	}

	public MeleeHit(int damage) {
		this(0, 0, Vector2D.ZERO);
		this.damage = damage;
	}
}
