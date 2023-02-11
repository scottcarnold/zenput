package org.xandercat.swing.zenput.processor;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextField;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.annotation.InputField;
import org.xandercat.swing.zenput.annotation.ValidateInteger;
import org.xandercat.swing.zenput.annotation.ValidateRequired;

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
	@Test
	public void testCommitModeAll() throws Exception {
		JTextField inputField = new JTextField();
		Source source = new Source();
		SourceProcessor sourceProcessor = new SourceProcessor(source);
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		inputProcessor.registerInput("quantity", inputField);
		inputField.setText("200");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(200, source.quantity);
	}
	
	@Test
	public void testCommitModeValid() throws Exception {
		JTextField inputField = new JTextField();
		Source source = new Source();
		source.setQuantity(10);
		SourceProcessor sourceProcessor = new SourceProcessor(source);
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
		JTextField inputField = new JTextField();
		Source source = new Source();
		source.setQuantity(2);
		SourceProcessor sourceProcessor = new SourceProcessor(source);
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_NONE, false);
		inputProcessor.registerInput("quantity", inputField);
		inputField.setText("50");
		boolean valid = inputProcessor.validate();
		assertTrue(valid);
		assertEquals(2, source.quantity);
	}
}
