package org.xandercat.swing.zenput.processor;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.xandercat.swing.zenput.adapter.InputAccessor;
import org.xandercat.swing.zenput.annotation.ControlNotEquals;
import org.xandercat.swing.zenput.annotation.InputField;
import org.xandercat.swing.zenput.annotation.ValidateDependencyNumeric;
import org.xandercat.swing.zenput.annotation.ValidateInteger;
import org.xandercat.swing.zenput.annotation.ValidateRequired;
import org.xandercat.swing.zenput.condition.Operator;
import org.xandercat.swing.zenput.converter.IntegerConverter;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.marker.BackgroundMarker;
import org.xandercat.swing.zenput.marker.MarkTargetProvider;
import org.xandercat.swing.zenput.marker.MarkerFactory;

public class InputProcessorTest {

	public static enum Size { SMALL, LARGE }
	
	public static class Source {
		@InputField(title="Quantity")
		@ValidateRequired
		@ValidateInteger(min=1,max=100)
		private Integer quantity;
		@InputField(title="Size")
		@ControlNotEquals(dependencyOn="quantity", valueType=Integer.class, stringValue="1")
		@ValidateRequired
		private Size size;
		@InputField(title="Dependent On Quantity")
		@ValidateDependencyNumeric(dependencyOn="quantity", operator=Operator.LT)
		private Integer lessThanQuantity;
		
		public Integer getLessThanQuantity() {
			return lessThanQuantity;
		}
		public void setLessThanQuantity(Integer lessThanQuantity) {
			this.lessThanQuantity = lessThanQuantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setSize(Size size) {
			this.size = size;
		}
		public Size getSize() {
			return size;
		}
	}
	
	public static class MarkTargetProviderInput implements MarkTargetProvider {
		JTextField markTarget1 = new JTextField();
		JTextField markTarget2 = new JTextField();
		@Override
		public Object[] getMarkTargets() {
			return new Object[] { markTarget1, markTarget2 };
		}
	}
	
	public static class NaughtyMarkTargetProviderInput implements MarkTargetProvider {
		JTextField markTarget1 = new JTextField();
		JTextField markTarget2 = new JTextField();
		@Override
		public Object[] getMarkTargets() {
			return new Object[] { markTarget1, this, markTarget2 };
		}
	}
	
	private JTextField quantityInputField;
	private JTextField lessThanQuantityInputField;
	private JComboBox<Size> sizeInputField;
	private Source source;
	private SourceProcessor sourceProcessor;
	private InputProcessor inputProcessor;
	
	@BeforeEach
	public void init() {
		this.quantityInputField = new JTextField();
		this.sizeInputField = new JComboBox(Size.values());
		this.lessThanQuantityInputField = new JTextField();
		this.source = new Source();
		source.setQuantity(10);
		source.setSize(Size.SMALL);
		source.setLessThanQuantity(0);
		this.sourceProcessor = new SourceProcessor(source);
		this.inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		try {
			this.inputProcessor.registerInput("quantity", this.quantityInputField);
			this.inputProcessor.registerInput("size", sizeInputField, Size.class);
			this.inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		} catch (ZenputException e) {}
	}
	
	@Test
	public void testCommitModeAll() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		inputProcessor.registerInput("quantity", quantityInputField);
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		quantityInputField.setText("200");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(200, source.quantity);
	}
	
	@Test
	public void testCommitModeValid() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_VALID, false);
		inputProcessor.registerInput("quantity", quantityInputField);
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		quantityInputField.setText("200");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(10, source.quantity);
		quantityInputField.setText("80");
		valid = inputProcessor.validate();
		assertTrue(valid);
		assertEquals(80, source.quantity);
	}
	
	@Test
	public void testCommitModeNone() throws Exception {
		source.setQuantity(2);
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_NONE, false);
		inputProcessor.registerInput("quantity", quantityInputField);
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		quantityInputField.setText("50");
		boolean valid = inputProcessor.validate();
		assertTrue(valid);
		assertEquals(2, source.quantity);
	}
	
	@Test
	public void testSetMarker() throws Exception {
		inputProcessor.setMarker("quantity", new BackgroundMarker(quantityInputField, Color.CYAN));
		quantityInputField.setText("200");
		inputProcessor.validate();
		assertEquals(Color.CYAN, quantityInputField.getBackground());
	}
	
	@Test
	public void testSetDefaultMarkerBuilder() throws Exception {
		inputProcessor.setDefaultMarkerBuilder(JTextField.class, MarkerFactory.backgroundMarkerBuilder(Color.CYAN));
		quantityInputField.setText("200");
		inputProcessor.validate();
		assertEquals(Color.CYAN, quantityInputField.getBackground());		
	}
	
	@Test
	public void testMarkTargetProviderMarking() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		final MarkTargetProviderInput input = new MarkTargetProviderInput();
		InputAccessor<String> inputAccessor = new InputAccessor<String>() {
			@Override
			public String getValue() {
				return input.markTarget1.getText();
			}
			@Override
			public void setValue(String value) {
				input.markTarget1.setText(value);
				input.markTarget2.setText(value);
			}
			@Override
			public Object getSource() {
				return input;
			}
		};
		inputProcessor.setDefaultMarkerBuilder(JTextField.class, MarkerFactory.backgroundMarkerBuilder(Color.ORANGE));
		inputProcessor.registerInput("quantity", inputAccessor, new IntegerConverter());
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		input.markTarget1.setText("not an integer");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(Color.ORANGE, input.markTarget1.getBackground());
		assertEquals(Color.ORANGE, input.markTarget2.getBackground());
	}
	
	@Test
	@Timeout(value=500, unit=TimeUnit.MILLISECONDS)
	public void testNaughtyMarkTargetProviderRecursionCheck() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
		final NaughtyMarkTargetProviderInput input = new NaughtyMarkTargetProviderInput();
		InputAccessor<String> inputAccessor = new InputAccessor<String>() {
			@Override
			public String getValue() {
				return input.markTarget1.getText();
			}
			@Override
			public void setValue(String value) {
				input.markTarget1.setText(value);
				input.markTarget2.setText(value);
			}
			@Override
			public Object getSource() {
				return input;
			}
		};
		inputProcessor.setDefaultMarkerBuilder(JTextField.class, MarkerFactory.backgroundMarkerBuilder(Color.ORANGE));
		inputProcessor.registerInput("quantity", inputAccessor, new IntegerConverter());
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		input.markTarget1.setText("not an integer");
		boolean valid = inputProcessor.validate();
		assertFalse(valid);
		assertEquals(Color.ORANGE, input.markTarget1.getBackground());
		assertEquals(Color.ORANGE, input.markTarget2.getBackground());		
	}
	
	@Test
	public void testValidateOnFocusLost() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_VALID, true);
		inputProcessor.registerInput("quantity", quantityInputField);
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		assertTrue(inputProcessor.validate());
		quantityInputField.setText("1000");
		FocusListener[] listeners = quantityInputField.getFocusListeners();
		assertTrue(containsValidateOnFocusLostListener(quantityInputField));
		for (FocusListener listener : listeners) {
			listener.focusLost(new FocusEvent(quantityInputField, FocusEvent.FOCUS_LOST));
		}
		ValidationException ve = inputProcessor.getError("quantity");
		assertNotNull(ve);
		inputProcessor.close();
		assertFalse(containsValidateOnFocusLostListener(quantityInputField)); // focus listener should have been removed on close
	}
	
	@Test
	public void testValidateOnFocusLostListenerMaintenance() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, true);
		final MarkTargetProviderInput input = new MarkTargetProviderInput();
		InputAccessor<String> inputAccessor = new InputAccessor<String>() {
			@Override
			public String getValue() {
				return input.markTarget1.getText();
			}
			@Override
			public void setValue(String value) {
				input.markTarget1.setText(value);
				input.markTarget2.setText(value);
			}
			@Override
			public Object getSource() {
				return input;
			}
		};
		assertFalse(containsValidateOnFocusLostListener(input.markTarget1));
		assertFalse(containsValidateOnFocusLostListener(input.markTarget2));
		inputProcessor.registerInput("quantity", inputAccessor, new IntegerConverter());
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		assertTrue(containsValidateOnFocusLostListener(input.markTarget1));
		assertTrue(containsValidateOnFocusLostListener(input.markTarget2));
		inputProcessor.close();
		assertFalse(containsValidateOnFocusLostListener(input.markTarget1));
		assertFalse(containsValidateOnFocusLostListener(input.markTarget2));	
	}
	
	private boolean containsValidateOnFocusLostListener(JComponent component) {
		for (FocusListener listener : component.getFocusListeners()) {
			if (listener != null && (listener instanceof ValidateOnFocusLostListener)) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void testCommitList() throws Exception {
		InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_NONE, false);
		inputProcessor.registerInput("quantity", quantityInputField);
		inputProcessor.registerInput("size", sizeInputField, Size.class);
		inputProcessor.registerInput("lessThanQuantity", this.lessThanQuantityInputField);
		source.setQuantity(2);
		source.setSize(Size.SMALL);
		quantityInputField.setText("50");
		sizeInputField.setSelectedItem(Size.LARGE);
		inputProcessor.validate();
		assertEquals(2, source.quantity);
		assertEquals(Size.SMALL, source.size);
		inputProcessor.commit(Arrays.asList("quantity", "size"));
		assertEquals(50, source.quantity);
		assertEquals(Size.LARGE, source.size);
	}
	
	@Test
	public void testDependencyValidation() throws Exception {
		quantityInputField.setText("20");
		lessThanQuantityInputField.setText("22");
		assertFalse(inputProcessor.validate());
		ValidationException ve = inputProcessor.getError("lessThanQuantity");
		assertNotNull(ve);
		lessThanQuantityInputField.setText("19");
		assertTrue(inputProcessor.validate());
	}
	
	@Test
	public void testGetErrors() throws Exception {
		quantityInputField.setText("200");
		inputProcessor.validate();
		List<ValidationException> errors = inputProcessor.getErrors(Arrays.asList("quantity", "size"));
		assertNotNull(errors);
		assertEquals(1, errors.size());
		assertNotNull(errors.get(0));
		assertEquals("quantity", errors.get(0).getFieldName());
	}
	

}
