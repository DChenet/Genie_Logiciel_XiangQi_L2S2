package stats;

import java.util.ArrayList;

import board.components.Piece;
import strategy.data.Coordinates;

/**
 * This class contains all the informations relative to a player.
 * @author Dorian CHENET
 *
 */
public class Player {

	//Name of the player
	private String name = "Player";
	
	//Color of the player (RED/BLACK), RED by default
	private String color = MatchParameters.RED_COLOR;
	
	//The player's pieces that are still on the board
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	
	//The player's pieces that have been lost
	private ArrayList<Piece> takenpieces = new ArrayList<Piece>();
	
	//The pieces the player has taken
	private ArrayList<Piece> lostpieces = new ArrayList<Piece>();
	
	//The player's win condition (the piece he must defend)
	private Piece winconditionpiece = null;
	
	//Flag telling whether the player is checked or not.
	private Boolean ischecked = false;

	public Player() {
		// TODO Auto-generated constructor stub
	}

	public Player(String name, String color, ArrayList<Piece> pieces, ArrayList<Piece> takenpieces,
			ArrayList<Piece> lostpieces) {
		super();
		this.name = name;
		this.color = color;
		this.pieces = pieces;
		this.takenpieces = takenpieces;
		this.lostpieces = lostpieces;
	}
	
	public void play(){
		
	}

	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}

	public Piece getWinconditionpiece() {
		return winconditionpiece;
	}

	public void setWinconditionpiece(Piece winconditionpiece) {
		this.winconditionpiece = winconditionpiece;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public ArrayList<Piece> getTakenpieces() {
		return takenpieces;
	}

	public void setTakenpieces(ArrayList<Piece> takenpieces) {
		this.takenpieces = takenpieces;
	}

	public ArrayList<Piece> getLostpieces() {
		return lostpieces;
	}

	public void setLostpieces(ArrayList<Piece> lostpieces) {
		this.lostpieces = lostpieces;
	}

	public Piece pieceAtCoordonates(Coordinates coordonates) {
		Piece piece = null;
		for (Piece scanpiece : pieces) {
			if (scanpiece.getCoordonates().equals(coordonates)) {
				piece = scanpiece;
			}
		}
		return piece;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((ischecked == null) ? 0 : ischecked.hashCode());
		result = prime * result + ((lostpieces == null) ? 0 : lostpieces.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pieces == null) ? 0 : pieces.hashCode());
		result = prime * result + ((takenpieces == null) ? 0 : takenpieces.hashCode());
		result = prime * result + ((winconditionpiece == null) ? 0 : winconditionpiece.hashCode());
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
		Player other = (Player) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (ischecked == null) {
			if (other.ischecked != null)
				return false;
		} else if (!ischecked.equals(other.ischecked))
			return false;
		if (lostpieces == null) {
			if (other.lostpieces != null)
				return false;
		} else if (!lostpieces.equals(other.lostpieces))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pieces == null) {
			if (other.pieces != null)
				return false;
		} else if (!pieces.equals(other.pieces))
			return false;
		if (takenpieces == null) {
			if (other.takenpieces != null)
				return false;
		} else if (!takenpieces.equals(other.takenpieces))
			return false;
		if (winconditionpiece == null) {
			if (other.winconditionpiece != null)
				return false;
		} else if (!winconditionpiece.equals(other.winconditionpiece))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + ", pieces=" + pieces + ", takenpieces=" + takenpieces
				+ ", lostpieces=" + lostpieces + ", winconditionpiece=" + winconditionpiece + ", ischecked=" + ischecked
				+ "]";
	}

}
