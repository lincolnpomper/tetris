package com.lincolnpomper.tetris.core;

import com.lincolnpomper.tetris.MainPanel;
import com.lincolnpomper.tetris.RoundChangedListener;
import com.lincolnpomper.tetris.graphics.BlockFactory;
import com.lincolnpomper.tetris.graphics.ImageLoader;
import com.lincolnpomper.tetris.BlocksImagesFacade;
import com.lincolnpomper.tetris.gui.GamePanel;
import com.lincolnpomper.tetris.gui.NextPanel;
import com.lincolnpomper.tetris.gui.PointsPanel;
import com.lincolnpomper.tetris.model.ModeProgression;
import com.lincolnpomper.tetris.model.Piece;
import com.lincolnpomper.tetris.util.Resolution;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class GameFactory {

	private static JPanel buildGame(List<Game> sessions, Piece[] pieces, Image[] borderImages, ModeProgression modeProgression,
			KeyConfig keyConfig, Resolution resolution, RoundChangedListener listener) {

		int size = resolution.getBlockSize();

		final int GAME_PANEL_HEIGHT = size * 22;
		final int GAME_PANEL_WIDTH = size * 12;
		final int POINTS_PANEL_WIDTH = size * 5;
		BlocksImagesFacade blocksImages = BlocksImagesFacade.getInstance();

		GamePanel gamePanel = new GamePanel(blocksImages, borderImages, resolution, listener);
		gamePanel.setPreferredSize(new Dimension(GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT));

		NextPanel nextPanel = new NextPanel(pieces, blocksImages, resolution, POINTS_PANEL_WIDTH);
		nextPanel.setPreferredSize(new Dimension(GAME_PANEL_WIDTH, 4 * size));

		PointsPanel pointsPanel = new PointsPanel(modeProgression, resolution);
		pointsPanel.setPreferredSize(new Dimension(POINTS_PANEL_WIDTH, GAME_PANEL_HEIGHT));

		Game game = new Game(gamePanel, nextPanel, pointsPanel, keyConfig, modeProgression);
		sessions.add(game);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(gamePanel, BorderLayout.CENTER);
		panel.add(nextPanel, BorderLayout.NORTH);
		panel.add(pointsPanel, BorderLayout.WEST);

		return panel;
	}

	public static List<Game> createAndBuildGui(Resolution resolution, MainPanel mainPanel, int numberOfPlayers, GameMode gameMode,
			KeyConfig[] playersKeyConfigs) {

		List<Game> sessions = new ArrayList<>();

		Piece[] pieces = BlockFactory.getInstance().getPieces();
		Image[] borderImages = ImageLoader.getInstance().getBorderImages(resolution);
		ModeProgression modeProgression = ModeProgressionFactory.buildFrom(gameMode);

		createPlayer1(resolution, mainPanel, pieces, playersKeyConfigs[0], sessions, borderImages, modeProgression);

		if (numberOfPlayers == 2) {
			createPlayer2(resolution, mainPanel, pieces, playersKeyConfigs[1], sessions, borderImages, modeProgression);
		}

		return sessions;
	}

	private static void createPlayer1(Resolution resolution, MainPanel mainPanel, Piece[] pieces, KeyConfig keyConfig, List<Game> sessions,
			Image[] borderImages, ModeProgression modeProgression) {

		JPanel panelPlayer1 = buildGame(sessions, pieces, borderImages, modeProgression, keyConfig, resolution, mainPanel);

		mainPanel.setLayout(new FlowLayout());

		mainPanel.add(Box.createHorizontalGlue());
		mainPanel.add(panelPlayer1);
		mainPanel.add(Box.createHorizontalStrut((int) (2.5 * resolution.getBlockSize())));
		mainPanel.add(Box.createHorizontalGlue());
	}

	private static void createPlayer2(Resolution resolution, MainPanel mainPanel, Piece[] pieces, KeyConfig keyConfig, List<Game> sessions,
			Image[] borderImages, ModeProgression modeProgression) {

		JPanel panelPlayer2 = buildGame(sessions, pieces, borderImages, modeProgression, keyConfig, resolution, mainPanel);

		mainPanel.add(panelPlayer2);
		mainPanel.add(Box.createHorizontalGlue());
	}
}