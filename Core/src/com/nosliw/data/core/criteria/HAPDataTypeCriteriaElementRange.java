package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementRange extends HAPDataTypeCriteriaImp{

	private HAPDataTypeId m_from;
	
	private HAPDataTypeId m_to;

	public HAPDataTypeCriteriaElementRange(HAPDataTypeId from, HAPDataTypeId to, HAPDataTypeCriteriaManager criteriaMan){
		super(criteriaMan);
		this.m_from = from;
		this.m_to = to;
	}
	
	public HAPDataTypeId getFromDataTypeId(){  return this.m_from;  }
	public HAPDataTypeId getToDataTypeId(){  return this.m_to;  }
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPERANGE;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		return this.getDataTypeCriteraManager().getAllDataTypeInRange(m_from, m_to);
	}
}
