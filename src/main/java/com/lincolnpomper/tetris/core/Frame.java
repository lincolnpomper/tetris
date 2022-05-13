package com.lincolnpomper.tetris.core;

import com.lincolnpomper.tetris.model.Piece;

public class Frame {

    public final static int COLUMNS = 10;
    public final static int ROWS = 20;
    private static final int EMPTY_BLOCK = 0;
    private int[][] frame;
    private boolean[] previousAllRows;

    public Frame() {
        restart();
    }

    private void clearLines(boolean[] fullLines) {

        int[][] goodLines = new int[ROWS][COLUMNS];
        int pos = ROWS - 1;
        for (int i = ROWS - 1; i >= 0; i--) {
            if (!fullLines[i]) {
                goodLines[pos--] = frame[i];
            }
        }

        frame = goodLines;
    }

    private void clearPieceFromFrame() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (frame[i][j] > 0) {
                    frame[i][j] = 0;
                }
            }
        }
    }

    private void fillFrameWithPieceContents(Piece piece, int col, int row, boolean[][] shape, int minimumX, int maximumX, int maximumY) {

        for (int i = 0; i < maximumY; i++) {
            for (int j = minimumX; j < maximumX; j++) {
                if (col + j == 10) {
                    break;
                }
                if (shape[i][j] && row + i >= 0) {
                    frame[row + i][col + j] = piece.getNumber();
                }
            }
        }
    }

    private boolean isLineFull(int i) {

        boolean fullLine = true;

        for (int j = 0; j < COLUMNS; j++) {
            if (frame[i][j] == EMPTY_BLOCK) {
                fullLine = false;
                break;
            }
        }

        return fullLine;
    }

    private void lockBlock(int i) {

        for (int j = 0; j < COLUMNS; j++) {
            if (frame[i][j] > 0) {
                frame[i][j] = frame[i][j] * (-1);
            }
        }
    }

    private int setupFirstPosition(Position position, int row, boolean[][] shape) {

        int emptyShapeSpace = 0;

        while (!shape[emptyShapeSpace][0] && !shape[emptyShapeSpace][1] && !shape[emptyShapeSpace][2] && !shape[emptyShapeSpace][3]) {
            emptyShapeSpace++;
        }

        while (position.getY() < -emptyShapeSpace) {
            position.setY(++row);
        }

        return row;
    }

    private boolean verifyIsLanding(int col, int row, boolean[][] shape, int minimumX, int maximumX, int maximumY) {

        for (int i = 0; i < maximumY; i++) {
            for (int j = minimumX; j < maximumX; j++) {
                if (shape[i][j]) {
                    if (row + i + 1 == ROWS) {
                        return true;
                    } else if (frame[row < 0 ? 0 : row + i + 1][col + j] < 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean verifyNotAllowed(int maximumY, int minimumX, int maximumX, boolean[][] shape, int row, int col) {

        for (int i = 0; i < maximumY; i++) {
            for (int j = minimumX; j < maximumX; j++) {
                if (shape[i][j]) {
                    if (frame[row < 0 ? 0 : row + i][col + j] < 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean verifyPieceIsLandingOnTheFrame(int row, boolean[][] shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j]) {
                    if (row + i >= ROWS) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean verifyPieceOutsideFrame(int col, boolean[][] shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j]) {
                    if (col + j < 0 || col + j >= COLUMNS) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getClearedLines(boolean[] allRows) {

        int clearedLines = 0;

        for (boolean row : allRows) {
            if (row) {
                clearedLines++;
            }
        }

        return clearedLines;
    }

    public int[][] getFrame() {

        int[][] copyOfFrame = new int[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                copyOfFrame[i][j] = frame[i][j];
            }
        }
        return copyOfFrame;
    }

    public boolean[] getPreviousAllRows() {
        return previousAllRows;
    }

    public boolean[] lockPieceAndClearLines() {

        boolean[] fullLines = new boolean[ROWS];
        int numberOfRowsFilled = 0;

        for (int i = 0; i < ROWS; i++) {

            lockBlock(i);

            fullLines[i] = isLineFull(i);

            if (fullLines[i]) {
                numberOfRowsFilled++;
            }
        }

        if (numberOfRowsFilled != 0) {
            clearLines(fullLines);
            return previousAllRows = fullLines;
        }

        return previousAllRows = null;
    }

    public void restart() {
        previousAllRows = null;
        frame = new int[ROWS][COLUMNS];
    }

    public PiecePosition setPieceAtDesiredPosition(Piece piece) {

        Position position = piece.getPosition();

        int col = position.getX();
        int row = position.getY();
        int gap = 3;

        if (col == COLUMNS || row == ROWS || col < -gap) {
            return PiecePosition.NOT_ALLOWED;
        }

        boolean[][] shape = piece.getShape();

        final boolean starterPosition = piece.isStarterPosition();

        if (starterPosition) {

            row = setupFirstPosition(position, row, shape);

        } else {

            if (verifyPieceOutsideFrame(col, shape)) {
                return PiecePosition.NOT_ALLOWED;
            }

            if (verifyPieceIsLandingOnTheFrame(row, shape)) {
                return PiecePosition.IS_LANDING;
            }
        }

        int minimumX = (col < 0) ? (-1) * col : 0;
        int maximumX = (col > ((COLUMNS - 1) - gap)) ? COLUMNS - col : 4;
        int maximumY = (row > ((ROWS - 1) - gap)) ? ROWS - row : 4;

        if (verifyNotAllowed(maximumY, minimumX, maximumX, shape, row, col)) {
            return PiecePosition.NOT_ALLOWED;
        }

        if (!starterPosition) {
            clearPieceFromFrame();
        }

        fillFrameWithPieceContents(piece, col, row, shape, minimumX, maximumX, maximumY);

        if (verifyIsLanding(col, row, shape, minimumX, maximumX, maximumY)) {
            return PiecePosition.IS_LANDING;
        }

        return PiecePosition.ALLOWED;
    }

    public PiecePosition setPieceAtGround(Piece piece) {

        boolean moved;
        PiecePosition position;

        do {

            moved = piece.moveDown();

            if (!moved) {
                return PiecePosition.NOT_ALLOWED;
            }
            position = setPieceAtDesiredPosition(piece);

        } while (position == PiecePosition.ALLOWED);

        return position;
    }
}