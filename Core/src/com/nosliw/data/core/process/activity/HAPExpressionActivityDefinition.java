package com.nosliw.data.core.process.activity;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	private HAPScriptExpression m_expression;
	
	public HAPExpressionActivityDefinition() {
	}
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;	}
	
	public String getExpression(){  return this.m_expression.getExpression();    }
	private void setExpression(String expression) {	this.m_expression = new HAPScriptExpression(expression);	}

	public HAPOperandWrapper getOperand() {  return this.m_expression.getOperand();  }

	public Set<String> getVariableNames() {	return this.m_expression.getVariableNames();	}

	public Set<String> getReferenceNames() {  return this.m_expression.getReferenceNames(); }

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
}
