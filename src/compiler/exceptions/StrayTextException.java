package compiler.exceptions;

public class StrayTextException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StrayTextException(int linenumber) {
		// TODO Auto-generated constructor stub
		super("Line: "+linenumber+" stray text, delete this token");
	}
	
	public StrayTextException() {
		// TODO Auto-generated constructor stub
		super("Stray text detected, please verify your file");
	}
}
