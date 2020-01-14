package board.components;

import java.io.File;
import java.util.ArrayList;

import compiler.lang.DefaultSettings;
import rules.Rule;
import strategy.data.MovementPatern;

/**
 * This class contains all the data relative to a single piece type. PieceModels
 * are created by {@link PieceSettingsCompiler} which fills the needed
 * informations and stores them in {@link PiecesRepository}. PieceModels are
 * used to create {@link Piece} easily.
 * 
 * @see Piece
 * @see PieceSettingsCompiler
 * @see PiecesRepository
 * @author Dorian CHENET
 *
 */
public class PieceModel {

	//The piece type (ex: Soldier, General ...)
	private String type = "";
	
	//Its weight (how strong the piece is)
	private int weight = 0;

	/**
	 * All movement paterns associated to the piece types.
	 * @see MovementPatern
	 */
	private ArrayList<MovementPatern> movementpaterns = new ArrayList<MovementPatern>();
	
	/**
	 * All the rules telling us how to exploit the movement paterns.
	 * @see MovementPatern
	 * @see Rule
	 */
	private ArrayList<Rule> rules = new ArrayList<Rule>();
	
	/**
	 * The textures to display for according to the piece color (RED / BLACK)
	 */
	private File redtexture = DefaultSettings.DEFAULT_TEXTURE;
	private File blacktexture = DefaultSettings.DEFAULT_TEXTURE;

	//The winning condition
	private Boolean winningcondition = false;

	public PieceModel() {

	}

	public PieceModel(String type, int weight, ArrayList<MovementPatern> movementpatern, ArrayList<Rule> rules,
			File redtexture, File blacktexture, Boolean winningcondition) {
		super();
		this.type = type;
		this.weight = weight;
		this.movementpaterns = movementpatern;
		this.rules = rules;
		this.redtexture = redtexture;
		this.blacktexture = blacktexture;
		this.winningcondition = winningcondition;
	}

	public Boolean getWinningcondition() {
		return winningcondition;
	}

	public void setWinningcondition(Boolean winningcondition) {
		this.winningcondition = winningcondition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public ArrayList<MovementPatern> getMovementpaterns() {
		return movementpaterns;
	}

	public void setMovementpaterns(ArrayList<MovementPatern> movementpatern) {
		this.movementpaterns = movementpatern;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}

	public File getRedtexture() {
		return redtexture;
	}

	public void setRedtexture(File redtexture) {
		this.redtexture = redtexture;
	}

	public File getBlacktexture() {
		return blacktexture;
	}

	public void setBlacktexture(File blacktexture) {
		this.blacktexture = blacktexture;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blacktexture == null) ? 0 : blacktexture.hashCode());
		result = prime * result + ((movementpaterns == null) ? 0 : movementpaterns.hashCode());
		result = prime * result + ((redtexture == null) ? 0 : redtexture.hashCode());
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + weight;
		result = prime * result + ((winningcondition == null) ? 0 : winningcondition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PieceModel other = (PieceModel) obj;
		if (blacktexture == null) {
			if (other.blacktexture != null)
				return false;
		} else if (!blacktexture.equals(other.blacktexture))
			return false;
		if (movementpaterns == null) {
			if (other.movementpaterns != null)
				return false;
		} else if (!movementpaterns.equals(other.movementpaterns))
			return false;
		if (redtexture == null) {
			if (other.redtexture != null)
				return false;
		} else if (!redtexture.equals(other.redtexture))
			return false;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (weight != other.weight)
			return false;
		if (winningcondition == null) {
			if (other.winningcondition != null)
				return false;
		} else if (!winningcondition.equals(other.winningcondition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PieceModel [type=" + type + ", weight=" + weight + ", movementpaterns=" + movementpaterns + ", rules="
				+ rules + ", redtexture=" + redtexture + ", blacktexture=" + blacktexture + ", winningcondition="
				+ winningcondition + "]";
	}

}
