package com.lincolnpomper.tetris.graphics.mainscreen;

import com.lincolnpomper.tetris.graphics.BlockFactory;
import java.util.Random;

public class AnimatedBlock {

    public static final int AMOUNT = 10;
    public static final int AMOUNT_SLOW = 5;
    public static final int MAXIMUM_TRANSPARENCY = 100;
    public static final int MINIMUM_TRANSPARENCY = 0;
    private final static Random RANDOM = new Random();
    boolean isBig = false;
    private int amount = AMOUNT;
    private boolean animating;
    private int blockImageNumber;
    private int currentMaximumTransparency;
    private int currentMinimumTransparency;
    private boolean isIncreasing;
    private boolean isMaximumVisibility;
    private int transparency;
    private boolean visible;

    public AnimatedBlock(boolean slow) {

        this.blockImageNumber = getRandomBlockNumber();
        isIncreasing = true;
        animating = false;
        visible = false;
        transparency = MINIMUM_TRANSPARENCY;

        if (slow) {
            this.amount = AMOUNT_SLOW;
            this.currentMinimumTransparency = this.amount;
            this.currentMaximumTransparency = MAXIMUM_TRANSPARENCY / 2;
        } else {
            this.currentMaximumTransparency = MAXIMUM_TRANSPARENCY;
        }
    }

    private void changeBlockNumber() {
        this.blockImageNumber = getRandomBlockNumber();
    }

    private static int getRandomBlockNumber() {
        return (int) (RANDOM.nextFloat() * BlockFactory.NUMBER_OF_PIECES);
    }

    public int getAmount() {
        return amount;
    }

    public int getBlockImageNumber() {
        return blockImageNumber;
    }

    public int getTransparency() {
        return transparency;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public boolean isAtMaximumVisibility() {
        return isMaximumVisibility;
    }

    public boolean isTransparent() {
        return transparency != 1f;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        changeBlockNumber();
    }

    public void processTransparency() {

        isMaximumVisibility = false;

        if (visible && animating) {

            if (isIncreasing) {
                transparency += amount;
            } else {
                transparency -= amount;
            }

            if (transparency >= currentMaximumTransparency) {
                transparency = currentMaximumTransparency;

                isIncreasing = false;
                isMaximumVisibility = true;
            }

            if (transparency <= currentMinimumTransparency) {
                transparency = currentMinimumTransparency;

                isIncreasing = true;
                animating = false;
                visible = false;
            }
        }
    }

    public void setBig(boolean isBig) {
        this.isBig = isBig;
    }
}
