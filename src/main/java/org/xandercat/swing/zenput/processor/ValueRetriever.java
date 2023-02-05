package org.xandercat.swing.zenput.processor;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * ValueRetriever is responsible for retrieving the field values to be validated.
 * 
 * @author Scott Arnold
 */
public interface ValueRetriever {

	/**
	 * Returns the value for the field of given name.  This method is allowed to throw
	 * a ValidationException for cases where the input value is coming from an input
	 * component that is part of the user interface.  Any other types of errors result
	 * in a ZenputException.
	 * 
	 * @param <T>			field type
	 * @param fieldName		field name
	 * 
	 * @return				value of field
	 * 
	 * @throws ValidationException
	 * @throws ZenputException
	 */
	public <T> T getValueForField(String fieldName) throws ValidationException, ZenputException;
}
