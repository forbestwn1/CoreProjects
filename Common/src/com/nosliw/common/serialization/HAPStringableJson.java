package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public abstract class HAPStringableJson implements HAPStringable{

	@Override
	public String toString(){
		String out = "";
		try{
			out = this.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON_FULL);
			out = HAPJsonUtility.formatJson(out);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	@Override
	public String toStringValue(String format) {
		if(HAPBasicUtility.isStringEmpty(format))  format = HAPConstant.CONS_SERIALIZATION_JSON_FULL;

		String out = null;
		if(format.equals(HAPConstant.CONS_SERIALIZATION_JSON_FULL)){
			Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
			Map<String, Class> typeJsonMap = new LinkedHashMap<String, Class>();
			this.buildFullJsonMap(outJsonMap, typeJsonMap, format);
			out = HAPJsonUtility.getMapJson(outJsonMap, typeJsonMap);
		}
		else if(format.equals(HAPConstant.CONS_SERIALIZATION_JSON)){
			out = this.buildJson(format);
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class> typeJsonMap = new LinkedHashMap<String, Class>();
				this.buildJsonMap(outJsonMap, typeJsonMap, format);
				out = HAPJsonUtility.getMapJson(outJsonMap, typeJsonMap);
			}
		}
		return out;
	}

	abstract protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format);

	protected String buildJson(String format){ return null; }
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format){
		this.buildFullJsonMap(jsonMap, typeJsonMap, format);
	}	
}
