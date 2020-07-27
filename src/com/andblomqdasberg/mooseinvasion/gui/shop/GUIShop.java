package com.andblomqdasberg.mooseinvasion.gui.shop;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.andblomqdasberg.mooseinvasion.Assets;
import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.entity.Player;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;

/**
 * 	Base class for shop GUI.
 * 
 * 	When opened this will draw a 50% black background and ontop the shop GUI
 * 	which contains the default background and its tab contents. Each shop will
 * 	have a unique amount of tabs and different content in tabs. The tabs are
 * 	only a bunch of text elements which can be interacted with.
 * 
 * 	To create a new shop a new class have to be made which extends this one
 * 	and then only tabs needs to be created and filled with text.
 * 
 * 	@author Anders Blomqvist
 */
public abstract class GUIShop 
{
	// Track if gui is opened or closed
	public boolean isOpen;
	
	public Player player;
	
	// Track what current tab is
	protected int currentTab;
	
	// Track current menu option
	protected int currentOption;
	
	// List of all tabs and reference to current tab object
	protected ArrayList<Tab> tabs = new ArrayList<Tab>();
	protected Tab activeTab;
	
	private Color black50 = new Color(0, 0, 0, 125);
	private int x = (MooseInvasion.WIDTH / 2 - 70) * MooseInvasion.X_SCALE;
	private int y = (MooseInvasion.HEIGHT / 2 - 68) * MooseInvasion.Y_SCALE;
	private int offsetX = x + 5*MooseInvasion.X_SCALE;
	private int offsetY = y - 11*MooseInvasion.Y_SCALE;
	
	protected ArrayList<GUIText> moneyLost = new ArrayList<GUIText>();
	
	public GUIShop() {
		player = GameManager.sInstance.getPlayer();
	}
	
	/**
	 * 	Tab object
	 */
	protected class Tab {
		public String name;
		public int id;
		public boolean focus;
		
		public ArrayList<GUIText> item = new ArrayList<GUIText>();
		public ArrayList<GUIText> misc1 = new ArrayList<GUIText>();
		public ArrayList<GUIText> money = new ArrayList<GUIText>();
		public ArrayList<GUIText> misc2 = new ArrayList<GUIText>();
		
		private GUIText tabTitle;
		private Color LIGHT = new Color(190, 178, 142);
		private Color DARK = new Color(108, 102, 85);
		private Color MONEY = new Color(255, 205, 85);
		private Color AMMO = new Color(255, 174, 86);
		
		/**
		 * 	Create a new tab. A Tab must have a title name and an id. Id is
		 * 	used for offseting to the right.
		 * 
		 * 	@param name Name of the tab
		 * 	@param id tab number starting from 0
		 */
		public Tab(String name, int id) {
			this.name = name;
			this.id = id;
			focus = false;
			
			tabTitle = new GUIText(name, 
					offsetX + (id*40)*MooseInvasion.X_SCALE, 
					offsetY, "");
			tabTitle.setColor(DARK);
		}
		
		/**
		 * 	Render Tab GUI at specifed x and y
		 */
		public void render(Graphics g) {
			g.drawImage(Assets.sInstance.gui_shop_tab, 
					offsetX + (id*40)*MooseInvasion.X_SCALE, 
					offsetY,
					40*MooseInvasion.X_SCALE,
					13*MooseInvasion.Y_SCALE,
					null);
			
			tabTitle.render(g, 
					offsetX+4*MooseInvasion.X_SCALE+(id*40)*MooseInvasion.X_SCALE, 
					y - 1*MooseInvasion.X_SCALE);
			
			if(!focus)
				return;
			
			// Render tab contents
			for(int i = 0; i < item.size(); i++) {
				int xx = x + 5*MooseInvasion.X_SCALE;
				int yy = y + 12*MooseInvasion.Y_SCALE+(i*14*MooseInvasion.Y_SCALE);
				item.get(i).render(g, xx, yy);
			}
			
			for(int i = 0; i < misc1.size(); i++) {
				int xx = x+70*MooseInvasion.X_SCALE;
				int yy = y + 12*MooseInvasion.Y_SCALE+(i*14*MooseInvasion.Y_SCALE);
				misc1.get(i).render(g, xx, yy);
			}
			
			for(int i = 0; i < money.size(); i++) {
				int xx = x+102*MooseInvasion.X_SCALE;
				int yy = y + 12*MooseInvasion.Y_SCALE+(i*14*MooseInvasion.Y_SCALE);
				money.get(i).render(g, xx, yy);
			}
			
			for(int i = 0; i < misc2.size(); i++) {
				int xx = x+145*MooseInvasion.X_SCALE;
				int yy = y + 12*MooseInvasion.Y_SCALE+(i*14*MooseInvasion.Y_SCALE);
				misc2.get(i).render(g, xx, yy);
			}
		}
		
		/**
		 * 	Adds a text element to specified list
		 * 
		 * 	@param text
		 * 	@param position 0 = left, 1 = middle, 2 = right, 3 = far right
		 */
		public void addText(String text, int position) {
			switch(position) {
    			case 0:
    				item.add(new GUIText(text, 0, 0, ""));
    				break;
    			case 1:
    				misc1.add(new GUIText(text, 0, 0, "").color(AMMO));
    				break;
    			case 2:
    				money.add(new GUIText(text, 0, 0, "").color(MONEY));
    				break;
    			case 3:
    				misc2.add(new GUIText(text, 0, 0, "").color(AMMO));
    				break;
			}
		}
		
		/**
		 * 	Called when we want to set this tab to the active one.
		 *  
		 * 	@return a reference to this object
		 */
		public Tab setActive(boolean state) {
			focus = state;
			if(state)
				tabTitle.setColor(LIGHT);
			else
				tabTitle.setColor(DARK);
			return this;
		}
		
		/**
		 * 	Changes the text color depending on
		 *  
		 * 	@param index List position
		 */
		public void setTextActive(int index, boolean state) {
			GUIText t = item.get(index);
			if(state)
				t.setColor(Color.RED);
			else
				t.setColor(Color.WHITE);
		}
	}
	
	/**
	 * 	Check for inputs and update lost money text position 
	 */
	public void tick() {
		if(InputHandler.down(true))
			arrowDown();
		
		if(InputHandler.up(true))
			arrowUp();
		
		if(InputHandler.right(true))
			arrowRight();
		
		if(InputHandler.left(true))
			arrowLeft();
		
		if(InputHandler.exit())
			close();
		
		if(InputHandler.enter() || InputHandler.shoot(true))
			buyItem();
		
		for(int i = 0; i < moneyLost.size(); i++) {
			if(moneyLost.get(i).y > 50) {
				moneyLost.get(i).destroy();
				moneyLost.remove(i);
				i--;
			} else
				moneyLost.get(i).y += 0.5;
		}
	}
	
	/**
	 * 	Draws 50% black and default shop gui background.
	 * 	Override for specific tabs etc.
	 */
	public void render(Graphics g) {
		g.setColor(black50);
		g.fillRect(0, 0, 
				MooseInvasion.RENDER_WIDTH, 
				MooseInvasion.RENDER_HEIGHT);
		
		for(Tab t : tabs)
			if(!t.focus)
				t.render(g);
			
		g.drawImage(Assets.sInstance.gui_shop_background, x, y,
				140*MooseInvasion.X_SCALE,
				125*MooseInvasion.Y_SCALE,
				null);
		
		activeTab.render(g);
		
		for(GUIText t : moneyLost)
			t.render(g);
	}
	
	/**
	 * 	Called when player hits enter on an menu option. Here we do error
	 * 	prevention where we leave if player does not have enough money or if
	 * 	the item can't be bought.
	 * 
	 * 	Override this for each shop.
	 */
	protected boolean buyItem() {
		String moneyText = activeTab.money.get(currentOption).text;
		
		if(moneyText.contentEquals("SOLD") || moneyText.contentEquals("")) {
			System.out.println("Item alreadt bought or can't be bought!");
			// TODO add err sound effect
			return false;
		}
		
		int money = Integer.parseInt(moneyText.substring(1));
		if(player.money < money) {
			// TODO add err sound
			System.out.println("Not enough money!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 	Go to text below
	 */
	protected void arrowDown() {
		int max = activeTab.item.size()-1;
		if(max == 0)
			return;
		int prev = currentOption;
		if(currentOption < max)
			currentOption++;
		else currentOption = 0;
		AudioPlayer.play("misc-menu-down.wav");
		activeTab.setTextActive(prev, false);
		activeTab.setTextActive(currentOption, true);
	}
	
	/**
	 * 	Go to text above
	 */
	protected void arrowUp() {
		int max = activeTab.item.size()-1;
		if(max == 0)
			return;
		int prev = currentOption;
		if(currentOption > 0)
			currentOption--;
		else currentOption = max;
		AudioPlayer.play("misc-menu-up.wav");
		activeTab.setTextActive(prev, false);
		activeTab.setTextActive(currentOption, true);
	}
	
	/**
	 * 	Go to next tab
	 */
	protected void arrowRight() {
		int max = tabs.size()-1;
		if(max == 0)
			return;
		if(currentTab < max)
			currentTab++;
		else currentTab = 0;
		AudioPlayer.play("misc-menu-up.wav");
		activeTab.setActive(false);
		activeTab.setTextActive(currentOption, false);
		activeTab = tabs.get(currentTab).setActive(true);
		activeTab.setTextActive(currentOption, true);
	}
	
	/**
	 * 	Go to prev tab
	 */
	protected void arrowLeft() {
		int max = tabs.size()-1;
		if(max == 0)
			return;
		if(currentTab > 0)
			currentTab--;
		else currentTab = max;
		AudioPlayer.play("misc-menu-down.wav");
		activeTab.setActive(false);
		activeTab.setTextActive(currentOption, false);
		activeTab = tabs.get(currentTab).setActive(true);
		activeTab.setTextActive(currentOption, true);
	}
	
	/**
	 * 	@returns the tab with id
	 */
	public Tab getTab(int id) {
		return tabs.get(id);
	}
	
	/**
	 * 	Methods for controlling opening/closing of the GUI
	 */
	public void open() { isOpen = true; }
	public void close() { isOpen = false; }
}
