package com.andblomqdasberg.mooseinvasion;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import javax.swing.JFrame;

/**
 * 	Handle all keyboard and mouse inputs.
 * 	
 * 	@author Anders Blomqvist
 *	@author David Åsberg
 */
public class InputHandler implements KeyListener, MouseListener {

	private static HashSet<Integer> keysPressed = new HashSet<>();
	
    private static boolean leftMouse = false;
    
    @SuppressWarnings("unused")
	private static boolean rightMouse = false;
    
    /**
     * 	Connect actions to specific key codes
     */
    private class Keys {
    	public static final int UP = 87;
    	public static final int DOWN = 83;
    	public static final int RIGHT = 68;
    	public static final int LEFT = 65;
    	
    	public static final int ARROW_DOWN = 40;
    	public static final int ARROW_RIGHT = 39;
    	public static final int ARROW_UP = 38;
    	public static final int ARROW_LEFT = 37;
    	
    	public static final int SPACE = 32;
    	public static final int ESC = 27;
    	public static final int R = 82;
    	public static final int Q = 81;
    	public static final int ENTER = 10;
    	
    	public static final int ONE = 49;
		public static final int TWO = 50;
		public static final int THREE = 51;
		public static final int FOUR = 52;
    }
    
	public InputHandler(JFrame frame) {
		frame.addKeyListener(this);
        frame.addMouseListener(this);
	}
	
	/**
	 * 	Key actions
	 */
	
	public static boolean shoot() {
		return (keysPressed.contains(Keys.SPACE) || leftMouse);
	}
	
	public static boolean up(boolean hold) {
		if(hold) {
			if(keysPressed.contains(Keys.UP) || keysPressed.contains(Keys.ARROW_UP)) {
				keysPressed.remove(Keys.ARROW_UP);
				keysPressed.remove(Keys.UP);
				return true;
			} else
				return false;
		} else {
			return keysPressed.contains(Keys.UP) || keysPressed.contains(Keys.ARROW_UP);	
		}
	}
	
	public static boolean down(boolean hold) {
		if(hold) {
			if(keysPressed.contains(Keys.DOWN) || keysPressed.contains(Keys.ARROW_DOWN)) {
				keysPressed.remove(Keys.ARROW_DOWN);
				keysPressed.remove(Keys.DOWN);
				return true;
			} else
				return false;
		} else {
			return keysPressed.contains(Keys.DOWN) || keysPressed.contains(Keys.ARROW_DOWN);	
		}
	}
	
	public static boolean left() {
		return keysPressed.contains(Keys.LEFT) || keysPressed.contains(Keys.ARROW_LEFT);
	}
	
	public static boolean right() {
		return (keysPressed.contains(Keys.RIGHT)) || keysPressed.contains(Keys.ARROW_RIGHT);
	}
	
	public static boolean exit() {
		if(keysPressed.contains(Keys.ESC)) {
			keysPressed.remove(Keys.ESC);
			return true;
		}
		return false;
	}
	
	public static boolean enter() {
		if(keysPressed.contains(Keys.ENTER)) {
			keysPressed.remove(Keys.ENTER);
			return true;
		}
		return false;
	}
	
	public static boolean nextWave() {
		return (keysPressed.contains(Keys.R));
	}

	public static boolean cycleWeapon() {
		if(keysPressed.contains(Keys.Q)) {
			keysPressed.remove(Keys.Q);
			return true;
		}
		return false;
	}

	public static boolean num1() {
		if(keysPressed.contains(Keys.ONE)) {
			keysPressed.remove(Keys.ONE);
			return true;
		}
		return false;
	}

	public static boolean num2() {
		if(keysPressed.contains(Keys.TWO)) {
			keysPressed.remove(Keys.TWO);
			return true;
		}
		return false;
	}

	public static boolean num3() {
		if(keysPressed.contains(Keys.THREE)) {
			keysPressed.remove(Keys.THREE);
			return true;
		}
		return false;
	}
	
	public static boolean num4() {
		if(keysPressed.contains(Keys.FOUR)) {
			keysPressed.remove(Keys.FOUR);
			return true;
		}
		return false;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftMouse = true;
        else if(e.getButton() == MouseEvent.BUTTON2)
        	rightMouse = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			 leftMouse = false;
        else if(e.getButton() == MouseEvent.BUTTON2)
        	rightMouse = false;
	}	

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
