package org.xandercat.swing.zenput.adapter;

import org.xandercat.swing.zenput.converter.InputConverter;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * InputAdapters provide access to input fields in the user interface and conversion of
 * input types to their internal source types.
 * 
 * @author Scott Arnold
 *
 * @param <I>		input type
 * @param <S>		source type
 */
public class InputAdapter<I, S> {

	private String fieldName;
	private InputAccessor<I> inputAccessor;
	private InputConverter<I, S> inputConverter;
	
	public InputAdapter(String fieldName, InputAccessor<I> inputAccessor, InputConverter<I, S> inputConverter) {
		this.fieldName = fieldName;
		this.inputAccessor = inputAccessor;
		this.inputConverter = inputConverter;
	}
	
	public S getValue() throws ValidationException {
		return inputConverter.convertInput(inputAccessor.getValue(), fieldName);
	}
	
	public void setValue(S value) throws ZenputException {
		inputAccessor.setValue(inputConverter.convertSource(value));
	}
	
	public Object getSource() {
		return inputAccessor.getSource();
	}
}
