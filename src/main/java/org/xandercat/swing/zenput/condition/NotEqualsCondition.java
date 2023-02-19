package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ControlNotEquals;
import org.xandercat.swing.zenput.annotation.ValidateDependencyNotEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

/**
 * A condition where the conditional value must be NOT equal to some other fixed value.
 * Values are considered equal if they the same reference or .equals(...) return true.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class NotEqualsCondition<D, T> implements DependentCondition<D, T> {

	private T compareToValue;
	private String dependencyFieldName;

	public static <D, T> NotEqualsCondition<D, T> newCondition(ControlNotEquals annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.valueType(), annotation.stringValue());
	}
	
	public static <D, T> NotEqualsCondition<D, T> newCondition(ValidateDependencyNotEquals annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.valueType(), annotation.stringValue());
	}
	
	private static <D, T> NotEqualsCondition<D, T> newCondition(String dependencyFieldName, Class<?> valueType, String stringValue) throws ParseException {
		if (valueType != null && stringValue == null) {
			throw new IllegalArgumentException("If specifying valueType, stringValue must also be provided.");
		}
		if (valueType == null && stringValue != null) {
			throw new IllegalArgumentException("If specifying stringValue, valueType must also be provided.");
		}
		if (valueType != null) {
			return new NotEqualsCondition<D, T>(dependencyFieldName, (T) TypeUtil.parse(valueType, stringValue));
		} else {
			return new NotEqualsCondition<D, T>(dependencyFieldName);
		}		
	}
	
	public NotEqualsCondition(String dependencyFieldName) {
		this.dependencyFieldName = dependencyFieldName;
	}
	
	public NotEqualsCondition(String dependencyFieldName, T compareToValue) {
		this(dependencyFieldName);
		this.compareToValue = compareToValue;
	}
	
	@Override
	public String getDependencyFieldName() {
		return dependencyFieldName;
	}
	
	@Override
	public boolean requiresFieldValue() {
		return compareToValue == null;
	}
	
	@Override
	public boolean isMet(D fieldValue, T conditionalValue) {
		if (compareToValue == null) {
			return fieldValue != conditionalValue
					&& (fieldValue == null || !fieldValue.equals(conditionalValue));
		} else {
			return compareToValue != conditionalValue
					&& (compareToValue == null || !compareToValue.equals(conditionalValue));
		}
	}

	@Override
	public String getDescription(String conditionalValueFieldName) {
		if (compareToValue == null) {
			return "Must be NOT equal to value in field " + conditionalValueFieldName;
		} else {
			return "Field " + conditionalValueFieldName + " must NOT have a value of " + compareToValue.toString();
		}
	}
	
	
}
