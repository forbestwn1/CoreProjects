package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementIds extends HAPDataTypeCriteriaImp{

	Set<HAPDataTypeId> m_dataTypeIds;
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEIDS;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		return this.m_dataTypeIds;
	}

}
