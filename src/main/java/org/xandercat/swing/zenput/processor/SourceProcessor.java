package org.xandercat.swing.zenput.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xandercat.swing.zenput.util.DependencyChain;
import org.xandercat.swing.zenput.util.ReflectionUtil;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.validator.DependencyValidator;
import org.xandercat.swing.zenput.validator.Validator;

/**
 * Processor that operates strictly on "source" objects.  This type of processor can be
 * used to validate objects loaded into memory.
 * 
 * @author Scott Arnold
 */
public class SourceProcessor implements Processor, ValueRetriever {

	protected final Map<String, Integer> fieldOrder = new HashMap<String, Integer>();
	protected final List<String> fieldNames = new ArrayList<String>();
	protected final List<String> fieldTitles = new ArrayList<String>();
	protected final List<Class<?>> fieldTypes = new ArrayList<Class<?>>();
	protected final List<Object> sources = new ArrayList<Object>();
	protected final List<ValidationException> errors = new ArrayList<ValidationException>();
	protected final List<List<Validator<?>>> validators = new ArrayList<List<Validator<?>>>();
	protected final List<List<String>> dependencies = new ArrayList<List<String>>();
	protected final Object defaultSource;
	private final DependencyChain<String> dependencyChain = new DependencyChain<String>();
	private ValueRetriever valueRetriever;
	private boolean dependenciesBuilt = false;
	
	/**
	 * Constructs a new SourceProcessor with the given default source object.
	 * 
	 * @param defaultSource		default source object
	 */
	public SourceProcessor(Object defaultSource) {
		this.defaultSource = defaultSource;
		this.valueRetriever = this;
		AnnotationHandler.registerFields(this, defaultSource);
	}
	
	/**
	 * Constructs a new SourceProcessor from the given sources.  The first source provided
	 * will be used as the default source, should any fields be registered without specifying
	 * a source.
	 * 
	 * This constructor is provided for cases where there are multiple source objects which
	 * make use of the Zenput annotations to define the fields and validators.
	 * 
	 * @param sources			source objects (first source will be used as default)
	 */
	public SourceProcessor(Object... sources) {
		this(sources[0]);
		for (int i=1; i<sources.length; i++) {
			AnnotationHandler.registerFields(this, sources[i]);
		}
	}
	
	@Override
	public List<ValidationException> getErrors() {
		return getErrors(fieldNames);
	}

	@Override
	public ValidationException getError(String fieldName) {
		return this.errors.get(this.fieldOrder.get(fieldName));
	}

	@Override
	public List<ValidationException> getErrors(List<String> fieldNames) {
		List<ValidationException> errors = new ArrayList<ValidationException>();
		for (String fieldName : fieldNames) {
			ValidationException ve = this.errors.get(this.fieldOrder.get(fieldName));
			if (ve != null) {
				errors.add(ve);
			}
		}
		return errors;
	}

	@Override
	public void registerField(String fieldName, String fieldTitle, Object source) throws ZenputException {
		if (source == null || fieldName == null) {
			throw new ZenputException("Source object and fieldName cannot be null");
		}
		try {
			Field field = ReflectionUtil.verifyField(source, fieldName, true);
			this.fieldTypes.add(field.getType());
		} catch (NoSuchFieldException e) {
			throw new ZenputException("Field " + fieldName + " must exist in source object.", e);
		} catch (NoSuchMethodException e) {
			throw new ZenputException("Getter and Setter methods must exist for field " + fieldName + " in the source object.", e);
		}
		this.fieldOrder.put(fieldName, Integer.valueOf(this.fieldNames.size()));
		this.fieldNames.add(fieldName);
		this.fieldTitles.add((fieldTitle == null)? fieldName : fieldTitle);
		this.sources.add(source);
		this.errors.add(null);
		this.dependencies.add(null);
		this.validators.add(new ArrayList<Validator<?>>());
	}

	@Override
	public void registerField(String fieldName, String fieldTitle) throws ZenputException {
		registerField(fieldName, fieldTitle, this.defaultSource);
	}

	@Override
	public void registerField(String fieldName, String fieldTitle, Validator<?> validator) throws ZenputException {
		registerField(fieldName, fieldTitle, this.defaultSource);
		registerValidator(fieldName, validator);
	}

	@Override
	public void registerValidator(String fieldName, Validator<?> validator) throws ZenputException {
		if (!validator.getValueType().isAssignableFrom(getRegisteredFieldType(fieldName))) {
			throw new ZenputException("Validator for type " + validator.getValueType().getName() + " is not class compatible with field " + fieldName + " of type " + getRegisteredFieldType(fieldName));
		}
		List<Validator<?>> validators = this.validators.get(this.fieldOrder.get(fieldName));
		validators.add(validator);
		if (validator instanceof DependencyValidator) {
			DependencyValidator<?> dependentValidator = (DependencyValidator<?>) validator;
			dependentValidator.setValueRetriever(this.valueRetriever);
			this.dependenciesBuilt = false;	// will force dependencies to be reconstructed on next validate
			this.dependencyChain.add(fieldName, dependentValidator.getDependencyFieldNames());
		}
	}

	private void checkFieldsRegistered(List<String> fieldNames) throws ZenputException {
		for (String fieldName : fieldNames) {
			checkFieldRegistered(fieldName);
		}
	}
	
	private int checkFieldRegistered(String fieldName) throws ZenputException {
		Integer i = this.fieldOrder.get(fieldName);
		if (i == null) {
			throw new ZenputException("Field by name " + fieldName + " is not registered to be handled by this processor.");
		}
		return i.intValue();
	}
	
	@Override
	public List<String> getValidationOrder(List<String> fieldNames) throws ZenputException {
		checkFieldsRegistered(fieldNames);
		if (!this.dependenciesBuilt) {
			// clear out old chains
			for (int i=0; i<this.dependencies.size(); i++) {
				dependencies.set(i, null);
			}
			// setup new chains
			Set<List<String>> chains = this.dependencyChain.getOrderedChains();
			for (List<String> chain : chains) {
				for (String fieldName : chain) {
					Integer idx = this.fieldOrder.get(fieldName);
					this.dependencies.set(idx.intValue(), chain);
				}
			}
			this.dependenciesBuilt = true;
		}
		// reorder/add fields based on chains
		List<String> fieldsToValidate = new ArrayList<String>();
		for (String fieldName : fieldNames) {
			if (!fieldsToValidate.contains(fieldName)) {
				List<String> chain = this.dependencies.get(this.fieldOrder.get(fieldName));
				if (chain == null || chain.size() == 0) {
					fieldsToValidate.add(fieldName);
				} else {
					fieldsToValidate.addAll(chain);
				}
			}
		}
		return fieldsToValidate;
	}

	@Override
	public boolean validate() throws ZenputException {
		return validate(this.fieldNames);
	}

	@Override
	public boolean validate(List<String> fieldNames) throws ZenputException {
		boolean invalid = false;
		List<String> fieldsToValidate = getValidationOrder(fieldNames);
		for (String fieldName : fieldsToValidate) {
			invalid = !validate(fieldName) || invalid;
		}
		return !invalid;
	}

	private <T> boolean validate(String fieldName) throws ZenputException {
		int idx = checkFieldRegistered(fieldName);
		this.errors.set(idx, null);
		T fieldValue = null;
		try {
			fieldValue = (T) this.valueRetriever.getValueForField(fieldName);
		} catch (ValidationException ve) {
			this.errors.set(idx, ve);
			return false;
		}
		List<Validator<?>> validators = this.validators.get(idx);
		for (Validator<?> validator : validators) {
			@SuppressWarnings("unchecked")	// type safety was ensured at time of registration
			Validator<? super T> typedValidator = (Validator<? super T>) validator;
			try {
				if (typedValidator.shouldValidate(fieldName, fieldValue)) {
					typedValidator.validate(fieldName, fieldValue);
				} 
			} catch (ValidationException ve) {
				this.errors.set(idx, ve);
				return false;
			}
		}		
		return true;
	}
	
	@Override
	public Object getSource(String fieldName) {
		return sources.get(this.fieldOrder.get(fieldName));
	}
	
	@Override
	public void setValueRetriever(ValueRetriever valueRetriever) {
		this.valueRetriever = valueRetriever;
	}

	@Override
	public <T> T getValueForField(String fieldName) throws ValidationException, ZenputException {
		Integer idx = this.fieldOrder.get(fieldName);
		Object source = this.sources.get(idx.intValue());
		T fieldValue = null;
		try {
			fieldValue = ReflectionUtil.invokeGetter(source, fieldName);
		} catch (Exception e) {
			throw new ZenputException("Unable to retrieve field value for field " + fieldName, e);
		}
		return fieldValue;
	}

	@Override
	public Class<?> getRegisteredFieldType(String fieldName) throws ZenputException {
		Integer order = this.fieldOrder.get(fieldName);
		if (order == null) {
			throw new ZenputException("Field " + fieldName + " is not registered with the processor.");
		}
		return this.fieldTypes.get(order.intValue());
	}

	@Override
	public String getRegisteredFieldTitle(String fieldName) {
		return this.fieldTitles.get(this.fieldOrder.get(fieldName).intValue());
	}

	@Override
	public List<String> getRegisteredFieldNames() {
		List<String> fieldNamesCopy = new ArrayList<String>();
		fieldNamesCopy.addAll(this.fieldNames);
		return fieldNamesCopy;
	}

	@Override
	public void close() {
		dependencyChain.clear();
		valueRetriever = null;
	}
}
