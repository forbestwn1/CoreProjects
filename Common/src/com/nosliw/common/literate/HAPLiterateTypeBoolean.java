package com.nosliw.common.literate;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateTypeBoolean implements HAPLiterateType{

	public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_BOOLEAN;	}
	public Object stringToValue(String strValue) {  return Boolean.valueOf(strValue);  }
	public String valueToString(Object value) {  return value.toString(); }
	public Class getObjectClass() {  return Boolean.class; }
	
}
