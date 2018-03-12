package com.nosliw.data.core.datasource;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDefinitionOutput {

	private HAPDataTypeCriteria m_criteria;
	
	public HAPDefinitionOutput(HAPDataTypeCriteria criteria){
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;    }
	
	
	
}
