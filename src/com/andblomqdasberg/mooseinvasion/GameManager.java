package com.andblomqdasberg.mooseinvasion;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.decoration.DecorationType;
import com.andblomqdasberg.mooseinvasion.entity.AbstractEntity;
import com.andblomqdasberg.mooseinvasion.entity.monster.EntityMonster;
import com.andblomqdasberg.mooseinvasion.entity.player.Player;
import com.andblomqdasberg.mooseinvasion.gui.AbstractGUI;
import com.andblomqdasberg.mooseinvasion.gui.GUIText;
import com.andblomqdasberg.mooseinvasion.level.City;
import com.andblomqdasberg.mooseinvasion.level.Level;
import com.andblomqdasberg.mooseinvasion.level.LevelLoader;
import com.andblomqdasberg.mooseinvasion.particle.AbstractParticle;
import com.andblomqdasberg.mooseinvasion.particle.BeerParticle;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;
import com.andblomqdasberg.mooseinvasion.util.GameRandom;
import com.andblomqdasberg.mooseinvasion.util.GameState;
import com.andblomqdasberg.mooseinvasion.weapon.AbstractWeapon;

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
    
    // Track if player is in city or not
    private boolean inCity;

    // List of all entities
    public ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();
    private ArrayList<AbstractParticle> specialParticles = new ArrayList<AbstractParticle>();
    
    // GUI lists
    public ArrayList<AbstractGUI> guiCity = new ArrayList<AbstractGUI>();
    public ArrayList<AbstractGUI> guiLevel = new ArrayList<AbstractGUI>();
    public ArrayList<AbstractGUI> guiPlayer = new ArrayList<AbstractGUI>();
    
    // Specific game components
    private ScreenStart startScreen;
    private ScreenMenu menuScreen;
    private ScreenSettings settingsScreen;
    private LevelLoader levelLoader;
    private Level level;
    public City city;
    
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
     * 
     * 	@throws IOException In case level.json fails to load
     */
    public void init() throws IOException {
    	player = new Player(64, 128);
        entities.add(player);
        health = 5;
        inCity = true;
        startScreen = new ScreenStart();
        menuScreen = new ScreenMenu();
        settingsScreen = new ScreenSettings();
        city = new City();
        healthText = new GUIText(""+health, 
        		MooseInvasion.WIDTH - 27,
        		MooseInvasion.HEIGHT - 8*3, 
        		"player-gui");
        
        AudioPlayer.play("ambient-wind.wav");

        // Load level.json file
        levelLoader = new LevelLoader("level");
        
        // Create new level from file
        level = new Level(levelLoader.getLevelData());
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
    public void spawnEntity(AbstractEntity e) {
		entities.add(e);
    }
    
    /**
     * 	Spawns particles in current level stage. However special particles can be
     * 	spawned which renders ontop of everything else. Not dependent on level stage.
     * 
     * 	@param type What type of particle will we spawn
     * 	@param amount How many particle objects
     * 	@param x
     *  @param y
     */
    public void spawnParticles(ParticleType type, int amount, float x, float y) {
    	if(type == ParticleType.BEER)
    		for(int i = 0; i < amount; i++)
	    		specialParticles.add(new BeerParticle(x + GameRandom.randomBetween(-2, 5), y+7));
    	
    	level.spawnParticle(type, amount, x, y);
	}
    
    /**
	 * 	Adds a decoration object of specified type in the level stage 
	 * 	decoration list
	 * 
	 * 	@param type What type of decoration
	 * 	@param x spawn position
	 * 	@param y spawn position
	 */
	public void spawnDecoration(DecorationType type, float x, float y) {
		level.spawnDecoration(type, x, y);
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
    	
    	if(inCity)
    		city.tick(ticks);
    	else
    		// Game progression
    		level.tick(ticks);
    	
    	// Save the game tick so render can utilize it
    	gameTick = ticks;
    	
    	// Update all the entities
        updateEntityList(ticks);
        
        for(int i = 0; i < specialParticles.size(); i++) {
        	AbstractParticle p = specialParticles.get(i);
        	p.tick();
        	p.animationTick();
        	
        	if(p.lifetime > 15)
        		specialParticles.remove(i);
        }	
        
        // z-index rendering for entities
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
    	
    	if(inCity)
    		city.render(g);
    	else
    		level.render(g);
        
        // Render entities
        for(int i = 0; i < entities.size(); i++)
        	entities.get(i).render(g);
        
        // Render special top particles
        for(int i = 0; i < specialParticles.size(); i++) {
        	AbstractParticle p = specialParticles.get(0);
        	p.render(g, gameTick);
        }
        
        if(inCity)
        	city.renderTop(g);
        
        // Render player GUI
        for(int i = 0; i < guiPlayer.size(); i++)
        	guiPlayer.get(i).render(g);
        
        // Game over state black overlay
        if(gameState == GameState.GAME_OVER) {
    		g.setColor(new Color(0,0,0,100));
    		g.fillRect(0,0, 
    				MooseInvasion.RENDER_WIDTH,
    				MooseInvasion.RENDER_HEIGHT);
    	}
        
        /** @DEBUG */
        g.setColor(Color.GREEN);
        g.drawString(player.velocity.toString(), 10, 40);
        g.drawString(player.movement.wishDir.toString(), 10, 80);
        g.drawString("N:" + player.movement.north, 10, 120);
        g.drawString("S:" + player.movement.south, 10, 160);
        g.drawString("W:" + player.movement.west, 10, 200);
        g.drawString("E:" + player.movement.east, 10, 240);
    }
    
    /**
     * 	Goes through all the entities in the list and calls
     * 	the {@code tick()} method
     */
    private void updateEntityList(int ticks)
    {
        for(int i = 0; i < entities.size(); i++) {
        	AbstractEntity e = entities.get(i);
            if (e.alive)
                e.tick(ticks);
            else
            	entities.remove(i);
        }
        
        // Collision detection
        for(int i = 0; i < entities.size()-1; i++) {
        	AbstractEntity e1 = entities.get(i);
        	for(int j = i+1; j < entities.size(); j++) {
        		AbstractEntity e2 = entities.get(j);
        		
        		// Skip collision if the entities are both Monsters
        		if(e1 instanceof EntityMonster && e2 instanceof EntityMonster)
        			continue;
        		
        		e1.collider.AABBCollision(e2.collider);
        	}
        }
    }
	
	/**
	 * 	Removes a particle from the speical paricle list
	 * 
	 * 	@param p reference to particle object
	 */
	public void removeParticle(AbstractParticle p) {
		specialParticles.remove(p);
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
	 * 	Progress wave level. Called from when a {@link Entity} dies
	 */
	public void onEntityKilled() {
		level.onEntityKilled();
	}
	
	public void addPlayerScore(int multiplier) {
		player.addScore(multiplier);
	}

	/**
	 * 	Reduces health by 1 when a moose have crossed the playing field
	 */
	public void reduceHealth() {
		health -= 1;
		healthText.text = "" + health;
		if(health <= 0) {
			// End game.
			setGameOverState();
		} else {
			// If the player did not die we want to add progress so even
			// when the player does not kill all the moose, the game should
			// still progress.
			level.onEntityKilled();
		}
	}
	
	/**
	 * 	Method for clearing and setting gui texts and game state to
	 * 	Game Over.
	 */
	private void setGameOverState() {
		gameState = GameState.GAME_OVER;
		System.out.println("GAME OVER");
	}
	
	/**
	 * 	Restarts the game.
	 */
	private void restartGame() {
		entities.clear();
		
		gameTick = 0;
        gameState = GameState.GAME;
        
		player = new Player(64, 128);
        entities.add(player);
        health = 5;
	}

	/**
	 * 	When players enter the city from a stage
	 * 	Here we switch from ticking+rendering in level to ticking in city.
	 */
	public void enterCity() {
		inCity = true;
		player.inCity = true;
		AbstractWeapon.ALLOW_SHOOTING = false;
	}
	
	/**
	 * 	When player leaves the city we change ticking+rendering to level.
	 */
	public void leaveCity() {
		inCity = false;
		player.inCity = false;
		AbstractWeapon.ALLOW_SHOOTING = true;
	}
	
	public Player getPlayer() {
		return player;
	}
}