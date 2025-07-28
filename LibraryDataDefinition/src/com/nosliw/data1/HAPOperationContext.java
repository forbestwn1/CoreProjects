package com.nosliw.data1;

import java.util.Map;

import com.nosliw.data.HAPData;


public class HAPOperationContext {

	private Map<String, HAPData> m_variables;
	
	public void setVariables(Map<String, HAPData> vars){
		this.m_variables = vars;
	}
	
	public Map<String, HAPData> getVariables(){
		return this.m_variables;
	}
}
