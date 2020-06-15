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
	
	public AbstractGUI() {
		// Add ourselves to the game manager gui text list for rendering.
		GameManager.sInstance.gui.add(this);
	}
	
	/**
	 * 	Override this
	 */
	public void render(Graphics G) {}
	
	/**
	 * 	Remove this object from rendering
	 */
	public void destroy() {
		GameManager.sInstance.gui.remove(this);
	}
}