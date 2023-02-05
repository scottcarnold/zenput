package org.xandercat.swing.zenput.converter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

	private static Map<Class<?>, InputConverter<String, ?>> stringConverters 
		= new HashMap<Class<?>, InputConverter<String, ?>>();
	
	static {
		stringConverters.put(String.class, new SameTypeConverter<String>());
		stringConverters.put(Integer.class, new IntegerConverter());
		stringConverters.put(File.class, new FileConverter());
		stringConverters.put(Boolean.class, new BooleanConverter());
		stringConverters.put(Boolean.TYPE, new BooleanConverter());
	}
	
	public static InputConverter<String, ?> getConverterForType(Class<?> type) {
		return stringConverters.get(type);
	}
}
