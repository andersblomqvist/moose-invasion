package com.andblomqdasberg.mooseinvasion.level;

import java.awt.Graphics;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.collider.BoxCollider;
import com.andblomqdasberg.mooseinvasion.collider.CollisionType;
import com.andblomqdasberg.mooseinvasion.entity.Player;
import com.andblomqdasberg.mooseinvasion.gui.GUIImage;
import com.andblomqdasberg.mooseinvasion.gui.shop.GunStore;

/**
 * 	The City where the player can buy upgrades
 * 
 * 	@author Anders Blomqvist
 */
public class City
{
	// Reference to player
	private Player player;

    // Colliders
    public ArrayList<BoxCollider> colliders = new ArrayList<BoxCollider>();
	private CollisionType type;
	
	// GUI
	private GUIImage marketShopTrigger;
	private GUIImage gunsShopTrigger;
	private GUIImage blacksmithShopTrigger;
	private GUIImage dassTrigger;
	
	private GunStore gunStore;
	
	public City() {
		player = GameManager.sInstance.getPlayer();
		
		// Colliders
		colliders.add(new BoxCollider(83, 99, 21, 4));		// Ammo boxes
		colliders.add(new BoxCollider(104, 91, 38, 8));		// Gun shop
		colliders.add(new BoxCollider(160, 71, 25, 6));		// Gun house
		colliders.add(new BoxCollider(206, 0, 20, 76));		// Trees 1
		colliders.add(new BoxCollider(226, 0, 12, 68));		// Trees 2
		colliders.add(new BoxCollider(258, 39, 12, 7));		// Well
		colliders.add(new BoxCollider(295, 0, 25, 57));		// Trees 3
		colliders.add(new BoxCollider(273, 57, 47, 15));	// Trees 4
		colliders.add(new BoxCollider(267, 72, 53, 14));	// Trees 5
		colliders.add(new BoxCollider(246, 86, 74, 75));	// Trees 6
		colliders.add(new BoxCollider(206, 113, 25, 6));	// Market house
		colliders.add(new BoxCollider(176, 113, 25, 6));	// Market shop
		colliders.add(new BoxCollider(105, 148, 37, 13));	// BS shop
		colliders.add(new BoxCollider(142, 158, 38, 3));	// Logs
		colliders.add(new BoxCollider(181, 154, 33, 6));	// BS house
		colliders.add(new BoxCollider(183, 194, 58, 28));	// Lake
		
		// Triggers
		colliders.add(new BoxCollider(179, 121, 20, 8, true, "market"));
		colliders.add(new BoxCollider(109, 99, 28, 10, true, "guns"));
		colliders.add(new BoxCollider(110, 162, 26, 10,true, "blacksmith"));
		colliders.add(new BoxCollider(300, 160, 17, 12,true, "dass"));
		
		// E image icons
		marketShopTrigger = new GUIImage(188, 104, Assets.sInstance.sprites[4][4], "city-gui", () -> {
			AudioPlayer.play("misc-popup.wav");
		});
		gunsShopTrigger = new GUIImage(123, 81, Assets.sInstance.sprites[4][4], "city-gui", () -> {
			AudioPlayer.play("misc-popup.wav");
		});
		blacksmithShopTrigger = new GUIImage(116, 145, Assets.sInstance.sprites[4][4], "city-gui", () -> {
			AudioPlayer.play("misc-popup.wav");
		});
		dassTrigger = new GUIImage(310, 145, Assets.sInstance.sprites[4][4], "city-gui", () -> {
			AudioPlayer.play("misc-popup.wav");
		});
		marketShopTrigger.enable(false);
		gunsShopTrigger.enable(false);
		blacksmithShopTrigger.enable(false);
		dassTrigger.enable(false);
		
		// The right pointing arrow
		new GUIImage(10,MooseInvasion.HEIGHT/2, 
				Assets.sInstance.sprites[4][2],
				"city-gui");
		
		gunStore = new GunStore();
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
			if((type = c.AABBCollision(player)) != CollisionType.NONE) {
				if(c.trigger)
					player.onTriggerEnter(c.tag);
				else
					player.onCollisionEnter(type);
			}
		}
		
		if(gunStore.isOpen)
			gunStore.tick();
		
		if(gunsShopTrigger.isEnabled) {
			if(InputHandler.interact())
				gunStore.open();
		} else
			gunStore.close();
	}
	
	/**
	 * 	Render city background image
	 */
	public void render(Graphics g) {
		g.drawImage(Assets.sInstance.city,0,0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
	}

	/**
	 * 	Render city top
	 */
	public void renderTop(Graphics g) {
		g.drawImage(Assets.sInstance.city_top,0,0,
				MooseInvasion.RENDER_WIDTH,
				MooseInvasion.RENDER_HEIGHT,
				null);
		
		// Render GUI elements
        for(int i = 0; i < GameManager.sInstance.guiCity.size(); i++)
        	GameManager.sInstance.guiCity.get(i).render(g);
        
        if(gunStore.isOpen)
			gunStore.render(g);
	}
	
	/**
	 * 	Called when player enters a shop trigger
	 * 
	 * 	@param shop trigger tag
	 * 	@param state set GUI on or off
	 */
	public void shopTrigger(String tag, boolean state) {
		switch(tag) {
    		case "market":
    			marketShopTrigger.enable(state);
    			break;
    		case "guns":
    			gunsShopTrigger.enable(state);
    			break;
    		case "blacksmith":
    			blacksmithShopTrigger.enable(state);
    			break;
    		case "dass":
    			dassTrigger.enable(state);
    			break;
    	}
	}
}
