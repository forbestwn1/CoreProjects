package com.nosliw.data.core.script.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.expression.HAPDefinitionExpression;

public class HAPExpressionInScriptExpression extends HAPDefinitionExpression{

	@HAPAttribute
	public static String ID = "id";

	private String m_id;
	
	public HAPExpressionInScriptExpression(String expression, String id) {
		super(expression);
		this.m_id = id;
	}

	public String getId() {   return this.m_id;  }
	
	public void setId(String id) {  this.m_id = id;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
	}
}
