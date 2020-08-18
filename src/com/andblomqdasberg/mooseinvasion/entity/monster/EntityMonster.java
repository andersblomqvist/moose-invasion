package com.andblomqdasberg.mooseinvasion.entity.monster;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.damage.EntityDamage;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.Vector2D;

/**
 * 	Holder for easily identifying if an entity is a monster. Also holds info about
 * 	health and if monster is dead.
 * 
 * 	Used to skip collision detection between two monsters.
 * 
 * 	@author Anders Blomqvist
 */
public class EntityMonster extends AbstractEntity {
	
	public int maxHealth;
	public int health;
	
	// Hit and dead controllers
	public boolean remainCorpse = false;
	public boolean finalDead = false;	// Sets to true when last dead frame is reached
	protected boolean hit = false;
	protected int deadFrameTick = 0;
	protected int deadFrame = 0;
	protected int hitTick = 0;
	
	// Control how much particles should spawn
	protected int bloodParticles;
	protected int bloodAndMeatParticles;
	
	// Animations for monster
	protected int[] anim;
	protected int[] deathAnim;
	protected int whiteFrameIndex; // TODO Make new spritesheet with only monsters?
	
	// GUI damage hit texts
	private ArrayList<GUIText> dmgText = new ArrayList<GUIText>();
	
	public EntityMonster(float x, float y, Vector2D velocity) {
		super(x, y, velocity);
	}
	
	/**
	 * 	Updates the damage texts and then calls regular tick()
	 */
	@Override
	public void tick(int ticks) {
		updateDamageText();
		
		// Set hit back to false after a very short time
		if(hit) {
			hitTick++;
			if(hitTick % 5 == 0)
				hit = false;
		}
		
		// Tick when dead until we remove the entity
		if(dead) {
			deadFrameTick++;
			
			// Slide effect
			if(velocity.x > 0f) {
				velocity.x -= 0.008f;
				x += velocity.x;
			}
			
			if(deadFrameTick % 15 == 0) {
				// When all death frames have been shown we either stay
				// on last frame or destroy this entity. Either way we spawn a
				// decorative blood pool.
				if(deadFrame + 1 > sprite.lastFrame()) {
					if(remainCorpse) {
						deadFrame = sprite.lastFrame();
						finalDead = true;
						return;
					} else {
						GameManager.sInstance.spawnDecoration(DecorationType.BLOOD_POOL, x, y);
						destroy();
					}
				}
				deadFrame++;
			}
			return;
		}
		
		// When the monster has crossed the entire playfied the player loses
		// a health point.
		if(this.x > MooseInvasion.WIDTH) {
			dead = true;
			alive = false;
			GameManager.sInstance.reduceHealth();
			return;
		}
		
		// Update movement and bounds check
		super.tick(ticks);
	}
	
	/**
	 * 	Render as a normal entity when monster is not dead.
	 * 	When dead we want to play death animation
	 * 
	 * 	Also when hit, we render a white overlay
	 */
	@Override
	public void render(Graphics g) {
		// Render as normal
		if(!dead)
			if(hit) {
				Image img = sprite.img[whiteFrameIndex];
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
			img = sprite.img[sprite.anim[deadFrame]];
			g.drawImage(img,
	                (int)x*MooseInvasion.X_SCALE,
	                (int)y*MooseInvasion.Y_SCALE,
	                MooseInvasion.X_SCALE*MooseInvasion.SPRITE_X_SIZE,
	                MooseInvasion.Y_SCALE*MooseInvasion.SPRITE_Y_SIZE,
	                null);
		}
	}
	
	/**
	 * 	Checks if the collider we entered have a damage entity connected to it
	 * 	so we can take damage.
	 */
	@Override
	public void onTriggerEnter(BoxCollider b) {
		if(dead)
			return;
		
		if(b.e instanceof EntityDamage) {
			EntityDamage hit = (EntityDamage) b.e;
			health -= hit.damage;
			addDamageText(hit.damage);
			onDamageTaken();
		}
	}
	
	/**
	 * 	Plays audio and does graphics. Also check if entity is dead where if true
	 * 	toggle dead state.
	 */
	protected void onDamageTaken() {
		GameManager.sInstance.spawnParticles(ParticleType.BLOOD, bloodParticles, x, y);
		AudioPlayer.play("entity-hit.wav");
		whiteOverLay();
		
		// Do nothing more if we're not dead yet
		if(health > 0)
			return;
		
		dead = true;
		collider.enabled = false;
		AudioPlayer.play("entity-splash.wav");
		GameManager.sInstance.spawnParticles(ParticleType.BLOOD_AND_MEAT, 
				bloodAndMeatParticles, x, y);
		GameManager.sInstance.onEntityKilled();
		sprite.anim = deathAnim;
	}
	
	/**
	 * 	Toggle white overlay to ON, will set to OFF after
	 * 	some short time in {@code tick()} method.
	 */
	private void whiteOverLay() {
		hitTick = 0;
		hit = true;
	}
	
	/**
	 * 	Destroys the damage text elements before doing the regular entity
	 * 	destroy method.
	 */
	@Override
	public void destroy() {
		// Need to remove the damage texts first
		for(int i = 0; i < dmgText.size(); i++) {
			GUIText t = dmgText.get(i);
			t.destroy();
		}
		
		super.destroy();
	}
	
	/**
	 * 	Adds a new text element into the list
	 * 
	 * 	@param damage Amount of damage (the text which will show)
	 */
	protected void addDamageText(int damage) {
		dmgText.add(new GUIText(
				String.valueOf(damage), 
				this.x + 8, 
				this.y, 
				"level-gui"));
	}

	/**
	 * 	Makes the text go upwards and when the text has taveled enough distance
	 * 	we remove the text element from the list.
	 */
	protected void updateDamageText() {
		for (int i = 0; i < dmgText.size(); i++) {
			if(dmgText.get(i).y < this.y - 15) {
				dmgText.get(i).destroy();
				dmgText.remove(i);
				i--;
			}
			else
				dmgText.get(i).y -= 0.75;
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
