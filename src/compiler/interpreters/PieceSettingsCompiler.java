package compiler.interpreters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import board.components.Piece;
import board.components.PieceModel;
import compiler.builders.MovementPaternBuilder;
import compiler.builders.PathBuilder;
import compiler.builders.RuleBuilder;
import compiler.exceptions.InvalidCommandException;
import compiler.exceptions.InvalidTextureFilePathException;
import compiler.exceptions.RuleRedefinitionException;
import compiler.exceptions.StrayTextException;
import compiler.exceptions.UndeclaredDefaultMovementsException;
import compiler.exceptions.UndeclaredGroundTypeException;
import compiler.exceptions.UnknownParameterException;
import compiler.exceptions.WinConditionSettingException;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import graphic.GraphicsMainMenu;
import repositories.GroundsRepository;
import repositories.PiecesRepository;
import rules.BaseRules;
import rules.Rule;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;
import test.io.InOutParameters;

/**
 * This treatment class represents the 2nd part of the compilation process occurring at the
 * when the game is launched. This class compiles the informations concerning
 * the {@link Piece} contained in the piece settings text file
 * {@link InOutParameters}. When compiling the data collected in the text file,
 * this class will create an amount of {@link PieceModel} which will be stored
 * in {@link PiecesRepository} to ease the creation of {@link Piece} objects.
 * This compiler understands the {@link Grammar}. More specifically, it creates
 * sets of {@link TaggedPath} which are then regrouped in {@link MovementPatern}
 * by the {@link MovementPaternBuilder}. It also creates the {@link Rule} using
 * the {@link RuleBuilder}, these rules are then associated to the right
 * {@link PieceModel}.
 * 
 * @see GraphicsMainMenu
 * @see Piece
 * @see PieceModel
 * @see PiecesRepository
 * @see Grammar
 * @author Dorian CHENET
 *
 */
public class PieceSettingsCompiler {

	private ArrayList<MovementPatern> paternlist = new ArrayList<MovementPatern>();
	private ArrayList<TaggedPath> pathlist = new ArrayList<TaggedPath>();
	private ArrayList<Rule> rulelist = new ArrayList<Rule>();

	private ArrayList<String> precompilationpiecetypes = new ArrayList<String>();

	private String piecetype = DefaultSettings.UNDEFINED_STRING;
	private String groundtype = DefaultSettings.UNDEFINED_STRING;
	private File redtexture = DefaultSettings.DEFAULT_TEXTURE;
	private File blacktexture = DefaultSettings.DEFAULT_TEXTURE;
	private int weight = 0;
	private Boolean winningcondition = false;

	private int linenumber = 0;

	public PieceSettingsCompiler() {

	}

	public void loadSettings(String filename) {
		try {

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			int wincount = 0;

			while ((line = reader.readLine()) != null) {

				if (line.contains(Grammar.PIECE_OPEN_BRACKET) && line.contains(Grammar.PIECE_CLOSE_BRACKET)) {
					int linelength = line.length();
					String piecename = line.substring(line.indexOf(Grammar.PIECE_OPEN_BRACKET) + 1,
							line.indexOf(Grammar.PIECE_CLOSE_BRACKET));
					if (piecename.length() == linelength - 2) {
						precompilationpiecetypes.add(piecename);
					}
				}
				if (line.equals(Grammar.WINNING_CONDITION)) {
					wincount++;
				}
			}

			reader.close();

			if (wincount != 1) {
				throw new WinConditionSettingException();
			}

			reader = new BufferedReader(new FileReader(filename));

			while ((line = reader.readLine()) != null) {
				linenumber++;

				if (line.contains(Grammar.RULE_OPEN_BRACKET) && line.contains(Grammar.RULE_CLOSE_BRACKET)) {
					createRule(line);
				}

				else if (line.contains(Grammar.PIECE_OPEN_BRACKET) && line.contains(Grammar.PIECE_CLOSE_BRACKET)) {
					piecetype = line.substring(line.indexOf(Grammar.PIECE_OPEN_BRACKET) + 1,
							line.indexOf(Grammar.PIECE_CLOSE_BRACKET));
				}

				else if (line.contains(Grammar.GROUND_OPEN_BRACKET) && line.contains(Grammar.GROUND_CLOSE_BRACKET)) {
					groundtype = line.substring(line.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
							line.indexOf(Grammar.GROUND_CLOSE_BRACKET));
					if (GroundsRepository.getInstance().exists(groundtype)
							&& !groundtype.equals(Grammar.DEFAULT_GROUND)) {
						groundtype = DefaultSettings.UNDEFINED_STRING;
						reader.close();
						throw new UndeclaredGroundTypeException(linenumber);
					}
				}

				else if (line.equals(Grammar.WINNING_CONDITION)) {
					winningcondition = true;
				}

				else if (line.contains(Grammar.SEPARATOR_DATA)) {
					analyzeData(line);
				}

				else if (line.contains(Grammar.SEPARATOR_PARAMETERS)) {
					createPath(line);
				}

				else if (line.equals(Grammar.BALISE_END)) {
					createMovementPaterns();
					registerPiece();
					compleateRules();
					completeReset();
				}

				else if (!line.equals("")) {
					reader.close();
					throw new StrayTextException(linenumber);
				}

			}

			reader.close();
		} catch (

		FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (UndeclaredGroundTypeException e) {
			System.out.println(e);
		} catch (StrayTextException e) {
			System.out.println(e);
		} catch (InvalidTextureFilePathException e) {
			System.err.println(e);
		} catch (WinConditionSettingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void analyzeData(String line) throws StrayTextException, InvalidTextureFilePathException {
		String datasplit[] = line.split(Grammar.SEPARATOR_DATA);

		if (datasplit.length == 2 && datasplit[0].contains(Grammar.TAG_RED_TEXTURE)) {
			redtexture = new File(datasplit[1]);
			if (!redtexture.exists()) {
				redtexture = DefaultSettings.DEFAULT_TEXTURE;
				throw new InvalidTextureFilePathException(redtexture.getName());
			}
		}

		else if (datasplit.length == 2 && datasplit[0].contains(Grammar.TAG_BLACK_TEXTURE)) {
			blacktexture = new File(datasplit[1]);
			if (!blacktexture.exists()) {
				blacktexture = DefaultSettings.DEFAULT_TEXTURE;
				throw new InvalidTextureFilePathException(blacktexture.getName());
			}
		}

		else if (datasplit.length == 2 && isConstant(datasplit[1])) {
			weight = Integer.parseInt(datasplit[1]);
		}

		else {
			throw new StrayTextException(linenumber);
		}
	}

	private void createMovementPaterns() {
		MovementPaternBuilder mpb = new MovementPaternBuilder();
		try {
			paternlist = mpb.buildMovementPaternList(pathlist);
		} catch (UndeclaredDefaultMovementsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createPath(String line) {
		PathBuilder pathbuilder = new PathBuilder();
		try {
			ArrayList<TaggedPath> paths = pathbuilder.buildPath(line);
			for (TaggedPath path : paths) {
				path.getTag().setGroundtype(groundtype);
				path.getTag().setPiecetype(piecetype);
			}
			pathlist.addAll(paths);
		} catch (UnknownParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createRule(String line) {
		RuleBuilder rulebuilder = new RuleBuilder(precompilationpiecetypes);
		try {
			ArrayList<Rule> rules = rulebuilder.buildRule(line);

			ArrayList<String> classnames = new ArrayList<String>();

			for (Rule rule : rulelist) {
				classnames.add(rule.getClass().getName());
			}

			for (Rule rule : rules) {
				if (!classnames.contains(rule.getClass().getName())) {
					rulelist.add(rule);
				}

				else {
					throw new RuleRedefinitionException(rule.getClass().getName(), piecetype, linenumber);
				}
			}

		} catch (InvalidCommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleRedefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void compleateRules() {

		for (Rule baserule : BaseRules.baserules) {
			Boolean checked = false;
			Rule rule = null;

			Iterator<Rule> rulesiterator = rulelist.iterator();

			while (rulesiterator.hasNext()) {
				rule = rulesiterator.next();

				if (rule.getClass().getName().equals(baserule.getClass().getName())) {
					checked = true;
				}
			}

			if (!checked) {
				rulelist.add(baserule);
			}
		}
	}

	private boolean isConstant(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void registerPiece() {
		if (paternlist.size() > 0 && !piecetype.equals("UNDEFINED") && !(weight == 0) && redtexture != null
				&& blacktexture != null) {
			PiecesRepository.getInstance().register(new PieceModel(piecetype, weight, paternlist, rulelist, redtexture,
					blacktexture, winningcondition));
		}
	}

	private void completeReset() {
		paternlist = new ArrayList<MovementPatern>();
		pathlist = new ArrayList<TaggedPath>();
		rulelist = new ArrayList<Rule>();
		piecetype = DefaultSettings.UNDEFINED_STRING;
		groundtype = DefaultSettings.UNDEFINED_STRING;
		redtexture = DefaultSettings.DEFAULT_TEXTURE;
		blacktexture = DefaultSettings.DEFAULT_TEXTURE;
		winningcondition = false;
		weight = 0;
	}

}
