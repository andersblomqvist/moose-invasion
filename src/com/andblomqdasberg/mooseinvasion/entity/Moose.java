package com.andblomqdasberg.mooseinvasion.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;

public class Moose extends Entity
{
	private static int SPRITE_ID = 3;
	private static int[] ANIM = {0, 1, 2, 1};
	private static int[] DEATH_ANIM = {3, 4, 5, 6};
	
	// Horizontal speed of the moose
	private float speedX = 0.33f;
	
	// Collision sizes
	private int width = 10;
	private int height = 8;
	private int offsetX = 3;
	private int offsetY = 7;
	
	// Health stuff
	public int health = 100;
	private boolean hit = false;
	private int deadFrameTick = 0;
	private int deadFrame = 0;
	private int hitTick = 0;
	
	// Reference to collision projectile if any
	private Projectile p;
	
	private AudioPlayer audioPlayer;

	private final ArrayList<GUIText> damageTextList;
	
	/**
	 * 	Default constructor with random spawn position
	 */
	public Moose() {
		super(SPRITE_ID, ANIM, -10, GameRandom.nextInt(MooseInvasion.HEIGHT-16));
		// Randomize animation
		if(GameRandom.nextFloat() > 0.5)
			sprite.anim = new int[] {1, 0};
		velocity.x = speedX;

		damageTextList = new ArrayList<>();
	}

	/**
	 *  TODO Make the moose not just go straight ahead.
	 */
	@Override
	public void tick() {
		updateDamageText();
		
		// Render blood animation when we are dead.
		if(dead)
		{
			if(velocity.x >= 0f)
			{
				velocity.x -= 0.008f;
				this.x += velocity.x;
			}
				
			if(deadFrame != sprite.lastFrame())
				deadFrameTick++;
			
			if(deadFrameTick % 15 == 0)
				deadFrame++;
			
			return;
		}
		
		// When hit we want to flash white for some short time
		if(hit) {
			hitTick++;
			if(hitTick % 5 == 0)
				hit = false;
		}
		
		// Check collision with projectile
		if((p = AABBCollision()) != null) {
			
			// Hit state, not dead yet
			playHitSound();
			whiteHitOverlay();
			health -= p.damage;
			p.tryRemove(p.index);
			GameManager.sInstance.spawnParticles(ParticleType.BLOOD, 4, x, y);

			addDamageText(p.damage);
			
			// Leave if health is not zero
			if(health > 0)
				return;
			
			// Set to dead state
			playDeathSound();
			GameManager.sInstance.spawnParticles(ParticleType.BLOOD_AND_MEAT, 1, x, y);
			GameManager.sInstance.addProgress();
			sprite.anim = DEATH_ANIM;
			velocity.y = 0;
			dead = true;
			return;
		}
		
		// When moose have cross the play field player looses 1 point of health
		if(this.x > MooseInvasion.WIDTH)
		{
			dead = true;
			alive = false;
			GameManager.sInstance.reduceHealth();
			return;
		}

		super.tick();
	}
	
	/**
	 * 	2D AABB Collision detection with projectiles.
	 * 
	 * 	@returns The projectile which we collided with
	 */
	private Projectile AABBCollision() {
		for(int i = 0; i < GameManager.sInstance.projectiles.size(); i++) {
			Projectile p = (Projectile) GameManager.sInstance.projectiles.get(i);
			
			// Offset is how many pixels in x and y direction to where the sprite
			// begins within its 16x16 bounds.
			if(this.x+offsetX < p.x+p.offsetX + p.width && 
				this.x+offsetX + width > p.x+p.offsetX &&
				this.y+offsetY < p.y+p.offsetY + p.height &&
				this.y+offsetY + height > p.y+p.offsetY) {
				
				// Set index and return reference to projectile
				p.index = i;
				return p;
			}
		}
		return null;
	}
	
	/**
	 * 	Toggle white overlay to ON, will set to OFF after
	 * 	some short time in {@code tick()} method.
	 */
	private void whiteHitOverlay() {
		hitTick = 0;
		hit = true;
	}
	
	/**
	 * 	Render as a normal entity when moose is not dead.
	 * 	When dead we want to play death animation and stop at the
	 * 	last death frame which is the blood pool.
	 * 
	 * 	Also when hit, we render a white overlay
	 */
	@Override
	public void render(Graphics g, int gameTick) {
		// Render as normal
		if(!dead)
			if(hit) {
				Image img = sprite.img[7];
				g.drawImage(img,
		                (int)x*MooseInvasion.X_SCALE,
		                (int)y*MooseInvasion.Y_SCALE,
		                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_SIZE,
		                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_SIZE,
		                null);
			} else
				super.render(g, gameTick);
		
		// Render death animation.
		else {
			Image img;
			
			if(deadFrame < sprite.lastFrame())
				deadFrame = deadFrameTick / 10 % sprite.anim.length;
			else
				deadFrame = sprite.lastFrame();
			
			img = sprite.img[sprite.anim[deadFrame]];
			g.drawImage(img,
	                (int)x*MooseInvasion.X_SCALE,
	                (int)y*MooseInvasion.Y_SCALE,
	                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_SIZE,
	                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_SIZE,
	                null);
		}
	}
	
	private void playHitSound() {
		try {
            audioPlayer = new AudioPlayer("moose-hit.wav");
            audioPlayer.setVolume(-22);
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
	}
	
	private void playDeathSound() {
		try {
            audioPlayer = new AudioPlayer("death-splash.wav");
            // audioPlayer.setVolume(0.01f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
        try {
            audioPlayer = new AudioPlayer("big-splash.wav");
            audioPlayer.setVolume(-22f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
	}

	private void addDamageText(int damage) {
		damageTextList.add(new GUIText(String.valueOf(damage), (int)this.x, (int)this.y, 2));
	}

	private void updateDamageText(){

		for (int i = 0; i < damageTextList.size(); i++) {
			if(damageTextList.get(i).y < this.y - 15){
				damageTextList.get(i).removeFromRender();
				damageTextList.remove(i);
				i--;
			}
			else{
				damageTextList.get(i).y -= 0.75;
			}
		}
	}

}
