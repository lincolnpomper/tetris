package com.lincolnpomper.tetris.animation;

import com.lincolnpomper.tetris.graphics.BlockFactory;
import com.lincolnpomper.tetris.util.AvailableResolutions;
import com.lincolnpomper.tetris.util.Resolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class BlockTestTool extends JFrame implements KeyListener {

    private final static Font FONT_INFO_BODY = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private final static String[][] INFO_TEXT = {{"[b]", "toggle backGround"}, {"[i]", "increase 3D"}, {"[d]", "decrease 3D"}};
    private final static int LINE_HEIGHT_INFO = 17;
    private final static int SCREEN_HEIGHT = 800;
    private final static int SCREEN_WIDTH = 1400;
    private Color[] backGroundColors = {Color.black, Color.gray, Color.white};
    private Color backGroundColor = backGroundColors[0];
    private BufferedImage[][] blockImages;
    private float blockThicknessFactor = BlockFactory.DEFAULT_BLOCK_THICKNESS_FACTOR;
    private Graphics graphics;
    private BufferStrategy strategy;

    public BlockTestTool() {

        buildImages();

        setBackground(Color.black);
        addKeyListener(this);
        setTitle("Block Tests");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    private void buildImages() {

        blockImages = new BufferedImage[BlockFactory.NUMBER_OF_PIECES][AvailableResolutions.RESOLUTIONS.length];
        int pos = 0;

        for (Resolution resolution : AvailableResolutions.RESOLUTIONS) {
            blockImages[pos++] = BlockFactory.getInstance(blockThicknessFactor).createBlockImages(resolution.getBlockSize());
        }
    }

    private void drawInfo(Graphics graphics) {

        int COLUMN_SPACE = 100;

        graphics.setColor(Color.blue);
        graphics.setFont(FONT_INFO_BODY);

        int xPos = 150;
        int yPos = SCREEN_HEIGHT - (4 * LINE_HEIGHT_INFO);

        for (String[] text : INFO_TEXT) {
            graphics.drawString(text[0], xPos + 1, yPos + 1);
            graphics.drawString(text[1], xPos + 1 + COLUMN_SPACE, yPos + 1);
            yPos += LINE_HEIGHT_INFO;
        }
    }

    private void endDraw() {
        graphics.dispose();
        strategy.show();
    }

    private void startDraw() {
        strategy = this.getBufferStrategy();
        graphics = strategy.getDrawGraphics();
    }

    private void toggleBackGroundColor() {
        if (backGroundColor == backGroundColors[0]) {
            backGroundColor = backGroundColors[1];
        } else if (backGroundColor == backGroundColors[1]) {
            backGroundColor = backGroundColors[2];
        } else if (backGroundColor == backGroundColors[2]) {
            backGroundColor = backGroundColors[0];
        }
    }

    public void draw() {

        if (blockImages == null) {
            return;
        }

        startDraw();

        graphics.setColor(backGroundColor);
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        int maxSize = AvailableResolutions.RESOLUTIONS[AvailableResolutions.RESOLUTIONS.length - 1].getBlockSize() + 4;

        int x = maxSize;
        int y = maxSize;
        final int xBigBlock = maxSize * (blockImages[0].length + 1);
        final int yBigBlock1 = maxSize;
        int pos = 0;
        int posBigBlock = 2;
        final int BIG_BLOCK_SiZE = AvailableResolutions.RESOLUTIONS[0].getBlockSize() * 10;

        for (BufferedImage[] images : blockImages) {
            for (BufferedImage image : images) {

                graphics.drawImage(image, x, y, this);

                if (pos++ == posBigBlock) {
                    graphics.drawImage(image, xBigBlock, yBigBlock1, BIG_BLOCK_SiZE, BIG_BLOCK_SiZE, this);
                }

                x += maxSize;
            }
            y += maxSize;
            x = maxSize;
        }

        drawInfo(graphics);

        endDraw();
    }

    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        /* backGroundColor */
        if (keyCode == KeyEvent.VK_B) {
            toggleBackGroundColor();
        }

        boolean changes = false;

        /* Thickness Factor */
        if (keyCode == KeyEvent.VK_D) {

            blockThicknessFactor -= 0.01f;
            if (blockThicknessFactor < BlockFactory.MINIMUM_BLOCK_THICKNESS_FACTOR) {
                blockThicknessFactor = BlockFactory.MINIMUM_BLOCK_THICKNESS_FACTOR;
            } else {
                changes = true;
            }

        } else if (keyCode == KeyEvent.VK_I) {
            blockThicknessFactor += 0.01f;

            if (blockThicknessFactor > BlockFactory.MAXIMUM_BLOCK_THICKNESS_FACTOR) {
                blockThicknessFactor = BlockFactory.MAXIMUM_BLOCK_THICKNESS_FACTOR;
            } else {
                changes = true;
            }
        }

        if (changes) {
            buildImages();
        }

        draw();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] s) {

        BlockTestTool frame = new BlockTestTool();
        frame.createBufferStrategy(2);
        frame.setVisible(true);
        frame.draw();
    }

}