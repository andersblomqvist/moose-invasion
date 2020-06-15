package com.andblomqdasberg.mooseinvasion.particle;

import java.awt.Graphics;
import java.awt.Image;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Abstract class for a particle object.
 * 
 * 	@author Anders Blomqvist
 */
public abstract class AbstractParticle {
	
	public float x, y, z;
	public Vector2D velocity;
	
	private int maxSpeed = 4;
	
	protected Sprite sprite;
	private int frame = 0;
	private int frameTick = 0;
	private Image img;
	
	public AbstractParticle() {}
	
	public AbstractParticle(float x, float y) {
		this.x = x; 
		this.y = y;
		this.velocity = new Vector2D(
				GameRandom.nextInt(2*maxSpeed) - maxSpeed, 
				GameRandom.nextInt(2*maxSpeed) - maxSpeed);
	}
	
	public void tick() {
		velocity.x *= 0.85;
		velocity.y *= 0.7;
		
		this.x += velocity.x;
		this.y += velocity.y;
	}
	
	public void animationTick() {
		if(frame != sprite.lastFrame())
			frameTick++;
		
		if(frameTick % sprite.animationSpeed == 0)
			frame++;
	}
	
	public void render(Graphics g, int gameTick) {
		if(frame >= sprite.lastFrame()) {
			// Stay on last frame
			frame = sprite.lastFrame();
			img = sprite.img[sprite.anim[frame]];
		} else {
			// Else, continue with animation
			frame = frameTick / sprite.animationSpeed % sprite.anim.length;
			img = sprite.img[sprite.anim[frame]];
		}
        
        g.drawImage(img,
                (int)x*MooseInvasion.X_SCALE,
                (int)y*MooseInvasion.Y_SCALE,
                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
                null
        );
	}
}