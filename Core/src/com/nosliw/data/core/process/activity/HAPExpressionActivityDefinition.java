package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpression;

public class HAPExpressionActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	private HAPDefinitionScriptExpression m_expression;
	
	public HAPExpressionActivityDefinition(String type) {
		super(type);
	}
	
	public HAPDefinitionScriptExpression getExpression(){  return this.m_expression;    }
	private void setExpression(String expression) {	this.m_expression = new HAPDefinitionScriptExpression(expression);	}

//	public Set<String> getVariableNames() {	return this.m_expression.getVariableNames();	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		String expressionStr = jsonObj.optString(EXPRESSION);
		this.setExpression(expressionStr);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPJsonUtility.buildJson(this.m_expression, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPExpressionActivityDefinition out = new HAPExpressionActivityDefinition(this.getType());
		this.cloneToNormalActivityDefinition(out);
		out.m_expression = this.m_expression.cloneScriptExpressionDefinition();
		return out;
	}
}
