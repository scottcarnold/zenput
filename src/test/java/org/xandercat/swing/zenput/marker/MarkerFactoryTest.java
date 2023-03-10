package org.xandercat.swing.zenput.marker;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class MarkerFactoryTest {

	@Test
	public void testBackgroundMarkerBuilder() {
		MarkerBuilder<JComponent> builder = MarkerFactory.backgroundMarkerBuilder(Color.CYAN);
		JTextField testField = new JTextField();
		testField.setBackground(Color.BLUE);
		Marker<JComponent> testMarker = builder.newMarker(testField);
		assertEquals(Color.BLUE, testField.getBackground());
		assertTrue(testMarker.isMarkedValid());
		ValidationException ve = new ValidationException("testField", "test");
		testMarker.markInvalid(ve, null);
		assertEquals(Color.CYAN, testField.getBackground());
		assertFalse(testField.isValid());
		testMarker.markValid();
		assertEquals(Color.BLUE, testField.getBackground());
		assertTrue(testMarker.isMarkedValid());		
	}
	
	@Test
	public void testForegroundMarkerBuilder() {
		MarkerBuilder<JComponent> builder = MarkerFactory.foregroundMarkerBuilder(Color.CYAN);
		JTextField testField = new JTextField();
		testField.setForeground(Color.BLUE);
		Marker<JComponent> testMarker = builder.newMarker(testField);
		assertEquals(Color.BLUE, testField.getForeground());
		assertTrue(testMarker.isMarkedValid());
		ValidationException ve = new ValidationException("testField", "test");
		testMarker.markInvalid(ve, null);
		assertEquals(Color.CYAN, testField.getForeground());
		assertFalse(testField.isValid());
		testMarker.markValid();
		assertEquals(Color.BLUE, testField.getForeground());
		assertTrue(testMarker.isMarkedValid());		
	}
	
	@Test
	public void testToolTipMarkerBuilder() {
		MarkerBuilder<JComponent> builder = MarkerFactory.toolTipMarkerBuilder();
		JTextField testField = new JTextField();
		testField.setToolTipText("text");
		Marker<JComponent> testMarker = builder.newMarker(testField);
		assertEquals("text", testField.getToolTipText());
		assertTrue(testMarker.isMarkedValid());
		ValidationException ve = new ValidationException("testField", "validator.required");
		Properties properties = new Properties();
		properties.setProperty("validator.required", "Value is required");
		testMarker.markInvalid(ve, properties);
		assertTrue(testField.getToolTipText().contains("Value is required"));
		assertFalse(testField.isValid());
		testMarker.markValid();
		assertEquals("text", testField.getToolTipText());
		assertTrue(testMarker.isMarkedValid());		
	}
	
	@Test
	public void testCompoundMarkerBuilder() {
		MarkerBuilder<JComponent> builder = MarkerFactory.compoundMarkerBuilder(
				MarkerFactory.backgroundMarkerBuilder(), MarkerFactory.foregroundMarkerBuilder());
		JTextField testField = new JTextField();
		testField.setForeground(Color.GREEN);
		testField.setBackground(Color.BLUE);
		Marker<JComponent> testMarker = builder.newMarker(testField);
		assertEquals(Color.GREEN, testField.getForeground());
		assertEquals(Color.BLUE, testField.getBackground());
		assertTrue(testMarker.isMarkedValid());
		ValidationException ve = new ValidationException("testField", "test");
		testMarker.markInvalid(ve, null);
		assertEquals(Marker.DEFAULT_INVALID_TEXT_COLOR, testField.getForeground());
		assertEquals(Marker.DEFAULT_INVALID_COLOR, testField.getBackground());
		assertFalse(testField.isValid());
		testMarker.markValid();
		assertEquals(Color.GREEN, testField.getForeground());
		assertEquals(Color.BLUE, testField.getBackground());
		assertTrue(testMarker.isMarkedValid());		
	}
}
