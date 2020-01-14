package compiler.exceptions;

public class ForbiddenArgumentException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ForbiddenArgumentException(String argument) {
		super("Argument :"+argument+" is forbidden, remove this token");
	}

}
