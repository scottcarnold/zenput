package org.xandercat.swing.zenput.adapter;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Adapter that utilizes reflection to get and set the input values from
 * the input objects.  Getter and setter method names are expected to conform to 
 * standard getter and setter naming conventions.
 * 
 * @author Scott Arnold
 *
 * @param <I>	input type
 */
public class ReflectionAccessor<I> implements InputAccessor<I> {

	private static final Logger log = LogManager.getLogger(ReflectionAccessor.class);
	
	private Object inputObject;
	private Method inputGetter;
	private Method inputSetter;
	
	/**
	 * Construct a new reflection adapter.
	 * 
	 * @param inputObject			input object
	 * @param inputFieldName		input field name
	 * @param inputFieldClass		input field class
	 */
	public ReflectionAccessor(Object inputObject, String inputFieldName, Class<I> inputFieldClass) {
		this.inputObject = inputObject;
		try {
			String methodName = getterMethodName(inputFieldName, inputFieldClass);
			this.inputGetter = this.inputObject.getClass().getMethod(methodName, (Class<?>[]) null);
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException("Getter method for " + inputFieldName + " not found.");
		}
		
		String methodName = setterMethodName(inputFieldName);
		Class<?> clazz = inputFieldClass;
		while (this.inputSetter == null && clazz != null) {
			try {
				this.inputSetter = this.inputObject.getClass().getMethod(methodName, clazz);
			} catch (NoSuchMethodException nsme) { }
			clazz = clazz.getSuperclass();
		}
		if (this.inputSetter == null) {
			throw new IllegalArgumentException("Setter method for " + inputFieldName + " not found.");
		}
	}
	
	@SuppressWarnings("unchecked")	
	public I getValue() {
		try {
			return (I) this.inputGetter.invoke(this.inputObject, (Object[]) null);
		} catch (Exception e) {
			log.error("Unable to get input value", e);
		}
		return null;
	}
	
	public void setValue(I inputValue) {
		try {
			this.inputSetter.invoke(this.inputObject, inputValue);
		} catch (Exception e) {
			log.error("Unable to set input value", e);
		}	
	}
	
	@Override
	public Object getSource() {
		return inputObject;
	}

	private String getterMethodName(String fieldName, Class<?> returnClass) {
		String prefix = (returnClass == Boolean.class || returnClass == Boolean.TYPE)? "is" : "get";
		return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	private String setterMethodName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
}
