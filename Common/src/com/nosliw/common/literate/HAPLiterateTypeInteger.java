package com.nosliw.common.literate;

import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateTypeInteger implements HAPLiterateType{

	public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_INTEGER;	}
	public Object stringToValue(String strValue) {  return Integer.valueOf(strValue);  }
	public String valueToString(Object value) {  return value.toString(); }
	public Class getObjectClass() {  return Integer.class; }

}
