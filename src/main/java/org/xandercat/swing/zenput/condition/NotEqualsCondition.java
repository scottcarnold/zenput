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
		if (annotation.valueType() != null && annotation.stringValue() == null) {
			throw new IllegalArgumentException("If specifying valueType, stringValue must also be provided.");
		}
		if (annotation.valueType() == null && annotation.stringValue() != null) {
			throw new IllegalArgumentException("If specifying stringValue, valueType must also be provided.");
		}
		if (annotation.valueType() != null) {
			return new NotEqualsCondition<D, T>((T) TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
		} else {
			return new NotEqualsCondition<D, T>();
		}
	}
	
	public NotEqualsCondition() {
	}
	
	public NotEqualsCondition(T compareToValue) {
		this.compareToValue = compareToValue;
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
