package com.lincolnpomper.tetris.graphics.mainscreen;

import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.MainScreenAnimationListener;
import com.lincolnpomper.tetris.core.Position;
import com.lincolnpomper.tetris.graphics.BlockFactory;
import com.lincolnpomper.tetris.graphics.BlurDegree;
import com.lincolnpomper.tetris.graphics.BlurDegreeFactory;
import com.lincolnpomper.tetris.graphics.EffectsFactory;
import com.lincolnpomper.tetris.graphics.animation.TextAnimation;
import com.lincolnpomper.tetris.gui.MenuPanel;
import com.lincolnpomper.tetris.util.Resolution;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.Phaser;
import javax.swing.Timer;

public class MainScreenAnimation implements ActionListener {

    public static final int DELAY = 170;
    private final static int MAXIMUM_INTRO_ITERATIONS = 100;
    private final static int MAXIMUM_SIMULTANEOUSLY_VISIBLE_BLOCKS = 10;
    private final static int MAXIMUM_SIMULTANEOUSLY_VISIBLE_BLOCKS_INTRO = 50;
    private static final Random rand = new Random();
    private AnimatedBlocksFrame animatedBlocks;
    private AnimatedBlocksFrame animatedBlocksIntro;
    private Image background;
    private BlockImagesWithTransparency blockImages;
    private BlockImagesWithTransparency blockImagesBigger;
    private MainScreenAnimationListener main;
    private Resolution resolution;
    private int size;
    private int sizeBigger;
    private TextAnimation tetrisAnimation;
    private Timer timer;
    private int timerCounterForIntro;

    public MainScreenAnimation(MainScreenAnimationListener main, Image background, Resolution resolution) {

        this.main = main;
        this.background = background;
        this.resolution = resolution;

        this.size = resolution.getBlockSize() * 2;
        this.sizeBigger = resolution.getBlockSize() * 3;

        int blocksIntroPerRow = (resolution.getWidth() / size) - 2;
        int blocksIntroPerColumn = (resolution.getStandardizedHeight() / size) - 2;
        int blocksPerRow = (resolution.getWidth() / sizeBigger) - 2;
        int blocksPerColumn = (resolution.getStandardizedHeight() / sizeBigger) - 2;

        Phaser phaser = new Phaser();
        phaser.register();

        animatedBlocksIntro = new AnimatedBlocksFrame(blocksIntroPerRow, blocksIntroPerColumn);
        blockImages = new BlockImagesWithTransparency(BlockFactory.getInstance().createBlockImagesPrimary(size), animatedBlocksIntro.getTransparencyValues(), false, "blockImages", phaser);

        animatedBlocks = new AnimatedBlocksFrame(blocksPerRow, blocksPerColumn, true);
        blockImagesBigger = new BlockImagesWithTransparency(BlockFactory.getInstance().createBlockImagesPrimary(sizeBigger), animatedBlocks.getTransparencyValues(), true, "blockImagesBigger", phaser);

        blockImages.start();
        blockImagesBigger.start();

        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();

        createTetrisAnimation(resolution);
    }

    private void createTetrisAnimation(Resolution resolution) {

        Font font = FontFactory.getInstance().fontTetrisLogo;
        BlurDegree blurDegree = BlurDegreeFactory.getInstance().tetrisLogo;

        BufferedImage[] images = EffectsFactory.getInstance().buildImages("Tetris", Color.orange, font, blurDegree);

        int tetrisX = resolution.getWidth() / 2 - images[0].getWidth() / 2;
        int tetrisY = (int) (resolution.getStandardizedHeight() * 0.025f);
        final Position position = new Position(tetrisX, tetrisY);

        tetrisAnimation = new TextAnimation(position, images, DELAY, null);
    }

    private void drawAnimatedBlocks(Graphics2D graphics, AnimatedBlocksFrame animatedBlocks, BlockImagesWithTransparency blockImages, int size) {

        int x = size;
        int y = size;

        for (int i = 0; i < animatedBlocks.row; i++) {
            for (int j = 0; j < animatedBlocks.column; j++) {

                if (animatedBlocks.get(i, j).isAnimating()) {

                    int imageNumber = animatedBlocks.get(i, j).getBlockImageNumber();
                    BufferedImage imageBlock;

                    int transparency = 100;
                    if (animatedBlocks.get(i, j).isTransparent()) {
                        transparency = animatedBlocks.get(i, j).getTransparency();
                    }
                    imageBlock = blockImages.get(imageNumber, transparency);

                    graphics.drawImage(imageBlock, x, y, null);
                }

                y += size;
            }

            y = size;
            x += size;
        }
    }

    private BufferedImage drawBackgroundImage() {

        BufferedImage backgroundImage = new BufferedImage(resolution.getWidth(), resolution.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = backgroundImage.createGraphics();

        graphics.drawImage(background, 0, 0, null);
        drawAnimatedBlocks(graphics, animatedBlocksIntro, blockImages, size);
        drawAnimatedBlocks(graphics, animatedBlocks, blockImagesBigger, sizeBigger);
        graphics.drawImage(tetrisAnimation.getDraw(), tetrisAnimation.getPosition().getX(), tetrisAnimation.getPosition().getY(), null);

        return backgroundImage;
    }

    private static int generateNumber(int seed) {
        return (int) (rand.nextFloat() * seed);
    }

    private void updateAnimatedBlocks(AnimatedBlocksFrame animatedBlocks, boolean newBlock) {

        int i;
        int j;

        if (newBlock) {

            boolean isBlocked;

            do {

                i = generateNumber(animatedBlocks.row);
                j = generateNumber(animatedBlocks.column);

                if (!animatedBlocks.get(i, j).isVisible()) {
                    animatedBlocks.get(i, j).setVisible(true);
                    animatedBlocks.get(i, j).setAnimating(true);
                    animatedBlocks.get(i, j).setBig(MenuPanel.isMenuEnabled());
                    break;
                }

                isBlocked = animatedBlocks.get(i, j).isVisible();

            } while (isBlocked);
        }

        for (i = 0; i < animatedBlocks.row; i++) {
            for (j = 0; j < animatedBlocks.column; j++) {

                boolean shouldChange = generateNumber(40) < 1;

                if (!animatedBlocks.get(i, j).isAtMaximumVisibility() || shouldChange) {
                    animatedBlocks.get(i, j).processTransparency();
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        boolean newBlock = false;

        if (!MenuPanel.isMenuEnabled()) {

            newBlock = generateNumber(10) < 8; // 80%
            if (animatedBlocksIntro.getVisibleBlocks() > MAXIMUM_SIMULTANEOUSLY_VISIBLE_BLOCKS_INTRO) {
                newBlock = false;
            }
        }
        updateAnimatedBlocks(animatedBlocksIntro, newBlock);

        newBlock = false;
        if (MenuPanel.isMenuEnabled()) {
            if (animatedBlocks.getVisibleBlocks() <= MAXIMUM_SIMULTANEOUSLY_VISIBLE_BLOCKS) {
                newBlock = generateNumber(10) < 4; // 40%
            }
        }
        updateAnimatedBlocks(animatedBlocks, newBlock);

        main.updateBackground(drawBackgroundImage());

        if (timerCounterForIntro < MAXIMUM_INTRO_ITERATIONS) {
            timerCounterForIntro++;
        }

        if (timerCounterForIntro == MAXIMUM_INTRO_ITERATIONS) {
            main.introAnimationEnded();
        }
    }

    public boolean isIntroEnded() {
        return timerCounterForIntro == MAXIMUM_INTRO_ITERATIONS;
    }

    public void prematurelyEndIntro() {
        timerCounterForIntro = MAXIMUM_INTRO_ITERATIONS;
    }

    public void start() {

        timer = new Timer(DELAY, this);
        timer.start();

        tetrisAnimation.setCycle(true);
        tetrisAnimation.start();
    }

    public void stop() {
        timer.stop();
    }
}