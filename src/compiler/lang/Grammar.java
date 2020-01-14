package compiler.lang;

import board.Board;
import board.components.Ground;
import board.components.Piece;
import compiler.builders.MovementPaternBuilder;
import compiler.builders.PathBuilder;
import compiler.builders.RuleBuilder;
import compiler.interpreters.BoardSettingsCompiler;
import compiler.interpreters.GroundSettingsCompiler;
import compiler.interpreters.PieceSettingsCompiler;
import engine.MainEngine;
import engine.MovementValidator;
import graphic.BoardGraphic;
import graphic.PlayerBoard;
import intelligence.Chesster;
import rules.BaseRules;
import rules.FinalPositionGroundTypeRule;
import rules.PieceTypeAlignmentRule;
import rules.Rule;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;
import test.io.InOutParameters;

/**
 * This class contains all the grammar understood by the compilers
 * {@link GroundSettingsCompiler, PieceSettingsCompiler, BoardSettingsCompiler}.
 * The grammar is a set of words/characters that are used in the settings files
 * {@link InOutParameters} to code the {@link Piece, Ground, Board} informations
 * / specifications. This grammar is made to make the modification of the
 * different parameters of the game easy and understandable.
 * 
 * It also contains the macros, some are used in the processing of {@link Rule}
 * {@link BaseRules}.
 * 
 * @see GroundSettingsCompiler
 * @see PieceSettingsCompiler
 * @see BoardSettingsCompiler
 * @see InOutParameters
 * 
 * @author Dorian CHENET
 *
 */
public class Grammar {

	/**
	 * This token is used in the declaration of a piece to tell the compiler
	 * that the piece type holds the winning condition.
	 * 
	 * @see Piece
	 * @see MainEngine
	 * @see PieceSettingsCompiler
	 */
	public static final String WINNING_CONDITION = "WINNINGCONDITION";

	/**
	 * This token is used to initialize a {@link Board} with default grounds.
	 */
	public static final String DEFAULT_GROUND = "DEFAULT";

	/**
	 * These are macros used to designate all/no objects.
	 * 
	 * @see FinalPositionGroundTypeRule
	 * @see PieceTypeAlignmentRule
	 */
	public static final String MACRO_ALL = "ALL";

	public static final String MACRO_NONE = "NONE";

	/**
	 * In the settings text files, we always refer to pieces by putting their
	 * name between the following brackets.
	 * 
	 * @see Piece
	 * @see PieceSettingsCompiler
	 */
	public static final String PIECE_OPEN_BRACKET = "(";

	public static final String PIECE_CLOSE_BRACKET = ")";

	/**
	 * In the settings text files, we always refer to grounds by putting their
	 * name between the following brackets.
	 * 
	 * @see Ground
	 * @see GroundSettingsCompiler
	 */
	public static final String GROUND_OPEN_BRACKET = "[";

	public static final String GROUND_CLOSE_BRACKET = "]";

	/**
	 * In the settings text files, rules are always represented between the
	 * following brackets.
	 * 
	 * @see Piece
	 * @see Rule
	 * @see RuleBuilder
	 * @see PieceSettingsCompiler
	 */
	public static final String RULE_OPEN_BRACKET = "{";

	public static final String RULE_CLOSE_BRACKET = "}";

	/**
	 * This token marks the end of a section/ the end of the declaration of an
	 * object.
	 * 
	 * @see GroundSettingsCompiler
	 * @see PieceSettingsCompiler
	 * @see BoardSettingsCompiler
	 * 
	 * 
	 */
	public static final String BALISE_END = "/";

	/**
	 * These tokens are used when declaring a piece movement to tell if the
	 * movement is standard or alternative.
	 * 
	 * @see TaggedPath
	 * @see PathBuilder
	 * 
	 * @see MovementPatern
	 * @see MovementPaternBuilder
	 * 
	 * @see MovementValidator
	 */
	public static final String TAG_ALTERNATIVE = "alt";

	public static final String TAG_STANDARD = "std";

	/**
	 * These tokens are path generation modifiers {@link TaggedPath}, they are
	 * understood by {@link PathBuilder}. They influence how the path that is
	 * written in the text file {@link InOutParameters} is understood by the
	 * builder.
	 */
	public static final String MODIFIER_INFINITE = "inf";

	public static final String MODIFIER_POLARIZED = "polar";

	/**
	 * This balise indicates that the following token is the weight of a
	 * {@link Piece}.
	 * 
	 * @see Chesster
	 */
	public static final String TAG_WEIGHT = "weight";

	/**
	 * These tokens indicate that the following tokens are the paths to the red
	 * / black textures of a {@link Piece}.
	 * 
	 * @see BoardGraphic
	 * @see PlayerBoard
	 */
	public static final String TAG_RED_TEXTURE = "RED_texture";

	public static final String TAG_BLACK_TEXTURE = "BLACK_texture";

	/**
	 * This token is understood by {@link GroundSettingsCompiler} only, it
	 * indicates that the following token is the path to a ground texture file.
	 * 
	 * @see Ground
	 * @see GroundSettingsCompiler
	 */
	public static final String GROUND_TEXTURE = "texturefile";

	/**
	 * The following tokens are separator on which the compilers and builders
	 * will be able to split to extract data from String lines (from the text
	 * files) and understand the nature of the extracted data.
	 * 
	 * @see GroundSettingsCompiler
	 * @see PieceSettingsCompiler
	 * @see BoardSettingsCompiler
	 * @see PathBuilder
	 * @see RuleBuilder
	 */
	public static final String SEPARATOR_FUNCTIONS = ";";

	public static final String SEPARATOR_PARAMETERS = ":";

	public static final String SEPARATOR_COORDONATES = ",";

	public static final String SEPARATOR_PATHING = ">";

	public static final String SEPARATOR_DATA = "=";

}
