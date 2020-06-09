package com.andblomqdasberg.mooseinvasion;

import javax.swing.*;
import java.awt.*;

/**
 * 	Render component. Calls the render method in {@link GameManager}
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Display extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public Display(int width, int height, int x_scale, int y_scale) {
        Dimension size = new Dimension(width * x_scale, height * y_scale);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setDoubleBuffered(true);
    }
	
	/**
	 * 	Root render function
	 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Assets.sInstance == null || GameManager.sInstance == null)
            return;
        GameManager.sInstance.render(g);
        Toolkit.getDefaultToolkit().sync();
    }
}