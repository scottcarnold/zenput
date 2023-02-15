package org.xandercat.swing.zenput.processor;

import java.util.List;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.validator.Validator;

public interface Processor {

	/**
	 * Registers a field to be processed.  A field of given field name should exist within
	 * the source object and have standard getter and setter methods.
	 * 
	 * @param fieldName			name of field
	 * @param fieldTitle		title for field
	 * @param source			source object that contains field
	 * 
	 * @throws ZenputException if error occurs registering field
	 */
	public void registerField(String fieldName, String fieldTitle, Object source) throws ZenputException;
	
	/**
	 * Registers a field to be processed.  The field will belong to some default
	 * source object.  Implementing classes should provide some way of setting
	 * a default source object which will be used when calls to this method are made.
	 * 
	 * @param fieldName			name of field
	 * @param fieldTitle		title for field
	 * 
	 * @throws ZenputException if error occurs registering field
	 */
	public void registerField(String fieldName, String fieldTitle) throws ZenputException;
	
	/**
	 * Registers a field to be processed with the given validators.  The field will belong to 
	 * some default source object.  Implementing classes should provide some way of setting
	 * a default source object which will be used when calls to this method are made.
	 * 
	 * @param fieldName        name of field
	 * @param fieldTitle       title of field
	 * @param validator        validator for field
	 * 
	 * @throws ZenputException if error occurs registering field
	 */
	public void registerField(String fieldName, String fieldTitle, Validator<?> validator) throws ZenputException;
	
	/**
	 * Registers a validator for the field of given field name.  Validator type should be
	 * of the same type or a superclass type of the given field type.  More than one validator
	 * can be registered on the same field.  Validation should be performed by validators
	 * in the order they were registered.
	 * 
	 * @param fieldName			name of field
	 * @param validator			validator for field
	 * 
	 * @throws ZenputException if error occurs registering validator
	 */
	public void registerValidator(String fieldName, Validator<?> validator) throws ZenputException;
	
	/**
	 * Sets the object responsible for retrieving values by field name.
	 * 
	 * @param valueRetriever	object responsible for retrieving values by field name
	 */
	public void setValueRetriever(ValueRetriever valueRetriever);
	
	/**
	 * Returns a list of field names for all registered fields.
	 * 
	 * @return					list of field names for all registered fields
	 */
	public List<String> getRegisteredFieldNames();
	
	/**
	 * Returns the type of the field with given field name.
	 * 
	 * @param fieldName			name of field
	 * 
	 * @return					field type
	 * 
	 * @throws ZenputException if field name is not registered
	 */
	public Class<?> getRegisteredFieldType(String fieldName) throws ZenputException;
	
	/**
	 * Returns the title of the field with given field name.  The title is a String
	 * suitable for display to users.
	 * 
	 * @param fieldName			name of field
	 * 
	 * @return					field title
	 */
	public String getRegisteredFieldTitle(String fieldName);
	
	/**
	 * Returns all known validation errors.  Errors should be returned in the order
	 * the fields were originally registered.
	 * 
	 * @return					known validation errors
	 */
	public List<ValidationException> getErrors();
	
	/**
	 * Returns the current validation error for the field of given name.
	 * 
	 * @param fieldName			field name
	 * 
	 * @return					current validation error on field
	 */
	public ValidationException getError(String fieldName);
	
	/**
	 * Returns all known validation errors for only the fields of given field names.
	 * Errors should be returned in the same order as the field names provided.
	 * 
	 * @param fieldNames		names of fields to get validation errors from
	 * 
	 * @return					validation errors for the fields of given names
	 */
	public List<ValidationException> getErrors(List<String> fieldNames);
	
	/**
	 * Validates all fields.  Validation errors should be remembered such that they can
	 * be retrieved through calls to the getErrors methods.
	 * 
	 * @return					whether or not all fields were valid
	 * 
	 * @throws ZenputException if program error occurs
	 */
	public boolean validate() throws ZenputException;
	
	/**
	 * Validates the fields of given field names.  Validation errors should be remembered 
	 * such that they can be retrieved through calls to the getErrors methods.
	 * 
	 * @param fieldNames		names of the fields to validate
	 * 
	 * @return					whether or not the fields of given names were all valid
	 * 
	 * @throws ZenputException if program error occurs
	 */
	public boolean validate(List<String> fieldNames) throws ZenputException;
	
	/**
	 * Returns the names of the fields that will be validated in the order they will 
	 * be validated based on a requested list of field names.  Field order can be
	 * rearranged and additional fields may be added when validating due to validator
	 * dependencies.  This method provides a way to get the ordered list of fields
	 * that will ultimately be validated based on a list of requested validations.
	 * 
	 * @param fieldNames		requested fields in requested order
	 * 
	 * @return					actual fields in actual order that validation will occur in
	 * 
	 * @throws ZenputException if program error occurs
	 */
	public List<String> getValidationOrder(List<String> fieldNames) throws ZenputException;
	
	/**
	 * Returns the source object for the field of given field name.
	 * 
	 * @param fieldName			name of field
	 * 
	 * @return					source object that contains the field
	 */
	public Object getSource(String fieldName);
	
	/**
	 * Closes down the processor, freeing all resources.
	 */
	public void close();
}
