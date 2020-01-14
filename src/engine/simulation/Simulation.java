package engine.simulation;

import java.util.ArrayList;

import board.Board;
import board.components.Piece;
import engine.MainEngine;
import engine.MovementValidator;
import engine.visitor.RuleEvaluator;
import intelligence.Chesster;
import rules.Rule;
import stats.Match;
import strategy.data.Coordinates;

/**
 * . Simulations are used in {@link MainEngine} and {@link Chesster}. A
 * simulation is a copy of the playing board {@link Board} the engine
 * {@link MainEngine} uses to calculate which moves are forbidden according to
 * the game rules after having evaluated the validity of each move according to
 * the rules {@link Rule, RuleEvaluator} associated to the piece {@link Piece}.
 * for instance, a move might be possible because it is allowed by the rules
 * associated to the piece but playing this move might put the player in check
 * which is forbidden by the game rules. the simulation is used in
 * {@link MainEngine} to ensure that this does not happen.
 * 
 * In {@link Chesster}, the game's AI, the simulation is used to know if a move
 * can put the enemy player in check and generate behaviour.
 * 
 * @see MainEngine
 * @see Chesster
 * @see Board
 * @see Rule
 * @see RuleEvaluator
 * @see Piece
 * @author Dorian CHENET
 *
 */
public class Simulation {

	// Regrouping the data extracted from the playing board.
	private ArrayList<Piece> curentplayerpieces = new ArrayList<Piece>();
	private ArrayList<Piece> waitingplayerpieces = new ArrayList<Piece>();
	private Piece curentplayerwincondition = null;
	private Piece waitingplayerwincondition = null;

	// The copy of the playing board.
	private Board simulation = new Board();

	public Simulation() {
		// TODO Auto-generated constructor stub
		reset();
	}

	// Making a copy of the playing board.
	public void reset() {

		curentplayerpieces.clear();
		waitingplayerpieces.clear();
		curentplayerwincondition = null;
		waitingplayerwincondition = null;

		simulation.getUnits().clear();
		simulation.setEnvironement(Match.getBoard().getEnvironement());

		for (Piece piece : Match.getBoard().getUnits().values()) {
			simulation.addPiece(new Piece(piece.getType(), piece.getCoordonates(), piece.getColor()));
		}

		for (Piece piece : simulation.getUnits().values()) {
			if (piece.getColor().equals(Match.getInstance().getCurrentplayer().getColor())) {
				if (piece.getWincondition()) {
					curentplayerwincondition = piece;
				} else {
					curentplayerpieces.add(piece);
				}
			} else {
				if (piece.getWincondition()) {
					waitingplayerwincondition = piece;
				} else {
					waitingplayerpieces.add(piece);
				}
			}
		}
	}

	/**
	 * This method is used to move a piece from the coodinates initial to the
	 * coordonates end.
	 * 
	 * @param initial
	 * @param end
	 * @return true if the playing who makes the move isn't in check after the
	 *         move, else false.
	 */
	public Boolean simulate(Coordinates initial, Coordinates end) {

		Boolean validity = true;

		// Making the move on the simulated board
		simulation.movePiece(initial, end);

		/**
		 * Giving the board with the move done to the {@link MovementValidator}
		 * for validation of all the deplacements.
		 */
		MovementValidator.getInstance().setBoard(simulation);
		MovementValidator.getInstance().clearCoveredZone();

		for (Piece piece : curentplayerpieces) {

			/**
			 * Generating the possible moves of all the pieces of the current
			 * player, if the {@link MovementValidator} return true for a piece,
			 * it means that after the simulated deplacement, the player who
			 * made the move is in check; thus, the validity of the move is
			 * false.
			 */
			if (MovementValidator.getInstance().generatePieceMovements(piece)) {
				validity = false;
			}
		}

		/**
		 * Same as above but for the Winning condition.
		 */
		if (MovementValidator.getInstance().generatePieceMovements(curentplayerwincondition)) {
			validity = false;
		}

		MovementValidator.getInstance().clearCoveredZone();
		return validity;
	}

	public Board getBoard() {
		return simulation;
	}
}
