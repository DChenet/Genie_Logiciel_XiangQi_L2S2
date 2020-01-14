package compiler.interpreters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import board.Board;
import board.BoardParameters;
import board.components.Ground;
import board.components.Piece;
import compiler.exceptions.UndeclaredPieceTypeException;
import compiler.lang.BoardGrammar;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import repositories.GroundsRepository;
import repositories.PiecesRepository;
import stats.Match;
import stats.MatchParameters;
import strategy.data.Coordinates;
import test.io.InOutParameters;

/**
 * This treatment class represents the 3rd part of the compilation process
 * occurring when the game is launched. The role of this compiler is to gather
 * data from the board settings text file {@link InOutParameters} in order to
 * initialize the playing board. It uses the data gathered in the 2 previous
 * compilation steps (1st: {@link GroundSettingsCompiler}, 2nd
 * {@link PieceSettingsCompiler}) to create the playing board. This compiler
 * understands the {@link Grammar} and the {@link BoardGrammar}, which contains
 * the world generation functions and landmarks. The data gathered is used to
 * fill the "environment" and "units" arrays of the playing board {@link Match},
 * {@link Board}.
 * 
 * @author Dorian CHENET
 *
 */
public class BoardSettingsCompiler {

	private int linenumber = 0;

	private Ground[][] environement = new Ground[BoardParameters.BOARD_X_LENGTH][BoardParameters.BOARD_Y_LENGTH];
	private HashMap<Coordinates, Piece> units = new HashMap<Coordinates, Piece>();

	private ArrayList<Coordinates> coordonateslist = new ArrayList<Coordinates>();
	private int coordonateslistindex = 0;

	private String piecetype = DefaultSettings.UNDEFINED_STRING;
	private String groundtype = DefaultSettings.UNDEFINED_STRING;
	private String color = DefaultSettings.UNDEFINED_STRING;
	private String piecesgenerationmacro = DefaultSettings.UNDEFINED_STRING;

	private String groundgenerationmacro = DefaultSettings.UNDEFINED_STRING;
	private Coordinates coordonates = new Coordinates();

	private String section = "";

	public BoardSettingsCompiler() {
		// TODO Auto-generated constructor stub
	}

	public void loadSettings(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				linenumber++;
				analyze(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (UndeclaredPieceTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void analyze(String line) throws UndeclaredPieceTypeException {
		if (line.equals(BoardGrammar.BALISE_PIECE_SECTION)) {
			section = BoardGrammar.BALISE_PIECE_SECTION;
		}

		else if (line.equals(BoardGrammar.BALISE_GROUND_SECTION)) {
			section = BoardGrammar.BALISE_GROUND_SECTION;
		}

		else if (line.equals(BoardGrammar.RED_COLOR_BALISE)) {
			color = MatchParameters.RED_COLOR;
		}

		else if (line.equals(BoardGrammar.BLACK_COLOR_BALISE)) {
			color = MatchParameters.BLACK_COLOR;
		}

		else if (line.equals(BoardGrammar.MACRO_SYMETRICAL)) {
			color = MatchParameters.RED_COLOR;
			piecesgenerationmacro = BoardGrammar.MACRO_SYMETRICAL;
		}

		else {
			if (line.contains(Grammar.SEPARATOR_PARAMETERS)) {
				String[] parameterssplit = line.split(Grammar.SEPARATOR_PARAMETERS);

				for (String parameter : parameterssplit) {

					if (parameter.contains(Grammar.PIECE_OPEN_BRACKET)
							&& parameter.contains(Grammar.PIECE_CLOSE_BRACKET)
							&& section.equals(BoardGrammar.BALISE_PIECE_SECTION)) {
						if (parameter
								.substring(parameter.indexOf(Grammar.PIECE_OPEN_BRACKET) + 1,
										parameter.indexOf(Grammar.PIECE_CLOSE_BRACKET))
								.length() == parameter.length() - 2) {

							piecetype = parameter.substring(parameter.indexOf(Grammar.PIECE_OPEN_BRACKET) + 1,
									parameter.indexOf(Grammar.PIECE_CLOSE_BRACKET));

							if (PiecesRepository.getInstance().getPiece(piecetype) == null) {
								throw new UndeclaredPieceTypeException(linenumber, piecetype);
							}
						}
					}

					if (parameter.contains(Grammar.GROUND_OPEN_BRACKET)
							&& parameter.contains(Grammar.GROUND_CLOSE_BRACKET)
							&& section.equals(BoardGrammar.BALISE_GROUND_SECTION)) {
						if (parameter
								.substring(parameter.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
										parameter.indexOf(Grammar.GROUND_CLOSE_BRACKET))
								.length() == parameter.length() - 2) {
							if (GroundsRepository.getInstance()
									.getGround(parameter.substring(parameter.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
											parameter.indexOf(Grammar.GROUND_CLOSE_BRACKET))) != null) {
								groundtype = parameter.substring(parameter.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
										parameter.indexOf(Grammar.GROUND_CLOSE_BRACKET));
							}
						}
					}

					if (parameter.contains(Grammar.SEPARATOR_COORDONATES)) {
						String[] coordonatessplit = parameter.split(Grammar.SEPARATOR_COORDONATES);

						if (isConstant(coordonatessplit[0]) && isConstant(coordonatessplit[1])) {
							coordonates.setX(Integer.valueOf(coordonatessplit[0]));
							coordonates.setY(Integer.valueOf(coordonatessplit[1]));
						}
					}

					if (parameter.equals(BoardGrammar.MACRO_FULL)) {
						groundgenerationmacro = BoardGrammar.MACRO_FULL;
					}

					if (parameter.equals(BoardGrammar.MACRO_RECTANGLE)) {
						groundgenerationmacro = BoardGrammar.MACRO_RECTANGLE;
					}

					if (groundgenerationmacro.equals(BoardGrammar.MACRO_RECTANGLE)
							&& parameter.contains(Grammar.SEPARATOR_COORDONATES)) {
						String[] coordonates = parameter.split(Grammar.SEPARATOR_COORDONATES);
						coordonateslist.add(coordonateslistindex,
								new Coordinates(Integer.valueOf(coordonates[0]), Integer.valueOf(coordonates[1])));
						coordonateslistindex++;
					}

				}
			}

			if (!groundtype.equals(DefaultSettings.UNDEFINED_STRING)
					&& groundgenerationmacro.equals(BoardGrammar.MACRO_FULL)) {
				for (int index1 = 0; index1 < BoardParameters.BOARD_X_LENGTH; index1++) {
					for (int index2 = 0; index2 < BoardParameters.BOARD_Y_LENGTH; index2++) {
						environement[index1][index2] = new Ground(groundtype);
					}
				}
				coordonateslistindex = 0;
			}

			else if (!groundtype.equals(DefaultSettings.UNDEFINED_STRING)
					&& groundgenerationmacro.equals(BoardGrammar.MACRO_RECTANGLE)) {
				for (int index1 = coordonateslist.get(0).getX(); index1 <= coordonateslist.get(1).getX(); index1++) {
					for (int index2 = coordonateslist.get(0).getY(); index2 <= coordonateslist.get(1)
							.getY(); index2++) {
						environement[index1][index2] = new Ground(groundtype);
					}
				}
				coordonateslistindex = 0;
			}

			if (!piecetype.equals(DefaultSettings.UNDEFINED_STRING) && isInbounds(coordonates)) {
				if (piecesgenerationmacro.equals(BoardGrammar.MACRO_SYMETRICAL)) {

					Piece newpiece = new Piece(piecetype, coordonates, color);
					units.put(coordonates, newpiece);

					addPieceToPlayer(color, newpiece);

					String reversedcolor = "";
					if (color.equals(MatchParameters.RED_COLOR)) {
						reversedcolor = MatchParameters.BLACK_COLOR;
					}

					else {
						reversedcolor = MatchParameters.RED_COLOR;
					}

					Coordinates reversedcoordonates = new Coordinates(
							BoardParameters.BOARD_X_LENGTH - coordonates.getX() - 1,
							BoardParameters.BOARD_Y_LENGTH - coordonates.getY() - 1);

					Piece reversedpiece = new Piece(piecetype, reversedcoordonates, reversedcolor);
					units.put(reversedcoordonates, reversedpiece);

					addPieceToPlayer(reversedcolor, reversedpiece);

				} else {
					Piece newpiece = new Piece(piecetype, coordonates, color);
					units.put(coordonates, newpiece);
					addPieceToPlayer(color, newpiece);
				}

			}

			else if (!groundtype.equals(DefaultSettings.UNDEFINED_STRING)
					&& groundgenerationmacro.equals(DefaultSettings.UNDEFINED_STRING) && isInbounds(coordonates)) {
				environement[coordonates.getX()][coordonates.getY()] = new Ground(groundtype);
			}
		}

		if (line.equals(Grammar.BALISE_END)) {

			if (section.equals(BoardGrammar.BALISE_GROUND_SECTION)) {
				Match.getBoard().setEnvironement(environement);
			}

			else if (section.equals(BoardGrammar.BALISE_PIECE_SECTION)) {
				Match.getBoard().setUnits(units);
			}
		}

		resetLine();
	}

	private void addPieceToPlayer(String color, Piece piece) {
		if (piece.getWincondition()) {
			Match.getInstance().getColorPlayer(color).setWinconditionpiece(piece);
		} else {
			Match.getInstance().getColorPlayer(color).getPieces().add(piece);
		}
	}

	private void resetLine() {
		piecetype = DefaultSettings.UNDEFINED_STRING;
		groundtype = DefaultSettings.UNDEFINED_STRING;
		groundgenerationmacro = DefaultSettings.UNDEFINED_STRING;
		coordonateslist.clear();
		coordonateslistindex = 0;
		coordonates = new Coordinates();
	}

	private boolean isConstant(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isInbounds(Coordinates coordonates) {
		return coordonates.getX() < BoardParameters.BOARD_X_LENGTH && coordonates.getX() >= 0
				&& coordonates.getY() < BoardParameters.BOARD_Y_LENGTH && coordonates.getY() >= 0;
	}
}
