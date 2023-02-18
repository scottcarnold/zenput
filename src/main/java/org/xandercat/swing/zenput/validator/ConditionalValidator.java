package org.xandercat.swing.zenput.validator;

import java.util.Collections;
import java.util.List;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.processor.ValueRetriever;

/**
 * Validator which only validates when a particular condition on another field is met.
 * If condition is met, validation is performed by another validator which this validator
 * serves as a wrapper for.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of field being validated
 * @param <D>	type of field validation is dependent upon
 */
public class ConditionalValidator<T, D> implements DependentValidator<T> {

	private DependentCondition<T, D> condition;
	private ValueRetriever valueRetriever;
	private String dependencyFieldName;
	private Validator<T> validator;
	
	public ConditionalValidator(String dependencyFieldName, 
			DependentCondition<T, D> condition,
			Validator<T> validator) {
		this.condition = condition;
		this.dependencyFieldName = dependencyFieldName;
		this.validator = validator;
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
		return validator.getValueType();
	}

	@Override
	public boolean shouldValidate(String fieldName, T value) throws ValidationException {
		D dependencyValue;
		try {
			dependencyValue = valueRetriever.getValueForField(dependencyFieldName);
		} catch (ZenputException e) {
			throw new ValidationException(fieldName, "Unable to validate.", e);
		}
		return condition.isMet(value, dependencyValue) && validator.shouldValidate(fieldName, value);
	}

	@Override
	public void validate(String fieldName, T value) throws ValidationException {
		validator.validate(fieldName, value);	
	}
}
