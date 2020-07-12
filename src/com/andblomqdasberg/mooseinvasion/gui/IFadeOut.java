package com.andblomqdasberg.mooseinvasion.gui;

/**
 * 	Interface for fading out alpha value for GUI
 * 
 * 	@author Anders Blomqvist
 */
public interface IFadeOut {
	
	/**
	 * 	Method for fading out alpha channel in color.
	 * 
	 * 	@param ticks Game tick, used for animation
	 * 	@param rate How fast the fade out will be
	 * 
	 * 	@return true when done.
	 */
	public boolean fadeOut(int ticks, int rate);
}
