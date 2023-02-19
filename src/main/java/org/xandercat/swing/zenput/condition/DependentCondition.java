package org.xandercat.swing.zenput.condition;

/**
 * A condition that is dependent upon some value.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public interface DependentCondition<D, T> {

	/**
	 * Returns true if the condition is met for the given value.
	 * 
	 * @param fieldValue        value of the field being validated
	 * @param conditionalValue	value to test against
	 * 
	 * @return		true if condition is met for the value
	 */
	public boolean isMet(D fieldValue, T conditionalValue);
	
	/**
	 * Returns a description of this condition.
	 * 
	 * @param conditionalValueFieldName field name for the field that contains the conditional value
	 * 
	 * @return description of this condition
	 */
	public String getDescription(String conditionalValueFieldName);
	
	/**
	 * Returns whether or not the dependency test will require the value of the field
	 * the condition is for.
	 * 
	 * @return whether or not the dependency test will require the value of the field the condition is for.
	 */
	public boolean requiresFieldValue();
}
