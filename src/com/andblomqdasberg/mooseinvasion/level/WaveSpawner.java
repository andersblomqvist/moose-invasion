package com.andblomqdasberg.mooseinvasion.level;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.InputHandler;
import com.andblomqdasberg.mooseinvasion.MooseInvasion;
import com.andblomqdasberg.mooseinvasion.entity.Moose;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;

/**
 * 	Manage level progression with moose spawning 
 * 	
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class WaveSpawner {
	
	public int wave;			// Current wave level
	public int killed;			// Track how many mobs the player have killed
	
	private boolean finished;	// Determine state of the game
	private int waveTime;		// For how long will we spawn mobs
	
	private int spawnAmount;	// How many mobs will be spawned in this wave
	private int spawnRate;		// Calculate spawn rate depending on wave amount and time
	private int spawns;			// Track how many we've spawned so far
	
	private int counter;		// Timer counter
	private int ticks;			// Counter getting seconds
	
	private boolean bought;		// Cool down for buying upgrades
	
	public GUIText progress;
	public GUIText startText;
	
	public WaveSpawner() {
		wave = 0;
		finished = true;
		
		// GUI texts
		progress = new GUIText(10, MooseInvasion.HEIGHT-6);
		startText = new GUIText(90, 48);
		
		startWave();
	}
	
	public void tick() {
		
		// Wave is going, keep on spawning
		if(!finished) {
			ticks++;
			
			if(killed - spawnAmount == 0) {
				startText.text = "[q] for next wave";
				GameManager.sInstance.toggleBuyMenu();
				ticks = 0;
				finished = true;
			}
				
			
			// Get seconds
			if(ticks % 60 == 0) {
				if(counter == spawnRate) {
					counter = 0;
					if(spawns < spawnAmount) {
						GameManager.sInstance.spawnEntity(new Moose());
						spawns++;
					}
				}
				counter++;
				ticks = 0;
			}
			progress.text = "Wave " + wave + " " + 100*killed/spawnAmount + "%";
		}
		
		// Finished with wave, wait for next
		else {
			if(InputHandler.nextWave() && finished)
				startWave();

			/**
			 * 	Inputs for weapon upgrades
			 */
			ticks++;
			
			if(InputHandler.num1() && !bought) {
				GameManager.sInstance.buyAmmoCap();
				bought = true;
			}
			if(InputHandler.num2() && !bought) {
				GameManager.sInstance.buyDamage();
				bought = true;
			}
			if(InputHandler.num3() && !bought) {
				GameManager.sInstance.buyFastReload();
				bought = true;
			}
			if(InputHandler.num4() && !bought) {
				GameManager.sInstance.buyFireRate();
				bought = true;
			}	
			
			if(ticks % 60 == 0) {
				bought = false;
				ticks = 0;
			}
		}
	}
  
	/**
	 * 	Starts next wave
	 */
	public void startWave() {
		
		// Do a lot of setup
		wave++;
		waveTime = getWaveTime();
		createSpawnList();
		spawnRate = (int) Math.ceil((double)waveTime / spawnAmount); 
		killed = 0;
		spawns = 0;
		finished = false;
		ticks = 0;
		counter = spawnRate;
		
		// Clear text
		startText.text = "";
		
		GameManager.sInstance.toggleBuyMenu();
		
		System.out.println("Starting wave: " + wave + ", total of " + 
				spawnAmount + " mobs, time: " +
				waveTime + ", rate: " + 
				spawnRate);
	}
	
	/**
	 * 	A formula for calculating how many mooses we will spawn depending
	 * 	on wave level.
	 * 
	 * 	@returns how many hostile entities we will spawn set by current level
	 */
	private void createSpawnList() {
		double a = 4.39;
		double b = 1.62;
		spawnAmount = (int) Math.pow(a*wave, b);
	}
	
	/**
	 *	Formula for setting how long the wave will be in ticks
	 *
	 * 	@returns number of ticks this wave will take
	 */
	private int getWaveTime() {
		return 60 + wave*2;
	}
}

