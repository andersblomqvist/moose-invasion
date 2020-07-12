package com.andblomqdasberg.mooseinvasion.decoration;

import java.awt.Graphics;
import java.awt.Image;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.util.Sprite;

/**
 * 	Abstract class for a decoration object. It's an object which is only
 * 	rendered (blood pools ... )
 * 
 * 	@author Anders Blomqvist
 */
public abstract class AbstractDecoration {

	// Render postion
	public float x, y;
	
	// Init in specific decoration objects
	protected Sprite sprite;
	
	/**
	 * 	Create a single image decoration sprite
	 * 
	 * 	@param x world position
	 * 	@param y world position
	 */
	public AbstractDecoration(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics g, int ticks) {
        int frame = ticks / sprite.animationSpeed % sprite.anim.length;
        Image img = sprite.img[sprite.anim[frame]];
        g.drawImage(img,
                (int)x*MooseInvasion.X_SCALE,
                (int)y*MooseInvasion.Y_SCALE,
                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
                null
        );
	}
}
