package org.xandercat.swing.zenput.validator;

/**
 * A condition that is dependent upon some value.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public interface DependentCondition<T> {

	/**
	 * Returns true if the condition is met for the given value.
	 * 
	 * @param conditionalValue	value to test against
	 * 
	 * @return		true if condition is met for the value
	 */
	public boolean isMet(T conditionalValue);
}
