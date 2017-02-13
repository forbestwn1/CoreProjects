package com.nosliw.common.literate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateString implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING;	}

	@Override
	public Object stringToValue(String strValue, String subType) {  return strValue;  }
	
	@Override
	public String valueToString(Object value) {  return value.toString(); }

	@Override
	public List<Class> getObjectClasses() {  
		List<Class> out = new ArrayList<Class>(); 
		out.add(String.class);
		return out;
	}

	@Override
	public String getSubTypeByObject(Object value) {	return null;	}
}
