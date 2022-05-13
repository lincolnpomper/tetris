package com.lincolnpomper.tetris.graphics.mainscreen;


import java.util.ArrayList;
import java.util.List;

public class AnimatedBlocksFrame {

    public int column;
    public int row;
    private AnimatedBlock[][] blocks;

    public AnimatedBlocksFrame(int row, int column) {
        this(row, column, false);
    }

    public AnimatedBlocksFrame(int row, int column, boolean slow) {

        this.row = row;
        this.column = column;

        blocks = new AnimatedBlock[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                blocks[i][j] = new AnimatedBlock(slow);
            }
        }
    }

    public AnimatedBlock get(int row, int column) {
        return blocks[row][column];
    }

    public Integer[] getTransparencyValues() {

        List<Integer> list = new ArrayList<>();
        int amount = blocks[0][0].getAmount();

        int transparency = amount;

        while(transparency < 100){
            list.add(transparency);
            transparency += amount;
        }

        Integer[] intArray = new Integer[list.size()];
        return list.toArray(intArray);
    }

    public int getVisibleBlocks() {

        int count = 0;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {

                if (blocks[i][j].isVisible()) {
                    count++;
                }
            }
        }

        return count;
    }
}
