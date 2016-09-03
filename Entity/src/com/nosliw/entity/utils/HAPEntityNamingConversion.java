package com.nosliw.entity.utils;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPEntityNamingConversion {

	public static String createAttributeWithCriticalFullName(String criticalValue, String attributeName){
		return HAPNamingConversionUtility.createKeyword(HAPConstant.ATTRIBUTE_PATH_CRITICAL)
				+HAPConstant.SEPERATOR_DETAIL
				+criticalValue
				+HAPConstant.SEPERATOR_DETAIL
				+attributeName;
	}
	
	public static String createContainerElementName(){
		return HAPNamingConversionUtility.createKeyword(HAPConstant.ATTRIBUTE_PATH_ELEMENT);
	}
	
	public static String getGroupName(String name){
		if(name.startsWith(HAPConstant.SYMBOL_GROUP)){
			return name.substring(1);
		}
		return null;
	}
}
