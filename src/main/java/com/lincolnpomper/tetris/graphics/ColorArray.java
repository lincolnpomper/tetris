package com.lincolnpomper.tetris.graphics;

public class ColorArray {

	int pos = 0;
	int[][] colorArray;
	private int quantity;

	public ColorArray(int quantity) {
		colorArray = new int[quantity][];
		this.quantity = quantity;
	}

	public void add(int red, int green, int blue) {

		int nextPos = 0;
		while (colorArray[nextPos] != null) {
			nextPos++;
		}

		colorArray[nextPos] = new int[]{red, green, blue, 255};
	}

	int[] next() {
		int[] value = null;
		if (pos < quantity) {
			value = colorArray[pos++];
		}
		return value;
	}

	void reset() {
		pos = 0;
	}
}
