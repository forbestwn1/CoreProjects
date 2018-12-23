package com.nosliw.data.core.script.expression;

import com.nosliw.common.updatename.HAPUpdateName;

public class HAPVariableInScript {

	private String m_variableName; 
	
	public HAPVariableInScript(String name){
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
