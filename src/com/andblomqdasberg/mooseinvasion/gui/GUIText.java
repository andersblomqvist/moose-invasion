package com.andblomqdasberg.mooseinvasion.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;

/**
 * 	GUI class for handling text elements on screen such as wave progress,
 * 	ammo and other text elements. 
 * 
 * 	This class adds itself to the GameManager guiText list.
 * 
 * 	@author Anders Blomqvist
 */
public class GUIText {
	
	public String text;
	public TextStyle style;
	
	public float x, y;
	
	/**
	 * 	Class for storing data about TextStyling
	 */
	public class TextStyle {
		public Font font;
		public Color color;
		
		public TextStyle() {}
		
		public TextStyle(Font font, Color color) {
			this.font = font;
			this.color = color;
		}
		
		/**
		 * 	@returns a TextStyle object with default styling
		 */
		public TextStyle getDefaultStyle() {
			Font font = Assets.sInstance.pressstart2p.deriveFont(32f);
			return new TextStyle(font, Color.WHITE);
		}
		
		/**
		 * 	@returns a TextStyle with bigger font
		 */
		public TextStyle getTitleStyle() {
			Font font = Assets.sInstance.pressstart2p.deriveFont(48f);
			return new TextStyle(font, Color.WHITE);
		}
		
		/**
		 * 	@returns a TextStyle with smaller font
		 */
		public TextStyle getSmallStyle() {
			Font font = Assets.sInstance.pressstart2p.deriveFont(24f);
			return new TextStyle(font, Color.WHITE);
		}
	}
	
	/**
	 * 	Default GUI constructor
	 */
	public GUIText(float x, float y) {
		this.x = x;
		this.y = y;
		this.text = "";
		style = new TextStyle().getDefaultStyle();
		
		// Add ourselves to the game manager gui text list for rendering.
		GameManager.sInstance.guiText.add(this);
	}
	
	/**
	 * 	Constructor with specified string
	 */
	public GUIText(String string, float x, float y) {
		this(x, y);
		this.text = string;
	}
	
	/**
	 * 	Constructor with specified style and string
	 */
	public GUIText(String string, float x, float y, int style) {
		this(x, y);
		this.text = string;
		switch(style) {
		case 0:
			this.style = this.style.getDefaultStyle();
			break;
		case 1:
			this.style = this.style.getTitleStyle();
			break;
		case 2:
			this.style = this.style.getSmallStyle();
			break;
		}
	}
	
	public void render(Graphics g) {
		g.setFont(style.font);
		
		// Drop shadow
		g.setColor(Color.BLACK);
		g.drawString(this.text, (int)x*MooseInvasion.SCALE + 4, (int)y*MooseInvasion.SCALE + 4);
		
		g.setColor(style.color);
		g.drawString(this.text, (int) x*MooseInvasion.SCALE, (int) (y*MooseInvasion.SCALE));
	}

	public void removeFromRender() {
		GameManager.sInstance.guiText.remove(this);
	}
	
	public void setColor(Color color) {
		this.style.color = color;
	}
	
	public TextStyle getTitleStyle() {
		return this.style.getTitleStyle();
	}
	
	public TextStyle getSmallStyle() {
		return this.style.getSmallStyle();
	}
}