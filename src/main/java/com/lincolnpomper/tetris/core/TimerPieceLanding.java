package com.lincolnpomper.tetris.core;

import javax.swing.Timer;

class TimerPieceLanding extends Timer {

	private final static int PIECE_LANDING_CLICKS = 17;
	private final static int PIECE_LANDED_CLICKS = 8;

	private PieceStage stage = PieceStage.LANDING;
	private int currentClick;

	public TimerPieceLanding(int delay, Game game) {
		super(delay, game);
		currentClick = 0;
	}

	public int getCurrentClick() {
		return currentClick;
	}

	public PieceStage getStage() {
		return stage;
	}

	public void setStage(PieceStage stage) {

		currentClick = 0;
		this.stage = stage;
	}

	public boolean isPieceLandedStage() {
		return isRunning() && stage == PieceStage.LANDED;
	}

	public boolean isPieceLandingStage() {
		return isRunning() && stage == PieceStage.LANDING;
	}

	public boolean isTimeToPerformAction() {

		if (stage == PieceStage.LANDING) {
			if (currentClick++ == PIECE_LANDING_CLICKS) {
				currentClick = 0;
				return true;
			}
		} else if (stage == PieceStage.LANDED) {
			if (currentClick++ == PIECE_LANDED_CLICKS) {
				currentClick = 0;
				return true;
			}
		}
		return false;
	}

	@Override public void start() {
		currentClick = 0;
		setStage(PieceStage.LANDING);
		super.start();
	}
}
