package org.xandercat.swing.zenput.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

public class CSVConverterTest {

	@Test
	public void testConvertInput() {
		CSVConverter converter = new CSVConverter();
		try {
			List<String> strings = converter.convertInput("one;two;three", "field");
			assertNotNull(strings);
			assertEquals(3, strings.size());
			assertTrue(strings.contains("one"));
			assertTrue(strings.contains("two"));
			assertTrue(strings.contains("three"));
		} catch (ValidationException e) {
			fail("No validation error should occur.  Validation message: " + e.getMessage());
		}
	}
	
	@Test
	public void testConvertSource() {
		CSVConverter converter = new CSVConverter();
		try {
			String s = converter.convertSource(Arrays.asList("one", "two", "three"));
			assertEquals("one,two,three", s);
		} catch (ZenputException e) {
			fail("No error should occur.  Exception message: " + e.getMessage());
		}
	}
	
	@Test
	public void testRegExAsSeparatorChar() {
		CSVConverter converter = new CSVConverter();
		converter.setSeparatorChars("$");
		try {
			List<String> strings = converter.convertInput("one$two$three", "field");
			assertNotNull(strings);
			assertEquals(3, strings.size());
			assertTrue(strings.contains("one"));
			assertTrue(strings.contains("two"));
			assertTrue(strings.contains("three"));
		} catch (ValidationException e) {
			fail("No validation error should occur.  Validation message: " + e.getMessage());
		}		
	}
}
