package com.lincolnpomper.tetris;

import java.awt.image.BufferedImage;

public class BlocksImagesFacade {

	private static BlocksImagesFacade facade;
	private BufferedImage[] images;

	private BlocksImagesFacade() {
	}

	public static BlocksImagesFacade getInstance() {

		if (facade == null) {
			facade = new BlocksImagesFacade();
		}

		return facade;
	}

	public BufferedImage getGray() {

		if (images == null) {
			throw new RuntimeException("Should set images first");
		}

		return images[images.length - 1];
	}

	public BufferedImage getImageBy(int number) {

		if (images == null) {
			throw new RuntimeException("Should set images first");
		}

		return images[number - 100];
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}
}
