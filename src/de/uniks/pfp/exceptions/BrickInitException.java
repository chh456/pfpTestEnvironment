package de.uniks.pfp.exceptions;

public class BrickInitException extends Exception {

	private static final long serialVersionUID = 1L;

	public BrickInitException(String m) {
		super(m != null ? m : "IP can not be null");
	}
	
}
