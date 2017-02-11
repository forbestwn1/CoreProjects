package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPJsonUtility;

public abstract class HAPSerialiableImp implements HAPSerializable{

	@Override
	public void buildObject(Object value, HAPSerializationFormat format){
		switch(format){
		case JSON_FULL:
			this.buildObjectByFullJson(value);
			break;
		case JSON:
			this.buildObjectByJson(value);
			break;
		case XML:
			this.buildObjectByXml(value);
			break;
		case LITERATE:
			this.buildObjectByLiterate((String)value);
			break;
		}
	}

	protected void buildObjectByFullJson(Object json){}

	protected void buildObjectByJson(Object json){}
	
	protected void buildObjectByXml(Object xml){}
	
	protected void buildObjectByLiterate(String literateValue){	}
	
	@Override
	public String toString(){
		String out = "";
		try{
			out = this.toStringValue(HAPSerializationFormat.JSON);
			out = HAPJsonUtility.formatJson(out);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	@Override
	public String toStringValue(HAPSerializationFormat format) {
		if(format==null) format = HAPSerializationFormat.JSON_FULL;
		
		String out = null;
		switch(format){
		case JSON_FULL:
			out = this.buildFullJson();
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				this.buildFullJsonMap(outJsonMap, typeJsonMap);
				out = HAPJsonUtility.buildMapJson(outJsonMap, typeJsonMap);
			}
			break;
		case JSON:
			out = this.buildJson();
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				this.buildJsonMap(outJsonMap, typeJsonMap);
				out = HAPJsonUtility.buildMapJson(outJsonMap, typeJsonMap);
			}
			break;
		case XML:
			break;
		case LITERATE:
			out = this.buildLiterate();
			break;
		}
		return out;
	}

	protected String buildLiterate(){  return null; }
	
	protected String buildFullJson(){ return null; }
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){}

	protected String buildJson(){ return null; }
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}	
}
