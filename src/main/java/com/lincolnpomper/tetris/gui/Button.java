package com.lincolnpomper.tetris.gui;

class Button {

	boolean selected;
	String text;
	int x;
	int y;

	Button(String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.selected = false;
	}
}
