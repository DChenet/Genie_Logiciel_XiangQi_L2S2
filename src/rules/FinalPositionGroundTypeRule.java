package rules;

import java.util.ArrayList;

import compiler.lang.Grammar;
import compiler.lang.RuleGrammar;
import engine.visitor.Evaluator;
import engine.visitor.RuleEvaluator;

/**
 * This rule, child of the class {@link Rule} tells us if a piece move is valid
 * according the ground type the piece will finish its deplacement on.
 * 
 * @see Rule
 * @see RuleEvaluator
 * @author Dorian CHENET
 *
 */
public class FinalPositionGroundTypeRule extends Rule {

	/**
	 * The list of ground types, a piece cannot land on. The list may contain
	 * the macros "ALL" or "NONE".
	 * @see Grammar
	 */
	private ArrayList<String> groundtypes = new ArrayList<String>();

	private final static int EVALUATION_PRIORITY = RuleGrammar.STAGE_FINAL;

	public FinalPositionGroundTypeRule() {
		// TODO Auto-generated constructor stub
		super(EVALUATION_PRIORITY);
	}

	public FinalPositionGroundTypeRule(ArrayList<String> groundtypes) {
		super(EVALUATION_PRIORITY);
		this.groundtypes = groundtypes;
	}

	public ArrayList<String> getGroundtypes() {
		return groundtypes;
	}

	public void setGroundtypes(ArrayList<String> groundtypes) {
		this.groundtypes = groundtypes;
	}

	public <R> R evaluate(Evaluator<R> evaluator) {
		return evaluator.evaluate(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((groundtypes == null) ? 0 : groundtypes.hashCode());
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
		FinalPositionGroundTypeRule other = (FinalPositionGroundTypeRule) obj;
		if (groundtypes == null) {
			if (other.groundtypes != null)
				return false;
		} else if (!groundtypes.equals(other.groundtypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String ret = "FinalPositionGroundTypeRule [groundtypes = ";
		for (String groundtype : groundtypes) {
			ret += groundtype + " ";
		}
		ret += "]\n";
		return ret;
	}

}
