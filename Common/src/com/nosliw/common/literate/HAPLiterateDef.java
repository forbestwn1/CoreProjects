package com.nosliw.common.literate;

import java.util.Set;

public interface HAPLiterateDef{
	
	String getName();
	
	Object stringToValue(String strValue, String subType);
	
	String valueToString(Object value);
	
	Set<Class> getObjectClasses();

	String getSubTypeByObject(Object value);
}
