package com.nosliw.data.core.datasource;



public class HAPDataSourceOutput {

	private HAPVariableInfo m_criteria;
	
	public HAPDataSourceOutput(HAPVariableInfo criteria){
		this.m_criteria = criteria;
	}
	
	public HAPVariableInfo getCriteria(){   return this.m_criteria;    }
	
}
