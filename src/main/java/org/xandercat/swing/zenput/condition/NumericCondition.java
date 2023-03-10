package org.xandercat.swing.zenput.condition;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.xandercat.swing.zenput.annotation.CompareTo;
import org.xandercat.swing.zenput.annotation.ControlNumeric;
import org.xandercat.swing.zenput.annotation.ValidateDependencyNumeric;
import org.xandercat.swing.zenput.util.TypeUtil;

public class NumericCondition implements DependentCondition<Number, Number> {

	private Operator operator;
	private Number compareToFixedValue;
	private String dependencyFieldName;
	
	public static NumericCondition newCondition(ControlNumeric annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.operator(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	public static NumericCondition newCondition(ValidateDependencyNumeric annotation) throws ParseException {
		return newCondition(annotation.dependencyOn(), annotation.operator(), annotation.compareTo(), annotation.valueType(), annotation.stringValue());
	}
	
	private static NumericCondition newCondition(String dependencyFieldName, Operator operator, CompareTo compareTo, Class<?> valueType, String stringValue) throws ParseException {
		if (compareTo == CompareTo.FIXED_VALUE) {
			return new NumericCondition(dependencyFieldName, operator, (Number) TypeUtil.parse(valueType, stringValue));
		} else {
			return new NumericCondition(dependencyFieldName, operator);
		}
	}
	
	public NumericCondition(String dependencyFieldName, Operator operator) {
		if (operator == null) {
			throw new IllegalArgumentException("operator value is required.");
		}
		this.operator = operator;
		this.dependencyFieldName = dependencyFieldName;
	}
	
	public NumericCondition(String dependencyFieldName, Operator operator, Number compareToFixedValue) {
		this(dependencyFieldName, operator);
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
	public String getMessageKey() {
		if (compareToFixedValue == null) {
			return "condition.numeric";
		} else {
			return "condition.numeric.fixedValue";
		}
	}

	@Override
	public Map<String, Object> getMessageParameters() {
		Map<String, Object> messageParameters = new HashMap<String, Object>();
		messageParameters.put("dependencyFieldName", dependencyFieldName);
		messageParameters.put("operator", operator);
		if (compareToFixedValue != null) {
			messageParameters.put("fixedValue", compareToFixedValue);
		}
		return messageParameters;
	}	
}
