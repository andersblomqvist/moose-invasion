package com.andblomqdasberg.mooseinvasion.level;

import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.entity.Entity;

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
	
	public LevelStage getStage(int index) {
		return stages.get(index);
	}
	
	@Override
	public String toString() {
		return stages.toString();
	}
}