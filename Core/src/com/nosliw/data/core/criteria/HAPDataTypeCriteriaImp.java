package com.nosliw.data.core.criteria;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPDataTypeId;

public abstract class HAPDataTypeCriteriaImp extends HAPSerializableImp implements HAPDataTypeCriteria{

	private HAPDataTypeCriteriaManager m_dataTypeCriteriaMan;
	
	public HAPDataTypeCriteriaImp(HAPDataTypeCriteriaManager criteriaMan){
		this.m_dataTypeCriteriaMan = criteriaMan;
	}
	
	@Override
	public boolean validate(HAPDataTypeCriteria criteria) {
		return this.getValidDataTypeId().containsAll(criteria.getValidDataTypeId());
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId) {
		return this.getValidDataTypeId().contains(dataTypeId);
	}
	
	protected HAPDataTypeCriteriaManager getDataTypeCriteraManager(){ return this.m_dataTypeCriteriaMan; }
}
