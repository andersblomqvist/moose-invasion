package com.andblomqdasberg.mooseinvasion;

import javax.swing.*;
import java.awt.*;

/**
 * 	Main class with game loop
 * 
 * 	@author Anders Blomvqist
 * 	@author David Åsberg
 */
public class MooseInvasion extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	// Render settings
	public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 4;
    public static final int SPRITE_SIZE = 16;

    // Display for rendering
    private Display display;
    
    private boolean running = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new MooseInvasion("Moose Invasion");
        });
    }

    public MooseInvasion(String title) {
        super(title);

        display = new Display(WIDTH, HEIGHT, SCALE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(display);
        pack();
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("assets\\" + "moose-invasion-icon-64.png").getImage());

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

            /** Update */
            boolean render = false;
            while (unprocessedTime > 1) {
                ticks++;
                tick(ticks);
                render = true;
                unprocessedTime -= 1;
                
                if(InputHandler.exit()) {
                	running = false;
                	System.exit(0);
                }
            }
            
            /** Render */
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
