package com.nosliw.common.literate;

import java.util.List;

public interface HAPLiterateDef{
	
	String getName();
	
	Object stringToValue(String strValue, String subType);
	
	String valueToString(Object value);
	
	List<Class> getObjectClasses();

	String getSubTypeByObject(Object value);
}
