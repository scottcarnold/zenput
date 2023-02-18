package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionFixedEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

public class FixedEqualsCondition<D, T> implements DependentCondition<D, T> {
	
	private T compareToFixedValue;
	
	public static <D, T> FixedEqualsCondition<D, T> newCondition(ConditionFixedEquals annotation) throws ParseException {
		return new FixedEqualsCondition<D, T>((T) TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
	}
	
	public FixedEqualsCondition(T compareToFixedValue) {
		this.compareToFixedValue = compareToFixedValue;
	}
	
	@Override
	public boolean isMet(D fieldValue, T conditionalValue) {
		return compareToFixedValue == conditionalValue
				|| (compareToFixedValue != null && compareToFixedValue.equals(conditionalValue));
	}
}
