package com.andblomqdasberg.mooseinvasion.level;

import java.awt.Graphics;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.entity.Player;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;

/**
 * 	The City where the player can buy upgrades
 * 
 * 	@author Anders
 */
public class City
{
	// Reference to player
	private Player player;

    // Colliders
    public ArrayList<BoxCollider> colliders = new ArrayList<BoxCollider>();
	private CollisionType type;
    
	public City() {
		player = GameManager.sInstance.getPlayer();
		
		// The right pointing arrow
		new GUIImage(
				10, 
				MooseInvasion.HEIGHT/2, 
				Assets.sInstance.sprites[4][2],
				"city-gui");
		
		colliders.add(new BoxCollider(83, 99, 21, 4));		// Ammo boxes
		colliders.add(new BoxCollider(104, 91, 38, 7));		// Gun shop
		colliders.add(new BoxCollider(160, 71, 25, 6));		// Gun house
		colliders.add(new BoxCollider(206, 0, 20, 76));		// Trees 1
		colliders.add(new BoxCollider(226, 0, 12, 68));		// Trees 2
		colliders.add(new BoxCollider(258, 39, 12, 7));		// Well
		colliders.add(new BoxCollider(295, 0, 25, 57));		// Trees 3
		colliders.add(new BoxCollider(273, 57, 47, 15));	// Trees 4
		colliders.add(new BoxCollider(267, 72, 53, 14));	// Trees 5
		colliders.add(new BoxCollider(246, 86, 74, 85));	// Trees 6
		colliders.add(new BoxCollider(206, 113, 25, 6));	// Market house
		colliders.add(new BoxCollider(176, 113, 25, 6));	// Market shop
		colliders.add(new BoxCollider(105, 151, 37, 9));	// BS shop
		colliders.add(new BoxCollider(142, 158, 38, 3));	// Logs
		colliders.add(new BoxCollider(181, 154, 33, 6));	// BS house
		colliders.add(new BoxCollider(183, 194, 58, 28));	// Lake
	}
	
	/**
	 * 	Check collisions
	 * 	
	 * 	@param ticks
	 */
	public void tick(int ticks) {
		if(player.x == 0) {
			player.x = MooseInvasion.WIDTH-20;
			GameManager.sInstance.leaveCity();
		}
		
		// Check collisions with player
		for(BoxCollider c : colliders) {
			type = c.AABBCollision(player);
			if(c.colliding) {
				player.onCollisionEnter(type);
			}
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
		 
		/**
         * 	Debug render
        
        for(BoxCollider c : colliders)
        	c.render(g);
    	*/
	}
}
