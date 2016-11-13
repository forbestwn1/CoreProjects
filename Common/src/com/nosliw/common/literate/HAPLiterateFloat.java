package com.nosliw.common.literate;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateFloat implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_FLOAT;	}

	@Override
	public Object stringToValue(String strValue, String subType) {  return Float.valueOf(strValue);  }

	@Override
	public String valueToString(Object value) {  return value.toString(); }

	@Override
	public Set<Class> getObjectClasses() {  
		Set<Class> out = new HashSet<Class>(); 
		out.add(Integer.class);
		return out;
	}

	@Override
	public String getSubTypeByObject(Object value) {	return null;	}
}
