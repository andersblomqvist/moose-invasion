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
public class GUIText extends AbstractGUI implements IFadeOut {
	
	public String text;
	public TextStyle style;
	
	/**
	 * 	Class for storing data about TextStyling
	 */
	public class TextStyle {
		public Font font;
		public Color color;
		private Color shadow;
		private Color savedColor;
		private int shadowLength;
		
		public TextStyle() {}
		
		public TextStyle(Font font, Color color) {
			this.font = font;
			this.color = color;
			this.savedColor = color;
			this.shadow = Color.BLACK;
			this.shadowLength = getShadowLength();
		}
		
		/**
		 * 	Different shadow lengths depending on resolution
		 * 
		 * 	@returns the amount of pixels which the shadow will be offset by
		 */
		private int getShadowLength() {
			if(MooseInvasion.FULLSCREEN)
				return 5;
			return MooseInvasion.X_SCALE;
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
	public GUIText(float x, float y, String listName) {
		super(listName);
		this.x = x;
		this.y = y;
		this.text = "";
		style = new TextStyle().getDefaultStyle();
	}
	
	/**
	 * 	Constructor with specified string
	 */
	public GUIText(String string, float x, float y, String listName) {
		this(x, y, listName);
		this.text = string;
	}
	
	/**
	 * 	Constructor with specified style and string
	 */
	public GUIText(String string, float x, float y, int style, String listName) {
		this(x, y, listName);
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
	
	/**
	 * 	Fade out funtion. Call this via a tick() method.
	 */
	@Override
	public boolean fadeOut(int ticks, int speed) {
		if(ticks % speed == 0) {
			int a1 = style.color.getAlpha();
			int a2 = style.shadow.getAlpha();
			if(a1 - 5 >= 0) {
				style.color = new Color(
						style.color.getRed(), 
						style.color.getGreen(),
						style.color.getBlue(), 
						a1 - 5);
			} else {
				enable(false);
				style.color = style.savedColor;
				style.shadow = Color.BLACK;
				return true;
			}
			
			if(a2 - 5 >= 0)
				style.shadow = new Color(0, 0, 0, a2 - 5);
			else {
				enable(false);
				style.color = style.savedColor;
				style.shadow = Color.BLACK;
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void render(Graphics g) {
		if(!enabled)
			return;
		
		g.setFont(style.font);
		
		// Drop shadow
		g.setColor(style.shadow);
		g.drawString(this.text, 
				(int)x*MooseInvasion.X_SCALE + style.shadowLength, 
				(int)y*MooseInvasion.Y_SCALE + style.shadowLength);
		
		g.setColor(style.color);
		g.drawString(this.text, 
				(int) x*MooseInvasion.X_SCALE, 
				(int) (y*MooseInvasion.Y_SCALE));
	}
	
	public void resetColor() {
		style.color = style.savedColor;
		style.shadow = Color.BLACK;
	}
	
	public void setColor(Color color) {
		this.style.color = color;
		this.style.savedColor = color;
	}
	
	public TextStyle getTitleStyle() {
		return this.style.getTitleStyle();
	}
	
	public TextStyle getSmallStyle() {
		return this.style.getSmallStyle();
	}
}