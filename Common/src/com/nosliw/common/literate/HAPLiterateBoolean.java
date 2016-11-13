package com.nosliw.common.literate;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateBoolean implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_BOOLEAN;	}

	@Override
	public Object stringToValue(String strValue, String subType) {  return Boolean.valueOf(strValue);  }

	@Override
	public String valueToString(Object value) {  return value.toString(); }

	@Override
	public Set<Class> getObjectClasses() {  
		Set<Class> out = new HashSet<Class>(); 
		out.add(Boolean.class);
		return out;
	}

	@Override
	public String getSubTypeByObject(Object value) {	return null;	}
}
