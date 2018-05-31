package com.nosliw.common.serialization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class HAPSerializeUtility {

	public static List buildListFromJsonArray(String className, JSONArray jsonArray){
		List out = new ArrayList();
		if(jsonArray!=null) {
			for(int i=0; i<jsonArray.length(); i++){
				Object ele = null;
				if(String.class.getName().equals(className)) {
					ele = jsonArray.opt(i);
				}
				else {
					ele = HAPSerializeManager.getInstance().buildObject(className, jsonArray.opt(i), HAPSerializationFormat.JSON);
				}
				out.add(ele);
			}
		}
		return out;
		
	}
	
	public static Map buildMapFromJsonObject(String className, JSONObject jsonObj){
		Map out = new LinkedHashMap();
		if(jsonObj!=null) {
			for(Object key : jsonObj.keySet()) {
				Object ele = null;
				if(String.class.getName().equals(className)) {
					ele = jsonObj.opt((String)key);
				}
				else {
					ele = HAPSerializeManager.getInstance().buildObject(className, jsonObj.opt((String)key), HAPSerializationFormat.JSON);
				}
				out.put(key, ele);
			}
		}
		return out;
	}
}
