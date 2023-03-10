package org.xandercat.swing.zenput.condition;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.xandercat.swing.zenput.annotation.CompareTo;
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
		return newCondition(annotation.dependencyOn(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	public static <D, T> NotEqualsCondition<D, T> newCondition(ValidateDependencyNotEquals annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	private static <D, T> NotEqualsCondition<D, T> newCondition(String dependencyFieldName, CompareTo compareTo, Class<?> valueType, String stringValue) throws ParseException {
		if (compareTo == CompareTo.FIXED_VALUE) {
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
	public String getMessageKey() {
		if (compareToValue == null) {
			return "condition.notEquals";
		} else {
			return "condition.notEquals.fixedValue";
		}
	}

	@Override
	public Map<String, Object> getMessageParameters() {
		Map<String, Object> messageParameters = new HashMap<String, Object>();
		messageParameters.put("dependencyFieldName", dependencyFieldName);
		if (compareToValue != null) {
			messageParameters.put("fixedValue", compareToValue);
		}
		return messageParameters;
	}	
}
