package com.nosliw.common.literate;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializableUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateTypeArray  implements HAPLiterateType{

	public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_ARRAY;	}
	public Object stringToValue(String strValue) {
		int startIndex = strValue.indexOf(HAPConstant.SEPERATOR_ARRAYSTART);
		int endIndex = strValue.indexOf(HAPConstant.SEPERATOR_ARRAYEND);
		
		String type = null;
		String className = null;
		String elesStr = null;
		String[] elesArray = null;
		
		if(startIndex==-1){
			elesStr = strValue;
			type = HAPConstant.STRINGABLE_BASICVALUETYPE_STRING;
		}
		else{
			elesStr = strValue.substring(startIndex+1, endIndex);
			String t = strValue.substring(0, startIndex).trim();
			if(HAPBasicUtility.isStringEmpty(t)){
				type = HAPConstant.STRINGABLE_BASICVALUETYPE_STRING;
			}
			else{
				if(HAPLiterateManager.isBasicType(t)){
					type = t;
				}
				else{
					className = t;
				}
			}
		}
		
		List<Object> out = new ArrayList<Object>();
		elesArray = HAPNamingConversionUtility.parseElements(elesStr);
		for(String eleStr : elesArray){
			if(HAPBasicUtility.isStringNotEmpty(type)){
				out.add(HAPLiterateManager.stringToValue(eleStr, type));
			}
			else{
				HAPSerializable eleObj = null;
				try {
					eleObj = (HAPSerializable)Class.forName(className).newInstance();
					eleObj.buildObject(eleStr, HAPSerializationFormat.LITERATE);
					out.add(eleObj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return out;  
	}
	
	public String valueToString(Object value) {  
		StringBuffer arrayStr = new StringBuffer();
		arrayStr.append(HAPConstant.SEPERATOR_ARRAYSTART);
		
		List<String> elesStr = new ArrayList<String>();
		for(Object eleObj : (List<Object>)value){
			elesStr.add(HAPSerializableUtility.toLiterateString(eleObj));
		}
		
		arrayStr.append(HAPNamingConversionUtility.cascadeElement(elesStr.toArray(new String[0])));
		arrayStr.append(HAPConstant.SEPERATOR_ARRAYEND);
		return arrayStr.toString(); 
		
	}
	public Class getObjectClass() {  return List.class; }
}
