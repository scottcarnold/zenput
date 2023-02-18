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
}
