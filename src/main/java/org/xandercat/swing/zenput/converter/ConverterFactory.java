package org.xandercat.swing.zenput.converter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

	private static Map<Class<?>, InputConverter<String, ?>> converters 
		= new HashMap<Class<?>, InputConverter<String, ?>>();
	
	static {
		converters.put(String.class, new SameTypeConverter<String>());
		converters.put(Integer.class, new IntegerConverter());
		converters.put(Integer.TYPE, new IntegerConverter());
		converters.put(Long.class, new LongConverter());
		converters.put(Long.TYPE, new LongConverter());
		converters.put(Float.class, new FloatConverter());
		converters.put(Float.TYPE, new FloatConverter());
		converters.put(Double.class, new DoubleConverter());
		converters.put(Double.TYPE, new DoubleConverter());
		converters.put(File.class, new FileConverter());
		converters.put(Boolean.class, new BooleanConverter());
		converters.put(Boolean.TYPE, new BooleanConverter());
	}
	
	public static InputConverter<String, ?> getConverterForType(Class<?> type) {
		return converters.get(type);
	}
}
