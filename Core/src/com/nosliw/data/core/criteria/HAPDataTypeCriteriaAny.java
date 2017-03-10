package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaAny implements HAPDataTypeCriteria{

	public HAPDataTypeCriteriaAny(){}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_ANY;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria) {	return true; }

	@Override
	public boolean validate(HAPDataTypeId dataTypeId) {		return true;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {		return null;	}

}
