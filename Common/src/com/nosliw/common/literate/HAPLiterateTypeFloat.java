package com.nosliw.common.literate;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateTypeFloat implements HAPLiterateType{
	public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_FLOAT;	}
	public Object stringToValue(String strValue) {  return Float.valueOf(strValue);  }
	public String valueToString(Object value) {  return value.toString(); }
	public Class getObjectClass() {  return Float.class; }
}
