package repositories;

import java.util.HashMap;

import board.components.Ground;
import compiler.interpreters.GroundSettingsCompiler;

/**
 * This repository uses the singleton pattern, it contains all the data
 * concerning the ground type {@link Ground} gathered during the compilation
 * process {@link GroundSettingsCompiler}. We use this repository to create
 * Ground objects easily.
 * 
 * @see Ground
 * @see GroundSettingsCompiler
 * @author Dorian CHENET
 *
 */
public class GroundsRepository {

	private HashMap<String, Ground> grounds = new HashMap<String, Ground>();

	private static GroundsRepository instance = new GroundsRepository();

	private GroundsRepository() {

	}

	public HashMap<String, Ground> getTextures() {
		return grounds;

	}

	public static GroundsRepository getInstance() {
		return instance;
	}

	public Ground getGround(String name) {
		return grounds.get(name);
	}

	public void register(Ground ground) {
		grounds.put(ground.getName(), ground);
	}

	public boolean exists(String name) {
		return grounds.get(name) == null;
	}
}
