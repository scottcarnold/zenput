package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.NotEqualsCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateDependencyNotEquals {
	String dependencyOn();
	Class<?> conditionClass() default NotEqualsCondition.class;
	CompareTo compareTo() default CompareTo.FIELD;
	Class<?> valueType() default Object.class;
	String stringValue() default "";
}
