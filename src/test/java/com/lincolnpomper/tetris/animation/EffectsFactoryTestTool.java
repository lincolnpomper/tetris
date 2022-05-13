package com.lincolnpomper.tetris.animation;

import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.graphics.BlurDegreeFactory;
import com.lincolnpomper.tetris.graphics.ColorChange;
import com.lincolnpomper.tetris.util.AvailableResolutions;
import com.lincolnpomper.tetris.util.Resolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.Timer;

public class EffectsFactoryTestTool extends JFrame implements KeyListener, ActionListener {

    private final static Color BACKGROUND_HELP_COLOR = new Color(170, 160, 170, 200);
    private final static Font FONT_INFO_BODY = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private final static Font FONT_INFO_TITLE = new Font(Font.MONOSPACED, Font.BOLD, 22);
    private final static String[][] HELP_TEXTS =
            {{"increase blur intensity", "UP"}, {"decrease blur intensity", "DOWN"}, {"increase blur range", "RIGHT"},
                    {"decrease blur range", "LEFT"}, {"increase font size", "PAGE_UP"}, {"decrease font size", "PAGE_DOWN"},
                    {"increase outline", "["}, {"decrease outline", "]"},
                    {"set red", "1"}, {"set green", "2"}, {"set blue", "3"}, {"set alpha", "4"}, {"decrease current color", "7"},
                    {"increase current color", "8"}, {"decrease more current color", "9"}, {"increase more current color", "0"},
                    {"toggle backGround", "b"}, {"toggle font", "f"}, {"toggle outline", "o"}, {"show this help", "F1"},
                    {"draw", "ENTER"}};
    private final static int LINE_HEIGHT_HELP = 22;
    private final static int LINE_HEIGHT_INFO = 17;
    private final static int SCREEN_HEIGHT = 768;
    private final static int SCREEN_WIDTH = 1024;
    private Color[] backGroundColors = {Color.black, Color.gray, Color.white};
    private Color backGroundColor = backGroundColors[0];
    private BufferedImage currentImage;
    private Graphics graphics;
    private int imagePosition = 0;
    private BufferedImage[] images;
    private boolean showHelp = false;
    private BufferStrategy strategy;
    private Timer timer;

    public EffectsFactoryTestTool() {

        Resolution resolution = AvailableResolutions.getDefaultResolution();
        BlurDegreeFactory.setResolution(resolution);
        FontFactory.buildFontsByResolution(resolution);

        timer = new Timer(80, this);

        buildImages();

        setBackground(Color.black);
        addKeyListener(this);
        setTitle("Effects Factory Tests");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        timer.start();
    }

    private void buildImages() {
        images = EffectsFactoryWithParameters.getInstance().buildImages("Tetris", 10);
    }

    private void drawHelp(Graphics graphics) {

        int COLUMN_SPACE = 300;

        graphics.setColor(BACKGROUND_HELP_COLOR);
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        int x = 50;
        int y = 80;

        graphics.setFont(FONT_INFO_TITLE);
        graphics.setColor(Color.lightGray.darker());
        graphics.drawString("Keyboard actions", x + 1, y + 1);

        graphics.setColor(Color.black);
        graphics.drawString("Keyboard actions", x, y);

        graphics.setFont(FONT_INFO_BODY);
        y += LINE_HEIGHT_HELP / 2;

        for (String[] line : HELP_TEXTS) {

            y += LINE_HEIGHT_HELP;

            graphics.setColor(Color.lightGray);
            graphics.drawString(line[0], x + 1, y + 1);
            graphics.drawString(line[1], x + 1 + COLUMN_SPACE, y + 1);

            graphics.setColor(Color.black);
            graphics.drawString(line[0], x, y);
            graphics.drawString(line[1], x + COLUMN_SPACE, y);
        }
    }

    private void drawInfo(Graphics graphics) {

        graphics.setColor(Color.blue);
        graphics.setFont(FONT_INFO_BODY);

        int xPos = 150;
        int yPos = SCREEN_HEIGHT - (5 * LINE_HEIGHT_INFO);

        graphics.drawString("Font Size: " + EffectsFactoryWithParameters.getInstance().getFontSize(), xPos, yPos);
        yPos += LINE_HEIGHT_INFO;
        graphics.drawString("Blur Intensity: " + EffectsFactoryWithParameters.getInstance().getIntensity() + ", Blur Range: " +
                EffectsFactoryWithParameters.getInstance().getRange(), xPos, yPos);
        yPos += LINE_HEIGHT_INFO;
        graphics.drawString("Outline: " + EffectsFactoryWithParameters.getInstance().getOutlineToggle() + ", Stroke: " +
                EffectsFactoryWithParameters.getInstance().getStroke(), xPos, yPos);
        yPos += LINE_HEIGHT_INFO;
        graphics.drawString("Color: " + getColorString(), xPos, yPos);
        yPos += LINE_HEIGHT_INFO;
        graphics.drawString("Press F1 for keys", xPos, yPos);
    }

    private void endDraw() {
        graphics.dispose();
        strategy.show();
    }

    private String getColorString() {
        final Color color = EffectsFactoryWithParameters.getInstance().getColor();
        return "[r=" + color.getRed() + ", g=" + color.getGreen() + ", b=" + color.getBlue() + ", a=" + color.getAlpha() + "]";
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (imagePosition == images.length - 1) {
            imagePosition = 0;
            this.currentImage = images[imagePosition];
            timer.stop();
        } else {
            imagePosition++;
            this.currentImage = images[imagePosition];
            draw();
        }
    }

    public void draw() {

        if (currentImage == null) {
            return;
        }

        startDraw();

        graphics.setColor(backGroundColor);
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        final int x = this.getWidth() / 2 - currentImage.getWidth() / 2;
        final int y = this.getHeight() / 2 - currentImage.getHeight() / 2;

        graphics.drawImage(currentImage, x, y, this);

        drawInfo(graphics);

        if (showHelp) {
            drawHelp(graphics);
        }

        endDraw();
    }

    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        /* Blur */
        if (keyCode == KeyEvent.VK_UP) {
            EffectsFactoryWithParameters.getInstance().increaseIntensity();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            EffectsFactoryWithParameters.getInstance().decreaseIntensity();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            EffectsFactoryWithParameters.getInstance().increaseRange();
        } else if (keyCode == KeyEvent.VK_LEFT) {
            EffectsFactoryWithParameters.getInstance().decreaseRange();
        }

        /* Font */
        else if (keyCode == KeyEvent.VK_PAGE_UP) {
            EffectsFactoryWithParameters.getInstance().increaseFont();
        } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
            EffectsFactoryWithParameters.getInstance().decreaseFont();
        }

        /* Colors */
        else if (keyCode == KeyEvent.VK_7) {
            EffectsFactoryWithParameters.getInstance().decreaseColorIntensity(1);
        } else if (keyCode == KeyEvent.VK_8) {
            EffectsFactoryWithParameters.getInstance().increaseColorIntensity(1);
        } else if (keyCode == KeyEvent.VK_9) {
            EffectsFactoryWithParameters.getInstance().decreaseColorIntensity(10);
        } else if (keyCode == KeyEvent.VK_0) {
            EffectsFactoryWithParameters.getInstance().increaseColorIntensity(10);
        }

        /* Outline */
        else if (keyCode == KeyEvent.VK_OPEN_BRACKET) {
            EffectsFactoryWithParameters.getInstance().decreaseStroke();
        } else if (keyCode == KeyEvent.VK_CLOSE_BRACKET) {
            EffectsFactoryWithParameters.getInstance().increaseStroke();
        }

        /* Color */
        else if (keyCode == KeyEvent.VK_1) {
            EffectsFactoryWithParameters.getInstance().setColorChange(ColorChange.red);
        } else if (keyCode == KeyEvent.VK_2) {
            EffectsFactoryWithParameters.getInstance().setColorChange(ColorChange.green);
        } else if (keyCode == KeyEvent.VK_3) {
            EffectsFactoryWithParameters.getInstance().setColorChange(ColorChange.blue);
        } else if (keyCode == KeyEvent.VK_4) {
            EffectsFactoryWithParameters.getInstance().setColorChange(ColorChange.alpha);
        }

        /* backGroundColor */
        else if (keyCode == KeyEvent.VK_B) {
            toggleBackGroundColor();
        } else if (keyCode == KeyEvent.VK_F) {
            EffectsFactoryWithParameters.getInstance().toggleFont();
        } else if (keyCode == KeyEvent.VK_O) {
            EffectsFactoryWithParameters.getInstance().toggleOutline();
        }

        /* help */
        else if (keyCode == KeyEvent.VK_F1) {
            showHelp = !showHelp;
        }

        /* draw */
        else if (keyCode == KeyEvent.VK_ENTER) {
            timer.stop();
            this.imagePosition = 0;
            buildImages();
            timer.start();
        }

        if (keyCode != KeyEvent.VK_ENTER) {
            this.imagePosition = images.length - 1;
            this.currentImage = images[imagePosition];

            draw();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] s) {

        EffectsFactoryTestTool frame = new EffectsFactoryTestTool();
        frame.createBufferStrategy(2);
        frame.setVisible(true);
        frame.draw();
    }

}