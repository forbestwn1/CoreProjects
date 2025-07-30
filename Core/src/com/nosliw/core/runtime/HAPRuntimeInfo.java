package com.nosliw.core.runtime;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

/*
 * This entity represent a runtime env
 * A runtime information is composed of two part:
 * 		language:  the execution language (java, javascription, ...)
 * 		environment: the execution environment (server, ui, mobile, ...)
 */
public class HAPRuntimeInfo extends HAPSerializableImp{

	public static final String LANGUAGE = "language";
	public static final String ENVIRONMENT = "environment";
	
	private String m_language;
	
	private String m_environment;

	public HAPRuntimeInfo(){}
	
	public HAPRuntimeInfo(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPRuntimeInfo(String language, String environment){
		this.m_language = language;
		this.m_environment = environment;
	}
	
	public String getLanguage(){		return this.m_language;	}
	
	public String getEnvironment(){ return this.m_environment;	}
	
	@Override
	protected String buildLiterate(){  return HAPUtilityNamingConversion.cascadeLevel1(m_language, m_environment); }

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPUtilityNamingConversion.parseLevel1(literateValue);
		this.m_language = segs[0];
		this.m_environment = segs[1];
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(LANGUAGE, m_language);
		jsonMap.put(ENVIRONMENT, m_environment);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_language = jsonObj.optString(LANGUAGE);
		this.m_environment = jsonObj.optString(ENVIRONMENT);
		return true; 
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof HAPRuntimeInfo){
			HAPRuntimeInfo runtimeInfo = (HAPRuntimeInfo)obj;
			return HAPUtilityBasic.isEquals(this.m_language, runtimeInfo.m_language) &&
					HAPUtilityBasic.isEquals(this.m_environment, runtimeInfo.m_environment);
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.buildLiterate();
	}
}
