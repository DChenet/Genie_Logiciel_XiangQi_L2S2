package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import board.Board;
import board.BoardParameters;
import board.components.Ground;
import board.components.Piece;
import compiler.lang.Grammar;
import compiler.lang.RuleGrammar;
import compiler.lang.ValidityGrammar;
import engine.simulation.Simulation;
import engine.visitor.RuleEvaluator;
import rules.Rule;
import stats.Match;
import strategy.data.Coordinates;
import strategy.data.MovementPatern;

/**
 * The goal of this class is to fill the possiblemoves attribute of a
 * {@link Piece}. It evaluates the validity of each path contained in the
 * relevant {@link MovementPatern} of the {@link Piece} through the
 * {@link RuleEvaluator}. According the the Integer returned by
 * {@link RuleEvaluator}, a move can be added to the possible moves or the
 * covered zone or be treated as invalid.
 * 
 * For each piece going through the validation process, a Boolean is returned:
 * true if the piece checks the enemy player, false if it does not.
 * 
 * The movement Validator is an instance (singleton patern) because it's easier
 * to access its methods/attributes with the static modifier.
 * 
 * This class is also used by {@link Simulation} to validate deplacements on the
 * simulated board.
 * 
 * @author Dorian CHENET
 *
 */
public class MovementValidator {

	// Instance of the validator
	private static MovementValidator instance = new MovementValidator();

	ArrayList<Coordinates> possiblemoves = new ArrayList<Coordinates>();
	ArrayList<Coordinates> coveredzone = new ArrayList<Coordinates>();

	// Tools for validation
	RuleEvaluator evaluator = new RuleEvaluator();
	Coordinates simulatedcoordonates = new Coordinates();

	/**
	 * The board, used for context, it might be set to an other board as the
	 * MovementValidator is also used by {@link Simulation}.
	 */
	Board board = Match.getBoard();

	private MovementValidator() {
		// TODO Auto-generated constructor stub
	}

	public static MovementValidator getInstance() {
		return instance;
	}

	/**
	 * This method first tries to generate the piece's possible moves by using
	 * its standard patterns. If after the generation, the piece has no
	 * movements possible, the piece will try to generate the possibles moves of
	 * the piece using the alternative {@link MovementPatern}. It calls the
	 * method evaluatePatern() to generated the possible moves.
	 * 
	 * @param piece
	 * @return true if the piece has a move which puts the enemy player in
	 *         check, else false.
	 */
	public Boolean generatePieceMovements(Piece piece) {
		piece.getPossiblemoves().clear();
		simulatedcoordonates = new Coordinates(piece.getCoordonates().getX(), piece.getCoordonates().getY());
		ArrayList<MovementPatern> paterns = piece.getMovementpaterns();
		Boolean checks = false;

		// check all the moves contained in the standard patterns, build the
		// priority list.
		checks = evaluatePaterns(getStandardPaterns(paterns), piece, buildPriorityList(piece));

		// If no movement is possible, do the same process with the alternative
		// movement patterns.
		if (piece.getPossiblemoves().size() == 0) {
			checks = evaluatePaterns(getAlternativePaterns(paterns), piece, buildPriorityList(piece));
		}

		return checks;
	}

	/**
	 * This Method generates the possible moves of the {@link Piece}. It first
	 * recovers the {@link MovementPatern} which has a tag corresponding to the
	 * {@link Ground} type the piece stands on. Then for every move contained in
	 * the movement pattern it uses the {@link RuleEvaluator} to check for the
	 * validity of the move. Then it analyzes the return value of
	 * {@link RuleEvaluator} to put the final position of the move in the arrays
	 * possiblemove or coveredzone of refuse the move because it is not valid.
	 * 
	 * @see ValidityGrammar
	 * 
	 * @param paterns
	 * @param piece
	 * @param prioritylist
	 * @return
	 */
	private Boolean evaluatePaterns(ArrayList<MovementPatern> paterns, Piece piece, LinkedList<Rule> prioritylist) {
		Coordinates position = new Coordinates();
		ArrayList<MovementPatern> ongroundpaternlist = new ArrayList<MovementPatern>();
		Boolean checks = false;

		// Recovering the movement patterns with a tag corresponding to the
		// ground type the piece is currently on.
		for (MovementPatern patern : paterns) {
			if (patern.getTag().getGroundtype().equals(board.getGround(piece.getCoordonates()).getName())) {
				ongroundpaternlist.add(patern);
			}
		}

		// If no specific pattern has been found, recover the default movement
		// pattern.
		if (ongroundpaternlist.isEmpty()) {
			for (MovementPatern patern : paterns) {
				if (patern.getTag().getGroundtype().equals(Grammar.DEFAULT_GROUND)) {
					ongroundpaternlist.add(patern);
				}
			}
		}

		// For each pattern, process each move.
		for (MovementPatern patern : ongroundpaternlist) {
			for (LinkedList<Coordinates> path : patern.getPathlist()) {
				// Make a copy of the current coordinates of the piece to work
				// with it.
				simulatedcoordonates = new Coordinates(piece.getCoordonates().getX(), piece.getCoordonates().getY());

				// Iteration presets
				Iterator<Rule> prioritylistiterator = prioritylist.iterator();
				Rule curentrule = null;
				int validity = ValidityGrammar.CAN_EAT;

				// calculate the final position of the move.
				position = getFinalPosition(simulatedcoordonates, path);

				// If the final position of the move is out of the board, it is
				// directly considered invalid.
				if (isInbounds(position)) {

					/**
					 * Validate each move through the {@link RuleEvaluator}.
					 */
					while (prioritylistiterator.hasNext() && validity == ValidityGrammar.CAN_EAT) {
						curentrule = prioritylistiterator.next();

						/**
						 * If the priority of the rule is STAGE_FINAL, it means
						 * that the rule must be applied to the pies with the
						 * coordinates of the end of the deplacement, so we
						 * update the simulated coordinates.
						 * 
						 * @see RuleGrammar
						 */
						if (curentrule.getEvalutaionpriority() == RuleGrammar.STAGE_FINAL) {
							simulatedcoordonates = position;
						}

						/**
						 * Setting the context of the {@link RuleEvaluator}.
						 */
						evaluator.setContext(simulatedcoordonates, path, piece, board);

						/**
						 * Getting the validity of the move
						 * {@link ValidityGrammar}.
						 */
						validity = curentrule.evaluate(evaluator);
					}

					// If the final position of the deplacement is a slot of the
					// board which is already taken by an other piece, we make
					// sure that this piece belongs to the enemy player. We also
					// make sure that the piece doesn't hold a winning
					// condition, otherwise it is impossible to eat it so the
					// validity becomes "CHECK".
					if (validity == ValidityGrammar.CAN_EAT) {
						if (board.getPiece(position) != null) {
							Piece pospiece = board.getPiece(position);
							if (pospiece.getWincondition()
									&& !pospiece.getColor().equals(Match.getInstance().getCurrentplayer().getColor())) {
								validity = ValidityGrammar.CHECK;
								checks = true;
							}
						}

						// If the piece we are calculating the possible moves of
						// holds a winning condition, it can't put itself in
						// check so it must not be able to go at a position when
						// the enemy can eat pieces. The validity becomes
						// "NOT_VALID".
						if (piece.getWincondition() && coveredzone.contains(position)) {
							validity = ValidityGrammar.NOT_VALID;
						}
					}

					// Some pieces might not be able to eat pieces all the time
					// so some moves might be "DEPLACEMENT_ONLY" which means
					// that the move is playable but it's not a position where
					// the piece can eat another. We put the position in the
					// possible moves array.
					if (validity == ValidityGrammar.DEPLACEMENT_ONLY) {
						piece.getPossiblemoves().add(position);
					}

					// If the piece can eat an other piece at the calculated
					// position , we put it in the covered zone as well as in the
					// possible moves.
					if (validity == ValidityGrammar.CAN_EAT) {
						piece.getPossiblemoves().add(position);
						piece.getCoveredzone().add(position);
						coveredzone.add(position);
					}

					// A piece might not be able to eat a piece but the zone is
					// still covered, we put the position in the covered zone.
					else if (validity == ValidityGrammar.IS_ONLY_COVERED) {
						piece.getCoveredzone().add(position);
						coveredzone.add(position);
					}

					// If the validity is checked then we must return that the
					// piece puts the enemy player in check.
					else if (validity == ValidityGrammar.CHECK) {
						piece.getCoveredzone().add(position);
						checks = true;
					}
				}
			}
		}

		return checks;
	}

	/**
	 * The priority list is build using the priority values contained in the
	 * different {@link Rule} of the {@link Piece}. The list contains the Rules
	 * with the highest priority at its head so that they are validated first.
	 * 
	 * @see RuleGrammar
	 * @param piece
	 * @return a list of rules sorted according to their priority
	 */
	private LinkedList<Rule> buildPriorityList(Piece piece) {
		LinkedList<Rule> prioritylist = new LinkedList<Rule>();

		for (int priority : RuleGrammar.PRIORITY_LIST) {
			for (Rule rule : piece.getRules()) {
				if (rule.getEvalutaionpriority() == priority) {
					prioritylist.addLast(rule);
				}
			}
		}

		return prioritylist;
	}

	private Coordinates getFinalPosition(Coordinates pieceposition, LinkedList<Coordinates> path) {

		Coordinates position = new Coordinates(pieceposition.getX(), pieceposition.getY());
		Coordinates increment = null;

		Iterator<Coordinates> pathiterator = path.iterator();

		while (pathiterator.hasNext()) {
			increment = pathiterator.next();
			position.setX(position.getX() + increment.getX());
			position.setY(position.getY() + increment.getY());
		}

		return position;
	}

	private ArrayList<MovementPatern> getStandardPaterns(ArrayList<MovementPatern> paterns) {
		ArrayList<MovementPatern> paternslist = new ArrayList<MovementPatern>();
		for (MovementPatern patern : paterns) {
			if (patern.getTag().getSpecification().equals(Grammar.TAG_STANDARD)
					|| patern.getTag().getSpecification().equals(Grammar.MACRO_ALL)) {
				paternslist.add(patern);
			}
		}
		return paternslist;
	}

	private ArrayList<MovementPatern> getAlternativePaterns(ArrayList<MovementPatern> paterns) {
		ArrayList<MovementPatern> paternslist = new ArrayList<MovementPatern>();
		for (MovementPatern patern : paterns) {
			if (patern.getTag().getSpecification().equals(Grammar.TAG_ALTERNATIVE)
					|| patern.getTag().getSpecification().equals(Grammar.MACRO_ALL)) {
				paternslist.add(patern);
			}
		}
		return paternslist;
	}

	private Boolean isInbounds(Coordinates coordonates) {
		return coordonates.getX() < BoardParameters.BOARD_X_LENGTH && coordonates.getX() >= 0
				&& coordonates.getY() < BoardParameters.BOARD_Y_LENGTH && coordonates.getY() >= 0;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void clearCoveredZone() {
		coveredzone.clear();
	}
}
