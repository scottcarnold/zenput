package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * Interface for input converters to implement.  Converters convert back and forth
 * between user input and internal source formats.
 * 
 * @author Scott Arnold
 *
 * @param <I>		input type
 * @param <S>		source type
 */
public interface InputConverter<I, S> {
	
	/**
	 * Convert an input value to the format of the source.  If the value cannot
	 * be converted, this should be reported as a ValidationException with an error
	 * level of ErrorLevel.ERROR for the given field name.
	 * 
	 * @param inputValue	input value
	 * @param fieldName		name of field; provided only for ValidationException construction
	 * 
	 * @return				source value
	 * 
	 * @throws ValidationException
	 */
	public S convertInput(I inputValue, String fieldName) throws ValidationException;
	
	/**
	 * Convert a source value to the format of the input.  
	 * 
	 * @param sourceValue	source value
	 * 
	 * @return				input value
	 * 
	 * @throws ZenputException
	 */
	public I convertSource(S sourceValue) throws ZenputException;
}
