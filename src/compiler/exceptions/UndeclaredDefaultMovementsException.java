package compiler.exceptions;

import compiler.lang.Grammar;

public class UndeclaredDefaultMovementsException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndeclaredDefaultMovementsException(String piecetype) {
		super("Default ground type "+Grammar.DEFAULT_GROUND+" undeclared for piecetype "+piecetype);
	}

}
