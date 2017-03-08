package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaAnd extends HAPDataTypeCriteriaImp{

	private List<HAPDataTypeCriteria> m_criterias;
	
	public HAPDataTypeCriteriaAnd(List<HAPDataTypeCriteria> criterias){
		this.m_criterias = criterias;
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_AND;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		int i = 0;
		for(HAPDataTypeCriteria ele : this.m_criterias){
			if(i==0){
				out = ele.getValidDataTypeId();
			}
			else{
				out = Sets.intersection(out, ele.getValidDataTypeId());
			}
		}
		return out;
	}

}
