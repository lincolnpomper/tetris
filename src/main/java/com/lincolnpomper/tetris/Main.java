package com.lincolnpomper.tetris;

import com.lincolnpomper.tetris.core.Game;
import com.lincolnpomper.tetris.core.GameFactory;
import com.lincolnpomper.tetris.core.GameMode;
import com.lincolnpomper.tetris.core.KeyConfig;
import com.lincolnpomper.tetris.graphics.BlockFactory;
import com.lincolnpomper.tetris.graphics.BlurDegreeFactory;
import com.lincolnpomper.tetris.graphics.ImageLoader;
import com.lincolnpomper.tetris.graphics.mainscreen.MainScreenAnimation;
import com.lincolnpomper.tetris.util.AvailableResolutions;
import com.lincolnpomper.tetris.util.Logger;
import com.lincolnpomper.tetris.util.Resolution;

import java.awt.Color;
import java.awt.IllegalComponentStateException;
import javax.swing.JFrame;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;
import sun.awt.CGraphicsDevice;

public class Main implements GameStarterListener, KeyListener, MainScreenAnimationListener {

    private static JFrame mainFrame;
    public Resolution resolution;
    private MainPanel mainPanel;
    private MainScreenAnimation mainScreenAnimation;

    public Main(boolean fullScreenMode, Resolution resolution) {

        this.resolution = resolution;

        mainFrame = new JFrame();
        mainFrame.setResizable(false);
        mainFrame.addKeyListener(this);
        mainFrame.getContentPane().setBackground(Color.black);

        if (fullScreenMode) {
            setupFullScreen(resolution);
        }

        BlurDegreeFactory.setResolution(resolution);
        FontFactory.buildFontsByResolution(resolution);

        ImageLoader.getInstance().loadBackgroundImages(resolution);

        mainPanel = new MainPanel(this, resolution);
        mainFrame.addKeyListener(mainPanel.getMenuPanel());

        BufferedImage[] images = BlockFactory.getInstance().createBlockImages(resolution.getBlockSize());
        BlocksImagesFacade.getInstance().setImages(images);

        long start = System.currentTimeMillis();
        mainScreenAnimation = new MainScreenAnimation(this, mainPanel.getTitleScreenBackgroundImage(), resolution);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;

        System.out.println("elapsedTime: " + elapsedTime);

        if (!fullScreenMode) {
            try {
                mainFrame.setUndecorated(true);
            } catch (IllegalComponentStateException e) {
                System.out.println(e.getMessage());
            }
        }

        mainFrame.setBackground(Color.black);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);

        mainScreenAnimation.start();
    }

    private void setupFullScreen(Resolution resolution) {

        try {

            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();

            if (device.isDisplayChangeSupported()) {

                final CGraphicsDevice deviceC = (CGraphicsDevice) device;
                final int scaleFactor = deviceC.getScaleFactor();
                final double realDPI = scaleFactor * (deviceC.getXResolution() + deviceC.getYResolution()) / 2;

                final DisplayMode[] displayModes = device.getDisplayModes();
                for (DisplayMode displayMode : displayModes) {

                    if (displayMode.getWidth() == resolution.getWidth() && displayMode.getHeight() == resolution.getHeight() &&
                            displayMode.getRefreshRate() == 60) {
                        try {
                            Logger.info(String.format("Setting full screen to width: %d, Height: %d, RefreshRate: %d, BitDepth: %d%n", displayMode.getWidth(),
                                    displayMode.getHeight(), displayMode.getRefreshRate(), displayMode.getBitDepth()));

                            device.setDisplayMode(displayMode);
                        } catch (IllegalArgumentException e) {
                            Logger.error(e.getMessage());
                            System.exit(1);
                        }
                        break;
                    }
                }
            }

            device.setFullScreenWindow(mainFrame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void introAnimationEnded() {
        mainFrame.removeKeyListener(this);
        mainPanel.activateMenu();
    }

    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER && !mainScreenAnimation.isIntroEnded()) {
            mainScreenAnimation.prematurelyEndIntro();
            mainFrame.removeKeyListener(this);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {

        boolean fullScreenMode = false;
        Resolution resolution;

        if (args != null && args.length > 0) {
            resolution = AvailableResolutions.getResolutionByKeyName(args[0]);
        } else {
            resolution = AvailableResolutions.getDefaultResolution();
        }

        if (args != null && args.length > 1) {
            fullScreenMode = Boolean.parseBoolean(args[1]);
        }

        new Main(fullScreenMode, resolution);
    }

    @Override
    public void startGame(int numberOfPlayers, GameMode gameMode, KeyConfig[] playersKeyConfigs) {

        mainScreenAnimation.stop();
        mainPanel.removeMenuPanel();
        mainFrame.pack();

        mainPanel.nextRoundImage();

        List<Game> games = GameFactory.createAndBuildGui(resolution, mainPanel, numberOfPlayers, gameMode, playersKeyConfigs);

        mainFrame.pack();

        for (Game game : games) {
            mainFrame.addKeyListener(game);
            game.start();
        }
    }

    @Override
    public void updateBackground(Image background) {
        mainPanel.setTitleScreenBackgroundImage(background);
        mainPanel.repaint();
    }
}