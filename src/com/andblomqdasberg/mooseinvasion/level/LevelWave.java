package com.andblomqdasberg.mooseinvasion.level;

import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.entity.Entity;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;

/**
 * 	Wave level object.
 * 	
 * 	@id Wave number
 * 	@duration How many ticks this wave will last
 * 	@entities Which entities will be spawned
 * 
 * 	@author Anders Blomqvist
 */
public class LevelWave {
	public int id;
	public int duration;
	public ArrayList<Entity> entities;
	
	public LevelWave(int id) {
		this.id = id;
		this.duration = 0;
		this.entities = new ArrayList<Entity>();
	}

	/**
	 * 	Used for spawning entities from wave entity list. We pick a random
	 * 	entity from list and return it, we also remove it from list so we 
	 * 	don't pick multiples.
	 * 
	 * 	@return reference to what type of entity to spawn
	 */
	public Entity spawnEntity() {
		int randomIndex = GameRandom.nextInt(entities.size());
		Entity e = entities.get(randomIndex);
		entities.remove(randomIndex);
		return e;
	}

	/**
	 * 	How many ticks are between the spawns.
	 * 
	 * 	@returns the spawn tick interval. 
	 */
	public int getSpawnRate() {
		return duration*60 / entities.size();
	}
}