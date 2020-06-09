package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import com.andblomqdasberg.mooseinvasion.config.ConfigHandler;
import com.andblomqdasberg.mooseinvasion.util.GameState;

public class ScreenSettings
{
	private int fullscreenButton = 0;
	private int resolutionButton = 1;
	private int volumeButton = 2;
	private int saveButton = 3;
	private int backButton = 4;
	
	private int currentButton = 0;
	private int iconFrame = 0;
	
	private String tempFullscreen = ConfigHandler.configData.getValue("fullscreen");
	private String tempScale = ConfigHandler.configData.getValue("scale");
	private String tempVolume = ConfigHandler.configData.getValue("volume");
	
	private boolean saved = false;
	private Color savedColor = Color.RED;
	private Color savedColorBG = new Color(255, 0, 0, 120);
	
	public void tick(int ticks) {
		
		if(ticks % 6 == 0)
			if(iconFrame == 7)
				iconFrame = 0;
			else
				iconFrame++;
		
		if(saved) {
			if(ticks % 6 == 0) {
				int alpha = savedColor.getAlpha();
				int alphaBg = savedColorBG.getAlpha();
				savedColor = new Color(255, 0, 0, alpha-5);
				if(savedColor.getAlpha() == 0) {
					saved = false;
					savedColor = Color.RED;
				}
				
				if(alphaBg-10 > 0)
					savedColorBG = new Color(255, 0, 0, alphaBg-10);
				else
					savedColorBG = new Color(255, 0, 0, 0);
			}
		}
		
		if(InputHandler.down(true))
			if(currentButton < 4)
				currentButton++;
			else
				currentButton = 0;
		
		if(InputHandler.up(true))
			if(currentButton > 0)
				currentButton--;
			else
				currentButton = 4;
		
		if(InputHandler.enter()) {
			switch(currentButton) {
				case 0:
					// Fullscreen
					if(tempFullscreen == "1")
						tempFullscreen = "0";
					else
						tempFullscreen = "1";
					break;
				case 1:
					// Scale
					int s = Integer.parseInt(tempScale);
					if(s < 5)
						s++;
					else s = 3;
					tempScale = ""+s;
					break;
				case 2:
					// Volume
					int v = Integer.parseInt(tempVolume);
					if(v < 100)
						v += 10;
					else v = 0;
					tempVolume = ""+v;
					break;
				case 3:
					// Save
					try {
						ConfigHandler.configData.setValue("fullscreen", tempFullscreen);
						ConfigHandler.configData.setValue("scale", tempScale);
						ConfigHandler.configData.setValue("volume", tempVolume);
						ConfigHandler.setPropertiesValues();
						saved = true;
						savedColor = Color.RED;
						savedColorBG = new Color(1.0f, 0f, 0f, 0.5f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 4:
					// Back
					GameManager.sInstance.setGameState(GameState.MENU);
					break;
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(Assets.sInstance.settings_screen[0][0],
				0,
				0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
		
		renderButton(g, "Fullscreen: " + tempFullscreen, fullscreenButton, currentButton);
		renderButton(g, "Scale: " + tempScale, resolutionButton, currentButton);
		renderButton(g, "Volume: " + tempVolume, volumeButton, currentButton);
		renderButton(g, "Save", saveButton, currentButton);
		renderButton(g, "Back", backButton, currentButton);
		renderButtonIcon(g, currentButton);
		
		if(saved) {
			g.setFont(Assets.sInstance.pressstart2p.deriveFont(MooseInvasion.RENDER_WIDTH*0.02f));
			g.setColor(savedColor);
			g.drawString("Successfully saved",
					(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH*0.18), 
					(int)(MooseInvasion.RENDER_HEIGHT/2 + MooseInvasion.RENDER_WIDTH*0.05f));
			
			g.setColor(savedColorBG);
			g.fillRect(0, 0, MooseInvasion.RENDER_WIDTH, MooseInvasion.RENDER_HEIGHT);
		}
	}
	
	/**
	 * 	Render button texts and change their color depending on which button is active
	 * 
	 * 	@param g graphics reference
	 * 	@param label button text
	 * 	@param id for button
	 * 	@param currentButton which button we are on right now
	 */
	public void renderButton(Graphics g, String label, int id, int currentButton) {
		g.setFont(Assets.sInstance.pressstart2p.deriveFont(MooseInvasion.RENDER_WIDTH*0.02f));
		
		if(currentButton == id)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);
		
		if(id == saveButton || id == backButton) {
			g.drawString(label, 
					(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH*0.032), 
					(int)(MooseInvasion.RENDER_HEIGHT/2 + MooseInvasion.RENDER_WIDTH*0.05f*id));
		} else {
			g.drawString(label, 
					(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH*0.18), 
					(int)(MooseInvasion.RENDER_HEIGHT/3.1 + MooseInvasion.RENDER_WIDTH*0.05f*id));
		}
	}
	
	public void renderButtonIcon(Graphics g, int currentButton) {
		if(currentButton == saveButton || currentButton == backButton) {
			g.drawImage(Assets.sInstance.sprites[5][iconFrame],
					(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH/10),
					(int)(MooseInvasion.RENDER_HEIGHT/2 +
							currentButton*MooseInvasion.RENDER_WIDTH*0.05f - MooseInvasion.Y_SCALE*12),
					MooseInvasion.SPRITE_SIZE*MooseInvasion.X_SCALE,
					MooseInvasion.SPRITE_SIZE*MooseInvasion.Y_SCALE,
					null);	
		} else {
			g.drawImage(Assets.sInstance.sprites[5][iconFrame],
					(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH/4),
					(int)(MooseInvasion.RENDER_HEIGHT/3.1 + 
							currentButton*MooseInvasion.RENDER_WIDTH*0.05f - MooseInvasion.Y_SCALE*12),
					MooseInvasion.SPRITE_SIZE*MooseInvasion.X_SCALE,
					MooseInvasion.SPRITE_SIZE*MooseInvasion.Y_SCALE,
					null);	
		}
	}
}
