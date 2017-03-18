package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaOr extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> eles, HAPDataTypeCriteriaManager criteriaMan){
		super(eles, criteriaMan);
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_OR;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		for(HAPDataTypeCriteria ele : this.getElements()){
			out.addAll(ele.getValidDataTypeId());
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
