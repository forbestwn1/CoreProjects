package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaAnd extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaAnd(List<HAPDataTypeCriteria> ele, HAPDataTypeCriteriaManager criteriaMan){
		super(ele, criteriaMan);
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_AND;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		int i = 0;
		for(HAPDataTypeCriteria ele : this.getElements()){
			if(i==0){
				out = ele.getValidDataTypeId();
			}
			else{
				out = Sets.intersection(out, ele.getValidDataTypeId());
			}
		}
		return out;
	}

	@Override
	public HAPDataTypeCriteria normalize() {
		Set<HAPDataTypeId> ids = this.getValidDataTypeId();
		Set<HAPDataTypeId> norIds = this.getDataTypeCriteraManager().normalize(ids);
		return this.buildCriteriaByIds(norIds);
	}
}
