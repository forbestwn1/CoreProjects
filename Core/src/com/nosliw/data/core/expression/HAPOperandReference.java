package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPOperandReference extends HAPOperandImp{

	public static final String EXPRESSIONNAME = "expressionName";
	
	private String m_expressionName;
	
	private HAPExpression m_expression;
	
	public HAPOperandReference(String expressionName){
		this.m_expressionName = expressionName;
	}

	@Override
	public String getType() {		return HAPConstant.EXPRESSION_OPERAND_REFERENCE;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionName);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionName);
	}
	
}
