package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPUIScriptExpression extends HAPScriptExpression{

	@HAPAttribute
	public static final String ID = "id";

	//id of script expression
	private String m_id;
	
	public HAPUIScriptExpression(String id, String content){
		super(content);
		this.m_id = id;
	}

	public String getId(){   return this.m_id;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
	}
}
