package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.FixedEqualsCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConditionFixedEquals {
	Class<?> conditionClass() default FixedEqualsCondition.class; 
	Class<?> valueType();
	String stringValue();
}
