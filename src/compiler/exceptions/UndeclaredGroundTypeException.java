package compiler.exceptions;

public class UndeclaredGroundTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndeclaredGroundTypeException(int linenumber) {
		// TODO Auto-generated constructor stub
		super("Line: " + linenumber
				+ " undeclared ground type, declare this groundtype with the appropriate synthax: (groundtype) and its associated texture: texturefile=filepath");
	}

	public UndeclaredGroundTypeException() {
		// TODO Auto-generated constructor stub
		super("Undeclared ground type detected, declare this groundtype with the appropriate synthax: (groundtype) and its associated texture: texturefile=filepath");
	}

}
