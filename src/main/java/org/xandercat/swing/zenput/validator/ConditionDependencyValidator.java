package org.xandercat.swing.zenput.validator;

import java.util.Collections;
import java.util.List;

import org.xandercat.swing.zenput.condition.DependentCondition;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.processor.ValueRetriever;

/**
 * Validator which validates based on a condition on another field.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of field being validated
 * @param <D>	type of field validation is dependent upon
 */
public class ConditionDependencyValidator<T, D> implements DependencyValidator<T> {

	private DependentCondition<T, D> condition;
	private ValueRetriever valueRetriever;
	private String dependencyFieldName;
	private Class<T> valueType;
	
	public ConditionDependencyValidator(DependentCondition<T, D> condition,
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
	public boolean shouldValidate(String fieldName, T value) throws ValidationException {
		return true;
	}

	@Override
	public void validate(String fieldName, T value) throws ValidationException {
		D dependencyValue;
		try {
			dependencyValue = valueRetriever.getValueForField(dependencyFieldName);
		} catch (ZenputException e) {
			throw new ValidationException(fieldName, "validator.condition.fail", e);
		}
		if (!condition.isMet(value, dependencyValue)) {
			throw new ValidationException(fieldName, condition.getMessageKey(), condition.getMessageParameters());
		}
	}
}
