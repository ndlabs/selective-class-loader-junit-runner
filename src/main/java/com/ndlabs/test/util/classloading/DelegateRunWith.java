package com.ndlabs.test.util.classloading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runner.Runner;
import org.junit.runners.JUnit4;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DelegateRunWith {
	public Class<? extends Runner> value() default JUnit4.class;
}
