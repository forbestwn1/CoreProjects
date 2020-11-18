package com.nosliw.common.value;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPNamingConversionUtility;

public class HAPJsonDataUtility {

	public static Object getValue(Object value, String path) {
		Object out = value;
		String[] pathSegs = HAPNamingConversionUtility.parseComponentPaths(path);
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
