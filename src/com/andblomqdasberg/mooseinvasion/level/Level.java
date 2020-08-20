package com.andblomqdasberg.mooseinvasion.level;

import java.awt.Color;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.decoration.AbstractDecoration;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.player.Player;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.particle.AbstractParticle;
import com.andblomqdasberg.mooseinvasion.particle.AmmoParticle;
import com.andblomqdasberg.mooseinvasion.particle.BeerGlassParticle;
import com.andblomqdasberg.mooseinvasion.particle.BeerParticle;
import com.andblomqdasberg.mooseinvasion.particle.BloodAndMeatParticle;
import com.andblomqdasberg.mooseinvasion.particle.BloodParticle;
import com.andblomqdasberg.mooseinvasion.particle.DashParticle;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;

/**
 *  Main game progression object.
 *  
 *  A level takes the data from the level.json file and spawns the spcified
 *  mobs. It also renders the ground.
 *  
 * 	@author Anders Blomqvist
 */
public class Level {
 
	// Level logic
	private LevelData data;			// Data from .json file for this level
	private LevelStage stage;		// Reference to level stage object
	private LevelWave currentWave; 	// Reference to current wave object
	private int currentStage;		// Track current stage number
	private int currentWaveId;		// Tack current wave id number
	
	// Handle states
	private boolean playing;		// Are we playing or not?
	private boolean completed;		// Are we finished with stage?
	
	// Entity spawning
	private int spawnInterval;		// Number of ticks between each spawn
	private int entitiesKilled;		// Track how many entities which have died
	private int totalEntities;		// Track number of total entities in this wave
	private int progress;			// 100 * entitiesKilled / totalEntities (percentage)
	private int spawnsLeft;			// Number of entities left to spawn
	private int ticksSinceLastSpawn;// Counter for tracking time since last spawn
	
	// GUI
	private GUIText readyText;		// Ready info text
	private GUIText progressText;	// Wave progress text
	private GUIText waveText;		// Red text showing wave finished
	private GUIText completedText;	// Yellow text showing stage finished
	private GUIImage leftArrow;		// Indicator for next stage
	private GUIImage rightArrow;	// Indicator for prev stage
	
	// References
	private Player player;			// Required for walking between stages
	
	private int ticks;
	
	public Level(LevelData data) {
		this.data = data;
		
		ticksSinceLastSpawn = 0;
		currentStage = 1;
		stage = data.getStage(currentStage);
		playing = false;
		completed = false;
		
		currentWaveId = 0;
		player = GameManager.sInstance.getPlayer();
		
		readyText = new GUIText("Hit [r] to start wave",
				MooseInvasion.WIDTH/4,
				MooseInvasion.HEIGHT/4, "level-gui");
		
		progressText = new GUIText("W1:0%",
				MooseInvasion.SPRITE_X_SIZE/2,
				MooseInvasion.SPRITE_Y_SIZE*14.75f, "level-gui");
		progressText.enable(false);
		
		waveText = new GUIText("Wave completed!",
				MooseInvasion.WIDTH/3,
				MooseInvasion.HEIGHT/4, "level-gui");
		waveText.setColor(Color.RED);
		waveText.enable(false);
		
		completedText = new GUIText("Stage completed!",
				MooseInvasion.WIDTH/3,
				MooseInvasion.HEIGHT/4, "level-gui");
		completedText.setColor(Color.YELLOW);
		completedText.enable(false);
		
		leftArrow = new GUIImage(
				10,
				MooseInvasion.HEIGHT/2, Assets.sInstance.sprites[4][2], 
				"level-gui");
		leftArrow.enable(false);
		rightArrow = new GUIImage(
				MooseInvasion.WIDTH-16, 
				MooseInvasion.HEIGHT/2, Assets.sInstance.sprites[4][3], 
				"level-gui");
	}
	
	/**
	 * 	Level progressing. Spawns the entities from each wave
	 * 
	 * 	@param ticks
	 */
	public void tick(int ticks) {
		
		this.ticks = ticks;
		
		// Require ready button for start wave
		if(!playing) {
			if(!completed) {
				// We aren't playing and not finished with stage - then we want 
				// to start next wave.
				if(InputHandler.nextWave())
					startWave();
				
				if(waveText.fadeOut(ticks, 4))
					readyText.enable(true);
			}
			checkStageChange();
		}
		
		// Entity spawning
		if(ticksSinceLastSpawn > spawnInterval) {
			ticksSinceLastSpawn = 0;
			if(spawnsLeft > 0) {
				GameManager.sInstance.spawnEntity(currentWave.spawnEntity());
				spawnsLeft--;
				System.out.println("Spawned an entity! Left: " + spawnsLeft);
			}
		}
		
		for(int i = 0; i < stage.particles.size(); i++) {
			AbstractParticle p = stage.particles.get(i);
			p.tick(ticks);
			p.animationTick();
		}
		
		ticksSinceLastSpawn++;
	}
	
	/**
	 * 	Render the ground tiles for current stage
	 * 
	 * 	@param g Graphic reference
	 */
	public void render(Graphics g) {
		g.drawImage(Assets.sInstance
				.getStageImage(stage.getBackground()),
				0,
				0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
		
		for(int i = 0; i < stage.particles.size(); i++) {
			AbstractParticle p = stage.particles.get(i);
			if(p.time > p.lifeTime)
				stage.particles.remove(i);
			else
				p.render(g, ticks);
		}
		
		for(int i = 0; i < stage.decorations.size(); i++) {
			AbstractDecoration d = stage.decorations.get(i);
			d.render(g, ticks);
		}
		
		for(int i = 0; i < stage.deadEntities.size(); i++) {
			AbstractEntity e = stage.deadEntities.get(i);
			e.render(g);
		}
	}
	
	/**
	 * 	Check if player tries to go into different zones, a stage or city
	 */
	private void checkStageChange() {
		// When at the first stage we can enter the city
		if(currentStage == 1) {
			if(player.x == MooseInvasion.WIDTH-16) {
				player.x = 0;
				GameManager.sInstance.enterCity();
			} else {
				if(player.x == 0 && completed) {
					player.x = MooseInvasion.WIDTH-20;
					nextStage();
				}
			}
		}
		// Otherwise, we just go to a earlier stage
		else {
			if(player.x == 0 && completed) {
				player.x = MooseInvasion.WIDTH-20;
				nextStage();
			} else if(player.x == MooseInvasion.WIDTH-16) {
				player.x = 4;
				prevStage();
			}
		}
	}
	
	/**
	 * 	When player starts the wave TODO add sounds and replace text
	 */
	private void startWave() {
		currentWaveId++;
		stage = data.getStage(currentStage);
		currentWave = stage.getWave("wave" + currentWaveId);
		
		readyText.enable(false);
		progressText.enable(true);
		spawnInterval = currentWave.getSpawnRate();
		spawnsLeft = currentWave.entities.size();
		totalEntities = spawnsLeft;
		entitiesKilled = 0;
		playing = true;
		rightArrow.enable(false);
		updateProgress();
		
		// In case the player starts next wave before text has faded out
		waveText.enable(false);
		waveText.resetColor();
		
		System.out.println("Started wave: " + currentWave.id
			+ " in stage " + currentStage);
		System.out.println("Duration is: " + currentWave.duration
			+ "s. With a tick spawnrate at: " + spawnInterval);
	}
	
	/**
	 * 	TODO what happens when we finish the level??
	 */
	private void stageCompleted() {
		System.out.println("Stage completed: " + currentWave.id + "/" 	
			+ stage.getWaves() + " waves");
		
		// Open up for entering next stage if there is one.
		if(data.getStages() == currentStage) {
			// Level is done. All stages completed.
			System.out.println("Level finished :D");
		} else {
			// Progress to next stage
			completed = true;
			currentWaveId = 0;
			stage.setCompleted();
			completedText.text = "Stage completed!";
			completedText.setColor(Color.YELLOW);
			progressText.enable(false);
			completedText.enable(true);
			leftArrow.enable(true);
			rightArrow.enable(true);
		}
	}
	
	/**
	 * 	When player goes into next stage
	 */
	private void nextStage() {
		currentStage++;
		rightArrow.enable(true);
		stage = data.getStage(currentStage);
		completed = stage.isCompleted(); 
		
		if(!completed) {
			leftArrow.enable(false);
			readyText.enable(true);
			completedText.enable(false);
		}
	}
	
	/**
	 *	When player goes into prev stage
	 */
	private void prevStage() {
		currentStage--;
		stage = data.getStage(currentStage);
		completed = true;
		leftArrow.enable(true);
		completedText.enable(true);
		readyText.enable(false);
	}
	
	/**
	 * 	Setup for next wave.
	 */
	private void nextWave() {
		System.out.println("Wave progress: " + currentWave.id + "/" 
			+ stage.getWaves() + " waves");
		rightArrow.enable(true);
		waveText.enable(true);
	}
	
	/**
	 * 	Update progress.
	 * 	Here we also check if stage has ended or if we should keep going
	 */
	private void updateProgress() {
		progress = 100*entitiesKilled/totalEntities;
		progressText.text = "W" + currentWave.id + ":" + progress + "%";
		
		// Wave completed (warning, might bug when player dies on the last moose).
		// TODO add sound
		if(progress == 100) {
			playing = false;
			if(stage.getWaves() == currentWave.id)
				stageCompleted();
			else
				nextWave();
		}
	}
	
	/**
	 * 	TODO Rework with timed multiplier just like boxhead.
	 * 	Called when a entity dies so we can track wave progress
	 */
	public void onEntityKilled() {
		GameManager.sInstance.addPlayerScore(currentWave.id * 10);
		entitiesKilled++;
		updateProgress();
	}

	/**
	 * 	Adds a decoration object to LevelStage
	 * 
	 * 	@param type What type of decoration
	 * 	@param x world spawn position
	 * 	@param y world spawn position
	 */
	public void spawnDecoration(DecorationType type, float x, float y) {
		stage.spawnDecoration(type, x, y);
	}

	/**
     * 	Spawns particles
     * 
     * 	@param type What type of particle will we spawn
     * 	@param amount How many particle objects
     * 	@param x
     *  @param y
     */
	public void spawnParticle(ParticleType type, int amount, float x, float y) {
		switch(type) {
    		case BLOOD:
    			for(int i = 0; i < amount; i++)
    	    		stage.particles.add(new BloodParticle(x, y));
    			break;
    		case BLOOD_AND_MEAT:
    			for(int i = 0; i < amount; i++)
    				stage.particles.add(new BloodAndMeatParticle(x, y));
    			break;
    		case AMMO:	
    			stage.particles.add(new AmmoParticle(x, y));
    			break;
    		case BEER_GLASS:
    			stage.particles.add(new BeerGlassParticle(x, y));
    			break;
    		case BEER:
    			for(int i = 0; i < amount; i++)
    	    		stage.particles.add(new BeerParticle(x, y));
    			break;
    		case DASH:
    			for(int i = 0; i < amount; i++)
    	    		stage.particles.add(new DashParticle(x, y));
    			break;
    		default:
    			break;
    	}
	}
	
	/**
	 * 	Adds a dead entity to this list
	 */
	public void addDeadEntity(AbstractEntity e) {
		if(!e.dead) {
			System.out.println("Tried to add a living entity to dead entities!");
			return;
		}
		
		stage.deadEntities.add(e);
	}
}