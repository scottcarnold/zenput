package org.xandercat.swing.zenput.validator;

import java.util.Collections;
import java.util.List;

import org.xandercat.swing.zenput.condition.DependentCondition;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.processor.ValueRetriever;

public class ConditionDependencyControl<T, D> implements DependencyControl<T> {
	
	private DependentCondition<T, D> condition;
	private ValueRetriever valueRetriever;
	private String dependencyFieldName;
	private Class<T> valueType;
	
	public ConditionDependencyControl(DependentCondition<T, D> condition,
			Class<T> valueType) {
		this.condition = condition;
		this.dependencyFieldName = condition.getDependencyFieldName();
		this.valueType = valueType;
	}
	
	@Override
	public List<String> getDependencyFieldNames() {
		return Collections.singletonList(dependencyFieldName);
	}

	@Override
	public void setValueRetriever(ValueRetriever valueRetriever) {
		this.valueRetriever = valueRetriever;
	}
	
	@Override
	public Class<T> getValueType() {
		return valueType;
	}
	
	@Override
	public boolean shouldValidate(String fieldName) throws ValidationException {
		D dependencyValue;
		T value = null;
		try {
			dependencyValue = valueRetriever.getValueForField(dependencyFieldName);
			if (condition.requiresFieldValue()) {
				value = valueRetriever.getValueForField(fieldName);
			}
		} catch (ZenputException e) {
			throw new ValidationException(fieldName, "Unable to validate.", e);
		}
		return condition.isMet(value, dependencyValue);
	}
}
