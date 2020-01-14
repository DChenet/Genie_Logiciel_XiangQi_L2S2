package compiler.exceptions;

public class UnknownParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownParameterException(String parameter) {
		super("Expression: " + parameter + "is unknown, delete this token");
	}

}
