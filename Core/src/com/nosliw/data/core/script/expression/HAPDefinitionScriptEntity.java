package com.nosliw.data.core.script.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPDefinitionScriptEntity extends HAPEntityInfoWritableImp{

	private HAPScript m_script;
	
	@HAPAttribute
	public static String SCRIPT = "script";

	@HAPAttribute
	public static String TYPE = "type";

	public HAPDefinitionScriptEntity() {
	}
	
	public HAPScript getScript() {  return this.m_script;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_script = HAPScript.newScript(jsonObj);
		return true;  
	}
}
