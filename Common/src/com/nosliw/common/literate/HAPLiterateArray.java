package com.nosliw.common.literate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateArray  implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY;	}

	@Override
	public Object stringToValue(String strValue, String subType) {
		int startIndex = strValue.indexOf(HAPConstant.SEPERATOR_ARRAYSTART);
		int endIndex = strValue.lastIndexOf(HAPConstant.SEPERATOR_ARRAYEND);
		
		String arrayStr = strValue;
		if(startIndex!=-1 && endIndex!=-1){
			arrayStr = strValue.substring(startIndex+1, endIndex);
		}
		
		String type = null;
		String type1 = null;
		if(HAPLiterateManager.getInstance().isValidType(subType)){
			type = subType;
		}
		else{
			type = HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT;
			type1 = subType;
		}
		
		List<Object> out = new ArrayList<Object>();
		String[] elesArray = HAPNamingConversionUtility.parseElements(arrayStr);
		for(String eleStr : elesArray){
			out.add(HAPLiterateManager.getInstance().stringToValue(eleStr, type, type1));
		}
		return out;  
	}
	
	@Override
	public String valueToString(Object value) {  
		StringBuffer arrayStr = new StringBuffer();
		arrayStr.append(HAPConstant.SEPERATOR_ARRAYSTART);
		
		List<String> elesStr = new ArrayList<String>();
		for(Object eleObj : (List<Object>)value){
			elesStr.add(HAPLiterateManager.getInstance().valueToString(eleObj));
		}
		
		arrayStr.append(HAPNamingConversionUtility.cascadeElement(elesStr.toArray(new String[0])));
		arrayStr.append(HAPConstant.SEPERATOR_ARRAYEND);
		return arrayStr.toString(); 
		
	}

	@Override
	public Set<Class> getObjectClasses() {  
		Set<Class> out = new HashSet<Class>(); 
		out.add(List.class);
		return out;
	}

	@Override
	public String getSubTypeByObject(Object value) {
		String out = null;
		if(value instanceof List){
			for(Object ele : (List)value){
				out = ele.getClass().getName();
				break;
			}
		}
		return out;
	}
}
