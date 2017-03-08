package com.nosliw.data.core.criteria;

import java.util.List;

public class HAPDataTypeCriteriaOr implements HAPDataTypeCriteria{

	private List<HAPDataTypeCriteria> m_criterias;
	
	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> criterias){
		this.m_criterias = criterias;
	}
}
