package compiler.exceptions;

public class UnspecifiedGroundTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnspecifiedGroundTypeException() {
		super("groundtype attribute isn't specified");

	}

}
