package com.lincolnpomper.tetris.core;

import com.lincolnpomper.tetris.gui.GamePanel;
import com.lincolnpomper.tetris.gui.NextPanel;
import com.lincolnpomper.tetris.gui.PointsPanel;
import com.lincolnpomper.tetris.model.ModeProgression;
import com.lincolnpomper.tetris.model.Piece;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends Thread implements KeyListener, ActionListener {

	public GamePanel gamePanel;
	private static long keyPressedEndTime = 0;
	private static long keyPressedStartTime = 0;
	private static int lastKey = -100;

	private Frame frame;
	private KeyConfig keyConfig;
	private ModeProgression modeProgression;
	private NextPanel nextPanel;
	private Piece piece;
	private Timer pieceDropTimer;
	private TimerPieceLanding pieceLandingTimer;

	private PointsPanel pointsPanel;

	private boolean running = false;
	private boolean waitForLandingAnimation = false;

	public Game(GamePanel gamePanel, NextPanel nextPanel, PointsPanel pointsPanel, KeyConfig keyConfig, ModeProgression modeProgression) {

		this.frame = new Frame();
		this.gamePanel = gamePanel;
		this.nextPanel = nextPanel;
		this.pointsPanel = pointsPanel;
		this.keyConfig = keyConfig;
		this.modeProgression = modeProgression;

		gamePanel.setGame(this);

		pieceDropTimer = new Timer(0, this);
		pieceLandingTimer = new TimerPieceLanding(0, this);

		this.nextPanel.firstPiece();

		setupTimers();
	}

	public void actionPerformed(ActionEvent event) {

		gamePanel.repaint();

		if (event.getSource().equals(pieceDropTimer)) {

			downPiece();

		} else if (event.getSource().equals(pieceLandingTimer)) {

			if (pieceLandingTimer.isTimeToPerformAction()) {

				if (pieceLandingTimer.getStage() == PieceStage.LANDING) {
					pieceLandingTimer.setStage(PieceStage.LANDED);
				} else if (pieceLandingTimer.getStage() == PieceStage.LANDED) {
					pieceLandingTimer.stop();
					pieceLanded();
				}
			}
		}
	}

	public void continueAfterPieceLanded() {

		boolean[] allRows = frame.getPreviousAllRows();
		int quantityLinesCleared = 0;

		if (allRows != null) {
			quantityLinesCleared = frame.getClearedLines(allRows);
		}

		if (quantityLinesCleared > 1) {
			gamePanel.addAnimationLinesCleared(quantityLinesCleared);
		}

		if (pointsPanel.isBackToBackTetris()) {

			gamePanel.addAnimationBackToBackTetris();

		} else if (pointsPanel.isCombo()) {

			gamePanel.addAnimationCombo();
		}

		pieceLandingTimer.stop();
		waitForLandingAnimation = false;

		if (!pointsPanel.isLastRound()) {

			piece = nextPanel.nextPiece();
			gamePanel.updateFrame(frame.getFrame());
		}

		if (running) {
			if (!pieceDropTimer.isRunning()) {
				pieceDropTimer.start();
			}
		}
	}

	public void downPiece() {

		if (piece == null) {
			piece = nextPanel.nextPiece();
		}

		if (!piece.isStarterPosition()) {
			piece.moveDown();
		}

		PiecePosition result = frame.setPieceAtDesiredPosition(piece);

		if (result == PiecePosition.ALLOWED) {
			gamePanel.updateFrame(frame.getFrame());
		} else if (result == PiecePosition.NOT_ALLOWED) {
			endGame();
		} else if (result == PiecePosition.IS_LANDING) {
			pieceIsLanding();
		}
	}

	public Piece getCurrentPiece() {
		return piece;
	}

	public int getPieceLandingCurrentClick() {
		return pieceLandingTimer.getCurrentClick();
	}

	public boolean isPieceLandedStage() {
		return pieceLandingTimer.isPieceLandedStage();
	}

	public boolean isPieceLandingStage() {
		return pieceLandingTimer.isPieceLandingStage();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {

			lastKey = e.getKeyCode();

			if (!running && !gamePanel.isGameEnding()) {
				restart();
			}

		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

			System.exit(0);

		} else if (running) {

			if (waitForLandingAnimation) {
				return;
			}

			if (e.getKeyCode() == keyConfig.down) {

				lastKey = e.getKeyCode();

				resolveKeyDownPressed();

			} else if (e.getKeyCode() == keyConfig.up) {

				if (frame.setPieceAtGround(piece) != PiecePosition.NOT_ALLOWED) {

					pieceIsLanding();

					gamePanel.updateFrame(frame.getFrame());
				}

			} else if ((e.getKeyCode() == keyConfig.right || e.getKeyCode() == keyConfig.left) &&
					(!pieceLandingTimer.isRunning() || pieceLandingTimer.isPieceLandingStage())) {

				resolveKeyLeftOrRightPressed(e);

			} else if (e.getKeyCode() == keyConfig.button1 || e.getKeyCode() == keyConfig.button2) {

				resolveKeyButton1OrButton2Pressed(e);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		lastKey = -1;
	}

	public void readyFinished() {

		nextPanel.lastFourPieces.clear();

		if (!pieceDropTimer.isRunning()) {
			pieceDropTimer.start();
		}
	}

	public void start() {
		gamePanel.ready();
		running = true;
	}

	private void endGame() {

		running = false;

		pieceDropTimer.stop();
		pieceLandingTimer.stop();

		gamePanel.showEndGameAnimation();
	}

	private void pieceIsLanding() {

		gamePanel.updateFrame(frame.getFrame());
		pieceDropTimer.stop();
		pieceLandingTimer.setStage(PieceStage.LANDING);
		pieceLandingTimer.start();
	}

	private void pieceLanded() {

		boolean[] clearedLines = frame.lockPieceAndClearLines();

		if (clearedLines != null) {

			boolean gainedRound = pointsPanel.updateLevel(frame.getClearedLines(clearedLines));

			if (gainedRound && !pointsPanel.isLastRound()) {
				pieceDropTimer.setDelay(modeProgression.getCurrentRound().getSpeedFall());
				gamePanel.goToNextRound();
			}

			gamePanel.animateClearingLines(clearedLines);

			waitForLandingAnimation = true;

		} else {

			pointsPanel.updateLevel(0);
			continueAfterPieceLanded();
		}
	}

	private void resolveKeyButton1OrButton2Pressed(KeyEvent e) {

		if (lastKey != e.getKeyCode()) {
			keyPressedStartTime = System.currentTimeMillis();
		} else {
			keyPressedEndTime = System.currentTimeMillis() - keyPressedStartTime;
		}

		if (lastKey != e.getKeyCode() || keyPressedEndTime > 1000) {

			lastKey = e.getKeyCode();
			keyPressedEndTime = keyPressedStartTime = System.currentTimeMillis();

			if (e.getKeyCode() == keyConfig.button1) {
				piece.turnRight();
			} else if (e.getKeyCode() == keyConfig.button2) {
				piece.turnLeft();
			}

			PiecePosition result = frame.setPieceAtDesiredPosition(piece);

			if (result == PiecePosition.ALLOWED) {

				if (pieceLandingTimer.isRunning()) {
					pieceLandingTimer.stop();
				}

				gamePanel.updateFrame(frame.getFrame());

				if (!pieceDropTimer.isRunning()) {
					pieceDropTimer.start();
				}

			} else if (result == PiecePosition.IS_LANDING) {

				gamePanel.updateFrame(frame.getFrame());

			} else if (result == PiecePosition.NOT_ALLOWED) {

				/*
				 * undo
				 */
				if (e.getKeyCode() == keyConfig.button1) {
					piece.turnLeft();
				} else if (e.getKeyCode() == keyConfig.button2) {
					piece.turnRight();
				}
			}
		}
	}

	private void resolveKeyDownPressed() {

		if (pieceLandingTimer.isPieceLandingStage()) {
			pieceLandingTimer.setStage(PieceStage.LANDED);
		}

		if (!pieceLandingTimer.isRunning()) {

			piece.moveDown();

			PiecePosition result = frame.setPieceAtDesiredPosition(piece);

			if (result != PiecePosition.NOT_ALLOWED) {

				if (result == PiecePosition.IS_LANDING) {
					pieceIsLanding();
				}

				gamePanel.updateFrame(frame.getFrame());
			}
		}
	}

	private void resolveKeyLeftOrRightPressed(KeyEvent e) {
		lastKey = e.getKeyCode();

		boolean pieceMoved = false;

		if (e.getKeyCode() == keyConfig.left) {
			pieceMoved = piece.moveLeft();
		} else if (e.getKeyCode() == keyConfig.right) {
			pieceMoved = piece.moveRight();
		}

		if (pieceMoved) {

			PiecePosition result = frame.setPieceAtDesiredPosition(piece);

			if (result == PiecePosition.ALLOWED) {

				if (pieceLandingTimer.isRunning()) {
					pieceLandingTimer.stop();
				}

				gamePanel.updateFrame(frame.getFrame());

				if (!pieceDropTimer.isRunning()) {
					pieceDropTimer.start();
				}

			} else if (result == PiecePosition.IS_LANDING) {

				gamePanel.updateFrame(frame.getFrame());

			} else if (result == PiecePosition.NOT_ALLOWED) {

				undoPieceMovement(e);
			}
		}
	}

	private void restart() {

		pointsPanel.restart();
		frame.restart();

		nextPanel.firstPiece();

		setupTimers();

		gamePanel.ready();
		running = true;
	}

	private void setupTimers() {

		pieceDropTimer.setDelay(modeProgression.getCurrentRound().getSpeedFall());

		pieceLandingTimer.setDelay(modeProgression.getCurrentRound().getSpeedLanding());
		pieceLandingTimer.setStage(PieceStage.LANDING);
	}

	private void undoPieceMovement(KeyEvent e) {

		if (e.getKeyCode() == keyConfig.left) {
			piece.moveRight();
		} else if (e.getKeyCode() == keyConfig.right) {
			piece.moveLeft();
		}
	}
}