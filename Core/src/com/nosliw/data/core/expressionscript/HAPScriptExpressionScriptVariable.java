package com.nosliw.data.core.expressionscript;

public class HAPScriptExpressionScriptVariable {

	private String m_variableName; 
	
	public HAPScriptExpressionScriptVariable(String name){
		this.m_variableName = name;
	}
	
	public String getVariableName(){
		return this.m_variableName;
	}
}
