package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPLiterateFloat implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_FLOAT;	}

	@Override
	public Object stringToValue(String strValue, String subType) {  return Float.valueOf(strValue);  }

	@Override
	public String valueToString(Object value) {  return value.toString(); }

	@Override
	public List<Class> getObjectClasses() {  
		List<Class> out = new ArrayList<Class>(); 
		out.add(Float.class);
		out.add(Float.TYPE);
		return out;
	}

	@Override
	public String getSubTypeByType(Type value) {	return null;	}
}
