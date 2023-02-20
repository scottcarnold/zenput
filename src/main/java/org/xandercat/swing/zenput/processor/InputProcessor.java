package org.xandercat.swing.zenput.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xandercat.swing.zenput.adapter.InputAccessor;
import org.xandercat.swing.zenput.adapter.InputAdapter;
import org.xandercat.swing.zenput.adapter.ReflectionAccessor;
import org.xandercat.swing.zenput.converter.ConverterFactory;
import org.xandercat.swing.zenput.converter.InputConverter;
import org.xandercat.swing.zenput.converter.SameTypeConverter;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.marker.CompoundMarker;
import org.xandercat.swing.zenput.marker.MarkTargetProvider;
import org.xandercat.swing.zenput.marker.Marker;
import org.xandercat.swing.zenput.marker.MarkerBuilder;
import org.xandercat.swing.zenput.marker.MarkerFactory;
import org.xandercat.swing.zenput.util.ReflectionUtil;
import org.xandercat.swing.zenput.validator.Control;
import org.xandercat.swing.zenput.validator.Validator;

/**
 * InputProcessor expands on the duties of a generic Processor by reading/writing values
 * from/to input components in a user interface and providing methods for committing the
 * input values back to their source objects.  The design is similar to that of a decorator.
 * 
 * Input fields in the user interface are loaded from the source objects when fields are 
 * registered.  Input field values are committed back to the source objects upon calling 
 * one of the commit(...) methods.
 * 
 * The commit(...) methods are provided and can be called independently of the validate(...)
 * methods.  However, values can be auto-committed after validation based on the CommitMode.
 * When the CommitMode is set to COMMIT_ALL or COMMIT_VALID, values (either all possible values
 * or just valid values respectively) will be automatically committed after validation.  When
 * the CommitMode is set to COMMIT_NONE, values must be committed back to source explicitly
 * by calling the commit(...) methods.
 * 
 * If validateOnFocusLost is set to true, any input components that extend JComponent will have
 * a FocusListener registered on them resulting in immediate validation of the field when
 * focus is lost.  Be careful when relying on this that all components are actually focusable.
 * 
 * @author Scott Arnold
 */
public class InputProcessor implements Processor, ValueRetriever {

	private static final Logger log = LogManager.getLogger(InputProcessor.class);
	
	protected final Map<String, InputAdapter<?, ?>> inputAdapters = new HashMap<String, InputAdapter<?, ?>>();
	protected final Map<String, Marker<?>> explicitMarkers = new HashMap<String, Marker<?>>();
	protected final Map<String, Marker<?>> implicitMarkers = new HashMap<String, Marker<?>>();
	private Map<Class<?>, MarkerBuilder<?>> markerBuilders = new HashMap<Class<?>, MarkerBuilder<?>>();	
	protected final Map<String, ValidateOnFocusLostListener> validateOnFocusLostListeners = new HashMap<String, ValidateOnFocusLostListener>();
	protected final MarkerFactory markerFactory = new MarkerFactory();
	private Processor processor;
	private CommitMode commitMode;
	private boolean validateOnFocusLost;
	
	public InputProcessor(Processor processor, CommitMode commitMode, boolean validateOnFocusLost) {
		this.processor = processor;
		this.processor.setValueRetriever(this);
		this.commitMode = commitMode;
		this.validateOnFocusLost = validateOnFocusLost;
	}

	public CommitMode getCommitMode() {
		return commitMode;
	}
	
	@Override
	public List<ValidationException> getErrors() {
		return processor.getErrors();
	}

	@Override
	public ValidationException getError(String fieldName) {
		return processor.getError(fieldName);
	}

	@Override
	public List<ValidationException> getErrors(List<String> fieldNames) {
		return processor.getErrors(fieldNames);
	}

	@Override
	public Object getSource(String fieldName) {
		return processor.getSource(fieldName);
	}

	@Override
	public void registerField(String fieldName, String fieldTitle, Object source) throws ZenputException {
		processor.registerField(fieldName, fieldTitle, source);		
	}

	@Override
	public void registerField(String fieldName, String fieldTitle) throws ZenputException {
		processor.registerField(fieldName, fieldTitle);
	}

	@Override
	public void registerField(String fieldName, String fieldTitle, Validator<?> validator) throws ZenputException {
		processor.registerField(fieldName, fieldTitle, validator);
	}

	@Override
	public void registerControl(String fieldName, Control<?> control) throws ZenputException {
		processor.registerControl(fieldName, control);
	}

	public <I> void registerInput(String fieldName, InputAccessor<I> inputAccessor, InputConverter<I, ?> inputConverter) throws ZenputException {
		InputAdapter inputAdapter = new InputAdapter(fieldName, inputAccessor, inputConverter);
		inputAdapters.put(fieldName, inputAdapter);
		implicitMarkers.put(fieldName, newMarker(inputAccessor.getSource()));
		if (validateOnFocusLost) {
			List<JComponent> components = new ArrayList<JComponent>();
			if (inputAccessor.getSource() instanceof MarkTargetProvider) {
				for (Object markTarget : ((MarkTargetProvider) inputAccessor.getSource()).getMarkTargets()) {
					if (markTarget instanceof JComponent) {
						components.add((JComponent) markTarget);
					}
				}
			} else if (inputAccessor.getSource() instanceof JComponent) {
				components.add((JComponent) inputAccessor.getSource());
			}
			ValidateOnFocusLostListener listener = new ValidateOnFocusLostListener(fieldName, this);
			validateOnFocusLostListeners.put(fieldName, listener);
			for (JComponent component : components) {
				component.addFocusListener(listener);
			}
		}
		// initialize input field
		// TODO: Should we also validate here?
		try {
			inputAdapter.setValue(ReflectionUtil.invokeGetter(getSource(fieldName), fieldName));
		} catch (ZenputException ze) {
			throw ze;
		} catch (Exception e) {
			throw new ZenputException("Unable to initialize input component for field " + fieldName, e);
		}
	}
	
	public void registerInput(String fieldName, JToggleButton toggleButton) throws ZenputException {
		InputAccessor<Boolean> toggleButtonAccessor = new ReflectionAccessor<Boolean>(toggleButton, "selected", Boolean.TYPE);
		registerInput(fieldName, toggleButtonAccessor, new SameTypeConverter<Boolean>());
	}
	
	public void registerInput(String fieldName, JTextComponent textComponent) throws ZenputException {
		InputAccessor<String> textComponentAccessor = new ReflectionAccessor<String>(textComponent, "text", String.class);
		InputConverter<String, ?> converter = ConverterFactory.getConverterForType(processor.getRegisteredFieldType(fieldName));
		if (converter == null) {
			throw new ZenputException("Converter cannot be generated for Input type String without Source type " + processor.getRegisteredFieldType(fieldName));
		}
		registerInput(fieldName, textComponentAccessor, converter);
	}
	
	/**
	 * Register an input for a JComboBox that uses the given enum class for the combo box values.
	 *  
	 * @param <T>			enum type
	 * @param fieldName		field name
	 * @param comboBox		combo box
	 * @param enumClass		enum class
	 * @throws ZenputException if field cannot be registered
	 */
	public <T> void registerInput(String fieldName, JComboBox<T> comboBox, Class<T> enumClass) throws ZenputException {
		InputAccessor<T> comboBoxAccessor = new ReflectionAccessor<T>(comboBox, "selectedItem", enumClass);
		SameTypeConverter<T> converter = new SameTypeConverter<T>();
		registerInput(fieldName, comboBoxAccessor, converter);
	}
	
	@Override
	public void registerValidator(String fieldName, Validator<?> validator) throws ZenputException {
		processor.registerValidator(fieldName, validator);
	}

	/**
	 * Creates a new marker for the given mark target.  If there is no compatible marker builder
	 * registered, null is returned.
	 * 
	 * @param <T>				target type
	 * @param target			target to be marked
	 * @return					marker for the mark target
	 */
	private <T> Marker<? super T> newMarker(T target) {
		return newMarker(target, new ArrayList<MarkTargetProvider>());
	}
	
	/**
	 * Creates a new marker for the given mark target.  If there is no compatible marker builder
	 * registered, null is returned.
	 * 
	 * @param <T>				target type
	 * @param target			target to be marked
	 * @param mtps              mark target provider chain; used to prevent potential infinite recursion
	 * @return					marker for the mark target
	 */
	private <T> Marker<? super T> newMarker(T target, List<MarkTargetProvider> mtps) {
		Class<?> c = target.getClass();
		MarkerBuilder<? super T> markerBuilder = null;
		while (markerBuilder == null && c != null) {
			markerBuilder = (MarkerBuilder<? super T>) markerBuilders.get(c);
			c = c.getSuperclass();
		}
		if (markerBuilder == null) {
			if (target instanceof MarkTargetProvider) {
				for (MarkTargetProvider mtp : mtps) {
					if (target == mtp) {
						log.warn("Inifinite recursion of MarkTargetProvider detected; mark target already in processing will be ignored.");
						return null;
					}
				}
				mtps.add((MarkTargetProvider) target);
				List<Marker<?>> markers = new ArrayList<Marker<?>>();
				for (Object mtpTarget : ((MarkTargetProvider) target).getMarkTargets()) {
					Marker<?> marker = newMarker(mtpTarget, mtps); // recursive marker creation
					if (marker != null) {
						markers.add(marker);
					}
				}
				return (Marker<? super T>) new CompoundMarker(markers);
			} else {
				return null;
			}
		} else {
			return markerBuilder.newMarker(target);
		}
	}
	
	private void reinitializeInputMarkers() {
		if (implicitMarkers.size() > 0) {
			for (String fieldName : implicitMarkers.keySet()) {
				InputAdapter<?, ?> inputAdapter = this.inputAdapters.get(fieldName);
				implicitMarkers.put(fieldName, newMarker(inputAdapter.getSource()));
			}
		}
	}
	
	/**
	 * Set a marker for a specific input by field name.  This will override any default markers.
	 * No checking is done to ensure the marker is actually a marker for the specified field.
	 * 
	 * @param <T>         input field type to be marked
	 * @param fieldName   name of field
	 * @param marker      marker for input field
	 */
	public <T> void setMarker(String fieldName, Marker<T> marker) {
		explicitMarkers.put(fieldName, marker);
	}
	
	/**
	 * Set default marker builder to be used to create markers for any input component of the given input class.
	 * 
	 * @param <T>              input field type to be marked
	 * @param inputSourceClass class of the input source
	 * @param markerBuilder    builder that builds markers for the class of given source
	 */
	public <T> void setDefaultMarkerBuilder(Class<T> inputSourceClass, MarkerBuilder<? super T> markerBuilder) {
		markerBuilders.put(inputSourceClass, markerBuilder);
		reinitializeInputMarkers();
	}
	
	private void mark(List<String> fieldNames) {
		for (String fieldName : fieldNames) {
			Marker<?> marker = this.explicitMarkers.get(fieldName);
			if (marker == null) {
				marker = this.implicitMarkers.get(fieldName);
			}
			if (marker != null) {
				ValidationException ve = processor.getError(fieldName);
				boolean valid = ve == null;
				if (marker.isMarkedValid() != valid) {
					if (valid) {
						marker.markValid();
					} else {
						marker.markInvalid(ve, processor.getMessageProperties());
					}
				}
			}
		}
	}
	
	@Override
	public boolean validate() throws ZenputException {
		boolean valid = processor.validate();
		mark(processor.getRegisteredFieldNames());
		if (this.commitMode != CommitMode.COMMIT_NONE) {
			for (Map.Entry<String, InputAdapter<?, ?>> entry : this.inputAdapters.entrySet()) {
				String fieldName = entry.getKey();
				if (this.commitMode == CommitMode.COMMIT_ALL || processor.getError(fieldName) == null) {
					try {
						commit(fieldName);
					} catch (ValidationException e) {
						// A ValidationException here indicates the value could not be converted from
						// the input field; in such cases, the field should just be skipped
						log.debug("Skipping commit on field " + fieldName + " due to unresolvable error: " + e.getMessage());
					}
				}	
			}
		}
		return valid;
	}

	@Override
	public boolean validate(List<String> fieldNames) throws ZenputException {
		boolean valid = processor.validate(fieldNames);
		mark(fieldNames);
		if (this.commitMode != CommitMode.COMMIT_NONE) {
			for (String fieldName : fieldNames) {
				if (this.commitMode == CommitMode.COMMIT_ALL || processor.getError(fieldName) == null) {
					try {
						commit(fieldName);
					} catch (ValidationException e) {
						// A ValidationException here indicates the value could not be converted from
						// the input field; in such cases, the field should just be skipped
						log.debug("Skipping commit on field " + fieldName + " due to unresolvable error: " + e.getMessage());
					}
				}
			}
		}
		return valid;
	}

	/**
	 * Commits the input value for the field of given field name back to the source object.
	 * 
	 * If a ValidationException is thrown, it means the value from the input field could not
	 * be successfully converted to a source value.
	 * 
	 * If a ZenputException is thrown, a more serious error has occurred.
	 * 
	 * @param fieldName					name of field to commit
	 * 
	 * @throws ZenputException if program error occurs
	 * @throws ValidationException if value cannot be committed due to validation error
	 */
	public void commit(String fieldName) throws ZenputException, ValidationException {
		Object source = processor.getSource(fieldName);
		try {
			ReflectionUtil.invokeSetter(source, fieldName, getValueForField(fieldName));
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			throw new ZenputException("Unable to commit value back to source for field " + fieldName, e);
		}		
	}
	
	/**
	 * Commits all input values back to their source objects.  Fields where the values 
	 * cannot be committed due to an inability to convert the input value are skipped, 
	 * but otherwise do not result in any error.
	 * 
	 * If a ZenputException is thrown, a more serious error has occurred and commit of 
	 * field values will terminate at the time the exception is thrown.
	 * 
	 * @throws ZenputException if program error occurs
	 */	
	public void commit() throws ZenputException {
		commit(processor.getRegisteredFieldNames());
	}
	
	/**
	 * Commits the input values for the fields of given names back to the source object.
	 * Fields where the values cannot be committed due to an inability to convert the
	 * input value are skipped, but otherwise do not result in any errors.
	 * 
	 * If a ZenputException is thrown, a more serious error has occurred and commit of 
	 * field values will terminate at the time the exception is thrown.
	 * 
	 * @param fieldNames			name of fields to commit
	 * 
	 * @throws ZenputException if program error occurs
	 */
	public void commit(List<String> fieldNames) throws ZenputException {
		for (String fieldName : fieldNames) {
			try {
				commit(fieldName);
			} catch (ValidationException e) {
				log.debug("Field " + fieldName + " will not be converted due to validation error: " + e.getMessage());
			} 
		}
	}
	
	@Override
	public List<String> getRegisteredFieldNames() {
		return processor.getRegisteredFieldNames();
	}

	@Override
	public Class<?> getRegisteredFieldType(String fieldName) throws ZenputException {
		return processor.getRegisteredFieldType(fieldName);
	}

	@Override
	public String getRegisteredFieldTitle(String fieldName) {
		return processor.getRegisteredFieldTitle(fieldName);
	}

	@Override
	public List<String> getValidationOrder(List<String> fieldNames) throws ZenputException {
		return processor.getValidationOrder(fieldNames);
	}

	@Override
	public void setValueRetriever(ValueRetriever valueRetriever) {
		throw new UnsupportedOperationException("ValueRetriever cannot be changed for this type of Processor.");
	}

	public void setMessageProperties(Properties messageProperties) {
		processor.setMessageProperties(messageProperties);
	}

	public Properties getMessageProperties() {
		return processor.getMessageProperties();
	}

	@Override
	public <S> S getValueForField(String fieldName) throws ValidationException, ZenputException {
		InputAdapter<?, ?> inputAdapter = inputAdapters.get(fieldName);
		if (inputAdapter == null) {
			throw new ZenputException("No input has been registered for field " + fieldName);
		}
		return (S) inputAdapter.getValue();
	}

	/**
	 * Closes down the input processor, removing any focus lost listeners from input components.
	 */
	@Override
	public void close() {
		processor.close();
		for (Map.Entry<String, InputAdapter<?, ?>> entry : this.inputAdapters.entrySet()) {
			ValidateOnFocusLostListener listener = this.validateOnFocusLostListeners.get(entry.getKey());
			if (listener != null && entry.getValue().getSource() instanceof JComponent) {
				((JComponent) entry.getValue().getSource()).removeFocusListener(listener);
			}
		}
	}
}
