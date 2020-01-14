package intelligence;

import strategy.data.Coordinates;

/**
 * PirorityMove objects are used by XiangQi's AI {@link Chesster}. A
 * PriorityMove associated a move (piece to move and where to put the piece) to
 * a priority weight (priority).
 * 
 * @see Chesster
 * @author Dorian CHENET
 *
 */
public class PriorityMove {

	/*
	 * The coordonates of the piece to move.
	 */
	private Coordinates initial = null;
	
	/*
	 * The position where the AI must put the piece.
	 */
	private Coordinates finalposition = null;
	
	/*
	 * The priority of the move.
	 */
	private int priority = 0;

	public PriorityMove() {
		// TODO Auto-generated constructor stub
	}

	public PriorityMove(Coordinates initial, Coordinates finalposition, int priority) {
		super();
		this.initial = initial;
		this.finalposition = finalposition;
		this.priority = priority;
	}

	public Coordinates getInitial() {
		return initial;
	}

	public void setInitial(Coordinates initial) {
		this.initial = initial;
	}

	public Coordinates getFinalposition() {
		return finalposition;
	}

	public void setFinalposition(Coordinates finalposition) {
		this.finalposition = finalposition;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((finalposition == null) ? 0 : finalposition.hashCode());
		result = prime * result + ((initial == null) ? 0 : initial.hashCode());
		result = prime * result + priority;
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
		PriorityMove other = (PriorityMove) obj;
		if (finalposition == null) {
			if (other.finalposition != null)
				return false;
		} else if (!finalposition.equals(other.finalposition))
			return false;
		if (initial == null) {
			if (other.initial != null)
				return false;
		} else if (!initial.equals(other.initial))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PriorityMove [initial=" + initial + ", finalposition=" + finalposition + ", priority=" + priority + "]";
	}

}
