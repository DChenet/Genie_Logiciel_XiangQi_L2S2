package strategy.data;

import java.util.LinkedList;

import tags.MovementTag;

/**
 * A tagged path represents a single move a piece can make. A move is
 * represented as a linked list of {@link Coordinates}. 
 * Each coordonate is an increment, for instance: 
 * A piece is at the coordonates x = 1 and y = 1, one of its paths contains 
 * a LinkedList containing 2 coordonates: x = 1 , y = 0 and x = 0 , y = 1.
 * From its initial position x = 1, y = 1 , the piece will first go to
 * x = 2 (1+1) , y = 1 (1+0) then to x = 2 (2+0) , y = 2 (1+1).
 * At the end of the deplacement, the piece will be at the position
 * x = 2 , y = 2. The rules associated to the piece will the tell if that
 * move can be done or not, for instance, during its deplacement, a piece might
 * not be able to jump over an other etc...
 * 
 * Each LinkedList<Coordonates> is associated to a MovementTag which is used to
 * regroup the different tagged paths together into {@link MovementPatern} in
 * the treatment class MovementPaternBuilder.
 * 
 * @see Coordinates
 * @see MovementPatern
 * @see MovementPaternBuilder
 * @author Dorian CHENET
 *
 */
public class TaggedPath {

	private MovementTag tag = new MovementTag();
	private LinkedList<Coordinates> path = new LinkedList<Coordinates>();

	public TaggedPath() {
		// TODO Auto-generated constructor stub
	}

	public TaggedPath(MovementTag tag, LinkedList<Coordinates> path) {
		super();
		this.tag = tag;
		this.path = path;
	}

	public TaggedPath(LinkedList<Coordinates> path) {
		this.path = path;
	}

	public MovementTag getTag() {
		return tag;
	}

	public void setTag(MovementTag tag) {
		this.tag = tag;
	}

	public LinkedList<Coordinates> getPath() {
		return path;
	}

	public void setPath(LinkedList<Coordinates> path) {
		this.path = path;
	}

	public void addPosition(Coordinates coordonates) {
		path.addLast(coordonates);
	}

	public int size() {
		return path.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		TaggedPath other = (TaggedPath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaggedPath [tag=" + tag + ", path=" + path + "]";
	}

}
