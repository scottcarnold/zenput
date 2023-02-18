package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.NotEqualsCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConditionNotEquals {
	Class<?> conditionClass() default NotEqualsCondition.class; 
	Class<?> valueType();
	String stringValue();
}
