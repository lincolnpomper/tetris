package com.lincolnpomper.tetris.model;

import com.lincolnpomper.tetris.core.Frame;
import com.lincolnpomper.tetris.core.Position;

public class Piece {

	public static final Position STARTER_POSITION = new Position(3, -10);

	private int number;
	private Position position = null;
	private int posture;
	private boolean[][][] shapeGroup;

	public Piece(boolean[][][] shapeGroup, int number) {
		this.shapeGroup = shapeGroup;
		this.number = number;
		startValues();
	}

	public void startValues() {
		posture = 0;
		position = new Position(STARTER_POSITION.getX(), STARTER_POSITION.getY());
	}

	public int getNumber() {
		return number;
	}

	public boolean[][] getOriginalShape() {
		return shapeGroup[0];
	}

	public Position getPosition() {
		return position;
	}

	public boolean[][] getShape() {
		return shapeGroup[posture];
	}

	public boolean[][][] getShapeGroup() {
		return shapeGroup;
	}

	public boolean isStarterPosition() {
		return position.equals(STARTER_POSITION);
	}

	public boolean moveDown() {
		int y = position.getY();
		if (y == Frame.ROWS) {
			return false;
		}
		position.setY(y + 1);
		return true;
	}

	public boolean moveLeft() {
		int x = position.getX();
		int gap = 0;

		for (int i = 0; i < 4; i++) {
			if (shapeGroup[posture][0][i] || shapeGroup[posture][1][i] || shapeGroup[posture][2][i] || shapeGroup[posture][3][i]) {
				break;
			} else {
				gap--;
			}
		}
		if (x == gap) {
			return false;
		}
		position.setX(x - 1);
		return true;
	}

	public boolean moveRight() {
		int x = position.getX();
		int gap = 4;

		for (int i = 3; i > -1; i--) {
			if (shapeGroup[posture][0][i] || shapeGroup[posture][1][i] || shapeGroup[posture][2][i] || shapeGroup[posture][3][i]) {
				break;
			} else {
				gap--;
			}
		}
		if (x == Frame.COLUMNS - gap) {
			return false;
		}
		position.setX(x + 1);
		return true;
	}

	public void turnLeft() {
		if (posture == 0) {
			posture = 3;
		} else {
			posture--;
		}
	}

	public void turnRight() {
		if (posture == 3) {
			posture = 0;
		} else {
			posture++;
		}
	}
}
