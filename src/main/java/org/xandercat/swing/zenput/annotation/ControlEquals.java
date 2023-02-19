package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.EqualsCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ControlEquals {
	String dependencyOn();
	Class<?> conditionClass() default EqualsCondition.class; 
	Class<?> valueType();
	String stringValue();
}