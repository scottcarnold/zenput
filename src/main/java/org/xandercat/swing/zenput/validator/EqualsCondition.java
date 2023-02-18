package org.xandercat.swing.zenput.validator;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionEquals;

/**
 * A condition where the conditional value must be equal to some other fixed value.
 * Values are considered equal if they the same reference or .equals(...) return true.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class EqualsCondition<T> implements DependentCondition<T, T> {

	public static <T> EqualsCondition<T> newCondition(ConditionEquals annotation) throws ParseException {
		return new EqualsCondition<T>();
	}
	
	public EqualsCondition() {
	}
	
	@Override
	public boolean isMet(T fieldValue, T conditionalValue) {
		return fieldValue == conditionalValue
				|| (fieldValue != null && fieldValue.equals(conditionalValue));
	}
}
