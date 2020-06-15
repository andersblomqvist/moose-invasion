package com.andblomqdasberg.mooseinvasion.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.andblomqdasberg.mooseinvasion.MooseInvasion;

public class GUIImage extends AbstractGUI
{
	public float x, y;
	public BufferedImage img;
	
	public GUIImage(float x, float y, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(img, (int)x*MooseInvasion.X_SCALE, (int)y*MooseInvasion.Y_SCALE, 
                MooseInvasion.SPRITE_X_SIZE*MooseInvasion.X_SCALE,
                MooseInvasion.SPRITE_Y_SIZE*MooseInvasion.Y_SCALE,
                null);
	}
}
