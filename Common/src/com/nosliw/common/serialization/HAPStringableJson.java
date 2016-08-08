package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPJsonUtility;

public abstract class HAPStringableJson extends HAPStringImp{

	@Override
	final public String toString(){
		String out = "";
		try{
			out = this.toStringValue(null);
			out = HAPJsonUtility.formatJson(out);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	@Override
	public final String toStringValue(String format) {
		Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class> typeJsonMap = new LinkedHashMap<String, Class>();
		this.buildJsonMap1(outJsonMap, typeJsonMap);
		return HAPJsonUtility.getMapJson(outJsonMap, typeJsonMap);
	}

	final void buildJsonMap1(Map<String, String> jsonMap, Map<String, Class> typeJsonMap){
		super.buildJsonMap1(jsonMap, typeJsonMap);
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	abstract protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap);
}

abstract class HAPStringImp implements HAPStringable{
	void buildJsonMap1(Map<String, String> jsonMap, Map<String, Class> typeJsonMap){
	}
}
