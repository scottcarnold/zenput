package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionNotEquals;
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

	public static <D, T> NotEqualsCondition<D, T> newCondition(ConditionNotEquals annotation) throws ParseException {
		return new NotEqualsCondition<D, T>((T) TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
	}
	
	public NotEqualsCondition(T compareToValue) {
		this.compareToValue = compareToValue;
	}
	
	@Override
	public boolean isMet(D fieldValue, T conditionalValue) {
		return compareToValue != conditionalValue
				&& (compareToValue == null || !compareToValue.equals(conditionalValue));
	}
}
