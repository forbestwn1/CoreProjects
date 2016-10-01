package com.nosliw.data;

import java.util.Map;


public class HAPOperationContext {

	private Map<String, HAPData> m_variables;
	
	public void setVariables(Map<String, HAPData> vars){
		this.m_variables = vars;
	}
	
	public Map<String, HAPData> getVariables(){
		return this.m_variables;
	}
}
