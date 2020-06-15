package com.andblomqdasberg.mooseinvasion.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;

/**
 * 	GUI class for handling text elements on screen such as wave progress,
 * 	ammo and other text elements. 
 * 
 * 	This class adds itself to the GameManager guiText list.
 * 
 * 	@author Anders Blomqvist
 */
public class GUIText extends AbstractGUI {
	
	public String text;
	public TextStyle style;
	
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
			Font font = Assets.sInstance.pressstart2p.deriveFont(MooseInvasion.FONT_SIZE);
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
		
		public void setStyle(float fontSize, Color color) {
			Font font = Assets.sInstance.pressstart2p.deriveFont(fontSize);
			this.font = font;
			this.color = color;
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
	
	@Override
	public void render(Graphics g) {
		g.setFont(style.font);
		
		// Drop shadow
		g.setColor(Color.BLACK);
		g.drawString(this.text, (int)x*MooseInvasion.X_SCALE + 4, (int)y*MooseInvasion.Y_SCALE + 4);
		
		g.setColor(style.color);
		g.drawString(this.text, (int) x*MooseInvasion.X_SCALE, (int) (y*MooseInvasion.Y_SCALE));
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