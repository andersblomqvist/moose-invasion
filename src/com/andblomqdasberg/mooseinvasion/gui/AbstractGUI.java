package com.andblomqdasberg.mooseinvasion.gui;

import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.GameManager;

/**
 * 	Base class for a GUI element
 * 
 * 	@author Anders Blomqvist
 */
public abstract class AbstractGUI {
	
	public float x, y;
	
	// Enable / Disable rendering
	protected boolean enabled;
	
	protected String listName;
	
	/**
	 * 	Adds this GUI object into specified list in the Game Manager.
	 * 	
	 * 	Available list names are: "city-gui", "level-gui", "player-gui"
	 * 
	 * 	@param listName Name of the list which this GUI object will be stored
	 * 	in. 
	 */
	public AbstractGUI(String listName) {
		this.listName = listName;
		if(listName.equals("city-gui"))
			GameManager.sInstance.guiCity.add(this);
		else if(listName.equals("level-gui"))
			GameManager.sInstance.guiLevel.add(this);
		else if(listName.equals("player-gui"))
			GameManager.sInstance.guiPlayer.add(this);
		else
			System.out.println("Failed to add GUI into list: " + listName);
		enabled = true;
	}
	
	/**
	 * 	Override this
	 */
	public void render(Graphics G) {}
	
	/**
	 * 	Remove this object from rendering
	 */
	public void destroy() {
		if(listName.equals("city-gui"))
			GameManager.sInstance.guiCity.remove(this);
		else if(listName.equals("level-gui"))
			GameManager.sInstance.guiLevel.remove(this);
		else if(listName.equals("player-gui"))
			GameManager.sInstance.guiPlayer.remove(this);
	}
	
	/**
	 * 	Enable/Disable render
	 */
	public void enable(boolean state) {
		enabled = state;
	}
}