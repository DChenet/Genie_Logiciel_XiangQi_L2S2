package compiler.lang;

import board.Board;
import board.components.Ground;
import board.components.Piece;
import compiler.interpreters.BoardSettingsCompiler;
import stats.Match;
import strategy.data.Coordinates;
import test.io.InOutParameters;

/**
 * Here is the grammar used in the board settings text file
 * {@link InOutParameters} which is understood/compiled by
 * {@link BoardSettingsCompiler}. This language is used to generate the playing
 * board.
 * 
 * @author Dorian CHENET
 *
 */
public class BoardGrammar {

	/**
	 * This token indicates the beggining of the declaration of the
	 * {@link Piece} objects to place on the {@link Board} {@link Match}.
	 */
	public static final String BALISE_PIECE_SECTION = "-PIECES-";

	/**
	 * The same as above but with {@link Ground} {@link Board} {@link Math}.
	 */
	public static final String BALISE_GROUND_SECTION = "-GROUND-";

	/**
	 * These tokens indicate that the {@link Piece} that are about to get
	 * declared in the text file are of a specific color.
	 */
	public static final String RED_COLOR_BALISE = "*RED*";

	public static final String BLACK_COLOR_BALISE = "*BLACK*";

	public static final String MACRO_SYMETRICAL = "*SYMETRICAL*";

	/**
	 * These are environment generation functions, used to place {@link Ground}
	 * objects in the environment array of a {@link Board} easily.
	 */

	/**
	 * Full makes the environment array full of a precise {@link Ground} type.
	 * 
	 * @see BoardSettingsCompiler
	 */
	public static final String MACRO_FULL = "FULL";

	/**
	 * Rectangle creates a rectangle pattern of a given type of {@link Ground}
	 * at the given {@link Coordinates} on the playing {@link Board}.
	 * 
	 * @see BoardSettingsCompiler
	 */
	public static final String MACRO_RECTANGLE = "RECTANGLE";

}
