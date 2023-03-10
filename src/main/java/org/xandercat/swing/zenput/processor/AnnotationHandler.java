package org.xandercat.swing.zenput.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xandercat.swing.zenput.annotation.InputField;
import org.xandercat.swing.zenput.condition.DependentCondition;
import org.xandercat.swing.zenput.error.ZenputException;
import org.xandercat.swing.zenput.validator.ConditionDependencyControl;
import org.xandercat.swing.zenput.validator.ConditionDependencyValidator;
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
		for (Annotation anno : field.getAnnotations()) {
			// discover controls and validation dependencies by inspecting class name (controls should start with "Control", dependency validations should start with "ValidateDependency")
			if (anno.annotationType().getSimpleName().startsWith("Control")) {
				DependentCondition controlCondition = createObjectFromAnnotation(anno, "conditionClass", "newCondition");	
				ConditionDependencyControl control = new ConditionDependencyControl(controlCondition,  processor.getRegisteredFieldType(field.getName()));
				processor.registerControl(field.getName(), control);
			} else if (anno.annotationType().getSimpleName().startsWith("ValidateDependency")) {
				DependentCondition condition = createObjectFromAnnotation(anno, "conditionClass", "newCondition");	
				ConditionDependencyValidator dependencyValidator = new ConditionDependencyValidator(condition, processor.getRegisteredFieldType(field.getName()));
				processor.registerValidator(field.getName(), dependencyValidator);
			} else if (anno.annotationType().getSimpleName().startsWith("Validate")) {
				Validator<?> validator = createObjectFromAnnotation(anno, "validatorClass", "newValidator");
				processor.registerValidator(field.getName(), validator);
			}
		}
	}
}
