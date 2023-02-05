package org.xandercat.swing.zenput.validator;

/**
 * Interface for determining if a non-null object should be considered "empty".
 * 
 * @author Scott Arnold
 *
 * @param <T>	object type
 */
public interface EmptyTest<T> {

	/**
	 * Returns whether or not the given object should be considered "empty".
	 * 
	 * @param value		value to test
	 * 
	 * @return			whether or not value is empty
	 */
	public boolean isEmpty(T value);
}
