package com.ndlabs.test.util.classloading;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface BanClassNamesStartingWith {
	public String[] value() default {};
}
