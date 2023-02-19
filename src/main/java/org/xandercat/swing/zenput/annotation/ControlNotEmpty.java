package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.NotEmptyCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ControlNotEmpty {
	String dependencyOn();
	Class<?> conditionClass() default NotEmptyCondition.class;
}
