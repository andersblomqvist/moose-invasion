package com.andblomqdasberg.mooseinvasion.gui.shop;

import java.awt.Color;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.weapon.AbstractWeapon;
import com.andblomqdasberg.mooseinvasion.weapon.WeaponList;

/**
 * 	Gun store contents
 * 
 * 	@author Anders Blomqvist
 */
public class GunStore extends GUIShop {

	private Tab guns = new Tab("GUNS", 0);
	private Tab ammo = new Tab("AMMO", 1);
	private Tab upg = new Tab("UPG.", 2);
	
	public GunStore() {
		player = GameManager.sInstance.getPlayer();
		
		/**
		 * 	GUNS tab
		 */
		guns.addText("PISTOL", 0);
		guns.addText("SOLD", 2);
		
		guns.addText("CARBINE", 0);
		guns.addText("$123", 2);
		
		guns.addText("UZI", 0);
		guns.addText("$123", 2);
		
		/*
		guns.addText("SHOTGUN", 0);
		guns.addText("$123", 2);
		
		guns.addText("SNIPER", 0);
		guns.addText("$123", 2);
		*/
		
		/**
		 * 	AMMO tab
		 */
		ammo.addText("PISTOL", 0);
		ammo.addText("X16", 1);
		ammo.addText("$16", 2);
		ammo.addText(""+WeaponList.PISTOL.ammo, 3);
		
		ammo.addText("CARBINE", 0);
		ammo.addText("X32", 1);
		ammo.addText("$64", 2);
		ammo.addText(""+WeaponList.CARBINE.ammo, 3);
		
		ammo.addText("UZI", 0);
		ammo.addText("X32", 1);
		ammo.addText("$24", 2);
		ammo.addText(""+WeaponList.UZI.ammo, 3);
		
		/*
		ammo.addText("SHOTGUN", 0);
		ammo.addText("X6", 1);
		ammo.addText("$20", 2);
		ammo.addText("", 3);
		
		ammo.addText("SNIPER", 0);
		ammo.addText("X6", 1);
		ammo.addText("$40", 2);
		ammo.addText("", 3);
		*/
		
		/**
		 * 	UPG. tab
		 */
		upg.addText("PISTOL", 0);
		upg.addText("1/5", 1);
		upg.addText("$128", 2);
		
		upg.addText("CARBINE", 0);
		upg.addText("0/5", 1);
		upg.addText("", 2);
		
		upg.addText("UZI", 0);
		upg.addText("0/5", 1);
		upg.addText("", 2);
		
		/*
		upg.addText("SHOTGUN", 0);
		upg.addText("0/5", 1);
		upg.addText("", 2);
		
		upg.addText("SNIPER", 0);
		upg.addText("0/5", 1);
		upg.addText("", 2);
		*/
		
		tabs.add(guns);
		tabs.add(ammo);
		tabs.add(upg);
		
		currentTab = 0;
		currentOption = 0;
		activeTab = tabs.get(0).setActive(true);
		activeTab.setTextActive(currentOption, true);
	}
	
	@Override
	public void open() {
		super.open();
		updateAmmo();
	}
	
	/**
	 * 	Updates the ammo texts
	 */
	private void updateAmmo() {
		Tab ammoTab = getTab(1);
		ammoTab.misc2.get(0).text = ""+WeaponList.PISTOL.ammo;
		ammoTab.misc2.get(1).text = ""+WeaponList.CARBINE.ammo;
		ammoTab.misc2.get(2).text = ""+WeaponList.UZI.ammo;
	}
	
	/**
	 * 	Enables buying weapon upgrades after player has bought the weapon.
	 * 
	 * 	@param weapon Name of the weapon
	 */
	private void updateUpgradeTab(String weapon) {
		Tab upgTab = getTab(2);
		AbstractWeapon w = WeaponList.getWeaponByName(weapon);
		upgTab.misc1.get(w.id).text = "1/5";
		upgTab.money.get(w.id).text = "$128";
	}
	
	@Override
	protected void buyItem() {
		
		// Get weapno type and money text
		String weapon = activeTab.item.get(currentOption).text;
		String moneyText = activeTab.money.get(currentOption).text;
		
		if(moneyText.contentEquals("SOLD") || moneyText.contentEquals("")) {
			System.out.println("Item already sold or cant be bought");
			// TODO add err sound
			return;
		}
		
		// Parse the money and check if we have enough
		int money = Integer.parseInt(moneyText.substring(1));
		if(player.money < money) {
			// TODO add err sound
			System.out.println("Not enough money");
			return;
		} else {
			// ... We had enough money. Buy the item
			
			player.money -= money;
			player.updateGoldText();
			moneyLost.add(new GUIText("-"+money,270,25,"").color(Color.RED));
			
			AudioPlayer.play("misc-buy.wav");
			switch(activeTab.name) {
    			case "GUNS":
    				System.out.println("Item: " + weapon);
    				player.buyWeapon(weapon);
    				activeTab.money.get(currentOption).text = "SOLD";
    				updateUpgradeTab(weapon);
    				break;
    			case "AMMO":
    				String ammo = activeTab.misc1.get(currentOption).text;
    				player.buyAmmo(weapon, ammo.substring(1));
    				updateAmmo();
    				break;
    			case "UPG.":
    				String s = activeTab.misc1.get(currentOption).text;
    				int level = Integer.parseInt(s.substring(0, 1)) + 1;
    				if(level <= 5 && level > 1) {
    					System.out.println(level);
    					player.buyUpgrade(weapon);
    					activeTab.misc1.get(currentOption).text = level + "/5";
    					if(level != 5) {
    						int nextCost = (int) Math.pow(2, level + 6);
        					activeTab.money.get(currentOption).text = "$"+nextCost;
    					} else {
    						activeTab.money.get(currentOption).text = "SOLD";
    					}
    				} 
    				break;
			}
		}
	}
}