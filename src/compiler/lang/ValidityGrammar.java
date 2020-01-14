package compiler.lang;

import engine.MovementValidator;
import engine.visitor.RuleEvaluator;

/**
 * In this class are listed all the values that the {@link RuleEvaluator} to
 * qualify the validity of a move. these values are also used in
 * {@link MovementValidator} for the possible moves processing.
 * 
 * @see RuleEvaluator
 * @see MovementValidator
 * @author Dorian CHENET
 *
 */
public class ValidityGrammar {

	// If the move is not valid
	public static final int NOT_VALID = 0;

	// If the piece can eat an enemy piece doing the move.
	public static final int CAN_EAT = 1;

	// If the piece cannot eat an enemy piece but the position is still covered
	// by the piece.
	public static final int IS_ONLY_COVERED = 2;

	// If a move is checking the enemy player.
	public static final int CHECK = 3;

	// If the piece cannot eat an enemy piece and the position isn't covered by
	// the piece but the piece can still move there.
	public static final int DEPLACEMENT_ONLY = 4;

}
