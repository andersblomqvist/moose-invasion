package com.andblomqdasberg.mooseinvasion.collider;

public enum CollisionType {
	
	// Direction collision (solids)
	NORTH,
	SOUTH,
	EAST,
	WEST,
	
	// Others
	TRIGGER,	// Not a solid collider
	DEFAULT,	// Solid collision but we don't care about direction
	NONE		// Collision failed.
}
