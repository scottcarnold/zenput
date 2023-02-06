package org.xandercat.swing.zenput.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xandercat.swing.zenput.annotation.InputField;
import org.xandercat.swing.zenput.annotation.ValidateConditional;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.validator.CompoundValidator;
import org.xandercat.swing.zenput.validator.ConditionalValidator;
import org.xandercat.swing.zenput.validator.DependentCondition;
import org.xandercat.swing.zenput.validator.Validator;

/**
 * AnnotationProcessor reads Zenput annotations from one or more source classes, registering
 * the fields and validators from those source classes with the given Processor.
 * 
 * Annotation classes are expected to have a "validatorClass" method that returns the Class
 * of the Validator the annotation corresponds to.  The Validator is expected to have a 
 * public static method named "newValidator" that takes an instance of the annotation as
 * an argument.
 * 
 * @author Scott Arnold
 */
public class AnnotationHandler {

	private static final Logger log = LogManager.getLogger(AnnotationHandler.class);
	
	public static void registerFields(Processor processor, Object... sourceObjects) {
		for (Object sourceObject : sourceObjects) {
			Class<?> clazz = sourceObject.getClass();
			while (clazz != null  && clazz != java.lang.Object.class) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					InputField zenputFieldAnno = field.getAnnotation(InputField.class);
					if (zenputFieldAnno != null) {
						try {
							processor.registerField(field.getName(), zenputFieldAnno.title(), sourceObject);
							registerValidators(processor, field);
						} catch (ZenputException ze) {
							log.error("Field " + field.getName() + " could not be registered", ze);
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
	}
	
	private static <T> T createObjectFromAnnotation(Annotation annotation, String classGetterMethodName, String objectBuilderMethodName) {
		Method classGetter = null;
		try {
			classGetter = annotation.getClass().getMethod(classGetterMethodName, (Class<?>[]) null);
		} catch (NoSuchMethodException nsme) {
			// annotations that do not have the target class getter method name are ignored
			return null;
		}
		try {
			Class<?> objectClass = (Class<?>) classGetter.invoke(annotation, (Object[]) null);
			Method objectBuilderMethod = objectClass.getMethod(objectBuilderMethodName, annotation.annotationType());
			int mods = objectBuilderMethod.getModifiers();
			if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) {
				return (T) objectBuilderMethod.invoke(null, annotation);
			} else {
				throw new Exception(objectBuilderMethodName + " method in class " + objectClass.getName() + " should be public and static.");
			}
		} catch (Exception e) {
			log.error("Unable to create object from annotation.", e);
		}	
		return null;
	}
	
	private static void registerValidators(Processor processor, Field field) throws ZenputException {
		List<Validator<?>> fieldValidators = new ArrayList<Validator<?>>();
		ValidateConditional cvAnno = null;
		DependentCondition cvCondition = null;
		for (Annotation anno : field.getAnnotations()) {
			if (anno instanceof ValidateConditional) {
				if (cvAnno == null) {
					cvAnno = (ValidateConditional) anno;
				} else {
					log.error("There can only be one ValidateConditional annotation per field.  All but one ValidateConditional annotation will be ignored.");
				}
			}
			DependentCondition condition = createObjectFromAnnotation(anno, "conditionClass", "newCondition");
			if (condition != null) {
				if (cvCondition == null) {
					cvCondition = condition;
				} else {
					log.error("There can only be one Condition annotation per field.  All but one Condition annotation will be ignored.");
				}
			} else {
				Validator<?> validator = createObjectFromAnnotation(anno, "validatorClass", "newValidator");
				if (validator != null) {
					fieldValidators.add(validator);
				}
			} 
		}
		if (cvAnno != null && cvCondition != null) {
			Validator<?> validator = null;
			if (fieldValidators.size() == 1) {
				validator = fieldValidators.get(0);
			} else if (fieldValidators.size() > 1) {
				Validator<?>[] validatorArray = new Validator[fieldValidators.size()];
				validator = new CompoundValidator(fieldValidators.toArray(validatorArray));
			}
			if (validator == null) {
				log.error("ValidateConditional annotation exists without an Validator annotations.");
			} else {
				ConditionalValidator cv = new ConditionalValidator(cvAnno.dependentOn(), cvCondition, validator);
				processor.registerValidator(field.getName(), cv);
			}
		} else if (cvAnno != null || cvCondition != null) {
			log.error("ValidateConditional annotation and a condition annotation must both be specified if either is specified.");
		} else {
			for (Validator<?> validator : fieldValidators) {
				processor.registerValidator(field.getName(), validator);
			}
		}
	}
}
