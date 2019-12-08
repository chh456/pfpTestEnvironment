package de.uniks.pfp.init.exceptions;

public class IllegalPortException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalPortException(String m, String type) {
		super(m != null ? m + " is not a valid port." : type + " can not be null.");
	}
	
}
