package rules;

import compiler.lang.RuleGrammar;
import engine.visitor.Evaluator;
import engine.visitor.RuleEvaluator;

/**
 * This rule, child of the class {@link Rule} tells us how many units a piece
 * can jump over. For instance, in the traditional chess game, a horse can jump
 * over two pieces before reaching its final position. This rule also tells us
 * when a piece can eat an enemy piece with the "eat only on last option"
 * (eooloption). If it is enabled, the piece this rule is associated to will
 * have to jump "jumpcount" pieces before being able to eat an enemy piece.
 * 
 * @see Rule
 * @see RuleEvaluator
 * @author Dorian CHENET
 *
 */
public class JumpRule extends Rule {

	/**
	 * The number of units the piece can jump over.
	 */
	private int jumpcount = 0;

	/**
	 * This option can restrict the piece to eating an enemy piece only if it
	 * has jumped over the given number of pieces (jumpcount).
	 */
	private Boolean eooloption = false;

	private static final int EVALUATION_PRIORITY = RuleGrammar.STAGE_TRAVELING;

	public JumpRule() {
		// TODO Auto-generated constructor stub
		super(EVALUATION_PRIORITY);
	}

	public JumpRule(int jumpcount, Boolean eooloption) {
		super(EVALUATION_PRIORITY);
		this.jumpcount = jumpcount;
		this.eooloption = eooloption;
	}

	public int getJumpcount() {
		return jumpcount;
	}

	public void setJumpcount(int jumpcount) {
		this.jumpcount = jumpcount;
	}

	public Boolean getEooloption() {
		return eooloption;
	}

	public void setEooloption(Boolean eooloption) {
		this.eooloption = eooloption;
	}

	public <R> R evaluate(Evaluator<R> evaluator) {
		return evaluator.evaluate(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((eooloption == null) ? 0 : eooloption.hashCode());
		result = prime * result + jumpcount;
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
		JumpRule other = (JumpRule) obj;
		if (eooloption == null) {
			if (other.eooloption != null)
				return false;
		} else if (!eooloption.equals(other.eooloption))
			return false;
		if (jumpcount != other.jumpcount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JumpRule [jumpcount=" + jumpcount + ", eooloption=" + eooloption + "]";
	}

}
