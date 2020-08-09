package com.andblomqdasberg.mooseinvasion.gui.shop;

import java.awt.Color;

import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;

/**
 * 	Market store.
 * 	Misc item, but beer. nice
 * 
 * 	@author Anders Blomqvist	
 */
public class Market extends GUIShop {

	private Tab store = new Tab("MAIN", 0);
	
	public Market() {
		store.addText("BEER", 0);
		store.addText("$7", 2);
		
		tabs.add(store);
		
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
			case "BEER":
				player.beers += 1;
				break;
		}
		
		return true;
	}
}
