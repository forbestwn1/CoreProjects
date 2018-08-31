package com.nosliw.data.core.script.expressionscript;

import com.nosliw.common.updatename.HAPUpdateName;

public class HAPScriptExpressionScriptConstant {

	private String m_constantName; 

	private Object m_value;
	
	public HAPScriptExpressionScriptConstant(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){	return this.m_constantName;	}
	
	public Object getValue() {  return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public String updateName(HAPUpdateName nameUpdate) {
		this.m_constantName = nameUpdate.getUpdatedName(m_constantName);
		return this.m_constantName;
	}
}
