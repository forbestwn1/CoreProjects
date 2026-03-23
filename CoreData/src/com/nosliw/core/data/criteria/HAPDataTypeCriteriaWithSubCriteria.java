package com.nosliw.core.data.criteria;

public interface HAPDataTypeCriteriaWithSubCriteria {

	/**
	 * For some complex datatype (array, map), we need to describe the data type for child element
	 * For instance, we need data type criteria for element in array, attribute in map 
	 * In order to validate on data type or data type criteria, both parent and children data type criteria have to meet
	 * @return
	 */
	HAPDataTypeSubCriteriaGroup getSubCriteria();

}
