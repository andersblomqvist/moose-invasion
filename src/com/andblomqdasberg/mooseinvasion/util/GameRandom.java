package com.andblomqdasberg.mooseinvasion.util;

import java.util.Random;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;

/**
 * 	Static helper class for random methods
 */
public class GameRandom
{
	private static Random random = new Random();
	
	/**
	 * 	Returns int between 0 and bound-1
	 */
	public static int nextInt(int bound)
	{
		return random.nextInt(bound);
	}
	
	/**
	 * 	Returns float between 0.0 and 1.0.
	 */
	public static float nextFloat()
	{
		return random.nextFloat();
	}
	
	/**
	 * 	Returns a random integer number between min and max (inclusive)
	 * 
	 * 	@param min Lower bound
	 * 	@param max Hihger bound
	 * 	@returns an integer between max and min
	 */
	public static int randomBetween(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	/**
	 * 	@returns a random y-value within the Window height
	 */
	public static float getRandomY() {
		return (float) GameRandom.nextInt(MooseInvasion.HEIGHT-16);
	}
}
