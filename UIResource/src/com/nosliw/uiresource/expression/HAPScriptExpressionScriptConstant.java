package com.nosliw.uiresource.expression;

public class HAPScriptExpressionScriptConstant {

	private String m_constantName; 
	
	public HAPScriptExpressionScriptConstant(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){
		return this.m_constantName;
	}
	
}
