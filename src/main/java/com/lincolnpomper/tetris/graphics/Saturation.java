package com.lincolnpomper.tetris.graphics;

public enum Saturation {

	shiny(1),
	brightest((double) 249 / 255),
	brighter((double) 230 / 255),
	mediumBright((double) 218 / 255),
	standard((double) 198 / 255),
	mediumDark((double) 160 / 255),
	dark((double) 130 / 255),
	darker((double) 80 / 255),
	darkest((double) 60 / 255);

	private double value;

	Saturation(double value) {

		this.value = value;
	}

	public double getValue() {
		return value;
	}

}

