package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.util.GameState;

/**
 * 	Handle start screen title animation and game start.
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class ScreenStart
{
	private Color introFadeColor = new Color(0, 0, 0, 255);
	private int fadeAmount = 10;
	private boolean fadeIn = true;
	private boolean screenSwitcher = true;
    private int waitTickAmount = 0;
    private int textBlinkSpeed = 8;
	
	public void tick(int ticks) {
		
		// Start game when we hit enter
		if(InputHandler.enter())
			GameManager.sInstance.setGameState(GameState.MENU);
		
		// Intro screen animation control
		if(ticks % 5 == 0) {
			if(fadeIn) {
				// Slight delay before animation starts
				waitTickAmount++;
				if(waitTickAmount < 10)
					return;
				
				int alpha = introFadeColor.getAlpha();
				if(alpha - fadeAmount <= 0) {
					introFadeColor = new Color(0, 0, 0, 0);
					waitTickAmount = 0;
					fadeIn = false;
				}
				else
					introFadeColor = new Color(0, 0, 0, alpha-fadeAmount);
			}
			
			else {
				// Animate "hit enter to start" text
				waitTickAmount++;
				if(waitTickAmount < textBlinkSpeed)
					return;
				screenSwitcher = !screenSwitcher;
				waitTickAmount = 0;
			}
		}
	}
	
	public void render(Graphics g) {
		
		if(fadeIn) {
			g.drawImage(Assets.sInstance.intro_screen[0][0],
					0,
					0,
					MooseInvasion.RENDER_WIDTH,
					MooseInvasion.RENDER_HEIGHT,
					null);
			g.setColor(introFadeColor);
			g.fillRect(0, 0, MooseInvasion.RENDER_WIDTH, MooseInvasion.RENDER_HEIGHT);
		}
		
		else {
			if(screenSwitcher) {
				g.drawImage(Assets.sInstance.intro_screen[0][0],
						0,
						0,
						MooseInvasion.RENDER_WIDTH,
						MooseInvasion.RENDER_HEIGHT,
						null);
			} else {
				g.drawImage(Assets.sInstance.intro_screen_start[0][0],
						0,
						0,
						MooseInvasion.RENDER_WIDTH,
						MooseInvasion.RENDER_HEIGHT,
						null);
			}
		}
	}
}
