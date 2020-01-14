package exceptions;

public class BlockedPieceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlockedPieceException() {
		super("This piece is blocked");
	}

}
