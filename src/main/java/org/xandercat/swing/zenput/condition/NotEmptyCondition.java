package org.xandercat.swing.zenput.condition;

import org.xandercat.swing.zenput.annotation.ControlNotEmpty;

public class NotEmptyCondition<D, T> implements DependentCondition<D, T> {

	private String dependencyFieldName;
	
	public static <D, T> NotEmptyCondition<D, T> newCondition(ControlNotEmpty annotation) {
		return new NotEmptyCondition<D, T>(annotation.dependencyOn());
	}
	
	public NotEmptyCondition(String dependencyFieldName) {
		this.dependencyFieldName = dependencyFieldName;
	}
	
	@Override
	public String getDependencyFieldName() {
		return dependencyFieldName;
	}
	
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
