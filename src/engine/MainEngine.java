package engine;

import java.util.ArrayList;

import board.components.Piece;
import engine.simulation.Simulation;
import stats.Match;
import strategy.data.Coordinates;

/**
 * This class is the key stone of the game's engine. It is here that we generate
 * the new turns, command to the {@link MovementValidator} to generate the
 * piece's possible moves, make the game evolve when a player is checked etc...
 * 
 * @see Match
 * @see MovementValidator
 * @author Dorian CHENET
 *
 */
public class MainEngine {

	public static MainEngine engine = new MainEngine();

	private Simulation simulation = new Simulation();

	private MainEngine() {

	}

	// This method is used to generate a new game turn.
	public void newTurn() {

		// Recovering the Movementvalidator and initializing it.
		MovementValidator mvalidator = MovementValidator.getInstance();

		mvalidator.clearCoveredZone();
		mvalidator.setBoard(Match.getBoard());

		// If a new turn is played it's that no player is in check, otherwise
		// the game is finished so we put both players as "not checked".
		Match.getInstance().getPlayer1().setIschecked(false);
		Match.getInstance().getPlayer2().setIschecked(false);

		/**
		 * Generating the possible moves of all the pieces of the player who
		 * just made the move. If the {@link MovementValidator} returns that the
		 * enemy player is checked, we put the enemy player checked.
		 */
		for (Piece piece : Match.getInstance().getCurrentplayer().getPieces()) {
			if (mvalidator.generatePieceMovements(piece)) {
				Match.getInstance().getWaitingplayer().setIschecked(true);
			}

		}

		// Same process with the winning condition piece of the player who just
		// made the move.
		if (mvalidator.generatePieceMovements(Match.getInstance().getCurrentplayer().getWinconditionpiece())) {
			Match.getInstance().getWaitingplayer().setIschecked(true);
		}

		System.out.println(Match.getInstance().getPlayer2().getWinconditionpiece().getWincondition());
		// We generate the possible moves of the enemy player's piece which
		// holds the winning condition.
		mvalidator.generatePieceMovements(Match.getInstance().getWaitingplayer().getWinconditionpiece());

		// If the enemy player is checked, do this.
		if (Match.getInstance().getWaitingplayer().getIschecked()) {

			// We generate all the possible moves for all the enemy player's
			// pieces.
			for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
				piece.getPossiblemoves().clear();
				mvalidator.generatePieceMovements(piece);
			}

			// For every enemy player's piece, we remove all the moves that
			// doesn't prevent him from beeing checked.
			for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
				selectMovement(piece);
			}

			// If the enemy player's winning condition piece doesn't have any
			// possible moves, do this.
			if (Match.getInstance().getWaitingplayer().getWinconditionpiece().getPossiblemoves().size() == 0) {
				Boolean wins = true;

				// Checking if any other piece has possible moves that could
				// prevent the enemy player from beeing checked.
				for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
					if (piece.getPossiblemoves().size() > 0) {
						wins = false;
					}
				}

				// If not, the player who made the last move has won.
				if (wins) {
					Match.getInstance().setWinner(Match.getInstance().getCurrentplayer());
				}

			}

		}

		// If the enemy player isn't checked, do this.
		else {
			// We generate all the possible moves for every enemy piece.
			for (Piece piece : Match.getInstance().getWaitingplayer().getPieces()) {
				piece.getPossiblemoves().clear();
				mvalidator.generatePieceMovements(piece);

				// We remove all the move that will make the enemy put himself
				// in check if played.
				selectMovement(piece);

			}
		}

		// If it is the first time that this method is called, we don't swap the
		// player's turns.
		if (Match.getInstance().getTurnnumber() != 0) {
			/**
			 * The enemy player becomes the player who plays {@link Match}
			 */
			Match.getInstance().swap();
		}
		Match.getInstance().setTurnnumber(Match.getInstance().getTurnnumber() + 1);

		/**
		 * We make the curent player play.
		 * 
		 * @see Chesster
		 * @see Player
		 */
		Match.getInstance().getCurrentplayer().play();

	}

	// This method removes every move that might make the player checked if they
	// are played, which we don't want.
	private void selectMovement(Piece piece) {
		ArrayList<Coordinates> toclearmovements = new ArrayList<Coordinates>();
		toclearmovements.clear();

		/**
		 * We use the {@link Simulation} to check if the player is checking
		 * himself when playing a move. If yes, the move is removed.
		 */
		for (Coordinates coordonates : piece.getPossiblemoves()) {
			simulation.reset();

			if (!simulation.simulate(piece.getCoordonates(), coordonates)) {
				toclearmovements.add(coordonates);
			}

		}
		piece.getPossiblemoves().removeAll(toclearmovements);
	}
}
