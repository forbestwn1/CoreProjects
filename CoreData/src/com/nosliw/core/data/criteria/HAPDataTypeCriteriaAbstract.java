package com.nosliw.core.data.criteria;

import java.util.Set;

import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.HAPDataTypeId;

public abstract class HAPDataTypeCriteriaAbstract extends HAPDataTypeCriteriaImp{
	
	private HAPDataTypeCriteria m_solidCriteria;
	
	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		return this.getSoldCriteria().validate(criteria, dataTypeHelper);
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		return this.getSoldCriteria().validate(dataTypeId, dataTypeHelper);
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		return this.getSoldCriteria().getValidDataTypeId(dataTypeHelper);
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		return this.getSoldCriteria().getValidDataTypeCriteriaId(dataTypeHelper);
	}

	public void setSolidCriteria(HAPDataTypeCriteria criteria){
		this.m_solidCriteria = criteria;
	}
	
	protected HAPDataTypeCriteria getSoldCriteria(){
		return this.m_solidCriteria;
	}
}
