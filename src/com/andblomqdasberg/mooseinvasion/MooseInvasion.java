package com.andblomqdasberg.mooseinvasion;

import javax.swing.*;

import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.config.ConfigHandler;
import com.andblomqdasberg.mooseinvasion.util.GameState;

import java.awt.*;
import java.io.IOException;

/**
 * 	Main class with game loop
 * 
 * 	@author Anders Blomvqist
 * 	@author David Åsberg
 */
public class MooseInvasion extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	// Standard Render settings (will be overwritten by config)
	public static int WIDTH = 320;
    public static int HEIGHT = 240;
    public static int X_SCALE = 3;
    public static int Y_SCALE = 3;
    public static int SPRITE_X_SIZE = 16;
    public static int SPRITE_Y_SIZE = 16;
    public static boolean FULLSCREEN = false;

    public static int RENDER_WIDTH = WIDTH*X_SCALE;
    public static int RENDER_HEIGHT = HEIGHT*Y_SCALE;
    
    // Display for rendering
    private Display display;
    
    private boolean running = false;
    private boolean paused = false;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new MooseInvasion("Moose Invasion");
        });
    }

    public MooseInvasion(String title) {
        super(title);
        
        // Monitor resolution
        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();

        /**
         * 	Load config settings
         */
        
        System.out.println("-- Loading config --");
        String prop = "";
        try {
        	prop = ConfigHandler.getPropertiesValues();
            System.out.println("Properties string: " + prop);
		} catch (IOException e) {
			System.out.println("Reading newly created config file.");
        	try {
				prop = ConfigHandler.getPropertiesValues();
			} catch (IOException e1) {
				System.out.println("Total failiure");
				e1.printStackTrace();
			}
		}
        
        /**
         *  Parse and apply config
         */
        
        String[] values = prop.split(",");
        int f = Integer.parseInt(values[0]);
        int s = Integer.parseInt(values[1]);
        int v = Integer.parseInt(values[2]);
        
        AudioPlayer.setGlobalVolume(v);
        FULLSCREEN = (f == 0) ? false : true;
        
        if(FULLSCREEN) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            X_SCALE = monitor.width / WIDTH;
            HEIGHT = monitor.height / (X_SCALE - 2);
            Y_SCALE = monitor.height / HEIGHT;
            SPRITE_Y_SIZE += 2;
        } else {
        	X_SCALE = s;
        	Y_SCALE = s;
        }
        
        RENDER_WIDTH = WIDTH*X_SCALE;
        RENDER_HEIGHT = HEIGHT*Y_SCALE;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display = new Display(WIDTH, HEIGHT, X_SCALE, Y_SCALE);
        setResizable(false);
        add(display);
        pack();
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("assets\\" + "moose-invasion-icon-64.png").getImage());
        setVisible(true);
        
        System.out.println("-- Window setup --");
        System.out.println("Monitor: " + monitor);
        System.out.println("X-Scale: " + X_SCALE);
        System.out.println("Y-Scale: " + Y_SCALE);
        System.out.println("Render-width: " + RENDER_WIDTH);
        System.out.println("Render-height: " + RENDER_HEIGHT);
        System.out.println("Fullscreen: " + FULLSCREEN);
        
        new InputHandler(this);
        new Thread(this).start();
    }
    
    /**
     * 	Init assets and the game manager before game start.
     * 	Assets will load and cut up all the sprites and the
     *  game manager will be used later.
     */
    private void init() {
        Assets.init();
        GameManager.awake();
        GameManager.sInstance.init();
    }
    
    @Override
    public void run() {
        init();
        double nsPerFrame = 1000000000.0 / 60.0;
        double unprocessedTime = 0;
        double maxSkipFrames = 10;

        long lastTime = System.nanoTime();
        long lastFrameTime = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        running = true;
        while (running) {

            long now = System.nanoTime();
            double passedTime = (now - lastTime) / nsPerFrame;
            lastTime = now;

            if (passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
            if (passedTime > maxSkipFrames) passedTime = maxSkipFrames;

            unprocessedTime += passedTime;

            /** 
             * 	Update 
             */
            boolean render = false;
            while (unprocessedTime > 1) {
            	
            	ticks++;
                render = true;
                unprocessedTime -= 1;
                
            	if(!paused)
                    tick(ticks);
                
            	if(paused) {
            		if(InputHandler.exit()) {
            			running = false;
                    	System.exit(0);
            		}
            		
            		if(InputHandler.enter())
            			paused = false;
            	}
            	
                if(InputHandler.exit() 
                		&& !paused 
                		&& GameManager.sInstance.getGameState() == GameState.GAME)
                	paused = true;
            }
            
            /** 
             * 	Render 
             */
            render = true;
            if (render) {
                display.repaint();
                frames++;
            }

            // Print ticks and frames every second, also reset
            if (System.currentTimeMillis() > lastFrameTime + 1000) {
                System.out.println(ticks + " ticks, " + frames + " fps");
                lastFrameTime += 1000;
                frames = 0;
                ticks = 0;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 	Main update for game logic
     *
     * 	@param ticks Which tick we are on, alternating from 0 - 60. Used in animations.
     */
    private void tick(int ticks) {
        GameManager.sInstance.tick(ticks);
    }
}