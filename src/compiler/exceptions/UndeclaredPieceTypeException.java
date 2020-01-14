package compiler.exceptions;

public class UndeclaredPieceTypeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndeclaredPieceTypeException(int line, String piecetype) {
		// TODO Auto-generated constructor stub
		super("line: "+line+" unknown piece type "+piecetype);
	}

}
