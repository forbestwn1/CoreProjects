package com.nosliw.data.core.script.expression1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

/**
 * Segment in script expression for in-line script 
 * it may also contains constants and variables in it
 */
public class HAPScriptInScriptExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String SCRIPT = "script";

	private String m_orignalScript;
	public HAPScriptInScriptExpression(String script){
		this.m_orignalScript = script;
	}
	
	public String getScript() {   return this.m_orignalScript;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, this.m_orignalScript);
	}
	
}
