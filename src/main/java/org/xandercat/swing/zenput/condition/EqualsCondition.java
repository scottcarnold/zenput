package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

public class EqualsCondition<D, T> implements DependentCondition<D, T> {
	
	private T compareToFixedValue;
	
	public static <D, T> EqualsCondition<D, T> newCondition(ConditionEquals annotation) throws ParseException {
		if (annotation.valueType() != null && annotation.stringValue() == null) {
			throw new IllegalArgumentException("If specifying valueType, stringValue must also be provided.");
		}
		if (annotation.valueType() == null && annotation.stringValue() != null) {
			throw new IllegalArgumentException("If specifying stringValue, valueType must also be provided.");
		}
		if (annotation.valueType() != null) {
			return new EqualsCondition<D, T>((T) TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
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
	public boolean isMet(D fieldValue, T conditionalValue) {
		if (compareToFixedValue == null) {
			return fieldValue == conditionalValue
					|| (fieldValue != null && fieldValue.equals(conditionalValue));
		} else {
			return compareToFixedValue == conditionalValue
					|| (compareToFixedValue != null && compareToFixedValue.equals(conditionalValue));
		}
	}
}
