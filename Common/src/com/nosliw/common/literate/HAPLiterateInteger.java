package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPLiterateInteger implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_INTEGER;	}

	@Override
	public Object stringToValue(String strValue, String subType) {  return Integer.valueOf(strValue);  }
	
	@Override
	public String valueToString(Object value) {  return value.toString(); }

	@Override
	public List<Class> getObjectClasses() {  
		List<Class> out = new ArrayList<Class>(); 
		out.add(Integer.class);
		out.add(Integer.TYPE);
		return out;
	}

	@Override
	public String getSubTypeByType(Type value) {	return null;	}
}
