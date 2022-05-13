package com.lincolnpomper.tetris.util;

public class Resolution {

    private final int width;
    private final int height;
    private final int blockSize;
    private final boolean isDefault;

    public Resolution(int width, int height, int blockSize, boolean isDefault) {

        this.width = width;
        this.height = height;
        this.blockSize = blockSize;
        this.isDefault = isDefault;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getHeight() {
        return height;
    }

    public int getStandardizedHeight() {
        return blockSize * getBlocksPerHeight();
    }

    public int getBlocksPerHeight() {
        return 30;
    }

    public String getResolutionKeyName() {
        return "" + width + "x" + height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isDefault() {
        return isDefault;
    }
}