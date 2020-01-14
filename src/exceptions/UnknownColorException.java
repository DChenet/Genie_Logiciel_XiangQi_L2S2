package exceptions;

public class UnknownColorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownColorException(String color) {
		// TODO Auto-generated constructor stub
		super(" "+color+" is undefined, available colors are RED or BLACK");
	}

}
