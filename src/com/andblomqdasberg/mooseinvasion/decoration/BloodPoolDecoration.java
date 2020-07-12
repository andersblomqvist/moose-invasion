package com.andblomqdasberg.mooseinvasion.decoration;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	A single image blood pool
 * 
 * 	@author Anders Blomqvist
 */
public class BloodPoolDecoration extends AbstractDecoration {

	private int spriteId = 0;
	
	public BloodPoolDecoration(float x, float y) {
		super(x, y);
		
		sprite = new Sprite(
				spriteId, 
				new int[] {0}, 
				Assets.sInstance.decorations[spriteId]);
	}
	
}