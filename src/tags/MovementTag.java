package tags;

import compiler.lang.DefaultSettings;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;

/**
 * This class is used as an attribute in {@link MovementPatern} and
 * {@link TaggedPath} to reference/label data in order to ease their treatment.
 * 
 * @see MovementPatern
 * @see TaggedPath
 * @author Dorian CHENET
 *
 */
public class MovementTag {

	/*
	 * These attributes are used to describe the data used to generate the moves
	 * a piece can make. 
	 * 
	 * -"piecetype" refers to the type of piece the data must
	 * be used for/associated to. 
	 * 
	 * -"groundtype" tells us that the deplacement is
	 * only valuable on that specific type of ground (exept if the type is
	 * "DEFAULT", in which case it will be used for every ground type without any
	 * associated specific movement data).
	 * 
	 * -"specification" tells us if  the movement is standard or alternative.
	 * In the first place, the standard movements will be used, if the piece
	 * cannot move with the standard movements, the alternative movements
	 * will be used, if the piece still has no movements possible, it is blocked.
	 * 
	 * -"color" tells us for which color of piece the movement must be used.
	 * Some movements may not be symetrical, for instance, red piece (left side 
	 * of the board) might be able to go only to the right while a black piece of 
	 * the same type might be able to go only to the left. 
	 * If the movement is the same for both RED and BLACK colors then it is 
	 * referenced as ALL.
	 */
	private String piecetype = DefaultSettings.UNDEFINED_STRING;
	private String groundtype = DefaultSettings.UNDEFINED_STRING;
	private String specification = DefaultSettings.UNDEFINED_STRING;
	private String color = DefaultSettings.UNDEFINED_STRING;

	public MovementTag() {
		// TODO Auto-generated constructor stub
	}

	public MovementTag(String piecetype, String groundtype, String specification, String color) {
		this.piecetype = piecetype;
		this.groundtype = groundtype;
		this.specification = specification;
		this.color = color;
	}

	public String getPiecetype() {
		return piecetype;
	}

	public String getGroundtype() {
		return groundtype;
	}

	public String getSpecification() {
		return specification;
	}

	public String getColor() {
		return color;
	}

	public void setPiecetype(String piecetype) {
		this.piecetype = piecetype;
	}

	public void setGroundtype(String groundtype) {
		this.groundtype = groundtype;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isDefined() {
		return this.equals(new MovementTag()) || this.equals(null);
	}

	@Override
	public String toString() {
		return " piecetype=" + piecetype + ", groundtype=" + groundtype + ", specification=" + specification
				+ ", color=" + color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((groundtype == null) ? 0 : groundtype.hashCode());
		result = prime * result + ((piecetype == null) ? 0 : piecetype.hashCode());
		result = prime * result + ((specification == null) ? 0 : specification.hashCode());
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
		MovementTag other = (MovementTag) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (groundtype == null) {
			if (other.groundtype != null)
				return false;
		} else if (!groundtype.equals(other.groundtype))
			return false;
		if (piecetype == null) {
			if (other.piecetype != null)
				return false;
		} else if (!piecetype.equals(other.piecetype))
			return false;
		if (specification == null) {
			if (other.specification != null)
				return false;
		} else if (!specification.equals(other.specification))
			return false;
		return true;
	}

}
