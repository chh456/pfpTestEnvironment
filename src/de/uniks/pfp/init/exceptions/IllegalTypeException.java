package de.uniks.pfp.init.exceptions;

public class IllegalTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalTypeException(String m, String type) {
		super(m != null ? m + " is not a valid type." : type + " can not be null.");
	}
	
}
