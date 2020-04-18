package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPUtilityRuntimeJS {
	
	//resource info parm name for how to load resource : value / file
	public static final String RESOURCE_LOADPATTERN = "loadPattern";
	
	public static final String RESOURCE_LOADPATTERN_VALUE = "value";
	
	public static final String RESOURCE_LOADPATTERN_FILE = "file";

	public static String buildConstantValue(Object obj) {
		if(obj instanceof String)   return "'"+obj+"'";
		else if(obj instanceof Integer)   return ""+obj;
		else if(obj instanceof Boolean)   return ""+obj;
		else if(obj instanceof Float)   return ""+obj;
		else if(obj instanceof Double)   return ""+obj;
		else return HAPSerializeManager.getInstance().toStringValue(obj, HAPSerializationFormat.JSON);
	}
	
}
