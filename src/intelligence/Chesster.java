package intelligence;

import java.util.ArrayList;
import java.util.LinkedList;

import board.components.Piece;
import engine.MainEngine;
import engine.simulation.Simulation;
import graphic.BoardGUI;
import graphic.GraphicsMainMenu;
import stats.Match;
import stats.Player;
import strategy.data.Coordinates;

/**
 * 
 * Chesster is a child of the class {@link Player}. Chesster is the XiangQi's
 * AI, in {@link GraphicsMainMenu}, the player has the possibility to choose
 * between playing against an other player or against Chesster. If the player
 * chooses to play against Chesster, it is set as the second player in
 * {@link Match}. Chesster uses {@link PriorityMove} to make decisions on what
 * to play.
 * 
 * @see Player
 * @see GraphicsMainMenu
 * @see Match
 * @see PriorityMove
 * @author Dorian CHENET
 *
 */
public class Chesster extends Player {

	/**
	 * Here is a list of all the moves Chesster can make.
	 * 
	 * @see PriorityMove
	 */
	private LinkedList<PriorityMove> possiblemoves = new LinkedList<PriorityMove>();

	/**
	 * This {@link Simulation} is used by Chesster to see if he can put the
	 * opposing player in check.
	 * 
	 * @see Simulation
	 */
	private Simulation simulation = new Simulation();

	public Chesster(String color, ArrayList<Piece> pieces, ArrayList<Piece> takenpieces, ArrayList<Piece> lostpieces) {
		super("Chesster", color, pieces, takenpieces, lostpieces);

		// TODO Auto-generated constructor stub
	}

	/**
	 * This method is used in {@link MainEngine} to tell Chesster to play.
	 * 
	 * @see MainEngine
	 */
	public void play() {

		// Clearing all the moves Chesster was able to play on its last turn.
		possiblemoves.clear();

		// Creating a list of all the possible moves Chesster can make this
		// current turn.
		generatePossibleChoices();

		// Evaluating the priority of each move to see which is the most
		// interesting to play.
		evaluateMovesPriority();

		/*
		 * Sorting all the moves to put the moves with the highest priority on
		 * the top of the priority list.
		 */
		sort();

		/*
		 * We take the move which is on the top of the stack and play it.
		 */
		if (!possiblemoves.isEmpty()) {
			makeTheMove(possiblemoves.getFirst());
		}

		/*
		 * Making a new turn / letting the opposite player play.
		 */
		BoardGUI.getInstance().repaint();
		MainEngine.engine.newTurn();
	}

	/**
	 * This method sets the priority of each move Chesster can make to see which
	 * is the most interesting to play. The new priority is set by incrementing
	 * and decrementing the priority of the move according to the state of the
	 * game.
	 * 
	 * @see PriorityMove
	 */
	private void evaluateMovesPriority() {
		// Recovering the zone where the enemy can eat pieces.
		ArrayList<Coordinates> enemycoveredzone = getEnemyCoveredZone();

		// Generating the priority for each move.
		for (PriorityMove move : possiblemoves) {
			int newpriority = 0;
			Piece chessterpiece = Match.getBoard().getPiece(move.getInitial());
			Piece finalpiece = Match.getBoard().getPiece(move.getFinalposition());

			// If the move ends on an enemy piece, use these conditions.
			if (finalpiece != null) {

				/**
				 * If it is possible to take a piece with a greater weight than
				 * the piece we want to play, it is interesting, +2 priority.
				 * 
				 * @see Piece
				 */
				if (chessterpiece.getWeight() < finalpiece.getWeight()) {
					newpriority += 2;
				}

				/*
				 * If the piece is of a lesser weight than the one we want to
				 * play then it is less interesting but still, +1 priority.
				 */
				else {
					newpriority++;
				}

				/*
				 * If in addition of taking an enemy piece, we can put the enemy
				 * player in check then it is interesting, +2 priority.
				 */
				if (checks(move)) {
					newpriority += 2;
				}

				/*
				 * If the enemy player is able to eat a piece in the slot we are
				 * about to place our piece then we might lose it the next turn
				 * so it is less interesting, -1 priority.
				 */
				if (enemycoveredzone.contains(move.getFinalposition())) {
					newpriority--;
				}
			}

			// If the move ends on an empty slot, use these conditions, work the
			// same as above.
			else {

				if (checks(move)) {
					newpriority += 2;
				}

				else if (Match.getBoard().getPiece(move.getInitial()).getWeight() == 1) {
					newpriority++;
				}

				else {
					newpriority--;
				}
			}

			// Setting the new priority.
			move.setPriority(newpriority);
		}
	}

	/**
	 * @return A list of all the positions where the enemy player can eat
	 *         pieces.
	 */
	private ArrayList<Coordinates> getEnemyCoveredZone() {
		ArrayList<Coordinates> coveredzone = new ArrayList<Coordinates>();
		for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
			coveredzone.addAll(piece.getPossiblemoves());
		}
		return coveredzone;
	}

	// Sorting all the possible moves in a decreasing priority order.
	private void sort() {
		LinkedList<PriorityMove> sortedmoves = new LinkedList<PriorityMove>();
		while (sortedmoves.size() < possiblemoves.size()) {
			PriorityMove highestprioritymove = null;
			for (PriorityMove move : possiblemoves) {
				if (highestprioritymove == null
						|| highestprioritymove.getPriority() < move.getPriority() && !sortedmoves.contains(move)) {
					highestprioritymove = move;
				}
			}
			sortedmoves.addLast(highestprioritymove);
		}
		possiblemoves = sortedmoves;
	}

	/**
	 * Creating a list of all the possible moves Chesster can play
	 * {@link PriorityMove}.
	 * 
	 * @see PriorityMove
	 */
	private void generatePossibleChoices() {

		for (Piece piece : super.getPieces()) {
			for (Coordinates coordonates : piece.getPossiblemoves()) {
				PriorityMove move = new PriorityMove(piece.getCoordonates(), coordonates, 0);
				if (Match.getBoard().getPiece(coordonates) == null) {
					possiblemoves.add(move);
				} else if (!Match.getBoard().getPiece(coordonates).getColor().equals(super.getColor())) {
					possiblemoves.add(move);
				}
			}
		}

		for (Coordinates coordinates : super.getWinconditionpiece().getPossiblemoves()) {
			PriorityMove move = new PriorityMove(super.getWinconditionpiece().getCoordonates(), coordinates, 0);
			if (Match.getBoard().getPiece(coordinates) == null) {
				possiblemoves.add(move);
			} else if (!Match.getBoard().getPiece(coordinates).getColor().equals(super.getColor())) {
				possiblemoves.add(move);
			}
		}
	}

	/**
	 * Making Chesster play a move.
	 * 
	 * @param move
	 */
	private void makeTheMove(PriorityMove move) {
		Match.getInstance().getWaitingplayer().getLostpieces().add(Match.getBoard().getPiece(move.getFinalposition()));
		Match.getInstance().getWaitingplayer().getPieces().remove(Match.getBoard().getPiece(move.getFinalposition()));
		super.getTakenpieces().add(Match.getBoard().getPiece(move.getFinalposition()));
		Match.getBoard().movePiece(move.getInitial(), move.getFinalposition());
	}

	/**
	 * This method checks if a move can put the enemy player in check using
	 * {@link Simulation}.
	 * 
	 * @see Simulation
	 * @param move
	 * @return true if the enemy player is checked after the move, else false.
	 */
	private Boolean checks(PriorityMove move) {
		simulation.reset();
		simulation.simulate(move.getInitial(), move.getFinalposition());
		Piece piece = simulation.getBoard().getPiece(move.getFinalposition());
		Boolean checks = false;
		if (piece != null) {
			if (piece.getCoveredzone()
					.contains(Match.getInstance().getWaitingplayer().getWinconditionpiece().getCoordonates())) {
				checks = true;
			}
		}
		return checks;
	}

}
