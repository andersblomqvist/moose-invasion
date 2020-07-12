package com.andblomqdasberg.mooseinvasion.level;

import java.util.ArrayList;

/**
 * 	Data struct for a level.
 * 
 * 	A level consists of multiple stages - where a stages contains its own
 * 	waves which spawn a set amount of mobs.
 * 
 * 	@author Anders Blomqvist
 */
public class LevelData {

	private ArrayList<LevelStage> stages = new ArrayList<LevelStage>();
	
	public LevelData() {}
	
	public void addStage(LevelStage stage) {
		stages.add(stage);
	}
	
	/**
	 * 	Returns the Level Stage object at index (not starting with 0).
	 * 
	 * 	@param index of the stage starting with 1
	 * 	@returns the Level Stage object
	 */
	public LevelStage getStage(int index) {
		if(index-1 >= stages.size())
			return null;
		
		return stages.get(index - 1);
	}

	/**
	 * 	@returns the number of stages for this level
	 */
	public int getStages() {
		return stages.size();
	}
}