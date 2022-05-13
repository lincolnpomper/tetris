package com.lincolnpomper.tetris.graphics.animation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import com.lincolnpomper.tetris.core.Position;

import javax.swing.Timer;

public class TextAnimation implements ActionListener {

	private Component component;
	private boolean cycle = false;
	private int frame = 0;
	private BufferedImage[] images;
	private Position position;
	private int step = 1;
	private Timer timer;

	public boolean isRunning() {
		return timer.isRunning();
	}

	public TextAnimation(Position position, BufferedImage[] images, int delay, Component component) {

		this.images = images;
		this.position = position;
		this.component = component;
		timer = new Timer(delay, this);
	}

	public TextAnimation(BufferedImage[] images, int delay) {
		this.images = images;
		timer = new Timer(delay, this);
	}

	public void actionPerformed(ActionEvent e) {

		frame += step;

		if (frame == 0) {
			step = 1;
		}

		if (frame == images.length) {
			frame--;
			if (cycle) {
				step = -1;
			} else {
				component.repaint();
				stop();
			}
		} else if (component != null) {
			component.repaint();
		}
	}

	public BufferedImage getDraw() {
		return images[frame];
	}

	public int getHeight() {
		return images[frame].getHeight();
	}

	public Position getPosition() {
		return position;
	}

	public int getWidth() {
		return images[frame].getWidth();
	}

	public void reset() {
		frame = 0;
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	public void start(Position position, Component component) {

		this.position = position;
		this.component = component;

		this.start();
	}

	public void start() {

		frame = 0;
		step = 1;
		timer.start();

		// painting immediately
		actionPerformed(null);
	}

	public void stop() {
		timer.stop();
	}
}