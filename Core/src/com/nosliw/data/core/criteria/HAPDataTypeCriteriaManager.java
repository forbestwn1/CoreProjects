package com.nosliw.data.core.criteria;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public interface HAPDataTypeCriteriaManager {

	/**
	 * List all data types between from and to
	 * This means that each data type should be able to convert to "From" and also can be converted from "To"
	 * @param from
	 * @param to
	 * @return
	 */
	Set<HAPDataTypeId> getAllDataTypeInRange(HAPDataTypeId from, HAPDataTypeId to);
	
	/**
	 * Build data type criteria based on a set of data type ids
	 * @param dataTypeIds
	 * @return
	 */
	HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeId> dataTypeIds);

	/**
	 * Do "And" operation between criteria1 and criteria2 
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);

	/**
	 * Loose criteria so that all the data type that can be converted to this criteria are included
	 * @param criteria
	 * @return
	 */
	HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria);

	/**
	 * Whether criteria1 is compatible with criteria2
	 * This means that all data type in criteria1 is also part of criteria2
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	boolean compatibleWith(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);

	/**
	 * Whether dataType1 is compatible with dataType2
	 * This means that dataType1 can be converted to dataType2
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	HAPRelationship compatibleWith(HAPDataTypeId dataTypeId1, HAPDataTypeId dataTypeId2);
	
	/**
	 * Find the root data type (all the parent data type which don't have parent data type)
	 * @param dataTypeId
	 * @return
	 */
	Set<HAPDataTypeId> getRootDataTypeId(HAPDataTypeId dataTypeId);

	/**
	 * Find the root data type relationship (all the parent data type which don't have parent data type)
	 * @param dataTypeId
	 * @return
	 */
	Set<HAPRelationship> getRootDataTypeRelationship(HAPDataTypeId dataTypeId);

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
