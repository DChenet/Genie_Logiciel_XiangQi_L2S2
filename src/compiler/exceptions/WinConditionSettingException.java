package compiler.exceptions;

public class WinConditionSettingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WinConditionSettingException() {
		// TODO Auto-generated constructor stub
		super("Win condition must be set at least and only one time");
	}

}
