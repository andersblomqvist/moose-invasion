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
	
	public BufferedImage[][] sprites = loadAndCut("assets\\chars.png", 16, 16);
	public BufferedImage[][] particles = loadAndCut("assets\\particles.png", 16, 16);
	
	public BufferedImage[][] intro_screen = loadAndCut("assets\\intro-screen.png", 320, 240);
	public BufferedImage[][] intro_screen_start = loadAndCut("assets\\intro-screen-2.png", 320, 240);
	public BufferedImage[][] menu_screen = loadAndCut("assets\\menu-screen.png", 320, 240);

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
}
