package com.nosliw.data.core.script.expression;

import com.nosliw.common.updatename.HAPUpdateName;

public class HAPConstantInScript {

	private String m_constantName; 

	private Object m_value;
	
	public HAPConstantInScript(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){	return this.m_constantName;	}
	
	public Object getValue() {  return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public String updateName(HAPUpdateName nameUpdate) {
		this.m_constantName = nameUpdate.getUpdatedName(m_constantName);
		return this.m_constantName;
	}

	public HAPConstantInScript cloneConstantInScript() {
		HAPConstantInScript out = new HAPConstantInScript(this.m_constantName);
		out.m_value = this.m_value;
		return out;
	}
}
