package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.List;

public interface HAPLiterateDef{
	
	String getName();
	
	Object stringToValue(String strValue, String subType);
	
	String valueToString(Object value);
	
	List<Class> getObjectClasses();

	String getSubTypeByType(Type value);
}
