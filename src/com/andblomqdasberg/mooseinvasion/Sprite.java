package com.andblomqdasberg.mooseinvasion;

import java.awt.image.BufferedImage;

/**
 * 	Template for various game objects which needs to be rendered.	
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Sprite {
	// Sprite ID is determined by row position in spritesheet
	// starting from 0. Example: player = 0, ground = 1 ... 
	public int spriteId;
	
	// Enables use of specific animation order
	public int[] anim;
	
	// Default animation speed
	public int animationSpeed = 15;
	
	// Reference to image
	public BufferedImage[] img;

	public Sprite(int spriteId, int[] anim) {
		this.spriteId = spriteId;
		this.anim = anim;
		img = Assets.sInstance.sprites[spriteId];
	}
	
	/**
	 * 	Specify other sprite sheet than regular sprite sheet from {@link Assets}
	 * 
	 * 	@param img Reference to {@code Assets.sInstance.<sprite sheet>}
	 */
	public Sprite(int spriteId, int[] anim, BufferedImage[] img) {
		this(spriteId, anim);
		this.img = img;
	}
	
	/**
	 * 	@returns the index of last frame in animation.
	 */
	public int lastFrame() {
		return anim.length-1;
	}
}
