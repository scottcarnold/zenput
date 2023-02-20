package org.xandercat.swing.zenput.condition;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.xandercat.swing.zenput.annotation.CompareTo;
import org.xandercat.swing.zenput.annotation.ControlEquals;
import org.xandercat.swing.zenput.annotation.ValidateDependencyEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

public class EqualsCondition<D, T> implements DependentCondition<D, T> {
	
	private String dependencyFieldName;
	private T compareToFixedValue;
	
	public static <D, T> EqualsCondition<D, T> newCondition(ControlEquals annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	public static <D, T> EqualsCondition<D, T> newCondition(ValidateDependencyEquals annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	private static <D, T> EqualsCondition<D, T> newCondition(String dependencyFieldName, CompareTo compareTo, Class<?> valueType, String stringValue) throws ParseException {
		if (compareTo == CompareTo.FIXED_VALUE) {
			return new EqualsCondition<D, T>(dependencyFieldName, (T) TypeUtil.parse(valueType, stringValue));
		} else {
			return new EqualsCondition<D, T>(dependencyFieldName);
		}		
	}
	
	public EqualsCondition(String dependencyFieldName) {
		this.dependencyFieldName = dependencyFieldName;
	}
	
	public EqualsCondition(String dependencyFieldName, T compareToFixedValue) {
		this(dependencyFieldName);
		this.compareToFixedValue = compareToFixedValue;
	}
	
	@Override
	public String getDependencyFieldName() {
		return dependencyFieldName;
	}

	@Override
	public boolean requiresFieldValue() {
		return compareToFixedValue == null;
	}

	@Override
	public boolean isMet(D fieldValue, T conditionalValue) {
		if (compareToFixedValue == null) {
			return fieldValue == conditionalValue
					|| (fieldValue != null && fieldValue.equals(conditionalValue));
		} else {
			return compareToFixedValue == conditionalValue
					|| (compareToFixedValue != null && compareToFixedValue.equals(conditionalValue));
		}
	}

	@Override
	public String getMessageKey() {
		if (compareToFixedValue == null) {
			return "condition.equals";
		} else {
			return "condition.equals.fixedValue";
		}
	}

	@Override
	public Map<String, Object> getMessageParameters() {
		Map<String, Object> messageParameters = new HashMap<String, Object>();
		messageParameters.put("dependencyFieldName", dependencyFieldName);
		if (compareToFixedValue != null) {
			messageParameters.put("fixedValue", compareToFixedValue);
		}
		return messageParameters;
	}
}
