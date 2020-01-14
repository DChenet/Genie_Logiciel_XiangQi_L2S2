package board;

import java.util.HashMap;

import board.components.Ground;
import board.components.Piece;
import engine.simulation.Simulation;
import stats.Match;
import strategy.data.Coordinates;

/**
 * This class represents the playing board. Boards are used in {@link Match}
 * (playing board) and {@link Simulation} to make simulations of the playing
 * board.
 * 
 * @author Dorian CHENET
 * @see Piece
 * @see Ground
 */
public class Board {

	// This array contains the ground informations
	private Ground[][] environement = new Ground[BoardParameters.BOARD_X_LENGTH][BoardParameters.BOARD_Y_LENGTH];

	// This HashMap contains the units
	private HashMap<Coordinates, Piece> units = new HashMap<Coordinates, Piece>();

	public Board() {
		// TODO Auto-generated constructor stub
	}

	// This method is used to move pieces across the board
	public void movePiece(Coordinates initial, Coordinates finality) {
		Piece tomovepiece = units.get(initial);
		tomovepiece.setCoordonates(finality);
		units.remove(initial);
		units.put(finality, tomovepiece);
	}

	public Ground[][] getEnvironement() {
		return environement;
	}

	public void setEnvironement(Ground[][] environement) {
		this.environement = environement;
	}

	public HashMap<Coordinates, Piece> getUnits() {
		return units;
	}

	public void setUnits(HashMap<Coordinates, Piece> units) {
		this.units = units;
	}

	public void addPiece(Piece piece) {
		units.put(piece.getCoordonates(), piece);
	}

	public void addGround(Coordinates coordonates, Ground ground) {
		environement[coordonates.getX()][coordonates.getY()] = ground;
	}

	public void addGround(int x, int y, Ground ground) {
		environement[x][y] = ground;
	}

	public void removePiece(Coordinates coordonates) {
		units.remove(coordonates);
	}

	public Piece getPiece(int x, int y) {
		return units.get(new Coordinates(x, y));
	}

	public Piece getPiece(Coordinates coordonates) {
		if (coordonates.getX() >= 0 && coordonates.getX() < BoardParameters.BOARD_X_LENGTH && coordonates.getY() >= 0
				&& coordonates.getY() < BoardParameters.BOARD_Y_LENGTH) {
			return units.get(coordonates);
		} else {
			return null;
		}
	}

	public Ground getGround(int x, int y) {
		return environement[x][y];
	}

	public Ground getGround(Coordinates coordonates) {
		return environement[coordonates.getX()][coordonates.getY()];
	}
}
