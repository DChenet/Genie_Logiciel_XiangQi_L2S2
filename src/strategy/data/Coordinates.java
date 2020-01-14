package strategy.data;

/**
 * Coordonates are x,y used to tell the position of the units on the playing board.
 * They are also in the deplacements representation.
 * 
 * @see TaggedPath
 * @see MovementPatern
 * @author Dorian CHENET
 *
 */
public class Coordinates {

	private int x = 0;
	private int y = 0;

	public Coordinates() {
		// TODO Auto-generated constructor stub
	}

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordonates [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	 
	
}
