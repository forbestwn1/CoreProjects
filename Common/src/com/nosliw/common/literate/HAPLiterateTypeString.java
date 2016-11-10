package com.nosliw.common.literate;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateTypeString implements HAPLiterateType{

	public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_STRING;	}
	public Object stringToValue(String strValue) {  return strValue;  }
	public String valueToString(Object value) {  return value.toString(); }
	public Class getObjectClass() {  return String.class; }
}
