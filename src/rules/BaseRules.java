package rules;

import java.util.ArrayList;
import java.util.Arrays;

import board.components.Ground;
import board.components.Piece;
import board.components.PieceModel;
import compiler.interpreters.PieceSettingsCompiler;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import engine.visitor.RuleEvaluator;
import strategy.data.TaggedPath;
import tags.MovementTag;

/**
 * This class contains all the default rules. Every move of every piece must be
 * validated by a set of rules containing at least and only one rule of each
 * type. For a specific piece, not every rule might be declared so we have to
 * have default rules to complete that piece's rule set before proceeding to the
 * movements validation {@link PieceSettingsCompiler}.
 * 
 * @see Rule
 * @see RuleEvaluator
 * @see PieceSettingsCompiler
 * @author Dorian CHENET
 *
 */
public class BaseRules {

	/**
	 * This is the default jumprule, by default, a piece cannot jump over any
	 * units and can only eat the first enemy unit it encounters.
	 * 
	 * @see JumpRule
	 */
	public static final JumpRule jumprule = new JumpRule(0, false);

	/**
	 * By default, a piece can be placed on any types of ground: the ground
	 * types list contains the macro "ALL".
	 * 
	 * @see FinalPositionGroundTypeRule
	 * @see Grammar
	 * @see Ground
	 */
	public static final FinalPositionGroundTypeRule finalpositionrule = new FinalPositionGroundTypeRule(
			new ArrayList<>(Arrays.asList(Grammar.MACRO_ALL)));

	/**
	 * By default, a piece is free to be aligned on the playing board with any
	 * other piece in any possible way: the list of piece types a given piece
	 * cannot be aligned with contains the macro "NONE"; the MovementTag of the
	 * TaggedPath specifies that the path is valid for every color (RED/BLACK)
	 * with the macro "ALL"; the path is set to null.
	 * 
	 * @see PieceTypeAlignmentRule
	 * @see Grammar
	 * @see Piece
	 */
	public static final PieceTypeAlignmentRule alignmentrule = new PieceTypeAlignmentRule(
			new ArrayList<>(Arrays.asList(Grammar.MACRO_NONE)),
			new ArrayList<TaggedPath>(Arrays.asList(new TaggedPath(new MovementTag(DefaultSettings.UNDEFINED_STRING,
					DefaultSettings.UNDEFINED_STRING, DefaultSettings.UNDEFINED_STRING, Grammar.MACRO_ALL), null))));

	/**
	 * All the base rules are regrouped in this set. This set is used in the
	 * compilation process {@link PieceSettingsCompiler} to complete a
	 * PieceModel's set of rules if not all the rules were specifically declared
	 * for a piece type.
	 * 
	 * @see PieceSettingsCompiler
	 * @see PieceModel
	 */
	public static final Rule[] baserules = { jumprule, finalpositionrule, alignmentrule };

}
