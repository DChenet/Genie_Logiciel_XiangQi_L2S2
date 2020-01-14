package exceptions;

public class UndefinedPieceTypeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndefinedPieceTypeException(String type) {
		// TODO Auto-generated constructor stub
		super("Piece type: "+type+" is undefined");
	}

}
