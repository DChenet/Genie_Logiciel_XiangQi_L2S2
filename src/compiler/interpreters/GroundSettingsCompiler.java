package compiler.interpreters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import board.components.Ground;
import compiler.exceptions.InvalidFilePathException;
import compiler.exceptions.StrayTextException;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import repositories.GroundsRepository;
import test.io.InOutParameters;

/**
 * This treatment class represents the 1st part of the compiling process
 * occurring when the game is launched. It collects data from the ground
 * settings text file {@link InOutParameters} and creates a list of
 * {@link Ground} objects which are then stored in the {@link GroundsRepository}
 * to ease the creation of {@link Ground} objects. It understands a
 * {@link Grammar}. More specifically the informations collected are the
 * {@link Ground} types and their associated textures.
 * 
 * @author Dorian CHENET
 *
 */
public class GroundSettingsCompiler {

	private String groundname = DefaultSettings.UNDEFINED_STRING;
	private File groundtexture = DefaultSettings.DEFAULT_TEXTURE;

	int linenumber = 0;

	public GroundSettingsCompiler() {
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
		}

	}

	private void analyze(String line) {
		try {
			if (line.contains(Grammar.GROUND_OPEN_BRACKET) && line.contains(Grammar.GROUND_CLOSE_BRACKET)) {
				groundname = line.substring(line.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
						line.indexOf(Grammar.GROUND_CLOSE_BRACKET));
			}

			else if (groundname != null && line.contains(Grammar.GROUND_TEXTURE)
					&& line.contains(Grammar.SEPARATOR_DATA)) {
				groundtexture = new File(line.substring(line.indexOf(Grammar.SEPARATOR_DATA) + 1, line.length()));

				if (!groundtexture.exists()) {
					throw new InvalidFilePathException(linenumber);
				}
			}

			else if (groundname != null && line.equals(Grammar.BALISE_END)) {
				GroundsRepository.getInstance().register(new Ground(groundname, groundtexture));
			}

			else if (!line.equals("")) {
				throw new StrayTextException(linenumber);
			}

		} catch (StrayTextException e) {
			System.out.println(e);
		} catch (InvalidFilePathException e) {
			System.out.println(e);
		}
	}
}
