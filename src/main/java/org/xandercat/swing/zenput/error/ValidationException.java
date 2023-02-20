package org.xandercat.swing.zenput.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 2010093001L;
	
	private String fieldName;
	private String messageKey;
	private final Map<String, Object> messageParameters = new HashMap<String, Object>();
	
	public ValidationException(String fieldName, String messageKey) {
		super(messageKey);
		this.fieldName = fieldName;
		this.messageKey = messageKey;
	}

	public ValidationException(String fieldName, String messageKey, Map<String, Object> messageParameters) {
		this(fieldName, messageKey);
		this.messageParameters.putAll(messageParameters);
	}
	
	public ValidationException(String fieldName, String messageKey, Throwable cause) {
		super(messageKey, cause);
		this.fieldName = fieldName;
	}
	
	public ValidationException(String fieldName, String messageKey, Map<String, Object> messageParameters, Throwable cause) {
		this(fieldName, messageKey, cause);
		this.messageParameters.putAll(messageParameters);
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public String getMessageKey() {
		return messageKey;
	}
	
	public Map<String, Object> getMessageParameters() {
		return Collections.unmodifiableMap(messageParameters);
	}
}
