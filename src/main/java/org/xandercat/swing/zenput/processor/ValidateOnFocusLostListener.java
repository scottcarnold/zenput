package org.xandercat.swing.zenput.processor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * FocusListener that can be added to input components to cause validation to be 
 * automatically performed when the input component loses focus.
 * 
 * @author Scott Arnold
 */
public class ValidateOnFocusLostListener implements FocusListener {

	private static final Logger log = LogManager.getLogger(ValidateOnFocusLostListener.class);
	
	private String fieldName;
	private InputProcessor inputProcessor;
	
	public ValidateOnFocusLostListener(String fieldName, InputProcessor inputProcessor) {
		this.fieldName = fieldName;
		this.inputProcessor = inputProcessor;
	}

	@Override
	public void focusGained(FocusEvent e) {
		// no action required
	}

	@Override
	public void focusLost(FocusEvent e) {
		try {
			inputProcessor.validate(Collections.singletonList(fieldName));
		} catch (ZenputException ze) {
			log.error("Unable to validate field " + fieldName, ze);
		}
	}
}
