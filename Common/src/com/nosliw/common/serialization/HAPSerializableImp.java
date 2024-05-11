package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class HAPSerializableImp implements HAPSerializable{

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		if(value==null) {
			return true;
		}
		boolean out = false;
		switch(format){
		case JSON_FULL:
			out = this.buildObjectByFullJson(value);
			break;
		case JSON:
			out = this.buildObjectByJson(value);
			break;
		case XML:
			out = this.buildObjectByXml(value);
			break;
		case LITERATE:
			out = this.buildObjectByLiterate((String)value);
			break;
		}
		return out;
	}

	protected boolean buildObjectByFullJson(Object json){ return this.buildObjectByJson(json); }

	protected boolean buildObjectByJson(Object json){  return false;  }
	
	protected boolean buildObjectByXml(Object xml){  return false;  }
	
	protected boolean buildObjectByLiterate(String literateValue){	return false;  }
	
	@Override
	public String toString(){
		String out = "";
		try{
			out = this.toStringValue(HAPSerializationFormat.JSON);
			out = HAPUtilityJson.formatJson(out);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	@Override
	public String toStringValue(HAPSerializationFormat format) {
		if(format==null) {
			format = HAPSerializationFormat.JSON_FULL;
		}
		
		String out = null;
		switch(format){
		case JSON_FULL:
			out = this.buildFullJson();
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				this.buildFullJsonMap(outJsonMap, typeJsonMap);
				out = HAPUtilityJson.buildMapJson(outJsonMap, typeJsonMap);
			}
			break;
		case JSON:
			out = this.buildJson();
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				this.buildJsonMap(outJsonMap, typeJsonMap);
				out = HAPUtilityJson.buildMapJson(outJsonMap, typeJsonMap);
			}
			break;
		case JAVASCRIPT:
			out = this.buildJavascript();
			if(out==null){
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				this.buildJSJsonMap(outJsonMap, typeJsonMap);
				out = HAPUtilityJson.buildMapJson(outJsonMap, typeJsonMap);
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
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	protected String buildJson(){ return null; }
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
	}

	protected String buildJavascript(){ return null; }
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	
	@Override
	public int hashCode() {
		String literateStr = this.toStringValue(HAPSerializationFormat.LITERATE);
		if(literateStr==null) {
			literateStr = this.toStringValue(HAPSerializationFormat.JSON);
		}
		if(literateStr==null) {
			return super.hashCode();
		}
		return literateStr.hashCode();
	}

	@Override
	public Object cloneValue() {    return this;     }

}
