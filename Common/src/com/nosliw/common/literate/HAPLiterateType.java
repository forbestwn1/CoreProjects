package com.nosliw.common.literate;

public interface HAPLiterateType{
	
	String getName();
	
	Object stringToValue(String strValue);
	
	String valueToString(Object value);
	
	Class getObjectClass();
}
