package com.andblomqdasberg.mooseinvasion.util;

/**
 * 	Simple Vector2D class. Default constructor {@code new Vector2D()}
 * 	gives the (0, 0) vector.
 * 
 * 	@author Anders Blomqvist
 */
public class Vector2D {
	
	public static final Vector2D ZERO = new Vector2D();
	
	public float x, y;
	
	public Vector2D() {
		x = 0f;
		y = 0f;
	}
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D add(Vector2D v1, int scalar) {
		return new Vector2D(v1.x + scalar, v1.y + scalar);
	}
	
	public Vector2D add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}
	
	public Vector2D mul(float scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}

	public float magnitude() {
		return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(" + this.x + "," + this.y + ")");
		return builder.toString();
	}
}
