
package compiler.builders;

import java.util.ArrayList;

import board.components.Piece;
import board.components.PieceModel;
import compiler.exceptions.InvalidCommandException;
import compiler.exceptions.UnknownParameterException;
import compiler.interpreters.PieceSettingsCompiler;
import compiler.lang.Grammar;
import compiler.lang.RuleGrammar;
import repositories.GroundsRepository;
import rules.FinalPositionGroundTypeRule;
import rules.JumpRule;
import rules.PieceTypeAlignmentRule;
import rules.Rule;

/**
 * This treatment class is used in the compilation process
 * {@link PieceSettingsCompiler} to create {@link Rule} objects which will be
 * associated to {@link PieceModel} and {@link Piece} objects. This builder
 * takes a String line in and returns a list of {@link Rule}. This builder
 * understands a part of the {@link Grammar} and uses the defined rule landmarks
 * defined in {@link RuleGrammar}.
 * 
 * @author Dorian CHENET
 *
 */
public class RuleBuilder {

	ArrayList<String> precompilationpiecetypes = new ArrayList<String>();
	private ArrayList<Rule> rulelist = new ArrayList<Rule>();
	private Rule newrule = null;
	private String[] parameters;

	public RuleBuilder(ArrayList<String> precompilationpiecetypes) {
		this.precompilationpiecetypes = precompilationpiecetypes;
	}

	public ArrayList<Rule> buildRule(String line) throws InvalidCommandException {

		rulelist = new ArrayList<Rule>();
		newrule = null;

		String unbrackettedexpression = line.substring(line.indexOf(Grammar.RULE_OPEN_BRACKET) + 1,
				line.indexOf(Grammar.RULE_CLOSE_BRACKET));

		String[] datasplit = unbrackettedexpression.split(Grammar.SEPARATOR_FUNCTIONS);
		parameters = datasplit;

		switch (datasplit[0]) {

		case RuleGrammar.JUMP_COUNT_RULE:
			newrule = create(new JumpRule());
			break;

		case RuleGrammar.FINAL_POSITION_GROUNDTYPE_RESTRICTION_RULE:
			newrule = create(new FinalPositionGroundTypeRule());
			break;

		case RuleGrammar.CANNOT_ENCOUNTER_PIECETYPE_ON_PATH_RULE:
			newrule = create(new PieceTypeAlignmentRule());
			break;
		default:

			break;
		}

		if (newrule != null && rulelist.size() == 0) {
			rulelist.add(newrule);
		}

		return rulelist;

	}

	private Rule create(JumpRule rule) {
		if (parameters.length == 2) {
			if (isConstant(parameters[1])) {
				rule.setJumpcount(Integer.valueOf(parameters[1]));
			} else {
				rule.setJumpcount(0);
			}
		}

		else if (parameters.length == 3) {
			if (isConstant(parameters[1])) {
				rule.setJumpcount(Integer.valueOf(parameters[1]));
			} else {
				rule.setJumpcount(0);
			}

			if (parameters[2].equals(RuleGrammar.EAT_ONLY_ON_LAST_JUMP_RULE_OPTION)) {
				rule.setEooloption(true);
			} else {
				rule.setEooloption(false);
			}
		} else {
			rule.setJumpcount(0);
			rule.setEooloption(false);
		}
		return rule;
	}

	private Rule create(FinalPositionGroundTypeRule rule) {
		String parameter = "";
		String groundtype = "";

		for (int index = 1; index < parameters.length; index++) {
			parameter = parameters[index];
			if (parameter.contains(Grammar.GROUND_OPEN_BRACKET) && parameter.contains(Grammar.GROUND_CLOSE_BRACKET)) {
				groundtype = parameter.substring(parameter.indexOf(Grammar.GROUND_OPEN_BRACKET) + 1,
						parameter.indexOf(Grammar.GROUND_CLOSE_BRACKET));
				if (groundtype.length() == parameter.length() - 2
						&& (GroundsRepository.getInstance().getGround(groundtype) != null || isMacro(groundtype))) {
					rule.getGroundtypes().add(groundtype);
				}
			}
		}

		if (rule.getGroundtypes().size() == 0) {

		}
		return rule;
	}

	private Rule create(PieceTypeAlignmentRule rule) {
		try {
			String parameter = "";
			String piecetype = "";

			for (int index = 1; index < parameters.length; index++) {
				parameter = parameters[index];
				if (parameter.contains(Grammar.PIECE_OPEN_BRACKET) && parameter.contains(Grammar.PIECE_CLOSE_BRACKET)) {
					piecetype = parameter.substring(parameter.indexOf(Grammar.PIECE_OPEN_BRACKET) + 1,
							parameter.indexOf(Grammar.PIECE_CLOSE_BRACKET));
					if (piecetype.length() == parameter.length() - 2 && precompilationpiecetypes.contains(piecetype)) {
						rule.getPiecetypes().add(piecetype);
					}
				}

				else if (parameter.contains(Grammar.SEPARATOR_COORDONATES)) {
					PathBuilder pathbuilder = new PathBuilder();
					rule.setPaths(pathbuilder.buildPath(parameter));
				}
			}
		} catch (UnknownParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rule;
	}

	private boolean isMacro(String value) {
		return value.equals(Grammar.MACRO_ALL) || value.equals(Grammar.MACRO_NONE);
	}

	private boolean isConstant(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
