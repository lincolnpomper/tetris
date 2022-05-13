package com.lincolnpomper.tetris.gui;

import com.lincolnpomper.tetris.model.ModeProgression;
import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.util.Resolution;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;

public class PointsPanel extends JComponent {

    private static final int BACK_TO_BACK_TETRIS = 12;
    private static final int COMBO_DOUBLE = 2;
    private static final int COMBO_TETRIS = 8;
    private static final int COMBO_TRIPLE = 3;
    private static final int LEVELS_BY_DOUBLE = 3;
    private static final int LEVELS_BY_SINGLE = 1;
    private static final int LEVELS_BY_TETRIS = 10;
    private static final int LEVELS_BY_TRIPLE = 6;
    private static final int LINES_DOUBLE = 2;
    private static final int LINES_SINGLE = 1;
    private static final int LINES_TETRIS = 4;
    private static final int LINES_TRIPLE = 3;
    private boolean backToBackTetris;
    private boolean combo;
    private int lastLinesCleared;
    private int level = 0;
    private int lineHeight;
    private ModeProgression modeProgression;
    private int round = 1;

    public PointsPanel(ModeProgression modeProgression, Resolution resolution) {
        this.modeProgression = modeProgression;
        this.lineHeight = resolution.getBlocksPerHeight();
    }

    private void drawText(Graphics graphics, String roundText, String levelText, boolean isShadow) {

        int yPos = lineHeight;
        int xPos = 6;

        if (isShadow) {

            xPos++;
            yPos++;

            graphics.setFont(FontFactory.getInstance().fontPoints);
            graphics.setColor(Color.black);
        } else {
            graphics.setFont(FontFactory.getInstance().fontPoints);
            graphics.setColor(Color.white);
        }

        graphics.drawString(roundText, xPos, yPos);
        yPos += lineHeight;
        graphics.drawString(levelText, xPos, yPos);
    }

    private int getLevelFromBackToBack(int linesCleared) {
        return this.lastLinesCleared == LINES_TETRIS && linesCleared == LINES_TETRIS ? BACK_TO_BACK_TETRIS : 0;
    }

    private int getLevelFromCombo(int linesCleared) {

        int level = 0;

        if (lastLinesCleared == LINES_DOUBLE || lastLinesCleared == LINES_TRIPLE || lastLinesCleared == LINES_TETRIS) {

            switch (linesCleared) {

                case LINES_DOUBLE:
                    level = COMBO_DOUBLE;
                    break;
                case LINES_TRIPLE:
                    level = COMBO_TRIPLE;
                    break;
                case LINES_TETRIS:
                    level = COMBO_TETRIS;
                    break;
            }
        }

        return level;
    }

    private int getLevelFromLines(int linesCleared) {

        int level = 0;

        switch (linesCleared) {
            case LINES_SINGLE:
                level = LEVELS_BY_SINGLE;
                break;
            case LINES_DOUBLE:
                level = LEVELS_BY_DOUBLE;
                break;
            case LINES_TRIPLE:
                level = LEVELS_BY_TRIPLE;
                break;
            case LINES_TETRIS:
                level = LEVELS_BY_TETRIS;
                break;
        }

        return level;
    }

    private int getLevelFromPieceLanded() {

        int level = 0;

        if (!modeProgression.isGameModeNormal()) {
            boolean lastLevel = modeProgression.getNextRound().getRequirement() - 1 == this.level;
            if (!lastLevel) {
                level = 1;
            }
        }

        return level;
    }

    private boolean wasBackToBackTetris(int linesCleared) {
        return this.lastLinesCleared == LINES_TETRIS && linesCleared == LINES_TETRIS;

    }

    private boolean wasCombo(int linesCleared) {
        return (this.lastLinesCleared == LINES_DOUBLE || this.lastLinesCleared == LINES_TRIPLE || this.lastLinesCleared == LINES_TETRIS) &&
                (linesCleared == LINES_DOUBLE || linesCleared == LINES_TRIPLE || linesCleared == LINES_TETRIS);

    }

    public int getRound() {
        return round;
    }

    public boolean isBackToBackTetris() {
        return backToBackTetris;
    }

    public boolean isCombo() {
        return combo;
    }

    public boolean isLastRound() {
        return modeProgression.getNextRound() == null;
    }

    public void paintComponent(Graphics graphics) {

        final String roundName = modeProgression.getCurrentRoundName();

        String roundText = "Round " + roundName;
        String levelText = "Level " + level;

        drawText(graphics, roundText, levelText, true);
        drawText(graphics, roundText, levelText, false);
    }

    public void restart() {
        level = 0;
        round = 1;
        combo = false;
        repaint();
    }

    public boolean updateLevel(int linesCleared) {

        boolean gainedRound = false;

        level += getLevelFromPieceLanded();

        if (linesCleared > 0) {

            level += getLevelFromLines(linesCleared);

            level += getLevelFromCombo(linesCleared);

            level += getLevelFromBackToBack(linesCleared);

            this.combo = wasCombo(linesCleared);

            this.backToBackTetris = wasBackToBackTetris(linesCleared);

            if (modeProgression.getNextRound().getRequirement() <= level) {
                modeProgression.advanceRound();
                gainedRound = true;
            }

        } else {

            this.combo = false;
            this.backToBackTetris = false;
        }

        lastLinesCleared = linesCleared;

        repaint();

        return gainedRound;
    }
}