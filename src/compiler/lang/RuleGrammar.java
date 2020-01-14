package compiler.lang;

import board.components.Piece;
import compiler.builders.RuleBuilder;
import engine.MovementValidator;
import engine.visitor.RuleEvaluator;
import rules.FinalPositionGroundTypeRule;
import rules.JumpRule;
import rules.PieceTypeAlignmentRule;
import rules.Rule;
import test.io.InOutParameters;

/**
 * The rule grammar is used in {@link RuleBuilder} to build the rules in the
 * compilation process. It is also used in the {@link MovementValidator} to
 * build a priority list of all the rules to evaluate {@link RuleEvaluator}. The
 * rule grammar gathers all the different constants defining the rules: how they
 * are declared in the text files {@link InOutParameters} and evaluation
 * priority.
 * 
 * @author Dorian CHENET
 *
 */
public class RuleGrammar {

	/**
	 * These constants are used in {@link RuleBuilder} to create {@link Rule}
	 * objects of the right type during the compilation process.
	 * 
	 * @see PieceSettingsCompiler
	 */

	/**
	 * This is the name of the {@link FinalPositionGroundTypeRule} in a text
	 * file.
	 */
	public static final String FINAL_POSITION_GROUNDTYPE_RESTRICTION_RULE = "final_position";

	/**
	 * This is the name of the {@link PieceTypeAlignmentRule} in a text file.
	 */
	public static final String CANNOT_ENCOUNTER_PIECETYPE_ON_PATH_RULE = "cannot_encounter";

	/**
	 * This is the name of the {@link JumpRule} in a text file.
	 */
	public static final String JUMP_COUNT_RULE = "jump";

	/**
	 * This is an option of the {@link JumpRule} as written in a text file. This
	 * option tells whether the piece has to jump over a given amount of pieces
	 * before beeing able to eat an enemy piece.
	 */
	public static final String EAT_ONLY_ON_LAST_JUMP_RULE_OPTION = "EAT_ONLY_ON_LAST";

	/**
	 * These are the evaluation priority values of rules, they are used to build
	 * a priority list of a {@link Piece}'s {@link Rule} set.
	 */

	/**
	 * This value tells us that the {@link Rule} must be evaluated before trying
	 * to move the {@link Piece}.
	 */
	public static final int STAGE_INITIAL = 3;

	/**
	 * This value tells us that the {@link Rule} must be evaluated while the
	 * piece is going to its final position. A move might not allow a Piece to
	 * go from a position to an other in one single step; the piece could have
	 * to go through a whole path before getting to the move's final position.
	 */
	public static final int STAGE_TRAVELING = 2;

	/**
	 * This value tells us that the {@link Rule} must be evaluated when we are
	 * sure that the piece can reach its final position, i.e when the rules of
	 * priority STAGE_INITIAL and STAGE_TRAVELING are all validated.
	 */
	public static final int STAGE_FINAL = 1;

	/**
	 * This list is used to build the {@link Rule} priority list in
	 * {@link MovementValidator}.
	 */
	public static final int[] PRIORITY_LIST = { STAGE_INITIAL, STAGE_TRAVELING, STAGE_FINAL };
}
