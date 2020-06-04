package com.andblomqdasberg.mooseinvasion.util;

/**
 * 	Simple Vector2D class. Default constructor {@code new Vector2D()}
 * 	gives the (0, 0) vector.
 * 
 * 	@author Anders Blomqvist
 */
public class Vector2D
{
	public float x, y;
	
	public Vector2D()
	{
		x = 0;
		y = 0;
	}
	
	public Vector2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D add(Vector2D v1, int scalar) {
		return new Vector2D(v1.x + scalar, v1.y + scalar);
	}
	
	public Vector2D add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}
	
	public Vector2D mul(Vector2D v1, int scalar) {
		return new Vector2D(v1.x * scalar, v1.y * scalar);
	}
}
