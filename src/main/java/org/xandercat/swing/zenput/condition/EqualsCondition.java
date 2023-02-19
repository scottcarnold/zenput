package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ControlEquals;
import org.xandercat.swing.zenput.annotation.ValidateDependencyEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

public class EqualsCondition<D, T> implements DependentCondition<D, T> {
	
	private T compareToFixedValue;
	
	public static <D, T> EqualsCondition<D, T> newCondition(ControlEquals annotation) throws ParseException {
		return newCondition(annotation.valueType(), annotation.stringValue());
	}
	
	public static <D, T> EqualsCondition<D, T> newCondition(ValidateDependencyEquals annotation) throws ParseException {
		return newCondition(annotation.valueType(), annotation.stringValue());
	}
	
	private static <D, T> EqualsCondition<D, T> newCondition(Class<?> valueType, String stringValue) throws ParseException {
		if (valueType != null && stringValue == null) {
			throw new IllegalArgumentException("If specifying valueType, stringValue must also be provided.");
		}
		if (valueType == null && stringValue != null) {
			throw new IllegalArgumentException("If specifying stringValue, valueType must also be provided.");
		}
		if (valueType != null) {
			return new EqualsCondition<D, T>((T) TypeUtil.parse(valueType, stringValue));
		} else {
			return new EqualsCondition<D, T>();
		}		
	}
	
	public EqualsCondition() {
	}
	
	public EqualsCondition(T compareToFixedValue) {
		this.compareToFixedValue = compareToFixedValue;
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
	public String getDescription(String conditionalValueFieldName) {
		if (compareToFixedValue == null) {
			return "Must be equal to value in field " + conditionalValueFieldName;
		} else {
			return "Field " + conditionalValueFieldName + " must have a value of " + compareToFixedValue.toString();
		}
	}
}
