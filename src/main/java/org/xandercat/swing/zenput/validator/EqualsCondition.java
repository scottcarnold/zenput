package org.xandercat.swing.zenput.validator;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionEquals;
import org.xandercat.swing.zenput.util.TypeUtil;

/**
 * A condition where the conditional value must be equal to some other fixed value.
 * Values are considered equal if they the same reference or .equals(...) return true.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class EqualsCondition<T> implements DependentCondition<T> {

	private T compareToValue;
	
	public static EqualsCondition newCondition(ConditionEquals annotation) throws ParseException {
		return new EqualsCondition(TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
	}
	
	public EqualsCondition(T compareToValue) {
		this.compareToValue = compareToValue;
	}
	
	@Override
	public boolean isMet(T conditionalValue) {
		return compareToValue == conditionalValue
				|| (compareToValue != null && compareToValue.equals(conditionalValue));
	}
}
