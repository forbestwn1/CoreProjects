package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;

public interface HAPDataTypeCriteriaManager {

	public Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to);
	
	public HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds);
}
