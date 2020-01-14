package board.components;

import java.io.File;
import java.util.ArrayList;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import board.BoardParameters;
import compiler.lang.DefaultSettings;
import compiler.lang.Grammar;
import engine.MovementValidator;
import engine.simulation.Simulation;
import engine.visitor.RuleEvaluator;
import exceptions.OutOfBoardException;
import exceptions.UndefinedPieceTypeException;
import exceptions.UnknownColorException;
import repositories.PiecesRepository;
import rules.Rule;
import stats.MatchParameters;
import strategy.data.Coordinates;
import strategy.data.MovementPatern;

/**
 * This class represents a playing piece, pieces are generated from
 * {@link PieceModel} contained in {@link PiecesRepository} according to the
 * given piecetype.
 * 
 * @see PieceModel
 * @see PiecesRepository
 * @author Dorian CHENET
 * 
 */
public class Piece {

	// The graphic texture of the piece
	private File texture = DefaultSettings.DEFAULT_TEXTURE;

	// The weigth of the piece (Used by the AI @see Chesster)
	private int weight = 0;

	// The piece type (ex: Soldier, General ...)
	private String type = DefaultSettings.UNDEFINED_STRING;

	// The piece color (which player it belongs to, RED / BLACK).
	private String color = DefaultSettings.UNDEFINED_STRING;

	// The curent coordonates of the piece
	private Coordinates coordonates = new Coordinates();

	// A list of all the possible moves the piece can make
	private ArrayList<Coordinates> possiblemoves = new ArrayList<Coordinates>();

	// A list of every positions where the piece can eat an other
	private ArrayList<Coordinates> coveredzone = new ArrayList<Coordinates>();

	/**
	 * A list of all the movement paterns relatives to the piece type, these
	 * paterns are used to generate the possible moves.
	 * 
	 * @see MovementValidator
	 * @see RuleEvaluator
	 */
	private ArrayList<MovementPatern> movementpaterns = new ArrayList<MovementPatern>();

	/**
	 * A list of all the rules relative to the piece type, they are used as
	 * conditions to determinate the possible moves from the MovementPaterns.
	 * 
	 * @see RuleEvaluator
	 */
	private ArrayList<Rule> rules = new ArrayList<Rule>();

	/**
	 * This attribute tells whether the piece is the winning condition or not,
	 * if it is, it's the piece that the player must defend. A piece holding a
	 * winning condition cannot be eaten.
	 */
	private Boolean wincondition = false;

	public Piece() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This constructor is used to create pieces from the corresponding informations stored in {@link PiecesRepository}.
	 * @see PiecesRepository
	 * @param type
	 * @param coordonates
	 * @param color
	 */
	public Piece(String type, Coordinates coordonates, String color) {
		try {

			// Checking if the piece getting creating is set inbounds
			if (coordonates.getX() > BoardParameters.BOARD_X_LENGTH
					|| coordonates.getY() > BoardParameters.BOARD_Y_LENGTH || coordonates.getX() < 0
					|| coordonates.getY() < 0) {
				throw new OutOfBoardException();
			}

			this.coordonates = coordonates;
			this.color = color;
			this.type = type;

			PieceModel model = null;

			// Recovering the PieceModel from which we will extract the needed
			// data
			if (PiecesRepository.getInstance().exists(type)) {
				model = PiecesRepository.getInstance().getPiece(type);
			} else {
				throw new UndefinedPieceTypeException(type);
			}

			// Extracting textures
			if (color.equals(MatchParameters.RED_COLOR)) {
				this.texture = model.getRedtexture();
			}

			else if (color.equals(MatchParameters.BLACK_COLOR)) {
				this.texture = model.getBlacktexture();
			}

			else {
				throw new UnknownColorException(color);
			}

			// Extracting the weight
			this.weight = model.getWeight();

			// Extracting the MovementPaterns
			for (MovementPatern patern : model.getMovementpaterns()) {
				if (patern.getTag().getColor().equals(color) || patern.getTag().getColor().equals(Grammar.MACRO_ALL)) {
					this.movementpaterns.add(patern);
				}
			}

			// Extracting the Rules
			this.rules = model.getRules();

			// Extracting the winning condition attribute
			this.wincondition = model.getWinningcondition();

		} catch (OutOfBoardException e) {
			System.out.println(e);
		} catch (UnknownColorException e) {
			System.out.println(e);
		} catch (UndefinedPieceTypeException e) {
			System.out.println(e);
		}
	}

	/**
	 * This Method is used to make copies of Pieces, this method is used to
	 * duplicate Pieces easily.
	 * 
	 * @see Simulation
	 * @param piece
	 */
	public Piece(Piece piece) {

		this.coordonates = piece.getCoordonates();
		this.color = piece.getColor();
		this.type = piece.getType();
		this.texture = piece.getTexture();
		this.weight = piece.getWeight();
		this.movementpaterns = piece.getMovementpaterns();
		this.rules = piece.getRules();
		this.wincondition = piece.getWincondition();
		this.possiblemoves = piece.getPossiblemoves();
		this.coveredzone = piece.coveredzone;

	}

	public ArrayList<Coordinates> getCoveredzone() {
		return coveredzone;
	}

	public void setCoveredzone(ArrayList<Coordinates> coveredzone) {
		this.coveredzone = coveredzone;
	}

	public Coordinates getCoordonates() {
		return coordonates;
	}

	public void setCoordonates(Coordinates coordonates) {
		try {
			if (coordonates.getX() > BoardParameters.BOARD_X_LENGTH
					|| coordonates.getY() > BoardParameters.BOARD_Y_LENGTH || coordonates.getX() < 0
					|| coordonates.getY() < 0) {
				throw new OutOfBoardException();
			}
			this.coordonates = coordonates;
		} catch (OutOfBoardException e) {
			System.out.println(e);
		}
	}

	public void setCoordonates(int x, int y) {
		try {
			if (x > BoardParameters.BOARD_X_LENGTH || y > BoardParameters.BOARD_Y_LENGTH || x < 0 || y < 0) {
				throw new OutOfBoardException();
			}
			coordonates.setX(x);
			coordonates.setY(y);
		} catch (OutOfBoardException e) {
			System.out.println(e);
		}
	}

	public Boolean getWincondition() {
		return wincondition;
	}

	public void setWincondition(Boolean wincondition) {
		this.wincondition = wincondition;
	}

	public String getColor() {
		return color;
	}

	public File getTexture() {
		return texture;
	}

	public int getWeight() {
		return weight;
	}

	public String getType() {
		return type;
	}

	public ArrayList<Coordinates> getPossiblemoves() {
		return possiblemoves;
	}

	public ArrayList<MovementPatern> getMovementpaterns() {
		return movementpaterns;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}

	public void setTexture(File texture) {
		this.texture = texture;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setPossiblemoves(ArrayList<Coordinates> possiblemoves) {
		this.possiblemoves = possiblemoves;
	}

	public void setMovementpaterns(ArrayList<MovementPatern> movementpatern) {
		this.movementpaterns = movementpatern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((coordonates == null) ? 0 : coordonates.hashCode());
		result = prime * result + ((coveredzone == null) ? 0 : coveredzone.hashCode());
		result = prime * result + ((movementpaterns == null) ? 0 : movementpaterns.hashCode());
		result = prime * result + ((possiblemoves == null) ? 0 : possiblemoves.hashCode());
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + weight;
		result = prime * result + ((wincondition == null) ? 0 : wincondition.hashCode());
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
		Piece other = (Piece) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (coordonates == null) {
			if (other.coordonates != null)
				return false;
		} else if (!coordonates.equals(other.coordonates))
			return false;
		if (coveredzone == null) {
			if (other.coveredzone != null)
				return false;
		} else if (!coveredzone.equals(other.coveredzone))
			return false;
		if (movementpaterns == null) {
			if (other.movementpaterns != null)
				return false;
		} else if (!movementpaterns.equals(other.movementpaterns))
			return false;
		if (possiblemoves == null) {
			if (other.possiblemoves != null)
				return false;
		} else if (!possiblemoves.equals(other.possiblemoves))
			return false;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		if (texture == null) {
			if (other.texture != null)
				return false;
		} else if (!texture.equals(other.texture))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (weight != other.weight)
			return false;
		if (wincondition == null) {
			if (other.wincondition != null)
				return false;
		} else if (!wincondition.equals(other.wincondition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Piece [texture=" + texture + ", weight=" + weight + ", type=" + type + ", color=" + color
				+ ", coordonates=" + coordonates + ", possiblemoves=" + possiblemoves + ", coveredzone=" + coveredzone
				+ ", movementpaterns=" + movementpaterns + ", rules=" + rules + ", wincondition=" + wincondition + "]";
	}

}
