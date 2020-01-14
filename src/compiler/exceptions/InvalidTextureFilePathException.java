package compiler.exceptions;

public class InvalidTextureFilePathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidTextureFilePathException(String name) {
		super("Texture file "+name+" i not found");
	}

}
