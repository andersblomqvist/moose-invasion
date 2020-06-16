package com.andblomqdasberg.mooseinvasion.level;

import java.awt.Graphics;

public class Level {

	private LevelData data;
	
	public Level(LevelData data) {
		this.data = data;
	}
	
	public void tick(int ticks) {
		
	}
	
	public void render(Graphics g) {
		
	}
	
	/**
	 * 	@returns the LevelData object
	 */
	public LevelData getLevelData() {
		return this.data;
	}
}
