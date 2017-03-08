package com.nosliw.data.core.criteria;

import java.util.List;

public class HAPDataTypeCriteriaAnd implements HAPDataTypeCriteria{

	private List<HAPDataTypeCriteria> m_criterias;
	
	public HAPDataTypeCriteriaAnd(List<HAPDataTypeCriteria> criterias){
		this.m_criterias = criterias;
	}
	
}
