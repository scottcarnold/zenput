package org.xandercat.swing.zenput.validator;

import java.util.Collections;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for validating String values.  Can optionally match against a regular expression.
 * 
 * @author Scott Arnold
 */
public class StringValidator extends AbstractValidator<String> {

	private String expression;		// regular expression
	private String expressionName;	// user-friendly name of the expression
	
	public StringValidator(String expression) {
		super();
		this.expression = expression;
	}
	
	public StringValidator(String expression, String expressionName) {
		this(expression);
		this.expressionName = expressionName;
	}
	
	@Override
	public Class<String> getValueType() {
		return String.class;
	}

	@Override
	public void validate(String fieldName, String value) throws ValidationException {
		if (expression != null) {
			if (!value.matches(expression)) {
				if (expressionName == null || expressionName.trim().length() == 0) {
					throw new ValidationException(fieldName, "validator.string.regex", Collections.singletonMap("regex", expression));
				} else {
					throw new ValidationException(fieldName, "validator.string.regexName", Collections.singletonMap("regexName", expressionName));
				}
			}
		}
	}
}
