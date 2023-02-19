package org.xandercat.swing.zenput.validator;

import java.util.List;

import org.xandercat.swing.zenput.processor.ValueRetriever;

/**
 * A validator that has dependencies on other fields.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of field to be validated
 */
public interface DependencyValidator<T> extends Validator<T> {
	
	/**
	 * Sets the value retriever this validator should use to retrieve the values
	 * for the fields it is dependent upon.
	 * 
	 * @param valueRetriever
	 */
	public void setValueRetriever(ValueRetriever valueRetriever);
	
	/**
	 * Returns a list of names for all fields this validator is dependent upon.
	 * In cases where there is no dependency on other values, this method can 
	 * return null.
	 * 
	 * @return			list of names for all fields this validator is dependent upon
	 */
	public List<String> getDependencyFieldNames();
}
