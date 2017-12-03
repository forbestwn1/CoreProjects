package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

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
		if(this.getSoldCriteria()==null){
			int kkkkk = 5555;
			kkkkk++;
		}
		
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
