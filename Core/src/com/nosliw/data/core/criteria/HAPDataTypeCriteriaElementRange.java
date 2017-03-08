package com.nosliw.data.core.criteria;

import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementRange implements HAPDataTypeCriteria{

	private HAPDataTypeId m_from;
	
	private HAPDataTypeId m_to;

	public HAPDataTypeCriteriaElementRange(HAPDataTypeId from, HAPDataTypeId to){
		this.m_from = from;
		this.m_to = to;
	}
	
}
