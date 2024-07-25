package com.nosliw.core.application.division.manual.common.scriptexpression;

public class HAPManualDefinitionScriptExpressionConstant {

	private String m_scriptExpressionStr;
	
	private String m_scriptExpressionType;
	
	public HAPManualDefinitionScriptExpressionConstant(String scriptExpressionStr, String scriptExpressionType) {
		this.m_scriptExpressionStr = scriptExpressionStr;
		this.m_scriptExpressionType = scriptExpressionType;
	}
	
	public String getScriptExpression() {   return this.m_scriptExpressionStr;      }
	
	public String getScriptExpressionType() {    return this.m_scriptExpressionType;      }
	
}
