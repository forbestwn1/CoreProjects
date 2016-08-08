package com.nosliw.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

/*
 * class for HAPJSOperationInfo annotation. 
 * This annotation is defined for class method and used on runtime
 * it is used for defining javascript data type operation information: dependent data types
 */

public @interface HAPScriptOperationInfoAnnotation {

	String[] dependent();

}
