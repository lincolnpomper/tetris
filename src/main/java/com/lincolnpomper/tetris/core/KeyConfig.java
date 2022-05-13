package com.lincolnpomper.tetris.core;

import java.awt.event.KeyEvent;

public class KeyConfig {

	public final static int DEFAULT_KEY_PLAYER1_LEFT = KeyEvent.VK_LEFT;
	public final static int DEFAULT_KEY_PLAYER1_DOWN = KeyEvent.VK_DOWN;
	public final static int DEFAULT_KEY_PLAYER1_RIGHT = KeyEvent.VK_RIGHT;
	public final static int DEFAULT_KEY_PLAYER1_UP = KeyEvent.VK_UP;
	public final static int DEFAULT_KEY_PLAYER1_BUTTON1 = KeyEvent.VK_A;
	public final static int DEFAULT_KEY_PLAYER1_BUTTON2 = KeyEvent.VK_S;

	public final static int DEFAULT_KEY_PLAYER2_LEFT = KeyEvent.VK_J;
	public final static int DEFAULT_KEY_PLAYER2_DOWN = KeyEvent.VK_K;
	public final static int DEFAULT_KEY_PLAYER2_RIGHT = KeyEvent.VK_L;
	public final static int DEFAULT_KEY_PLAYER2_UP = KeyEvent.VK_I;
	public final static int DEFAULT_KEY_PLAYER2_BUTTON1 = KeyEvent.VK_Q;
	public final static int DEFAULT_KEY_PLAYER2_BUTTON2 = KeyEvent.VK_W;

	public int left;
	public int down;
	public int right;
	public int up;
	public int button1;
	public int button2;

	public final static int DEFAULT_PLAYER1_KEY_CONFIG = 1;
	public final static int DEFAULT_PLAYER2_KEY_CONFIG = 2;

	public KeyConfig(int defaultKeyConfig) {

		if (defaultKeyConfig == DEFAULT_PLAYER1_KEY_CONFIG) {
			this.left = DEFAULT_KEY_PLAYER1_LEFT;
			this.down = DEFAULT_KEY_PLAYER1_DOWN;
			this.right = DEFAULT_KEY_PLAYER1_RIGHT;
			this.up = DEFAULT_KEY_PLAYER1_UP;
			this.button1 = DEFAULT_KEY_PLAYER1_BUTTON1;
			this.button2 = DEFAULT_KEY_PLAYER1_BUTTON2;
		} else if (defaultKeyConfig == DEFAULT_PLAYER2_KEY_CONFIG) {
			this.left = DEFAULT_KEY_PLAYER2_LEFT;
			this.down = DEFAULT_KEY_PLAYER2_DOWN;
			this.right = DEFAULT_KEY_PLAYER2_RIGHT;
			this.up = DEFAULT_KEY_PLAYER2_UP;
			this.button1 = DEFAULT_KEY_PLAYER2_BUTTON1;
			this.button2 = DEFAULT_KEY_PLAYER2_BUTTON2;
		}
	}

	public int[] getAllKeys() {
		return new int[]{
				left,
				down,
				right,
				up,
				button1,
				button2
		};
	}
}
