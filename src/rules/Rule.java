package rules;

import board.components.Piece;
import compiler.lang.RuleGrammar;
import engine.visitor.Evaluator;
import engine.visitor.RuleEvaluator;
import strategy.data.MovementPatern;

/**
 * Rules are used to restrict the movements of a given piece / to tell the game
 * engine how to interpret the MovementPaterns of a piece. Rules are evaluated
 * by the RuleEvaluator for every move a piece can make. If a move doesn't
 * respect the rules of the piece, it cannot be played.
 * 
 * Every Rule has a priority given by an integer. The different priority values
 * are given in the class RuleGrammar. the priority is used to know when the
 * rule should be evaluated.
 * 
 * Every rule is a child of this abstract class.
 * 
 * @see RuleEvaluator
 * @see RuleGrammar
 * @see Piece
 * @see MovementPatern
 * @author Dorian CHENET
 *
 */
public abstract class Rule {

	private int evalutaionpriority = 0;

	public Rule(int evalutaionpriority) {
		this.evalutaionpriority = evalutaionpriority;
	}

	public Rule() {

	}

	public int getEvalutaionpriority() {
		return evalutaionpriority;
	}

	public void setEvalutaionpriority(int evalutaionpriority) {
		this.evalutaionpriority = evalutaionpriority;
	}

	public abstract <R> R evaluate(Evaluator<R> evaluator);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + evalutaionpriority;
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
		Rule other = (Rule) obj;
		if (evalutaionpriority != other.evalutaionpriority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rule [evalutaionpriority=" + evalutaionpriority + "]";
	}

}
