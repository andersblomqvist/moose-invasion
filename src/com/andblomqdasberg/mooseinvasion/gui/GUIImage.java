package com.andblomqdasberg.mooseinvasion.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;

/**
 * 	Single image GUI
 * 
 * 	@author Anders Blomqvist
 */
public class GUIImage extends AbstractGUI
{
	public float x, y;
	public BufferedImage img;
	
	public GUIImage.IEnabled action;
	
	public interface IEnabled {
		void onEnabled();
	}

	/**
	 * 	Create a new single imge GUI
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param img
	 * 	@param listName "city-gui", "player-gui" or "level-gui"
	 */
	public GUIImage(float x, float y, BufferedImage img, String listName) {
		super(listName);
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	/**
	 * 	Create a new single imge GUI with action
	 * 
	 * 	@param x
	 * 	@param y
	 * 	@param img
	 * 	@param listName "city-gui", "player-gui" or "level-gui"
	 */
	public GUIImage(float x, float y, BufferedImage img, String listName, GUIImage.IEnabled action) {
		super(listName);
		this.x = x;
		this.y = y;
		this.img = img;
		this.action = action;
	}

	@Override
	public void render(Graphics g) {
		if(!enabled)
			return;
		
		g.drawImage(img, (int)x*MooseInvasion.X_SCALE, (int)y*MooseInvasion.Y_SCALE, 
                MooseInvasion.SPRITE_X_SIZE*MooseInvasion.X_SCALE,
                MooseInvasion.SPRITE_Y_SIZE*MooseInvasion.Y_SCALE,
                null);
	}
	
	@Override
	public void enable(boolean state) {
		if(!enabled && state)
			action.onEnabled();
		super.enable(state);
	}
}
