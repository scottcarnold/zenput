package org.xandercat.swing.zenput.condition;

import java.text.ParseException;

import org.xandercat.swing.zenput.annotation.ConditionNumeric;
import org.xandercat.swing.zenput.util.TypeUtil;

public class NumericCondition implements DependentCondition<Number, Number> {

	private Operator operator;
	private Number compareToFixedValue;
	
	public static NumericCondition newCondition(ConditionNumeric annotation) throws ParseException {
		if (annotation.operator() == null) {
			throw new IllegalArgumentException("operator value is required.");
		}
		if (annotation.valueType() != null && annotation.stringValue() == null) {
			throw new IllegalArgumentException("If specifying valueType, stringValue must also be provided.");
		}
		if (annotation.valueType() == null && annotation.stringValue() != null) {
			throw new IllegalArgumentException("If specifying stringValue, valueType must also be provided.");
		}
		if (annotation.valueType() != null) {
			return new NumericCondition(annotation.operator(), (Number) TypeUtil.parse(annotation.valueType(), annotation.stringValue()));
		} else {
			return new NumericCondition(annotation.operator());
		}
	}
	
	public NumericCondition(Operator operator) {
		this.operator = operator;
	}
	
	public NumericCondition(Operator operator, Number compareToFixedValue) {
		this(operator);
		this.compareToFixedValue = compareToFixedValue;
	}
	
	@Override
	public boolean requiresFieldValue() {
		return compareToFixedValue == null;
	}
	
	@Override
	public boolean isMet(Number fieldValue, Number conditionalValue) {
		Number number1 = null, number2 = null;
		if (compareToFixedValue == null) {
			number1 = fieldValue;
			number2 = conditionalValue;
		} else {
			number1 = conditionalValue;
			number2 = compareToFixedValue;
		}
		boolean eq = number1.equals(number2);
		boolean lt;
		if (wholeNumber(number1) && wholeNumber(number2)) {
			lt = number1.longValue() < number2.longValue();
		} else {
			lt = number1.doubleValue() < number2.doubleValue();
		}
		switch (operator) {
		case LT:
			return lt;
		case LTEQ:
			return lt || eq;
		case EQ:
			return eq;
		case GTEQ:
			return !lt || eq;
		case GT:
			return !lt && !eq;
		}
		return false;
	}
	
	private boolean wholeNumber(Number number) {
		Class<?> clazz = number.getClass();
		return (clazz == Short.class || clazz == Integer.class || clazz == Long.class);
	}

	@Override
	public String getDescription(String conditionalValueFieldName) {
		if (compareToFixedValue == null) {
			return "Must be " + operator + " value in field " + conditionalValueFieldName;
		} else {
			return "Field " + conditionalValueFieldName + " must be " + operator + " value of " + compareToFixedValue.toString();
		}
	}	
}
