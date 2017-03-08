package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaOr extends HAPDataTypeCriteriaImp{

	private List<HAPDataTypeCriteria> m_criterias;
	
	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> criterias){
		this.m_criterias = criterias;
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_OR;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		for(HAPDataTypeCriteria ele : this.m_criterias){
			out.addAll(ele.getValidDataTypeId());
		}
		return out;
	}
}
