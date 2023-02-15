package org.xandercat.swing.zenput.processor;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.annotation.InputField;
import org.xandercat.swing.zenput.annotation.ValidateInteger;
import org.xandercat.swing.zenput.annotation.ValidateRequired;
import org.xandercat.swing.zenput.marker.BackgroundMarker;
import org.xandercat.swing.zenput.marker.MarkerFactory;

public class InputProcessorTest {

	public static class Source {
		@InputField(title="Quantity")
		@ValidateRequired
		@ValidateInteger(min=1,max=100)
		private Integer quantity;
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Integer getQuantity() {
			return quantity;
		}
	}
	
	private JTextField inputField;
	private Source source;
	private SourceProcessor sourceProcessor;
	
	@BeforeEach
	public void init() {
		this.inputField = new JTextField();
		this.source = new Source();
		source.setQuantity(10);
		this.sourceProcessor = new SourceProcessor(source);		
	}
	
	@Test
	public void testCommitModeAll() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		inputProcessor.registerInput("quantity", inputField);
		inputField.setText("200");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(200, source.quantity);
	}
	
	@Test
	public void testCommitModeValid() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_VALID, false);
		inputProcessor.registerInput("quantity", inputField);
		inputField.setText("200");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(10, source.quantity);
		inputField.setText("80");
		valid = inputProcessor.validate();
		assertTrue(valid);
		assertEquals(80, source.quantity);
	}
	
	@Test
	public void testCommitModeNone() throws Exception {
		source.setQuantity(2);
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_NONE, false);
		inputProcessor.registerInput("quantity", inputField);
		inputField.setText("50");
		boolean valid = inputProcessor.validate();
		assertTrue(valid);
		assertEquals(2, source.quantity);
	}
	
	@Test
	public void testSetMarker() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		inputProcessor.registerInput("quantity", inputField);
		inputProcessor.setMarker("quantity", new BackgroundMarker(inputField, Color.CYAN));
		inputField.setText("200");
		inputProcessor.validate();
		assertEquals(Color.CYAN, inputField.getBackground());
	}
	
	@Test
	public void testSetDefaultMarkerBuilder() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		inputProcessor.registerInput("quantity", inputField);
		inputProcessor.setDefaultMarkerBuilder(JTextField.class, MarkerFactory.backgroundMarkerBuilder(Color.CYAN));
		inputField.setText("200");
		inputProcessor.validate();
		assertEquals(Color.CYAN, inputField.getBackground());		
	}
}
