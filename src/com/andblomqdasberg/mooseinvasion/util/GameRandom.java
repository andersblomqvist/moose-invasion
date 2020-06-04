package com.andblomqdasberg.mooseinvasion.util;

import java.util.Random;

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
}
