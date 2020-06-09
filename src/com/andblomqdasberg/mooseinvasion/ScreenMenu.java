package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.util.GameState;

/**
 * 	Menu screen where settings can be changed and game start
 * 
 * 	@author Anders Blomqvist
 */
public class ScreenMenu 
{
	private int startButton = 0;
	private int settingsButton = 1;
	private int quitButton = 2;
	
	private int currentButton = 0;
	private int iconFrame = 0;
	
	public void tick(int ticks) {
		
		if(ticks % 6 == 0)
			if(iconFrame == 7)
				iconFrame = 0;
			else
				iconFrame++;
		
		if(InputHandler.down(true))
			if(currentButton < 2)
				currentButton++;
			else
				currentButton = 0;
		
		if(InputHandler.up(true))
			if(currentButton > 0)
				currentButton--;
			else
				currentButton = 2;
		
		if(InputHandler.enter()) {
			switch(currentButton) {
				case 0:
					GameManager.sInstance.setGameState(GameState.GAME);
					break;
				case 1:
					GameManager.sInstance.setGameState(GameState.SETTINGS);
					break;
				case 2:
					System.exit(0);
					break;
			}
		}
	}
	
	public void render(Graphics g) {
		// Draw background
		g.drawImage(Assets.sInstance.menu_screen[0][0],
				0,
				0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
		
		renderButton(g, "Start game", startButton, currentButton);
		renderButton(g, "Settings", settingsButton, currentButton);
		renderButton(g, "Quit", quitButton, currentButton);
		renderButtonIcon(g, currentButton);
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
		
		g.drawString(label, 
				(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH*0.1), 
				(int)(MooseInvasion.RENDER_HEIGHT/2 + MooseInvasion.RENDER_WIDTH*0.05f*id));
	}
	
	public void renderButtonIcon(Graphics g, int currentButton) {
		g.drawImage(Assets.sInstance.sprites[5][iconFrame],
				(int)(MooseInvasion.RENDER_WIDTH/2 - MooseInvasion.RENDER_WIDTH/6.5),
				(int)(MooseInvasion.RENDER_HEIGHT/2 + currentButton*MooseInvasion.RENDER_WIDTH*0.05f - MooseInvasion.Y_SCALE*12),
				MooseInvasion.SPRITE_SIZE*MooseInvasion.X_SCALE,
				MooseInvasion.SPRITE_SIZE*MooseInvasion.Y_SCALE,
				null);
	}
}