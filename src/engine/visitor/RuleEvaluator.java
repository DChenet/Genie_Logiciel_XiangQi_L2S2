package engine.visitor;

import java.util.Iterator;
import java.util.LinkedList;

import board.Board;
import board.BoardParameters;
import board.components.Piece;
import compiler.lang.Grammar;
import compiler.lang.ValidityGrammar;
import engine.MovementValidator;
import rules.FinalPositionGroundTypeRule;
import rules.JumpRule;
import rules.PieceTypeAlignmentRule;
import rules.Rule;
import stats.Match;
import strategy.data.Coordinates;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;

/**
 * This treatment class is a part of the game's engine. Its role is to check for
 * the validity of a piece's deplacements according to a {@link Rule} which is
 * associated to the {@link Piece}. This class uses the Visitor pattern
 * {@link Evaluator} to evaluate every rules (which are of different types) in a
 * piece's rule set. For each move that will be put into the validation process,
 * the RuleValidator will return an Integer telling informations about the move
 * {@link ValidityGrammar}.
 * 
 * The different methods are called by the {@link MovementValidator} to check
 * for validity of each move according to a set of rules. Data
 * 
 * @see Rule
 * @see Piece
 * 
 *      Evaluation
 * @see MovementValidator
 * @see Evaluator
 * @see ValidityGrammar
 * 
 *      Moves
 * @see MovementPatern
 * @see TaggedPath
 * 
 * @author Dorian CHENET
 *
 */
public class RuleEvaluator implements Evaluator<Integer> {

	Coordinates piececoordonates = new Coordinates();
	LinkedList<Coordinates> path = new LinkedList<Coordinates>();
	String piececolor = "";
	Piece piece = null;
	Board board = null;

	public RuleEvaluator() {

	}

	public void setContext(Coordinates piececoordonates, LinkedList<Coordinates> path, Piece piece, Board board) {
		this.piececoordonates = piececoordonates;
		this.path = path;
		this.piececolor = piece.getColor();
		this.piece = piece;
		this.board = board;
	}

	/**
	 * Evaluating the {@link JumpRule}.
	 * 
	 * @see ValidityGrammar
	 */
	public Integer evaluate(JumpRule rule) {
		int validity = 0;
		int piececount = 0;

		Iterator<Coordinates> pathiterator = path.iterator();

		// Creating a copy of the piece's coordinates to be able to work with
		// them.
		Coordinates position = new Coordinates(piececoordonates.getX(), piececoordonates.getY());

		Coordinates increment = null;
		Piece piece = board.getPiece(position);

		// Calculating the final position of the move, a piece might not be able
		// to jump directly from a position to another but follow a path.
		while (pathiterator.hasNext()) {
			increment = pathiterator.next();
			position.setX(position.getX() + increment.getX());
			position.setY(position.getY() + increment.getY());

			piece = board.getPiece(position);

			if (piece != null && !piece.equals(this.piece)) {
				piececount++;
			}
		}

		if (rule.getEooloption()) {
			if (piece == null) {
				if (piececount < rule.getJumpcount()) {
					validity = ValidityGrammar.DEPLACEMENT_ONLY;
				} else if (piececount == rule.getJumpcount()) {
					validity = ValidityGrammar.IS_ONLY_COVERED;
				} else {
					validity = ValidityGrammar.NOT_VALID;
				}
			}

			if (piece != null) {
				if (!piece.getColor().equals(piececolor)) {
					if (piece.getWincondition()) {
						if (piececount <= rule.getJumpcount()) {
							validity = ValidityGrammar.NOT_VALID;
						}

						else if (piececount == rule.getJumpcount() + 1
								&& piece.getColor().equals(Match.getInstance().getWaitingplayer().getColor())) {
							validity = ValidityGrammar.CHECK;
						}
					} else {
						if (piececount <= rule.getJumpcount()) {
							validity = ValidityGrammar.NOT_VALID;
						}

						else if (piececount == rule.getJumpcount() + 1) {
							validity = ValidityGrammar.CAN_EAT;
						}
					}
				} else {
					if (piececount <= rule.getJumpcount()) {
						validity = ValidityGrammar.NOT_VALID;
					}

					else if (piececount == rule.getJumpcount() + 1) {
						validity = ValidityGrammar.IS_ONLY_COVERED;
					}
				}
			}
		}

		else {
			if (piece == null && piececount == rule.getJumpcount()) {
				validity = ValidityGrammar.CAN_EAT;
			}

			if (piece != null) {
				if (!piece.getColor().equals(piececolor)) {
					if (piececount <= rule.getJumpcount()+1) {
						validity = ValidityGrammar.CAN_EAT;
					}
				} else {
					if (piececount <= rule.getJumpcount() + 1) {
						validity = ValidityGrammar.IS_ONLY_COVERED;
					}
				}
			}
		}

		return validity;

	}

	/**
	 * Evaluating the {@link FinalPositionGroundTypeRule}.
	 * 
	 * @see ValidityGrammar
	 */
	public Integer evaluate(FinalPositionGroundTypeRule rule) {
		if (rule.getGroundtypes().contains(Grammar.MACRO_ALL)) {
			return ValidityGrammar.CAN_EAT;
		}

		else if (rule.getGroundtypes().contains(Grammar.MACRO_NONE)) {
			return ValidityGrammar.NOT_VALID;
		}

		else {
			int validity = ValidityGrammar.CAN_EAT;

			// Recovering the final position of the move
			Coordinates position = getFinalPosition(piece.getCoordonates(), path);

			if (isInbounds(position)) {
				if (!rule.getGroundtypes().contains(board.getGround(position).getName())) {
					// Checking if it is forbidden for the piece to be on that
					// type of ground
					validity = ValidityGrammar.NOT_VALID;
				}
			}

			else {
				validity = ValidityGrammar.NOT_VALID;
			}
			return validity;
		}
	}

	/**
	 * Evaluating the {@link PieceTypeAlignmentRule}.
	 * 
	 * @see ValidityGrammar
	 */
	public Integer evaluate(PieceTypeAlignmentRule rule) {

		// If the piece cannot be aligned with any other piece, the movement is
		// directly considered not valid.
		if (rule.getPiecetypes().contains(Grammar.MACRO_ALL)) {
			return ValidityGrammar.NOT_VALID;
		}

		// If the piece can be aligned with any other piece, the move is
		// directly considered valid.
		else if (rule.getPiecetypes().contains(Grammar.MACRO_NONE)) {
			return ValidityGrammar.CAN_EAT;
		}

		else {
			int validity = ValidityGrammar.CAN_EAT;
			Boolean stopcondition = false;

			Iterator<TaggedPath> pathlistiterator = rule.getPaths().iterator();
			TaggedPath path = null;

			// For each position of each paths contained in the rule, checks if
			// a forbidden piece type is encountered.
			while (pathlistiterator.hasNext() && !stopcondition) {
				path = pathlistiterator.next();
				if (path.getTag().getColor().equals(piececolor) || path.getTag().getColor().equals(Grammar.MACRO_ALL)) {
					Iterator<Coordinates> pathiterator = path.getPath().iterator();
					Coordinates position = new Coordinates(piececoordonates.getX(), piececoordonates.getY());
					Coordinates increment = null;

					while (pathiterator.hasNext() && !stopcondition) {
						increment = pathiterator.next();
						position.setX(position.getX() + increment.getX());
						position.setY(position.getY() + increment.getY());

						if (isInbounds(position)) {
							if (board.getPiece(position) != null) {
								if (rule.getPiecetypes().contains(board.getPiece(position).getType())
										&& !board.getPiece(position).getColor().equals(piececolor)) {
									// If a forbidden piece type is encountered,
									// the movement isn't valid.
									validity = ValidityGrammar.NOT_VALID;
									stopcondition = true;
								} else {
									stopcondition = true;
								}
							}

						} else {
							stopcondition = true;
						}
					}

				}
			}

			return validity;
		}
	}

	// Checks if a position is on the board.
	private Boolean isInbounds(Coordinates coordonates) {
		return coordonates.getX() < BoardParameters.BOARD_X_LENGTH && coordonates.getX() >= 0
				&& coordonates.getY() < BoardParameters.BOARD_Y_LENGTH && coordonates.getY() >= 0;

	}

	// Calculates the final position of a move
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

}
