package org.xandercat.swing.zenput.adapter;

/**
 * InputAccessors provides the connection to an input field.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of value stored in the input field
 */
public interface InputAccessor<T> {

	/**
	 * Gets the value stored in the input field.
	 * 
	 * @return			value stored in the input field
	 */
	public T getValue();
	
	/**
	 * Sets the value of the input field.
	 * 
	 * @param value		value to set in the input field
	 */
	public void setValue(T value);
	
	/**
	 * Gets the input component that is the source the input value.
	 * 
	 * @return			the input component
	 */
	public Object getSource();
}
