package compiler.exceptions;

public class RuleRedefinitionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RuleRedefinitionException(String rulename, String piecetype, int linenumber) {
		// TODO Auto-generated constructor stub
		super("Cannot define rule : "+rulename+" multiple times for piecetype "+piecetype+" at line "+linenumber);
	}

}
