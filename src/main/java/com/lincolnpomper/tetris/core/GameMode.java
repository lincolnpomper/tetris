package com.lincolnpomper.tetris.core;

public enum GameMode {

	NORMAL(0),
	MASTER(1),
	ULTRA(2);

	private int value;

	GameMode(int value) {
		this.value = value;
	}

	public static GameMode from(int value) {

		GameMode mode = null;

		if (value == NORMAL.value) {
			mode = NORMAL;
		} else if (value == MASTER.value) {
			mode = MASTER;
		} else if (value == ULTRA.value) {
			mode = ULTRA;
		}

		return mode;
	}
}
