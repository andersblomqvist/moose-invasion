package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.entity.Entity;
import com.andblomqdasberg.mooseinvasion.entity.Moose;
import com.andblomqdasberg.mooseinvasion.entity.Player;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.gui.AbstractGUI;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.level.WaveSpawner;
import com.andblomqdasberg.mooseinvasion.particle.AbstractParticle;
import com.andblomqdasberg.mooseinvasion.particle.AmmoParticle;
import com.andblomqdasberg.mooseinvasion.particle.BloodAndMeatParticle;
import com.andblomqdasberg.mooseinvasion.particle.BloodParticle;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.GameState;

/**
 * 	Manager class which controls all the game objects and
 * 	game states. Also handles update and render for the game objects
 *
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class GameManager {
    public static GameManager sInstance;

    public static void awake() {
        sInstance = new GameManager();
    }
    
    // Track game states
    private GameState gameState;
    
    private int gameTick;
    
    // Player and health
    private Player player;
    public int health;

    // List of all entities
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    public ArrayList<Entity> projectiles = new ArrayList<Entity>();
    public ArrayList<AbstractParticle> particles = new ArrayList<AbstractParticle>();

    // GUI list for rendering
    public ArrayList<AbstractGUI> gui = new ArrayList<AbstractGUI>();
    
    // Specific game components
    private ScreenStart startScreen;
    private ScreenMenu menuScreen;
    private ScreenSettings settingsScreen;
    private WaveSpawner waveSpawner;
    
    private GUIText healthText;
    
    /**
     * 	First initialize
     */
    public GameManager() {
        gameTick = 0;
        gameState = GameState.INTRO;
    }
    
    /**
     * 	Second initialize, need to wait so the static instance is not null so
     * 	other classes can safely be created.
     */
    public void init() {
    	player = new Player(64, 128);
        entities.add(player);
        health = 5;
        
        startScreen = new ScreenStart();
        menuScreen = new ScreenMenu();
        settingsScreen = new ScreenSettings();
        waveSpawner = new WaveSpawner();
        healthText = new GUIText("HP:" + health, 128, MooseInvasion.HEIGHT-6);
        
        AudioPlayer.play("ambient-wind.wav");
    }
    
    /**
     * 	Init when game state changes to GAME
     */
    private void gameStartInit() {
    	// Cancel menu sounds
    	AudioPlayer.stop("ambient-wind.wav");
    }
    
    /**
     *  Spawns an entity
     */
    public void spawnEntity(Entity e) {
    	if(e instanceof Projectile)
    		projectiles.add(e);
    	else
    		entities.add(e);
    }
    
    /**
     * 	Spawns particles
     * 
     * 	@param type What type of particle will we spawn
     * 	@param amount How many particle objects
     * 	@param x
     *  @param y
     */
    public void spawnParticles(ParticleType type, int amount, float x, float y) {
    	switch(type) {
    		case BLOOD:
    			for(int i = 0; i < amount; i++)
    	    		particles.add(new BloodParticle(x, y));
    		break;
    		case BLOOD_AND_MEAT:
    			for(int i = 0; i < amount; i++)
    	    		particles.add(new BloodAndMeatParticle(x, y));
    		break;
    		case AMMO:	
	    		particles.add(new AmmoParticle(x, y));
    		break;
    	}
	}

    /**
     *  Game update
     */
    public void tick(int ticks) {
    	
    	if(gameState == GameState.INTRO) {
    		startScreen.tick(ticks);
    		return;
    	} 
    	else if(gameState == GameState.MENU) {
    		menuScreen.tick(ticks);
    		return;
    	}
    	else if(gameState == GameState.SETTINGS) {
    		settingsScreen.tick(ticks);
    		return;
    	}
    	else if (gameState == GameState.GAME_OVER) {
    		if(InputHandler.nextWave())
    			restartGame();
    		return;
    	}
    	
    	waveSpawner.tick();
    	
    	// Save the game tick so render can utilize it
    	gameTick = ticks;
    	
    	// Update all the entities
        updateEntityList(projectiles);
        updateEntityList(entities);
        
        // Update particles
        for(int i = 0; i < particles.size(); i++) {
        	particles.get(i).tick();
        	particles.get(i).animationTick();
        }
        
        // TODO remove this and replace with a new system where the moose blood is stored
        // TODO in a different list which we just render earlier than the entities. We need to create a
        // TODO new game object which is just the blood sprite and instantiate it when a moose dies.
        // TODO Sort entity list once a tick for depth rendering
        if(gameTick % 60 == 0)
        	Collections.sort(entities);
    }

    /**
     * 	Game render
     */
    public void render(Graphics g) {

    	if(gameState == GameState.INTRO) {
    		startScreen.render(g);
    		return;
    	}
    	if(gameState == GameState.MENU) {
    		menuScreen.render(g);
    		return;
    	}
    	if(gameState == GameState.SETTINGS) {
    		settingsScreen.render(g);
    		return;
    	}
    	
    	// Render ground
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y <= 14; y++) {
            	// Fake random distribution of tiles
            	int tile = x*2*y / 3 % 4;
                g.drawImage(Assets.sInstance.sprites[1][tile],
                        x*MooseInvasion.SPRITE_X_SIZE*MooseInvasion.X_SCALE,
                        y*MooseInvasion.SPRITE_Y_SIZE*MooseInvasion.Y_SCALE,
                        MooseInvasion.SPRITE_X_SIZE*MooseInvasion.X_SCALE,
                        MooseInvasion.SPRITE_Y_SIZE*MooseInvasion.Y_SCALE,
                        null);
            }
        }
        
        // Render particles
        for(int i = 0; i < particles.size(); i++)
        	particles.get(i).render(g, gameTick);
        
        // Render entities
        for(int i = 0; i < entities.size(); i++)
        	entities.get(i).render(g, gameTick);
        
        // Render projectiles
        for(int i = 0; i < projectiles.size(); i++)
        	projectiles.get(i).render(g, gameTick);
        
        // Game over state black overlay
        if(gameState == GameState.GAME_OVER) {
    		g.setColor(new Color(0,0,0,100));
    		g.fillRect(0,0, 
    				MooseInvasion.RENDER_WIDTH, 
    				MooseInvasion.RENDER_HEIGHT);
    	}
        
        // Render GUI text elements
        for(int i = 0; i < gui.size(); i++)
        	gui.get(i).render(g);
    }
    
    /**
     * 	Goes through all the entities in the list and calls
     * 	the {@code tick()} method
     */
    private void updateEntityList(ArrayList<Entity> list)
    {
        for(int i = 0; i < list.size(); i++) {
        	Entity e = list.get(i);
            if (e.alive)
                e.tick();
            else
            	list.remove(i);
        }
    }
    
    /**
     * 	Removes the projectile at index
     * 
     * 	@param index List index position
     */
	public void removeProjectile(int index) {
		projectiles.remove(index);
	}
	
	/**
	 * 	Starts the game when player hits enter from {@link ScreenStart} or start game
	 * 	from menu screen.
	 */
	public void setGameState(GameState state) {
		gameState = state;
		
		if(state == GameState.GAME)
			gameStartInit();
	}
	
	/**
	 * 	@returns current state of the game
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * 	Progress wave level. Called from when a {@link Moose} dies
	 */
	public void addProgress() {
		waveSpawner.killed += 1;
		player.setGold(player.getGold() + waveSpawner.wave);
	}

	/**
	 * 	Reduces health by 1 when a moose have crossed the playing field
	 */
	public void reduceHealth() {
		health -= 1;
		healthText.text = "HP:" + health;
		if(health <= 0) {
			// End game.
			setGameOverState();
		} else {
			// If the player did not die we want to add progress so even
			// when the player does not kill all the moose, the game should
			// still progress.
			addProgress();
		}
	}
	
	/**
	 * 	Method for clearing and setting gui texts and game state to
	 * 	Game Over.
	 */
	private void setGameOverState() {
		gameState = GameState.GAME_OVER;
		gui.clear();
		
		GUIText gameOverText = new GUIText("Game Over!", 102, 64, 1);
        gameOverText.setColor(Color.RED);
        
        new GUIText("[r] to restart", 104, 85);
        new GUIText("[esc] to quit", 108, 100);
        new GUIText("Died at wave " + waveSpawner.wave, 106, 115);
	}
	
	/**
	 * 	Restarts the game.
	 */
	private void restartGame() {
		entities.clear();
		projectiles.clear();
		particles.clear();
		gui.clear();
		
		gameTick = 0;
        gameState = GameState.GAME;
        
		player = new Player(64, 128);
        entities.add(player);
        health = 5;
        
        waveSpawner = new WaveSpawner();
        healthText = new GUIText("HP:" + health, 128, MooseInvasion.HEIGHT-6);
	}
}