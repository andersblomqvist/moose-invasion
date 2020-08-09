package com.andblomqdasberg.mooseinvasion;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 	Class for handling imports of sprite images.
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Assets
{
	public static Assets sInstance;
	
	public static void init() {
		sInstance = new Assets();
	}
	
	// Sprites
	public BufferedImage[][] sprites = loadAndCut("assets\\chars.png", 16, 16);
	public BufferedImage[][] particles = loadAndCut("assets\\particles.png", 16, 16);
	public BufferedImage[][] decorations = loadAndCut("assets\\decorations.png", 16, 16);
	
	// Menu images
	public BufferedImage intro_screen = loadImage("assets\\intro-screen.png");
	public BufferedImage intro_screen_start = loadImage("assets\\intro-screen-2.png");
	public BufferedImage menu_screen = loadImage("assets\\menu-screen.png");
	public BufferedImage settings_screen = loadImage("assets\\settings-screen.png");

	// GUI
	public BufferedImage gui_shop_background = loadImage("assets\\gui\\gui-shop-background.png");
	public BufferedImage gui_shop_tab = loadImage("assets\\gui\\gui-shop-tab.png");
	
	public BufferedImage city = loadImage("assets\\levels\\city.png");
	public BufferedImage city_top = loadImage("assets\\levels\\city-top.png");
	
	// Level stages images
	public BufferedImage l1s1 = loadImage("assets\\levels\\l1s1.png");
	public BufferedImage l1s2 = loadImage("assets\\levels\\l1s2.png");
	
	// Fonts
	public Font pressstart2p = loadFont("pressstart2p.ttf");
	
	/**
	 * 	Load and cut sprite sheet. No cuts will be made if image size is equal to sw and sh
	 * 
	 * 	@param name filename
	 * 	@param sw individual sprite size width
	 * 	@param sh individual sprite size height
	 * 
	 * 	@returns a buffered image where each sprite can be accessed with x and y cords just
	 * 			 like a 2D array.
	 */
	private BufferedImage[][] loadAndCut(String name, int sw, int sh){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new FileInputStream(name));
		} catch (IOException e) {
			System.out.println("Could not load: " + name);
			e.printStackTrace();
		}
		int xSlice = img.getWidth() / sw;
		int ySlice = img.getHeight() / sh;

		BufferedImage[][] result = new BufferedImage[xSlice][ySlice];

		for(int x = 0; x < xSlice; x++) {
			for(int y = 0; y < ySlice; y++) {
				result[y][x] = img.getSubimage(x*sw, y*sh, sw, sh);
			}
		}
		return result;
	}
	
	/**
	 * 	Load and create a Font from file
	 * 
	 * 	@param name filename
	 * 
	 * 	@returns a new Font object with loaded font
	 */
	private Font loadFont(String name) {
		File file = new File("assets\\fonts\\" + name);
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not load font: " + name);
			e.printStackTrace();
		}
		return font;
	}

	/**
	 * 	Directly load an image
	 * 
	 * 	@param file path to image
	 * 	@returns a buffered image object
	 */
	public BufferedImage loadImage(String file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new FileInputStream(file));
		} catch (IOException e) {
			System.out.println("Could not load: " + file);
			e.printStackTrace();
		}
		
		return img;
	}

	/**
	 * 	Returns the ground image for a stage
	 * 
	 * 	@param background File name as in level.json
	 * 	@returns Buffered Image
	 */
	public BufferedImage getStageImage(String background) {
		switch(background) {
			case "l1s1.png":
				return l1s1;
			case "l1s2.png":
				return l1s2;
			default:
				return l1s1;
		}
	}
}
