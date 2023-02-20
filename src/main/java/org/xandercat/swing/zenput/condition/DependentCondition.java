package org.xandercat.swing.zenput.condition;

import java.util.Map;

/**
 * A condition that is dependent upon some value.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public interface DependentCondition<T, D> {

	/**
	 * Returns true if the condition is met for the given value.
	 * 
	 * @param fieldValue        value of the field being validated
	 * @param conditionalValue	value to test against
	 * 
	 * @return		true if condition is met for the value
	 */
	public boolean isMet(T fieldValue, D conditionalValue);
	
	/**
	 * Returns a message key that should be associated with this condition.
	 * 
	 * @return message key that should be associated with this condition
	 */
	public String getMessageKey();
	
	/**
	 * Returns message parameters that should be associated with this condition.
	 * 
	 * @return message parameters that should be associated with this condition.
	 */
	public Map<String, Object> getMessageParameters();
	
	/**
	 * Returns the name of the field that the condition dependency exists on.
	 * 
	 * @return the name of the field that the condition dependency exists on
	 */
	public String getDependencyFieldName();
	
	/**
	 * Returns whether or not the dependency test will require the value of the field
	 * the condition is for.
	 * 
	 * @return whether or not the dependency test will require the value of the field the condition is for.
	 */
	public boolean requiresFieldValue();
}
