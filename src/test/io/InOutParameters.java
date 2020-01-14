package test.io;

import board.components.Ground;
import board.components.Piece;
import compiler.interpreters.BoardSettingsCompiler;
import compiler.interpreters.GroundSettingsCompiler;
import compiler.interpreters.PieceSettingsCompiler;
import graphic.GraphicsMainMenu;

/**
 * In this file are referenced all the paths leading to the different external
 * text files used in the compilation process {@link GroundSettingsCompiler,
 * PieceSettingsCompiler, BoardSettingsCompiler}. The links to the different
 * folders containing the graphic resources are also listed.
 * 
 * @author Dorian CHENET
 *
 */
public class InOutParameters {

	/**
	 * Link to the text file containing the {@link Piece} informations compiled
	 * by {@link PieceSettingsCompiler}.
	 */
	public static final String MOVEMENT_SETTINGS_PATH = "src/test/io/piecesettings.txt";

	/**
	 * Link to the folder containing the {@link Ground} texture images.
	 */
	public static final String TEXTURES_REPOSITORY_PATH = "src/textures/menu";

	/**
	 * Link to the text file containing the {@link Ground} informations compiled
	 * by {@link GroundSettingsCompiler}.
	 */
	public static final String GROUND_SETTING_PATH = "src/test/io/groundsettings.txt";

	/**
	 * Links to the different test files containing informations about the
	 * playing board which are likely to be compiled by
	 * {@link BoardSettingsCompiler} if the corresponding board was selected in
	 * {@link GraphicsMainMenu}.
	 */
	public static final String BOARD_SETTING_PATH = "src/test/io/boardsettings.txt";

	public static final String BOARD_ICE_SETTING_PATH = "src/test/io/boardicesetting.txt";

	public static final String BOARD_SAND_SETTING_PATH = "src/test/io/boardsandsetting.txt";

	public static final String BOARD_MONTAIN_SETTING_PATH = "src/test/io/boardmontainsetting.txt";

	// Link to the logo image of the game.
	public static final String LOGO_ICON_PATH = "src/textures/pieces/logo.png";
}
