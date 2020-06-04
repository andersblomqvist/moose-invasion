package com.andblomqdasberg.mooseinvasion;

import javax.swing.*;
import java.awt.*;

/**
 * 	Render component. Calls the render method in {@link GameManager}
 * 
 * 	@author Anders Blomqvist
 * 	@author David �sberg
 */
public class Display extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public Display(int width, int height, int scale) {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setDoubleBuffered(true);
    }
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Assets.sInstance == null || GameManager.sInstance == null)
            return;
        GameManager.sInstance.render(g);
        Toolkit.getDefaultToolkit().sync();
    }
}

