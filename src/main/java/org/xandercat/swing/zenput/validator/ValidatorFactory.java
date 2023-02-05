package org.xandercat.swing.zenput.validator;

/**
 * Factory class with convenience methods for creating various validator combinations.
 * 
 * @author Scott Arnold
 */
public class ValidatorFactory {

	/**
	 * Constructs and returns a new validator that is a composite of all the provided
	 * validators.
	 * 
	 * @param <T>				type of value being validated
	 * @param valueType			class for type
	 * @param validators		validators to combine into a single validator
	 * 
	 * @return					new validator
	 */
	public static <T> Validator<T> newValidator(Class<T> valueType, Validator<? super T>... validators) {
		return new CompoundValidator<T>(valueType, validators);
	}
	
	/**
	 * Constructs and returns a new validator for a required field that is a composite 
	 * of a RequiredValidator and all the provided validators.
	 * 
	 * @param <T>				type of value being validated
	 * @param valueType			class for type
	 * @param validators		validators to combine into a single validator
	 * 
	 * @return					new validator for a required field
	 */	
	public static <T> Validator<T> newRequiredValidator(Class<T> valueType, Validator<? super T>... validators) {
		Validator<? super T>[] rvalidators = new Validator[validators.length+1];
		rvalidators[0] = new RequiredValidator();
		System.arraycopy(validators, 0, rvalidators, 1, validators.length);
		return newValidator(valueType, rvalidators);
	}
	
	/**
	 * Constructs and returns a new conditional validator for the provided dependency
	 * and validators for when the dependency condition is met.
	 * 
	 * @param <T>					type of value being validated
	 * @param valueType			class for type
	 * @param dependencyFieldName	name of field dependency condition will be applied to
	 * @param dependencyCondition	condition in which to use the validators
	 * @param validators			validator or validators
	 * 
	 * @return						new validator
	 */
	public static <T> Validator<T> newConditionalValidator(Class<T> valueType, String dependencyFieldName, 
			DependentCondition dependencyCondition, Validator<? super T>... validators) {
		if (validators.length == 1) {
			return new ConditionalValidator(dependencyFieldName, 
					dependencyCondition, 
					validators[0]);
		} else {
			return new ConditionalValidator(dependencyFieldName, 
					dependencyCondition, 
					new CompoundValidator<T>(valueType, validators));
		}
	}
}
