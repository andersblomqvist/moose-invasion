package com.andblomqdasberg.mooseinvasion.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.andblomqdasberg.mooseinvasion.decoration.AbstractDecoration;
import com.andblomqdasberg.mooseinvasion.decoration.BloodPoolDecoration;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.EntityList;
import com.andblomqdasberg.mooseinvasion.particle.AbstractParticle;

/**
 * 	Handle information about a stage in a level.
 * 
 * 	A stage contains information about which ground to render
 * 	and a list with all the waves for the stage.
 * 
 * 	@author Anders Blomqvist
 */
public class LevelStage {
	
	/**
	 * 	Background image for the ground
	 */
	private String background;
	
	/**
	 * 	List with all the waves in for this stage
	 */
	private Map<String, LevelWave> waves = new HashMap<String, LevelWave>();
	
	/**
	 * 	List with render decals (blood, particles etc)
	 */
	public ArrayList<AbstractDecoration> decorations = 
			new ArrayList<AbstractDecoration>();
	
	/**
	 * 	List with entities which have died but we still want to render the
	 * 	last dead frame.
	 */
	public ArrayList<AbstractEntity> deadEntities =
			new ArrayList<AbstractEntity>();
	
	/**
	 * 	Particles
	 */
	public ArrayList<AbstractParticle> particles = 
			new ArrayList<AbstractParticle>();
	
	/**
	 * 	True if this stages is done, otherwise false
	 */
	private boolean completed;
	
	/**
	 * 	Creates a new stage in a level. A stage has waves which holds
	 * 	what entities to spawn and how many of them. The stage is also
	 * 	connected with a background image which is the rendered ground.
	 * 	
	 * 	@param background Filename of the ground .png asset
	 */
	public LevelStage(String background) {
		this.background = background;
	}
	
	/**
	 * 	@returns the background .png asset for this stage
	 */
	public String getBackground() {
		return this.background;
	}
	
	/**
	 * 	Adds a key to the map with an empty wave object.
	 
	 * 	@param wave Unique wave key
	 * 	@param id Wave id
	 */
	public void addEmptyWave(String wave, int id) {
		waves.put(wave, new LevelWave(id));
	}
	
	/**
	 * 	Adds entities to the wave.
	 * 
	 * 	@param entity String of the entity name: "moose"
	 * 	@param count How many entities
	 * 	@param wave Which wave to add to
	 */
	public void addEntityToWave(String entity, int count, String wave) {
		ArrayList<AbstractEntity> e = new ArrayList<AbstractEntity>();
		for(int i = 0; i < count; i++)
			e.add(EntityList.getEntity(entity));
		waves.get(wave).entities.addAll(e);
	}
	
	/**
	 * 	Set time duration for specific wave
	 * 
	 * 	@param wave id
	 * 	@param duration time
	 */
	public void setWaveDuration(String wave, int duration) {
		waves.get(wave).duration = duration;
	}
	
	/**
	 * 	@returns the number of waves for this stage
	 */
	public int getWaves() {
		return waves.size();
	}
	
	/**
	 * 	Get entities for a wave
	 * 
	 * 	@param wave Wave id
	 * 	@returns an array list with all the entities for a specific wave
	 */
	public ArrayList<AbstractEntity> getEntities(String wave) {
		return waves.get(wave).entities;
	}

	/**
	 * 	Returns a reference to the LevelWave object
	 * 
	 * 	@param id wave id
	 * 	@returns the LevelWave object
	 */
	public LevelWave getWave(String wave) {
		return waves.get(wave);
	}
	
	/**
	 * 	@returns true if this stages is completed, otherwise false
	 */
	public boolean isCompleted() {
		return completed;
	}
	
	/**
	 * 	Set this stages as completed
	 */
	public void setCompleted() {
		completed = true;
	}

	/**
	 * 	Here we acrually create the decoration object
	 * 
	 * 	@param type What type of decoration
	 * 	@param x world spawn position
	 * 	@param y world spawn position
	 */
	public void spawnDecoration(DecorationType type, float x, float y) {
		switch(type) {
		case BLOOD_POOL:
			decorations.add(new BloodPoolDecoration(x, y));
			break;
		}
	}
}