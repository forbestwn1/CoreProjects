package com.nosliw.datasource;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDataSourceOutput {

	private HAPDataTypeCriteria m_criteria;
	
	public HAPDataSourceOutput(HAPDataTypeCriteria criteria){
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;    }
	
}
