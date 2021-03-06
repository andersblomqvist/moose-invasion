package com.andblomqdasberg.mooseinvasion.entity.monster;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.entity.MeleeHit;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.Sprite;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

public class Moose extends EntityMonster
{
	private int spriteId = 3;
	private int[] anim = {0, 1, 2, 1};
	private int[] deathAnim = {3, 4, 5};
	
	// Collision sizes
	private int width = 10;
	private int height = 8;
	private int offsetX = 3;
	private int offsetY = 7;
	
	// Health
	public int health = 100;
	private boolean hit = false;
	private boolean dead = false;
	private int deadFrameTick = 0;
	private int deadFrame = 0;
	private int hitTick = 0;

	private ArrayList<GUIText> damageTextList;
	
	public Moose() {
		super(-10f, (float) GameRandom.nextInt(MooseInvasion.HEIGHT-16), new Vector2D());
		
		sprite = new Sprite(spriteId, anim);
		collider = new BoxCollider(this, width, height, "moose");
		
		// Randomize animation	
		if(GameRandom.nextFloat() > 0.5)
			sprite.anim = new int[] {1, 0};
		
		// Set random x speed.
		velocity.x = GameRandom.randomBetween(2, 4) / 10f;
		
		damageTextList = new ArrayList<>();
	}
	
	@Override
	public void tick(int ticks) {
		updateDamageText();
		
		// Render blood animation when we are dead.
		if(dead) {
			if(velocity.x >= 0f) {
				velocity.x -= 0.008f;
				this.x += velocity.x;
			}
				
			if(deadFrame != sprite.lastFrame())
				deadFrameTick++;
			else {
				// When we reach the last frame, spawn a decoration blood pool
				// and remove this moose entity
				GameManager.sInstance.spawnDecoration(
						DecorationType.BLOOD_POOL, x, y);
				
				// Need to remove the damage texts
				for(GUIText t : damageTextList)
					t.destroy();
				
				// Destroy the moose
				destroy();
			}
				
			
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
		
		// When moose have cross the play field player looses 1 point of health
		if(this.x > MooseInvasion.WIDTH) {
			dead = true;
			alive = false;
			GameManager.sInstance.reduceHealth();
			return;
		}
		super.tick(ticks);
	}
	
	@Override
	public void onTriggerEnter(BoxCollider b) {
		if(b.tag == "projectile") {
			Projectile p = (Projectile) b.e;
			health -= p.damage;
			addDamageText(p.damage);
			onDamageHit();
		}
		if(b.tag == "melee") {
			MeleeHit hit = (MeleeHit) b.e;
			health -= hit.damage;
			addDamageText(hit.damage);
			onDamageHit();
		}
	}
	
	/**
	 * 	Do the things needed when we get hit.
	 */
	private void onDamageHit() {
		AudioPlayer.play("moose-hit.wav");
		whiteHitOverlay();
		GameManager.sInstance.spawnParticles(ParticleType.BLOOD, 4, x, y);
		
		// Leave if health is not zero
		if(health > 0)
			return;
		
		// Set to dead state
		AudioPlayer.play("moose-splash.wav");
		GameManager.sInstance.spawnParticles(ParticleType.BLOOD_AND_MEAT, 1, x, y);
		GameManager.sInstance.onEntityKilled();
		sprite.anim = deathAnim;
		velocity.y = 0;
		dead = true;
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
	public void render(Graphics g) {
		// Render as normal
		if(!dead)
			if(hit) {
				Image img = sprite.img[7];
				g.drawImage(img,
		                (int)x*MooseInvasion.X_SCALE,
		                (int)y*MooseInvasion.Y_SCALE,
		                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
		                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
		                null);
			} else
				super.render(g);
		
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
	                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
	                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
	                null);
		}
	}

	private void addDamageText(int damage) {
		damageTextList.add(new GUIText(
				String.valueOf(damage), 
				this.x, 
				this.y, 
				"level-gui"));
	}

	private void updateDamageText() {
		for (int i = 0; i < damageTextList.size(); i++) {
			if(damageTextList.get(i).y < this.y - 15) {
				damageTextList.get(i).destroy();
				damageTextList.remove(i);
				i--;
			}
			else
				damageTextList.get(i).y -= 0.75;
		}
	}
	
	@Override
	public float getX() {
		return x + offsetX;
	}
	
	@Override
	public float getY() {
		return y + offsetY;
	}
}
