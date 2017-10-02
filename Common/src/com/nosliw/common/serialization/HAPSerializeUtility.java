package com.nosliw.common.serialization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class HAPSerializeUtility {

	public static List buildListFromJsonArray(String className, JSONArray jsonArray){
		List out = new ArrayList();
		for(int i=0; i<jsonArray.length(); i++){
			Object ele = HAPSerializeManager.getInstance().buildObject(className, jsonArray.opt(i), HAPSerializationFormat.JSON);
			out.add(ele);
		}
		return out;
		
	}
	
}
