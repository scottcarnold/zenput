package org.xandercat.swing.zenput.error;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 2010093001L;
	
	private String fieldName;
	
	public ValidationException(String fieldName, String message) {
		super(message);
		this.fieldName = fieldName;
	}

	public ValidationException(String fieldName, String message, Throwable cause) {
		super(message, cause);
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
}
