package com.nosliw.utils;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.utils.HAPEntityUtility;

public class HAPNamingUtility {

	/*
	static public String getContainerElementPath(String containerPath){
		if(HAPEntityUtility.isStringEmpty(containerPath))	return HAPConstant.ATTRPATH_ELEMENT;
		else  return containerPath+":#"+HAPConstant.ATTRPATH_ELEMENT;
	}
	
	static public String getCriticalGroupPath(String parentPath){
		if(HAPEntityUtility.isStringEmpty(parentPath))	return HAPConstant.ATTRPATH_CRITICAL;
		else  return parentPath+":"+HAPConstant.ATTRPATH_CRITICAL;
	}
	
	static public String getCriticalAttributePath(String parentPath, String criticalValue, String attribute){
		String out = "";
		if(HAPEntityUtility.isStringNotEmpty(parentPath))  out = parentPath + ":";
		out = out + HAPConstant.ATTRPATH_CRITICAL + ":" + criticalValue + ":" + attribute;
		return out;
	}
	
	static public String getFullAttributePath(String attrPath, String parentPath){
		if(attrPath.contains(":"))  return attrPath;
		if(HAPEntityUtility.isStringEmpty(parentPath))  return attrPath;
		return parentPath + ":" + attrPath;
	}

	
	
	static public String getCriticalAttributeOption(String symbol){
		return symbol.substring(HAPConstant.SYMBOL_ATTRPATH_CRITICAL.length()+1, symbol.length()-1);
	}
	static public String buildCriticalAttributeOption(String criticalValue){
		return HAPConstant.SYMBOL_ATTRPATH_SYMBOL+HAPConstant.SYMBOL_ATTRPATH_CRITICAL+"["+criticalValue+"]";
	}

	static public String buildElementAttribute(){
		return HAPConstant.SYMBOL_ATTRPATH_SYMBOL+HAPConstant.SYMBOL_ATTRPATH_ELEMENT;
	}

	*/
	
}
