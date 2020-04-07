package com.nosliw.data.core.script.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPDefinitionScript extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SCRIPT = "script";

	@HAPAttribute
	public static String TYPE = "type";

	private String m_type;
	
	private String m_script;
	
	
	
	public String getType() {   return this.m_type;    }
	
	public String getScript() {   return this.m_script;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_script = jsonObj.getString(SCRIPT);
		this.m_type = (String)jsonObj.opt(TYPE);
		return true;  
	}
}
