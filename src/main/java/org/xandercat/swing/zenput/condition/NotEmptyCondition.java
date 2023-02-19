package org.xandercat.swing.zenput.condition;

public class NotEmptyCondition<D, T> implements DependentCondition<D, T> {

	@Override
	public boolean isMet(D fieldValue, T conditionalValue) {
		if (conditionalValue == null || ((conditionalValue instanceof String) && conditionalValue.toString().trim().length() == 0)) {
			return false;
		}
		return true;
	}

	@Override
	public String getDescription(String conditionalValueFieldName) {
		return "Field " + conditionalValueFieldName + " must be not empty.";
	}

	@Override
	public boolean requiresFieldValue() {
		return false;
	}
}
