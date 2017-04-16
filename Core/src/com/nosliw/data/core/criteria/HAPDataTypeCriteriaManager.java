package com.nosliw.data.core.criteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public interface HAPDataTypeCriteriaManager {

	Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to);
	
	HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds);

	HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);

	HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria);

	boolean compatibleWith(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);
	
	/**
	 * Find the root data type (all the parent data type which don't have parent data type)
	 * @param dataTypeId
	 * @return
	 */
	List<HAPDataTypeId> getRootDataTypeId(HAPDataTypeId dataTypeId);
	
	/**
	 * Find the trunk data type for criteria. Trunk data type is the highest parent data type that is shared by all data type under criteria
	 * @param criteria
	 * @return
	 */
	HAPDataTypeId getTrunkDataType(HAPDataTypeCriteria criteria);
	
	/**
	 * Remove all the child data type so that the return data type can not convert to each other
	 * @param dataTypeIds
	 * @return
	 */
	Set<HAPDataTypeId> normalize(Set<HAPDataTypeId> dataTypeIds);
	
	/**
	 * Build all possible relationship in order to convert the data type from "from" criteria to "to" criteria  
	 * @param from
	 * @param to
	 * @return
	 */
	Map<HAPDataTypeId, HAPRelationship> buildConvertor(HAPDataTypeCriteria from, HAPDataTypeCriteria to);
	
}
