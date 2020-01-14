package engine.visitor;

import board.components.Piece;
import compiler.lang.ValidityGrammar;
import rules.FinalPositionGroundTypeRule;
import rules.JumpRule;
import rules.PieceTypeAlignmentRule;
import strategy.data.MovementPatern;
import strategy.data.TaggedPath;

/**
 * 
 * this class uses the Visitor pattern, it is used to evaluate the validity of
 * piece moves {@link RuleEvaluator} according to the rules set associated to
 * the {@link Piece}. for each evaluation, this class of returns an Integer that
 * tells informations about the validity of a move {@link ValidityGrammar}.
 * 
 * @see TaggedPath
 * @see MovementPatern
 * @author Dorian CHENET
 *
 * @param <R>
 */
public interface Evaluator<R> {

	R evaluate(JumpRule rule);

	R evaluate(FinalPositionGroundTypeRule rule);

	R evaluate(PieceTypeAlignmentRule rule);

}
