package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public interface HAPDataTypeCriteriaManager {

	Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to);
	
	HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds);

	HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);

	HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria);

	boolean compatibleWith(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);
	
	HAPDataTypeId getRootDataTypeId(HAPDataTypeId dataTypeId);
	
	Set<HAPDataTypeId> normalize(Set<HAPDataTypeId> dataTypeIds);
	
	HAPRelationship buildConvertor(HAPDataTypeCriteria from, HAPDataTypeCriteria to);
}
