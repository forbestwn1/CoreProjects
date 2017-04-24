package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaOr extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> eles){
		super(eles);
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_OR;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		for(HAPDataTypeCriteria ele : this.getElements()){
			out.addAll(ele.getValidDataTypeId(dataTypeHelper));
		}
		return out;
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
		Set<HAPDataTypeId> ids = this.getValidDataTypeId(dataTypeHelper);
		Set<HAPDataTypeId> norIds = dataTypeHelper.normalize(ids);
		return this.buildCriteriaByIds(norIds);
	}
}
