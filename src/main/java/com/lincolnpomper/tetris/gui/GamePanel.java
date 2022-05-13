package com.lincolnpomper.tetris.gui;

import com.lincolnpomper.tetris.BlocksImagesFacade;
import com.lincolnpomper.tetris.RoundChangedListener;
import com.lincolnpomper.tetris.core.Frame;
import com.lincolnpomper.tetris.core.Game;
import com.lincolnpomper.tetris.core.Position;
import com.lincolnpomper.tetris.graphics.BlockFactory;
import com.lincolnpomper.tetris.graphics.animation.TextAnimation;
import com.lincolnpomper.tetris.graphics.animation.AnimationControlClearingLines;
import com.lincolnpomper.tetris.model.Piece;
import com.lincolnpomper.tetris.util.Resolution;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class GamePanel extends JComponent implements ActionListener {

    private static final int ENDING_TIME_DELAY = 50;
    private static final int NUMBER_OF_BRIGHTER_BLOCKS = 5;
    private static final int NUMBER_OF_BRIGHTER_LINES = 10;
    private static final int NUMBER_OF_DARKER_BLOCKS = 18;
    private static final int[] TRANSPARENT = new int[]{0, 0, 0, 128};
    private static final int clearingLinesTimeDelay = 35;
    private ArrayList<Position> allEndBlocks = null;
    private boolean animatingClearingLines = false;
    private AnimationControlClearingLines animationControl;
    private BlocksImagesFacade blocksImages;
    private BufferedImage[] brighterBlocks = null;
    private BufferedImage[] brighterLines = null;
    private BufferedImage[] darkerBlocks = null;
    private int endingGameBlocksGrayPainted;
    private int[][] frame;
    private int frameAnimatingClearingLines = 0;
    private Game game;
    private boolean gameRunning = false;
    private Image gray;
    private boolean isGameEnding = false;
    private boolean[] linesToAnimate = new boolean[]{true};
    private ReadyAnimationComponent readyComponent;
    private RoundChangedListener roundChangedListener;
    private boolean roundWantsToChange = false;
    private int size;
    private Timer timer;
    private BufferedImage transparentBackground = null;

    public GamePanel(BlocksImagesFacade blocksImages, Image[] borderImages, Resolution resolution, RoundChangedListener roundChangedListener) {

        this.size = resolution.getBlockSize();

        prepareImages();

        this.blocksImages = blocksImages;
        this.gray = this.blocksImages.getGray();
        this.roundChangedListener = roundChangedListener;

        animationControl = AnimationControlClearingLines.getInstance(size);

        setLayout(new BorderLayout());
        setBackground(Color.white);
        setDoubleBuffered(true);

        Border border = new Border(this.size, borderImages);
        setBorder(border);

        readyComponent = new ReadyAnimationComponent(this, this.size);
    }

    private void drawCleaningLinesTextAnimation(Graphics graphics) {

        ArrayList<TextAnimation> animations = animationControl.getAnimations();

        for (int i = 0; i < animations.size(); i++) {

            TextAnimation animation = animations.get(i);
            if (animation.isRunning()) {
                graphics.drawImage(animation.getDraw(), animation.getPosition().getX(), animation.getPosition().getY(), this);
            } else {
                animations.remove(animation);
            }
        }
    }

    private BufferedImage[] createBrighterBlocks() {

        int[] iArray;

        BufferedImage[] images = new BufferedImage[NUMBER_OF_BRIGHTER_BLOCKS];
        WritableRaster[] rasters = new WritableRaster[NUMBER_OF_BRIGHTER_BLOCKS];

        for (int i = 0; i < NUMBER_OF_BRIGHTER_BLOCKS; i++) {

            images[i] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            rasters[i] = images[i].getRaster();
        }

        int alpha = 120;

        for (int k = 0; k < NUMBER_OF_BRIGHTER_BLOCKS; k++) {

            alpha -= 20;

            iArray = new int[]{150, 150, 150, alpha};

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    rasters[k].setPixel(i, j, iArray);
                }
            }
        }

        return images;
    }

    private BufferedImage[] createBrighterLines() {

        BufferedImage[] images = new BufferedImage[NUMBER_OF_BRIGHTER_LINES];

        WritableRaster[] raster = new WritableRaster[images.length];
        int lineWidth = size * 10;

        for (int i = 0; i < images.length; i++) {

            images[i] = new BufferedImage(lineWidth, size, BufferedImage.TYPE_INT_ARGB);
            raster[i] = images[i].getRaster();
        }

        int color = 0;
        int[] iArray;

        for (int k = 0; k < images.length; k++) {

            color += 25;
            iArray = new int[]{color, color, color, 128};

            for (int i = 0; i < lineWidth; i++) {
                for (int j = 0; j < size; j++) {
                    raster[k].setPixel(i, j, iArray);
                }
            }
        }

        return images;
    }

    private BufferedImage[] createDarkerBlocks() {

        BufferedImage[] images = new BufferedImage[NUMBER_OF_DARKER_BLOCKS];
        WritableRaster[] rasterArray = new WritableRaster[NUMBER_OF_DARKER_BLOCKS];

        for (int i = 0; i < NUMBER_OF_DARKER_BLOCKS; i++) {

            images[i] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            rasterArray[i] = images[i].getRaster();
        }

        int alpha = 10;
        int[] iArray;

        for (int k = 0; k < NUMBER_OF_DARKER_BLOCKS; k++) {

            alpha += 5;
            iArray = new int[]{0, 0, 0, alpha};

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    rasterArray[k].setPixel(i, j, iArray);
                }
            }
        }

        return images;
    }

    private BufferedImage createTransparentBackgroundBlock() {

        int width = Frame.COLUMNS * size;
        int height = Frame.ROWS * size;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = bi.getRaster();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                raster.setPixel(i, j, TRANSPARENT);
            }
        }

        return bi;
    }

    private boolean currentPieceHasSolidBlockAt(int i, int j) {

        boolean isSolid = false;

        Piece currentGamePiece = game.getCurrentPiece();

        int x = currentGamePiece.getPosition().getX();
        int y = currentGamePiece.getPosition().getY();
        boolean[][] shape = currentGamePiece.getShape();

        if (j - x < 4 && j - x > -1 && i - y < 4 && i - y > -1) {
            isSolid = shape[i - y][j - x];
        }

        return isSolid;
    }

    private void drawAnimationFrameFinishingGame(Graphics g, int size) {

        if (frame != null) {

            drawFrameBackground(g);

            int i;
            int j;

            for (int x = 0; x < allEndBlocks.size(); x++) {

                Position p = allEndBlocks.get(x);
                i = p.getX();
                j = p.getY();

                if (x > endingGameBlocksGrayPainted) {
                    g.drawImage(blocksImages.getImageBy(frame[i][j]), this.size + (j * size), this.size + (i * size), size, size, this);
                } else {
                    g.drawImage(gray, this.size + (j * size), this.size + (i * size), size, size, this);
                }
            }
        }
    }

    private void drawBlock(Graphics graphics, BufferedImage blockImage, int size, int i, int j) {
        graphics.drawImage(blockImage, this.size + (j * size), this.size + (i * size), size, size, this);
    }

    private void drawFrameBackground(Graphics g) {
        g.drawImage(transparentBackground, size, size, this);
    }

    private void drawPieceLandingOrLanded(Graphics g, int size, int i, int j) {

        if (currentPieceHasSolidBlockAt(i, j)) {

            if (game.isPieceLandingStage()) {

                BufferedImage darkerBlock = darkerBlocks[game.getPieceLandingCurrentClick()];
                drawBlock(g, darkerBlock, size, i, j);

            } else if (game.isPieceLandedStage()) {

                if (game.getPieceLandingCurrentClick() < brighterBlocks.length) {
                    BufferedImage brighterBlock = brighterBlocks[game.getPieceLandingCurrentClick()];
                    drawBlock(g, brighterBlock, size, i, j);
                }
            }
        }
    }

    private int getClearedLinesLowestRow() {

        int row = -1;

        for (int i = 0; i < linesToAnimate.length; i++) {
            if (linesToAnimate[i]) {
                row = i;
            }
        }

        return row;
    }

    private void prepareImages() {

        transparentBackground = createTransparentBackgroundBlock();

        brighterLines = createBrighterLines();

        brighterBlocks = createBrighterBlocks();

        darkerBlocks = createDarkerBlocks();
    }

    public void actionPerformed(ActionEvent e) {

        if (readyComponent.isRunning()) {

            readyComponent.increment();

            if (!readyComponent.isRunning()) {
                game.readyFinished();
                gameRunning = true;
            }

            repaint();

        } else if (animatingClearingLines) {

            repaint();
            frameAnimatingClearingLines++;

            if (frameAnimatingClearingLines == NUMBER_OF_BRIGHTER_LINES) {
                frameAnimatingClearingLines = 0;
                animatingClearingLines = false;
                timer.stop();

                if (roundWantsToChange) {
                    roundChangedListener.nextRoundImage();
                    roundWantsToChange = false;
                }

                game.continueAfterPieceLanded();
            }

        } else if (allEndBlocks != null) {

            repaint();

            endingGameBlocksGrayPainted++;

            if (endingGameBlocksGrayPainted == allEndBlocks.size()) {
                timer.stop();
                isGameEnding = false;
            }
        }
    }

    public void addAnimationBackToBackTetris() {
        animationControl.generateBackToBackAnimation(getClearedLinesLowestRow(), this);
    }

    public void addAnimationCombo() {
        animationControl.generateComboAnimation(getClearedLinesLowestRow(), this);
    }

    public void addAnimationLinesCleared(int quantityLinesCleared) {
        animationControl.generateLinesClearedAnimation(quantityLinesCleared, getClearedLinesLowestRow(), this);
    }

    public void animateClearingLines(boolean[] lines) {
        animatingClearingLines = true;
        linesToAnimate = lines;

        timer = new Timer(clearingLinesTimeDelay, this);
        timer.start();
    }

    public void goToNextRound() {
        roundWantsToChange = true;
    }

    public boolean isGameEnding() {
        return isGameEnding;
    }

    public void paintComponent(Graphics g) {

        if (gameRunning && frame != null) {

            drawFrameBackground(g);

            for (int i = 0; i < Frame.ROWS; i++) {
                for (int j = 0; j < Frame.COLUMNS; j++) {

                    if (frame[i][j] != 0) {
                        drawBlock(g, blocksImages.getImageBy(frame[i][j]), size, i, j);

                        if (game.isPieceLandingStage() || game.isPieceLandedStage()) {
                            drawPieceLandingOrLanded(g, size, i, j);
                        }
                    }
                }
            }

            drawCleaningLinesTextAnimation(g);

            if (animatingClearingLines) {

                for (int k = 0; k < linesToAnimate.length; k++) {
                    if (linesToAnimate[k]) {
                        g.drawImage(brighterLines[frameAnimatingClearingLines], size, size + (k * size), size * 10, size, this);
                    }
                }
            }

        } else { // gameOff

            drawFrameBackground(g);

            if (readyComponent.isRunning()) {
                readyComponent.drawAnimationFrame(g, this.getWidth(), this.getHeight());
            } else {
                drawAnimationFrameFinishingGame(g, size);
            }
        }
    }

    public void ready() {

        frame = new int[Frame.ROWS][Frame.COLUMNS];
        readyComponent.start();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void showEndGameAnimation() {

        gameRunning = false;
        isGameEnding = true;
        endingGameBlocksGrayPainted = 0;
        allEndBlocks = new ArrayList<>();

        for (int i = Frame.ROWS - 1; i >= 0; i--) {
            for (int j = Frame.COLUMNS - 1; j >= 0; j--) {
                if (frame[i][j] != 0) {
                    allEndBlocks.add(new Position(i, j));
                }
            }
        }
        timer = new Timer(ENDING_TIME_DELAY, this);
        timer.start();
    }

    public void updateFrame(int[][] frame) {

        for (int i = 0; i < Frame.ROWS; i++) {
            for (int j = 0; j < Frame.COLUMNS; j++) {
                if (frame[i][j] < 0) {
                    frame[i][j] = (frame[i][j] * (-1)) + BlockFactory.NUMBER_OF_PIECES;
                }
            }
        }
        this.frame = frame;
        repaint();
    }
}