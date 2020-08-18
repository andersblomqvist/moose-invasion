package com.andblomqdasberg.mooseinvasion.gui.shop;

import java.awt.Color;

import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;

/**
 * 	Blacksmith store.
 * 	Melee weapons and non hostile player upgrades (movement etc ...)
 * 	
 * 	@author Anders Blomqvist
 */
public class Blacksmith extends GUIShop {

	private Tab main = new Tab("MAIN", 0);
	
	public Blacksmith() {
		
		main.addText("Knife", 0);
		main.addText("$99", 2);
		
		tabs.add(main);
		
		currentTab = 0;
		currentOption = 0;
		activeTab = tabs.get(0).setActive(true);
		activeTab.setTextActive(currentOption, true);
	}
	
	@Override
	protected boolean buyItem() {
		if(!super.buyItem())
			return false;
		
		String moneyText = activeTab.money.get(currentOption).text;
		int money = Integer.parseInt(moneyText.substring(1));
		String item = activeTab.item.get(currentOption).text;
		
		AudioPlayer.play("misc-buy.wav");
		
		player.money -= money;
		player.updateGUIText();
		moneyLost.add(new GUIText("-"+money,270,25,"").color(Color.RED));
		
		switch(item) {
			case "Knife":
				activeTab.money.get(currentOption).text = "SOLD";
				player.buyWeapon(item);
				break;
		}
		
		return true;
	}
}