package org.xandercat.swing.zenput.validator;

import java.util.Collections;
import java.util.List;

import org.xandercat.swing.zenput.condition.DependencyType;
import org.xandercat.swing.zenput.condition.DependentCondition;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.processor.ValueRetriever;

/**
 * Validator which validates based on a condition on another field.
 * 
 * For DependencyType CONDITION, the validation is only performed if the
 * dependent condition is met.  If validation is to be performed, it is 
 * performed by another validator which this validator serves as a wrapper for.
 * 
 * For DependencyType VALIDATION, the condition acts as a validation itself,
 * but does not affect whether or not the field gets validated.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of field being validated
 * @param <D>	type of field validation is dependent upon
 */
public class ConditionalValidator<T, D> implements DependencyValidator<T> {

	private DependentCondition<T, D> condition;
	private DependencyType type;
	private ValueRetriever valueRetriever;
	private String dependencyFieldName;
	private Validator<T> validator;
	private Class<T> valueType;
	
	public ConditionalValidator(String dependencyFieldName, 
			DependentCondition<T, D> condition,
			DependencyType type,
			Validator<T> validator,
			Class<T> valueType) {
		this.condition = condition;
		this.dependencyFieldName = dependencyFieldName;
		this.type = type;
		this.validator = validator;
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
		if (type == DependencyType.CONDITION) {
			D dependencyValue;
			try {
				dependencyValue = valueRetriever.getValueForField(dependencyFieldName);
			} catch (ZenputException e) {
				throw new ValidationException(fieldName, "Unable to validate.", e);
			}
			return condition.isMet(value, dependencyValue) && validator.shouldValidate(fieldName, value);
		} else {
			return validator.shouldValidate(fieldName, value);
		}
	}

	@Override
	public void validate(String fieldName, T value) throws ValidationException {
		if (type == DependencyType.VALIDATION) {
			D dependencyValue;
			try {
				dependencyValue = valueRetriever.getValueForField(dependencyFieldName);
			} catch (ZenputException e) {
				throw new ValidationException(fieldName, "Unable to validate.", e);
			}
			if (!condition.isMet(value, dependencyValue)) {
				throw new ValidationException(fieldName, condition.getDescription(dependencyFieldName));
			}
		}
		if (validator != null) {
			validator.validate(fieldName, value);
		}
	}
}
