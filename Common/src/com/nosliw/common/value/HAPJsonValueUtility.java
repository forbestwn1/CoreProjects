package com.nosliw.common.value;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPJsonValueUtility {

	public static Object getValue(Map<String, Object> valueSet, HAPComplexPath complexPath) {
		Object rootValue = valueSet.get(complexPath.getRoot());
		return getValue(rootValue, complexPath.getPath().getPath());
	}
	
	public static Object getValue(Object value, String path) {
		Object out = value;
		if(HAPBasicUtility.isStringEmpty(path))   return out;
		String[] pathSegs = HAPUtilityNamingConversion.parseComponentPaths(path);
		for(String seg : pathSegs) {
			out = getChild(out, seg);
			if(out==null)   break;
		}
		return out;
	}
	
	private static Object getChild(Object value, String childName) {
		if(value==null)   return null;
		if(value instanceof JSONObject)  return ((JSONObject)value).get(childName);
		if(value instanceof JSONArray)   return ((JSONArray)value).get(Integer.valueOf(childName));
		return null;
	}
	
	
}
