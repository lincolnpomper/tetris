package com.lincolnpomper.tetris.gui;

import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.util.Messages;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class ReadyAnimationComponent extends Timer {

    private final static int DELAY = 100;
    private final static int FRAMES = 20;
    private final static String TEXT_READY = Messages.getString("GamePanel.ready");
    private int frame;
    private int leftRandomColor;
    private GradientPaint readyGradient;
    private int readyHeight;
    private int readyPositionX;
    private int readyPositionY;
    private int readyWidth;
    private int rightRandomColor;

    public ReadyAnimationComponent(GamePanel parent, int borderSize) {

        super(DELAY, parent);

        readyPositionX = borderSize + borderSize;
        readyPositionY = borderSize + (4 * borderSize);
        readyWidth = borderSize + (7 * borderSize);
        readyHeight = borderSize + (5 * borderSize);

        reset();
    }

    private GradientPaint createRandomColorsGradient(int positionX, int positionY, int width, int height) {

        int[] c = new int[6];

        c[0] = leftRandomColor * 30;
        c[1] = leftRandomColor * 15;
        c[2] = 50 - (rightRandomColor * 10);
        c[3] = 100;
        c[4] = 255 - rightRandomColor * 50;
        c[5] = frame * 12;

        for (int i = 0; i < c.length; i++) {
            if (c[i] < 0) {
                c[i] = 0;
            }
            if (c[i] > 255) {
                c[i] = 255;
            }
        }

        Color color1 = new Color(c[0], c[1], c[2]);
        Color color2 = new Color(c[3], c[4], c[5]);

        return new GradientPaint(positionX, positionY, color1, width, height, color2);
    }

    private void reset() {
        leftRandomColor = 1 + (int) (new Random().nextFloat() * 6);
        rightRandomColor = 3 + (int) (new Random().nextFloat() * 2);
        frame = 0;
    }

    public void drawAnimationFrame(Graphics g, int gamePanelWidth, int gamePanelHeight) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(readyGradient);
        g2.fillRoundRect(readyPositionX, readyPositionY, readyWidth, readyHeight, 20, 20);

        g.setFont(FontFactory.getInstance().fontReady);

        g.setColor(Color.lightGray);
        g.drawString(TEXT_READY, 1 + (int) (gamePanelWidth * .26), 1 + (int) (gamePanelHeight * .4));

        g.setColor(Color.white);
        g.drawString(TEXT_READY, (int) (gamePanelWidth * .26), (int) (gamePanelHeight * .4));

        this.start();
    }

    public void increment() {

        frame++;

        if (frame != FRAMES) {
            readyGradient = createRandomColorsGradient(readyPositionX, readyPositionY, readyWidth, readyHeight);
        } else {
            reset();
            stop();
        }
    }
}