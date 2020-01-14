package compiler.exceptions;

public class InvalidFilePathException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFilePathException(int linenumber) {
		// TODO Auto-generated constructor stub
		super("Line: "+linenumber+" this file does not exist");
	}

}
