package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 	Handle start screen title animation and game start.
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class StartScreen
{
	private Color introFadeColor = new Color(0, 0, 0, 255);
	private int fadeAmount = 10;
	private boolean fadeIn = true;
	private boolean screenSwitcher = true;
    private int waitTickAmount = 0;
    private int textBlinkSpeed = 8;
	
	public void tick(int ticks) {
		if(InputHandler.enter())
			GameManager.sInstance.startGame();
		
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
					320*MooseInvasion.SCALE,
					240*MooseInvasion.SCALE,
					null);
			g.setColor(introFadeColor);
			g.fillRect(0, 0, 320*MooseInvasion.SCALE, 240*MooseInvasion.SCALE);
		}
		
		else {
			if(screenSwitcher) {
				g.drawImage(Assets.sInstance.intro_screen[0][0],
						0,
						0,
						320*MooseInvasion.SCALE,
						240*MooseInvasion.SCALE,
						null);
			} else {
				g.drawImage(Assets.sInstance.intro_screen_start[0][0],
						0,
						0,
						320*MooseInvasion.SCALE,
						240*MooseInvasion.SCALE,
						null);
			}
		}
	}
}
