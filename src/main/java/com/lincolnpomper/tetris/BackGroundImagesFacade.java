package com.lincolnpomper.tetris;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class BackGroundImagesFacade {

	private static BackGroundImagesFacade facade;
	private List<Image> backgroundImagesList;
	private int imageNumber = -1;

	private BackGroundImagesFacade() {
		this.backgroundImagesList = new ArrayList<>();
	}

	public static BackGroundImagesFacade getInstance() {

		BackGroundImagesFacade instance = new BackGroundImagesFacade();

		if (facade == null) {
			facade = instance;
		}

		return facade;
	}

	public void addAll(List<Image> images) {
		backgroundImagesList.addAll(images);
	}

	public Image getNextImage() {
		return backgroundImagesList.get(getNextImageNumber());
	}

	private int getNextImageNumber() {

		imageNumber++;

		if (imageNumber == backgroundImagesList.size()) {
			imageNumber = 0;
		}

		return imageNumber;
	}
}