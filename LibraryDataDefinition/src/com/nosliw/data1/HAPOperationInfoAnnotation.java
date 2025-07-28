package com.nosliw.data1;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

/*
 * class for HAPOperationInfo annotation. 
 * This annotation is defined for class method and used on runtime
 * it is used for defining data type operation information: in data type, out data type, description
 */

public @interface HAPOperationInfoAnnotation {

	String out();
	String[] in();
	String description() default "";
	
}
