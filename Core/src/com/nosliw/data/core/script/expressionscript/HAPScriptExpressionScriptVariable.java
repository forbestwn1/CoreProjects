package com.nosliw.data.core.script.expressionscript;

import com.nosliw.common.updatename.HAPUpdateName;

public class HAPScriptExpressionScriptVariable {

	private String m_variableName; 
	
	public HAPScriptExpressionScriptVariable(String name){
		this.m_variableName = name;
	}
	
	public String getVariableName(){
		return this.m_variableName;
	}

	public String updateName(HAPUpdateName nameUpdate) {
		this.m_variableName = nameUpdate.getUpdatedName(m_variableName);
		return this.m_variableName;
	}
}
