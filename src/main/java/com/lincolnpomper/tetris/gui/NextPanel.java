package com.lincolnpomper.tetris.gui;

import com.lincolnpomper.tetris.BlocksImagesFacade;
import com.lincolnpomper.tetris.model.Piece;
import com.lincolnpomper.tetris.util.Resolution;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class NextPanel extends JComponent {

	public ArrayList<Integer> lastFourPieces = new ArrayList<>();

	private BlocksImagesFacade blocksImages;
	private Piece piece;
	private Piece[] pieces;
	private Random random = new Random();
	private int size;
	private int x;

	public NextPanel(Piece[] pieces, BlocksImagesFacade blocksImages, Resolution resolution, int gap) {

		this.pieces = pieces;

		this.blocksImages = blocksImages;
		this.size = resolution.getBlockSize();

		x = gap + this.size * 4;
	}

	public void firstPiece() {

		int number = getNextPieceNumber();
		this.piece = new Piece(pieces[number].getShapeGroup(), pieces[number].getNumber());
		this.repaint();
	}

	public Piece nextPiece() {

		Piece previousPiece = this.piece;

		int number = getNextPieceNumber();
		this.piece = new Piece(pieces[number].getShapeGroup(), pieces[number].getNumber());
		this.lastFourPieces.add(piece.getNumber());

		this.repaint();

		return previousPiece;
	}

	public void paintComponent(Graphics graphics) {

		super.paintComponent(graphics);

		if (piece != null) {
			for (int i = 0; i < piece.getOriginalShape().length; i++) {
				for (int j = 0; j < piece.getOriginalShape()[0].length; j++) {
					if (piece.getOriginalShape()[i][j]) {
						graphics.drawImage(blocksImages.getImageBy(piece.getNumber()), x + j * size, i * size, this);
					}
				}
			}
		}
	}

	private int generateNumber() {
		return (int) (random.nextFloat() * pieces.length);
	}

	private int getNextPieceNumber() {

		int tryingFreshPiece = 0;
		int number;

		do {

			number = generateNumber();

			if (tryingFreshPiece++ == 4) {
				break;
			}

		} while (lastFourPieces.contains(number));

		lastFourPieces.add(number);

		return number;
	}
}