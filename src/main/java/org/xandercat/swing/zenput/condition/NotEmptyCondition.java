package org.xandercat.swing.zenput.condition;

import java.util.Collections;
import java.util.Map;

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
	public boolean requiresFieldValue() {
		return false;
	}

	@Override
	public String getMessageKey() {
		return "condition.notEmpty";
	}

	@Override
	public Map<String, Object> getMessageParameters() {
		return Collections.singletonMap("dependencyFieldName", dependencyFieldName);
	}
	
	
}
