package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPManualDefinitionScriptExpressionItemInContainer extends HAPEntityInfoImp{

	private static String SCRIPTEXPRESSION = "expression";
	
	private String m_scriptExpression;
	
	public HAPManualDefinitionScriptExpressionItemInContainer(String scriptExpression) {
		this.m_scriptExpression = scriptExpression;
	}
	
	public String getScriptExpression() {    return this.m_scriptExpression;     }
	public void setScriptExpression(String scriptExpression) {    this.m_scriptExpression = scriptExpression;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, m_scriptExpression);
	}

}
