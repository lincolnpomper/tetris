package com.lincolnpomper.tetris.graphics;

import java.awt.Color;

public class BlockModel {

	String[] metadata;
	Color color;

	public BlockModel(String[] metadata, Color color) {

		this.metadata = metadata;
		this.color = color;
	}
}