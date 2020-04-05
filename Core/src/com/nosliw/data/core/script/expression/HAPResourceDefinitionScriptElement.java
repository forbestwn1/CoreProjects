package com.nosliw.data.core.script.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPResourceDefinitionScriptElement extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	private HAPDefinitionScriptExpression m_script;
	
	public HAPDefinitionScriptExpression getScript() {   return this.m_script;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		String script = jsonObj.getString(SCRIPT);
		this.m_script = new HAPDefinitionScriptExpression(script);
		return true;  
	}
}
