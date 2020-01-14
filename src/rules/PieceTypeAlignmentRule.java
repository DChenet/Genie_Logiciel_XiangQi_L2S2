package rules;

import java.util.ArrayList;

import compiler.lang.RuleGrammar;
import engine.visitor.Evaluator;
import engine.visitor.RuleEvaluator;
import strategy.data.TaggedPath;

/**
 * This rule is a child class of {@link Rule}. This rule is evaluated when we
 * are sure that the piece can reach its final position (EVALUATION_PRIORITY =
 * RuleGrammar.STAGE_FINAL). This rule prevents a given piece to be aligned with
 * an other piece along a given paths. For instance, in the Xiang Qi, two
 * Generals can't be aligned on the same horizontal line. This rule will ensure
 * that for each move of the General, this does not happen.
 * 
 * @see RuleEvaluator
 * @see Rule
 * @author Dorian CHENET
 *
 */
public class PieceTypeAlignmentRule extends Rule {

	/**
	 * The piece this rule is associated to cannot be aligned with any piece
	 * type of the opposing color referred in this array.
	 */
	private ArrayList<String> piecetypes = new ArrayList<String>();

	/**
	 * The piece this rule is associated to cannot be aligned with a given piece
	 * type along one of these paths.
	 */
	private ArrayList<TaggedPath> paths = new ArrayList<TaggedPath>();

	/**
	 * This rule is evaluated when we are sure that the piece can reach the
	 * final position of the move.
	 */
	private final static int EVALUATION_PRIORITY = RuleGrammar.STAGE_FINAL;

	public PieceTypeAlignmentRule() {
		// TODO Auto-generated constructor stub
		super(EVALUATION_PRIORITY);
	}

	public PieceTypeAlignmentRule(ArrayList<String> piecetypes, ArrayList<TaggedPath> path) {
		super(EVALUATION_PRIORITY);
		this.piecetypes = piecetypes;
		this.paths = path;
	}

	public ArrayList<String> getPiecetypes() {
		return piecetypes;
	}

	public void setPiecetypes(ArrayList<String> piecetypes) {
		this.piecetypes = piecetypes;
	}

	public ArrayList<TaggedPath> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<TaggedPath> paths) {
		this.paths = paths;
	}

	public <R> R evaluate(Evaluator<R> evaluator) {
		return evaluator.evaluate(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((paths == null) ? 0 : paths.hashCode());
		result = prime * result + ((piecetypes == null) ? 0 : piecetypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PieceTypeAlignmentRule other = (PieceTypeAlignmentRule) obj;
		if (paths == null) {
			if (other.paths != null)
				return false;
		} else if (!paths.equals(other.paths))
			return false;
		if (piecetypes == null) {
			if (other.piecetypes != null)
				return false;
		} else if (!piecetypes.equals(other.piecetypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String ts = "PieceTypeAlignmentRule [piecetypes=" + piecetypes + "]\n";
		ts += ":PATHS\n";
		for (TaggedPath path : paths) {
			ts += path.toString() + "\n";
		}
		return ts;

	}

}
