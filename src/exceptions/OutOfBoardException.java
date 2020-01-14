package exceptions;

public class OutOfBoardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfBoardException() {
		super("This object if out of the board!");
	}

}
