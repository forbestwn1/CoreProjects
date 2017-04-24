package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.List;

public abstract class HAPDataTypeCriteriaComplex extends HAPDataTypeCriteriaImp{

	private List<HAPDataTypeCriteria> m_eles;
	
	public HAPDataTypeCriteriaComplex(List<HAPDataTypeCriteria> eles) {
		this.m_eles = new ArrayList<HAPDataTypeCriteria>();
		this.m_eles.addAll(eles);
	}

	public List<HAPDataTypeCriteria> getElements(){  return this.m_eles;  }
}
