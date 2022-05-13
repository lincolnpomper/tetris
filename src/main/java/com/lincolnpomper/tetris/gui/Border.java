package com.lincolnpomper.tetris.gui;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

public class Border implements javax.swing.border.Border {

	private int size;
	private Image[] images;

	public Border(int size, Image[] images) {
		this.size = size;
		this.images = images;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2 = (Graphics2D) g;

		// Paint top
		g2.drawImage(images[0], 0, 0, null);

		// Paint left
		g2.drawImage(images[1], 0, size, null);

		// Paint bottom
		g2.drawImage(images[2], 0, height - size, null);

		// Paint right
		g2.drawImage(images[3], width - size, size, null);
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(size, size, size, size);
	}

	public boolean isBorderOpaque() {
		return false;
	}
}