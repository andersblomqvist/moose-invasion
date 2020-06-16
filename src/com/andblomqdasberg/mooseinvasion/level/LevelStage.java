package com.andblomqdasberg.mooseinvasion.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.andblomqdasberg.mooseinvasion.entity.Entity;
import com.andblomqdasberg.mooseinvasion.entity.EntityList;

/**
 * 	Handle information about a stage in a level.
 * 
 * 	@author Anders Blomqvist
 */
public class LevelStage {
	
	private String background;
	private Map<String, ArrayList<Entity>> waves = 
			new HashMap<String, ArrayList<Entity>>();
	
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
	 * 	Adds a key to the map with an empty array of entities
	 * 
	 * 	@param wave Unique wave key
	 */
	public void addEmptyWave(String wave) {
		waves.put(wave, new ArrayList<Entity>());
	}
	
	/**
	 * 	Adds entities to the wave.
	 * 
	 * 	@param entity String of the entity name: "moose"
	 * 	@param count How many entities
	 * 	@param wave Which wave to add to
	 */
	public void addEntityToWave(String entity, int count, String wave) {
		ArrayList<Entity> e = new ArrayList<Entity>();
		for(int i = 0; i < count; i++)
			e.add(EntityList.getEntity(entity));
		waves.get(wave).addAll(e);
	}
}
