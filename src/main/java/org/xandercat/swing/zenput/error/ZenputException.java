package org.xandercat.swing.zenput.error;

public class ZenputException extends Exception {

	private static final long serialVersionUID = 2010093001L;
	
	public ZenputException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZenputException(String message) {
		super(message);
	}

	public ZenputException(Throwable cause) {
		super(cause);
	}
}
