package com.nosliw.data.core.script.expressionscript;

import com.nosliw.common.updatename.HAPUpdateName;

public class HAPScriptExpressionScriptConstant {

	private String m_constantName; 
	
	public HAPScriptExpressionScriptConstant(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){
		return this.m_constantName;
	}
	
	public String updateName(HAPUpdateName nameUpdate) {
		this.m_constantName = nameUpdate.getUpdatedName(m_constantName);
		return this.m_constantName;
	}
}
