package org.xandercat.swing.zenput.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Convenience operations utilizing Java Reflection.
 * 
 * @author Scott Arnold
 */
public class ReflectionUtil {

	/**
	 * Returns the expected getter method name for a given field name and class.
	 * 
	 * @param fieldName		field name
	 * @param returnClass	field class
	 * 
	 * @return				expected getter method name
	 */
	public static String getterMethodName(String fieldName, Class<?> returnClass) {
		String prefix = (returnClass == Boolean.class || returnClass == Boolean.TYPE)? "is" : "get";
		return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	/**
	 * Returns the expected setter method name for a given field name.
	 * 
	 * @param fieldName		field name
	 * 
	 * @return				expected setter method name
	 */
	public static String setterMethodName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	/**
	 * Verify that the given object has a field of given name.  If verifyGetterAndSetter is 
	 * set to true, it will also be verified that the field has standard getter and setter
	 * methods.
	 * 
	 * @param o						object to look for field name within
	 * @param fieldName				name of field to find
	 * @param verifyGetterAndSetter	whether or not to verify the field has the expected getter and setter methods
	 * 
	 * @return						Field object, if found
	 * 
	 * @throws NoSuchFieldException, NoSuchMethodException
	 */
	public static Field verifyField(Object o, String fieldName, boolean verifyGetterAndSetter) 
			throws NoSuchFieldException, NoSuchMethodException {
		Class<?> c = o.getClass();
		Field f = null;
		NoSuchFieldException e = null;
		while (c != null && f == null) {
			try {
				f = c.getDeclaredField(fieldName);
			} catch (NoSuchFieldException nsfe) {
				if (e == null) {
					e = nsfe;
				}
				c = c.getSuperclass();
			}
		}
		if (f == null) {
			throw e;
		}
		if (verifyGetterAndSetter) {
			Class<?> returnClass = f.getType();
			c.getMethod(getterMethodName(fieldName, returnClass), (Class<?>[]) null);
			c.getMethod(setterMethodName(fieldName), returnClass);
		}
		return f;
	}
	
	public static <S> S invokeGetter(Object o, String fieldName) throws Exception {
		Field f = verifyField(o, fieldName, false);
		return invokeGetter(o, fieldName, (Class<S>) f.getType());
	}
	
	public static <S> S invokeGetter(Object o, String fieldName, Class<S> fieldClass) throws Exception {
		Method getterMethod = o.getClass().getMethod(getterMethodName(fieldName, fieldClass), (Class<?>[]) null);
		return (S) getterMethod.invoke(o, (Object[]) null);
	}
	
	public static <S> void invokeSetter(Object o, String fieldName, S fieldValue) throws Exception {
		// get the type from the field itself; this is superior to simply calling getClass()
		// on the field value because it works for null values and also allows for auto-boxing.
		Field f = verifyField(o, fieldName, false);  
		Class<S> fieldClass = (Class<S>) f.getType();
		invokeSetter(o, fieldName, fieldClass, fieldValue);
	}
	
	public static <S> void invokeSetter(Object o, String fieldName, Class<S> fieldClass, S fieldValue) throws Exception {
		Method setterMethod = o.getClass().getMethod(setterMethodName(fieldName), fieldClass);
		setterMethod.invoke(o, fieldValue);
	}
}
