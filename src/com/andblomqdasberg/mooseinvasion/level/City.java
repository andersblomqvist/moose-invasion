package com.andblomqdasberg.mooseinvasion.level;

import java.awt.Graphics;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.entity.Player;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;

/**
 * 	The City where the player can buy upgrades
 * 
 * 	@author Anders
 */
public class City
{
	private Player player;

	public City() {
		player = GameManager.sInstance.getPlayer();
		
		new GUIImage(10, MooseInvasion.HEIGHT/2, Assets.sInstance.sprites[4][2],
				"city-gui");
	}
	
	public void tick(int ticks) {
		if(player.x == 0) {
			player.x = MooseInvasion.WIDTH-20;
			GameManager.sInstance.leaveCity();
		}
	}
	
	/**
	 * 	Render city background image
	 */
	public void render(Graphics g) {
		g.drawImage(Assets.sInstance.city,
				0,
				0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
		
        // Render GUI elements
        for(int i = 0; i < GameManager.sInstance.guiCity.size(); i++)
        	GameManager.sInstance.guiCity.get(i).render(g);
	}

	public void renderTop(Graphics g) {
		g.drawImage(Assets.sInstance.city_top,
				0,
				0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
	}
}
