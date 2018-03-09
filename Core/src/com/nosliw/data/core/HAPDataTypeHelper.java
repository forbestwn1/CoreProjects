package com.nosliw.data.core;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.expression.HAPMatcher;
import com.nosliw.data.core.expression.HAPMatchers;

/**
 * This is helper class that provide methods that related with data type and operation information
 */
public interface HAPDataTypeHelper {

	/**
	 * Figure out data type criteria id (data type, sub data type) according to data
	 */
	HAPDataTypeCriteriaId getDataTypeCriteriaByData(HAPData data);
	
	/**
	 * 
	 * @param dataTypeInfo
	 * @param name
	 * @return
	 */
	HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name);

	
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
	HAPDataTypeCriteria buildDataTypeCriteria(Set<HAPDataTypeCriteriaId> dataTypeIds);

	/**
	 * Do "And" operation between criteria1 and criteria2 
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);

	
	/**
	 * Find criteria that can convert to both criteria
	 * @param expectCriteria
	 * @param criteria
	 * @return
	 */
	HAPDataTypeCriteria merge(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2);
	
	
	/**
	 * Loose criteria so that all the data type that can be converted to this criteria are included
	 * @param criteria
	 * @return
	 */
	HAPDataTypeCriteria looseCriteria(HAPDataTypeCriteria criteria);

	/**
	 * Whether source criteria is convertable to target criteria
	 * This means that all data type in criteria1 is also part of criteria2
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	HAPMatchers convertable(HAPDataTypeCriteria sourceCriteria, HAPDataTypeCriteria targetCriteria);

	/**
	 * Whether source dataType is convertable to target dataType
	 * This means that dataType1 can be converted to dataType2
	 * @param criteria1
	 * @param criteria2
	 * @return
	 */
	HAPRelationship convertable(HAPDataTypeId sourceDataTypeId, HAPDataTypeId targetDataTypeId);
	
	/**
	 * 
	 * @param sourceCriteria
	 * @param targetCriteria
	 * @return
	 */
	HAPMatcher convertableIdCriteria(HAPDataTypeCriteriaId sourceCriteria, HAPDataTypeCriteriaId targetCriteria);
	
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

	Set<HAPDataTypeCriteriaId> normalizeCriteria(Set<HAPDataTypeCriteriaId> dataTypeCriteriaIds);
	
	/**
	 * Build all possible relationship in order to convert the data type from "from" criteria to "to" criteria  
	 * @param from
	 * @param to
	 * @return
	 */
	HAPMatchers buildMatchers(HAPDataTypeCriteria from, HAPDataTypeCriteria to);
	
	/**
	 * Process all expression in criteria in order to figure out solid criteria for each expression criteria
	 * @param criteria
	 * @param parms parms used in expression 
	 */
	void processExpressionCriteria(HAPDataTypeCriteria criteria, Map<String, HAPData> parms);
	
}
