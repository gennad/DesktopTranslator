package com.gennadz;

import java.awt.Graphics;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
	public static final int MESSAGE_X = 75;
	public static final int MESSAGE_Y = 100;
	
	public void paintComponent(Graphics g) {
		g.drawString("My program", MESSAGE_X, MESSAGE_Y);
	}
}