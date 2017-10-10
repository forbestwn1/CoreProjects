package com.nosliw.uiresource;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Store information for node context
 * For now, the information is data type criteria for the node 
 */
public class HAPContextNodeDefinition{
	
	private HAPDataTypeCriteria m_criteria;

	public HAPContextNodeDefinition(HAPDataTypeCriteria criteria){
		this.m_criteria = criteria;
	}
}
